package com.redfootdev.gametracker.game.result

import com.redfootdev.gametracker.common.Displayable
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.common.Optionable
import com.redfootdev.gametracker.common.OptionsData
import com.redfootdev.gametracker.game.rule.RuleSchematic
import com.redfootdev.gametracker.utils.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor.*

typealias ResultDataMap = HashMap<String, ResultData>

data class ResultData(
    val schematic: ResultSchematic,
    val name: String,
    val options: HashMap<String, String> = hashMapOf()
)

typealias Results = List<Result>
fun Results.activate() {
    this.forEach { it.activate() }
}

abstract class Result(
    val game: Game,
    val schematicName: String,
    val name: String,
    options: HashMap<String, String>
) : Optionable(options), Displayable {

    override val optionsDataMap = createOptionDataMap(getOptionData())

    abstract fun activate()

    fun getOptionData(): OptionsData {
        return getSchematic().defaultOptions.optionsData
    }

    fun getSchematic(): RuleSchematic {
        return game.gameTracker.ruleManager.ruleRegistry.getSchematic(schematicName)
    }

    override fun displayType(): Component {
        return getSchematic().displaySchematic()
    }

    override fun displayInfo(): Component {
        val displayInfo = displayType().appendNewline()
        displayInfo.append(displayOptions(options))
        return displayInfo
    }

    override fun display(): Component {
        val display = GREEN.text("Rule Name: ").append(GOLD.text(name))
        display.append(displayInfo())
        return display
    }
}