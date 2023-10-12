package com.redfootdev.gametracker.common

import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.utils.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor.*
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern


typealias OptionsMap = HashMap<String, String>
typealias OptionsData = List<OptionData>
typealias OptionsDataMap = HashMap<String, OptionData>


data class OptionData(val internalName: String, val displayName: String, val optionDefault: Any, val optionDescription: String) {
    fun displayDefaults() : Component {
        return displayName()
            .append(displayDefault())
            .append(displayDesc())
    }
    
    fun display(optionValue: String) : Component {
        return displayName()
            .append(displayValue(optionValue))
            .append(displayDesc())
    }
    
    private fun displayName() : Component {
        return GREEN.text(" - Name: ").append(GOLD.text(internalName))
    }

    private fun displayDefault() : Component {
        return GREEN.text(" | Default: ").append(GOLD.text("$optionDefault"))
    }

    private fun displayValue(optionValue: String) : Component {
        return GREEN.text(" | Value: ").append(GOLD.text(optionValue))
    }

    private fun displayDesc() : Component {
        return GREEN.text(" | Desc: ").append(GOLD.text(optionDescription))
    }
}

interface DefaultOptions {
    val optionsData: OptionsData
}

object EmptyOptions: DefaultOptions {
    override val optionsData: OptionsData
        get() = listOf()
}

abstract class Optionable(val options: OptionsMap) {
    
    companion object {
        fun displayDefaultOptions(defaultOptions: DefaultOptions): Component {
            val displayOptions = GREEN.text("Options:")
            val optionsData = defaultOptions.optionsData
            if(optionsData.isNotEmpty()) {
                displayOptions.appendNewline()
                optionsData.forEach { optionData ->
                    displayOptions.append(optionData.displayDefaults()).appendNewline()
                }
            } else {
                displayOptions.append(GOLD.text(" None").appendNewline())
            }
            return displayOptions
        }
    }

    open val optionsDataMap: OptionsDataMap = hashMapOf()


    fun createOptionDataMap(optionsData: OptionsData): OptionsDataMap {
        val optionDataMap = hashMapOf<String, OptionData>()
        optionsData.forEach { optionData -> optionDataMap[optionData.internalName.lowercase()] =  optionData}
        return optionDataMap
    }

    private fun <T> getOptionDefault(optionName: String): T {
        val default = optionsDataMap[optionName]!!.optionDefault
        try { return default as T }
        catch (e: Exception) { throw GameTrackerException("Failed to cast default for $optionName to it's assigned type") }
    }

    fun displayOptions(options: OptionsMap): Component {
        val displayOptions = GREEN.text("Options:")
        if(optionsDataMap.isNotEmpty()) {
            displayOptions.appendNewline()
            options.forEach { (optionName, optionValue) ->
                val optionData = optionsDataMap[optionName] ?: throw GameTrackerException("Error Attempting to get OptionData from Option Name: $optionName")
                displayOptions.append(optionData.display(optionValue)).appendNewline()
            }
        } else {
            displayOptions.append(GOLD.text(" None").appendNewline())
        }
        return displayOptions
    }


    fun getOptionName(optionName: String): String {
        return optionsDataMap[optionName]!!.internalName
    }

    fun getOptionDesc(optionName: String): String {
        return optionsDataMap[optionName]!!.optionDescription
    }

    fun parseOptionPlayer(optionName: String) : Player {
        val optionValue = options[optionName] ?: return getOptionDefault<Player>(optionName)
        return Bukkit.getServer().getPlayer(optionValue) ?: throw GameTrackerException("Option: $optionName can't be convert to a Player from name: $optionValue")
    }

    fun parseOptionLong(optionName: String) : Long {
        val optionValue = options[optionName] ?: return getOptionDefault<Long>(optionName)
        return try { optionValue.toLong() }
        catch (e: NumberFormatException) { throw GameTrackerException("Option: $optionName can't be convert to a Long (large number) from value: $optionValue")}
    }

    fun parseOptionInt(optionName: String) : Int {
        val optionValue = options[optionName] ?: return getOptionDefault<Int>(optionName)
        return try { optionValue.toInt() }
        catch (e: NumberFormatException) { throw GameTrackerException("Option: $optionName can't be convert to an Integer (number) from value: $optionValue")}
    }

    fun parseOptionBoolean(optionName: String) : Boolean {
        val optionValue = options[optionName] ?: return getOptionDefault<Boolean>(optionName)
        return try { optionValue.toBoolean() }
        catch (e: NumberFormatException) { throw GameTrackerException("Option: $optionName can't be convert to a Boolean (true/false) from value: $optionValue")}
    }

    fun parseOptionTrimMaterial(optionName: String): TrimMaterial {
        val optionValue = options[optionName] ?: return getOptionDefault<TrimMaterial>(optionName)
        val trimMaterialRegistry = Bukkit.getRegistry(TrimMaterial::class.java)
            ?: throw GameTrackerException("Couldn't find the TrimMaterial Registry this is a bug. Contact the developer!")
        return trimMaterialRegistry.get(NamespacedKey.minecraft(optionValue.lowercase()))
            ?: throw GameTrackerException("Option: $optionName can't be convert to a TrimMaterial (materials used to color trim) from value: $optionValue")
    }

    fun parseOptionTrimPattern(optionName: String): TrimPattern {
        val optionValue = options[optionName] ?: return getOptionDefault<TrimPattern>(optionName)
        val trimPatternRegistry = Bukkit.getRegistry(TrimPattern::class.java)
            ?: throw GameTrackerException("Couldn't find the TrimPattern Registry this is a bug. Contact the developer!")
        return trimPatternRegistry.get(NamespacedKey.minecraft(optionValue.lowercase()))
            ?: throw GameTrackerException("Option: $optionName can't be convert to a TrimPattern (patterns used to color trim) from value: $optionValue")
    }

    fun parseOptionStringList(optionValue: String?, default: List<String>) : List<String> {
        if (optionValue == null) return default
        return optionValue.split(",").map {it.trim()}
    }

    fun parseOptionPlayerList(optionName: String) : List<Player> {
        val optionValue = options[optionName] ?: return getOptionDefault<List<Player>>(optionName)
        return parseOptionStringList(optionValue, emptyList()).map { stringOption -> parseOptionPlayer(optionName)}
    }

    fun parseOptionLongList(optionName: String) : List<Long> {
        val optionValue = options[optionName] ?: return getOptionDefault<List<Long>>(optionName)
        return parseOptionStringList(optionValue, emptyList()).map {stringOption -> parseOptionLong(optionName)}
    }

    fun parseOptionIntList(optionName: String) : List<Int> {
        val optionValue = options[optionName] ?: return getOptionDefault<List<Int>>(optionName)
        return parseOptionStringList(optionValue, emptyList()).map {stringOption -> parseOptionInt(optionName)}
    }

    fun parseOptionBooleanList(optionName: String) : List<Boolean> {
        val optionValue = options[optionName] ?: return getOptionDefault<List<Boolean>>(optionName)
        return parseOptionStringList(optionValue, emptyList()).map {stringOption -> parseOptionBoolean(optionName)}
    }

}