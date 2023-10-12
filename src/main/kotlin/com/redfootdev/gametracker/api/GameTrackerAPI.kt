package com.redfootdev.gametracker.api

import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.commandset.TextCommandSet
import com.redfootdev.gametracker.game.result.ResultData
import com.redfootdev.gametracker.game.result.Result
import com.redfootdev.gametracker.game.result.ResultSchematic
import com.redfootdev.gametracker.game.rule.Rule
import com.redfootdev.gametracker.game.rule.RuleData
import com.redfootdev.gametracker.game.rule.RuleSchematic

interface GameTrackerAPI {

    fun getGame(gameName: String): Game

    fun getRuleData(ruleDataName: String): RuleData

    fun getGameRule(game: Game, ruleName: String): Rule

    fun getGameRules(game: Game): HashMap<String, Rule>

    fun getResultData(resultDataName: String): ResultData

    fun getRuleResults(rule: Rule, resultName: String): List<Result>

    fun getTextCommandSet(commandSetName: String): TextCommandSet

    fun registerRuleSchematic(schematic: RuleSchematic)

    fun unregisterRuleSchematic(schematicName: String)

    fun registerResultSchematic(schematic: ResultSchematic)

    fun unregisterResultSchematic(schematicName: String)
}