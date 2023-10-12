package com.redfootdev.gametracker.game.rule

import com.redfootdev.gametracker.GameTracker
import com.redfootdev.gametracker.common.Optionable
import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.result.ResultData
import net.kyori.adventure.text.Component
import java.util.*


class RuleManager(private val gameTracker: GameTracker) {

    val rules: HashMap<String, RuleData> = hashMapOf()
    val ruleRegistry = RuleRegistry()

    private val testGameName = "ruleManagerTestGame"
    private val testGame = Game(gameTracker, testGameName)

    fun listRuleTypes(): List<RuleSchematic> {
        return ruleRegistry.ruleSchematics.values.toList()
    }

    fun displayRuleTypeOptions(schematicName: String): Component {
        val schematic = ruleRegistry.getSchematic(schematicName)
        return Optionable.displayDefaultOptions(schematic.defaultOptions)
    }

    fun displayRuleInfo(typeName: String): Component {
        val schematic = ruleRegistry.getSchematic(typeName)
        val displaySchematic = schematic.displaySchematic()
        val displayOptions = Optionable.displayDefaultOptions(schematic.defaultOptions)
        return displaySchematic.appendNewline().append(displayOptions)
    }

    fun displayRuleCheck(ruleName: String): Component {
        val ruleData = getRuleData(ruleName)
        val testRule = ruleRegistry.buildSchematic(testGame, ruleData)
        return testRule.display()
    }

    fun createRuleData(schematicName: String, ruleName: String) {
        val schematic = ruleRegistry.getSchematic(schematicName)
        rules[ruleName] = RuleData(schematic, ruleName)
    }

    fun removeRuleData(ruleName: String) {
        rules.remove(ruleName) ?: throw GameTrackerException("No Rule with name: $ruleName found")
    }

    fun listRuleData(): List<String> {
        return rules.keys.toList()
    }

    fun createRule(game: Game, ruleName: String): Rule {
        val ruleData = getRuleData(ruleName)
        return ruleRegistry.buildSchematic(testGame, ruleData)
    }

    fun setRuleOption(ruleName: String, optionName: String, optionValue: String) {
        val ruleData = getRuleData(ruleName)
        ruleData.options[optionName] = optionValue
    }


    fun addRuleResult(ruleName: String, resultName: String) {
        val ruleData = getRuleData(ruleName)
        val resultData = getResultData(resultName)
        ruleData.results.add(resultData)
    }

    fun removeRuleResult(ruleName: String, resultName: String) {
        val ruleData = getRuleData(ruleName)
        val resultData = getResultData(resultName)
        ruleData.results.remove(resultData)
    }

    fun getResultData(resultName: String): ResultData {
        return gameTracker.resultManager.results[resultName] ?: throw GameTrackerException("No Result with name: $resultName found")
    }

    fun getRuleData(ruleName: String) : RuleData {
        return rules[ruleName] ?: throw GameTrackerException("No Rule with name: $ruleName found")
    }
}