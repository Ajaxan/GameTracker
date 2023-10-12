package com.redfootdev.gametracker.game.result

import com.redfootdev.gametracker.GameTracker
import com.redfootdev.gametracker.common.Optionable
import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.game.Game
import net.kyori.adventure.text.Component
import java.util.*
import kotlin.collections.HashMap


class ResultManager(private val gameTracker: GameTracker) {

    val results: HashMap<String, ResultData> = hashMapOf()
    val resultRegistry = ResultRegistry()

    private val testGameName = "resultManagerTestGame"
    private val testGame = Game(gameTracker, testGameName)

    fun listResultTypes(): List<ResultSchematic> {
        return resultRegistry.resultSchematics.values.toList()
    }

    fun displayResultTypeOptions(schematicName: String): Component {
        val schematic = resultRegistry.getSchematic(schematicName)
        return Optionable.displayDefaultOptions(schematic.defaultOptions)
    }

    fun displayResultInfo(schematicName: String): Component {
        val schematic = resultRegistry.getSchematic(schematicName)
        val displaySchematic = schematic.displaySchematic()
        val displayOptions = Optionable.displayDefaultOptions(schematic.defaultOptions)
        return displaySchematic.appendNewline().append(displayOptions)
    }


    fun displayResultCheck(resultName: String): Component {
        val resultData = getResultData(resultName)
        val testResult = resultRegistry.buildSchematic(testGame, resultData)
        return testResult.display()
    }

    fun createResultData(schematicName: String, resultName: String) {
        val schematic = resultRegistry.getSchematic(schematicName)
        results[resultName] = ResultData(schematic, resultName)
    }

    fun removeResultData(resultName: String) {
        results.remove(resultName) ?: throw GameTrackerException("No Result with name: $resultName found")
    }

    fun listResultData(): List<String> {
        return results.keys.toList()
    }

    fun createResult(gameName: Game, resultName: String): Result {
        val resultData = getResultData(resultName)
        return resultRegistry.buildSchematic(gameName, resultData)
    }

    fun setResultOption(resultName: String, optionName: String, optionValue: String) {
        val resultData = getResultData(resultName)
        resultData.options[optionName] = optionValue
    }

    fun getResultData(resultName: String): ResultData {
        return results[resultName] ?: throw GameTrackerException("No Result with name: $resultName found")
    }

}