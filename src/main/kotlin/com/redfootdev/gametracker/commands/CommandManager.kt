package com.redfootdev.gametracker.commands

import co.aikar.commands.PaperCommandManager
import com.google.common.collect.ImmutableList
import com.redfootdev.gametracker.GameTracker
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.result.ResultData
import com.redfootdev.gametracker.game.rule.RuleData
import java.util.*

class CommandManager(val gameTracker: GameTracker) {
    private var manager = PaperCommandManager(gameTracker)

    private val gameManager = gameTracker.gameManager
    private val ruleManager = gameTracker.ruleManager
    private val resultManager = gameTracker.resultManager
    private val commandSetManager = gameTracker.commandSetManager

    fun initialize() {

    }

    fun registerCommands() {
        manager.registerCommand(GTGameCommand(gameTracker))

    }

    fun registerContextHandler() {

        /*manager.commandContexts.registerContext(ResultType::class.java) {
            val issuer = it.issuer
            val resultTypeName: String = it.popFirstArg()
            ResultType.getType(resultTypeName)
        }

        manager.commandContexts.registerContext(RuleType::class.java) {
            val issuer = it.issuer
            val ruleTypeName: String = it.popFirstArg()
            RuleType.getType(ruleTypeName)
        }*/

        manager.commandContexts.registerContext(Game::class.java) {
            val issuer = it.issuer
            val gameName: String = it.popFirstArg()
            gameManager.getGame(gameName)
        }

        manager.commandContexts.registerContext(RuleData::class.java) {
            val issuer = it.issuer
            val ruleDataName: String = it.popFirstArg()
            ruleManager.getRuleData(ruleDataName)
        }

        manager.commandContexts.registerContext(ResultData::class.java) {
            val issuer = it.issuer
            val resultDataName: String = it.popFirstArg()
            ruleManager.getResultData(resultDataName)
        }

        manager.commandContexts.registerContext(EnumMap::class.java) {
            val issuer = it.issuer
            val textCommandSetName: String = it.popFirstArg()
            commandSetManager.getTextCommandSet(textCommandSetName)
        }
    }

    fun registerCompletionHandler() {
        /*manager.commandCompletions.registerAsyncCompletion("resultType") {
            ImmutableList.copyOf(ResultType.getTypeList().map { it.lowercase() })
        }

        manager.commandCompletions.registerAsyncCompletion("ruleType") {
            ImmutableList.copyOf(RuleType.getTypeList().map { it.lowercase() })
        }*/

        manager.commandCompletions.registerAsyncCompletion("game") {
            ImmutableList.copyOf(gameManager.games.keys)
        }

        manager.commandCompletions.registerAsyncCompletion("rule") {
            ImmutableList.copyOf(ruleManager.rules.keys)
        }

        manager.commandCompletions.registerAsyncCompletion("result") {
            ImmutableList.copyOf(resultManager.results.keys)
        }

        manager.commandCompletions.registerAsyncCompletion("commands") {
            ImmutableList.copyOf(commandSetManager.textCommandSets.keys)
        }
    }
}