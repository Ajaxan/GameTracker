package com.redfootdev.gametracker.api

import com.redfootdev.gametracker.GameTracker
import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.commandset.TextCommandSet
import com.redfootdev.gametracker.game.result.Result
import com.redfootdev.gametracker.game.result.ResultData
import com.redfootdev.gametracker.game.result.ResultSchematic
import com.redfootdev.gametracker.game.rule.Rule
import com.redfootdev.gametracker.game.rule.RuleData
import com.redfootdev.gametracker.game.rule.RuleSchematic

class GameTrackerService(val gameTracker: GameTracker) : GameTrackerAPI {
    override fun getGame(gameName: String): Game {
        return gameTracker.gameManager.getGame(gameName)
    }

    override fun getRuleData(ruleDataName: String): RuleData {
        return gameTracker.ruleManager.rules[ruleDataName] ?: throw GameTrackerException("Could not find ruleData: $ruleDataName")
    }

    override fun getGameRule(game: Game, ruleName: String): Rule {
        return game.rules[ruleName] ?: throw GameTrackerException("Could not find rule: $ruleName")
    }

    override fun getGameRules(game: Game): HashMap<String, Rule> {
        return game.rules
    }

    override fun getResultData(resultDataName: String): ResultData {
        return gameTracker.resultManager.results[resultDataName] ?: throw GameTrackerException("Could not find resultData: $resultDataName")
    }

    override fun getRuleResults(rule: Rule, resultName: String): List<Result> {
        return rule.results
    }

    override fun getTextCommandSet(commandSetName: String): TextCommandSet {
        return gameTracker.commandSetManager.getTextCommandSet(commandSetName)
    }

    override fun registerRuleSchematic(schematic: RuleSchematic) {
        gameTracker.ruleManager.ruleRegistry.ruleSchematics[schematic.internalTypeName] = schematic
    }

    override fun unregisterRuleSchematic(schematicName: String) {
        gameTracker.ruleManager.ruleRegistry.ruleSchematics.remove(schematicName)
    }

    override fun registerResultSchematic(schematic: ResultSchematic) {
        gameTracker.resultManager.resultRegistry.resultSchematics[schematic.internalTypeName] = schematic
    }

    override fun unregisterResultSchematic(schematicName: String) {
        gameTracker.resultManager.resultRegistry.resultSchematics.remove(schematicName)
    }
}