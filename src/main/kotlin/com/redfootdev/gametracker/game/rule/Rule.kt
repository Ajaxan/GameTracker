package com.redfootdev.gametracker.game.rule

import com.redfootdev.gametracker.common.*
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.GamePlayer
import com.redfootdev.gametracker.game.result.Result
import com.redfootdev.gametracker.game.result.ResultData
import com.redfootdev.gametracker.utils.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor.*

data class RuleData(
    val schematic: RuleSchematic,
    val name: String,
    val results: ArrayList<ResultData> = arrayListOf(),
    val options: HashMap<String, String> = hashMapOf()
)

abstract class Rule(
    val game: Game,
    val schematicName: String,
    val name: String,
    val results: List<Result>,
    options: HashMap<String, String>
) : Optionable(options), Displayable {

    override val optionsDataMap = createOptionDataMap(getOptionData())

    open fun onAdded() {}
    open fun onRemoved() {}

    open fun onStart() {}
    open fun onEnd() {}

    open fun onDeath(gamePlayer: GamePlayer) {}
    open fun onRespawn(gamePlayer: GamePlayer) {}


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
        results.forEach { result ->
            display.append(result.display()).appendNewline()
        }
        return display
    }

}