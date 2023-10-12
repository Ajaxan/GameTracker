package com.redfootdev.gametracker.game

import com.redfootdev.gametracker.GameTracker
import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.game.commandset.CommandType
import com.redfootdev.gametracker.game.commandset.TextCommandSet
import com.redfootdev.gametracker.game.rule.Rule
import com.redfootdev.gametracker.utils.c
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class GameManager(private val gameTracker: GameTracker) {

    val games: HashMap<String, Game> = hashMapOf()

    fun listGames(): List<Game> {
        return games.values.toList()
    }

    fun displayGameInfo(game: Game): Component {
        val display = "Game Display Placeholder".c()
        game.rules.values.forEach { display.append(it.display()) }
        display.append("display commands here".c())
        return display
    }

    fun createGame(gameName: String) {
        games[gameName] = Game(gameTracker, gameName)
    }

    fun removeGame(gameName: String) {
        games.remove(gameName) ?: throw GameTrackerException("No game found with name: $gameName")
    }

    fun listGamePlayers(gameName: String): List<GamePlayer> {
        val game = getGame(gameName)
        return game.gamePlayers.values.toList()
    }

    fun addGamePlayer(gameName: String, playerName: String) {
        val game = getGame(gameName)
        val player = getPlayer(playerName)
        game.addPlayer(player)
    }

    fun removeGamePlayer(gameName: String, playerName: String) {
        val game = getGame(gameName)
        val player = getPlayer(playerName)
        game.removePlayer(player)
    }

    fun listRules(gameName: String): List<GamePlayer> {
        val game = getGame(gameName)
        return game.gamePlayers.values.toList()
    }

    fun addRule(gameName: String, ruleName: String) {
        val game = getGame(gameName)
        val rule = gameTracker.ruleManager.createRule(game, ruleName)
        game.addRule(rule)
    }

    fun removeRule(gameName: String, ruleName: String) {
        val game = getGame(gameName)
        game.removeRule(ruleName)
    }

    fun setCommandSet(gameName: String, textCommandSetName: String) {
        val game = getGame(gameName)
        val textCommandSet = gameTracker.commandSetManager.getTextCommandSet(textCommandSetName)
        game.commandSet = TextCommandSet(textCommandSet)
    }

    fun removeCommandSet(gameName: String) {
        val game = getGame(gameName)
        game.commandSet = TextCommandSet(CommandType::class.java)
    }

    fun startGame(gameName: String) {
        val game = getGame(gameName)
    }

    fun endGame(gameName: String, winners: List<String>?, losers: List<String>?) {
        val game = getGame(gameName)
    }

    fun respawnPlayer(gameName: String, playerName: String) {
        val game = getGame(gameName)
    }

    fun respawnAll(gameName: String) {
        val game = getGame(gameName)
    }

    fun setStartTitle(gameName: String, title: String) {
        val game = getGame(gameName)
        game.startTitle = title
    }

    fun setStartSubtitle(gameName: String, subtitle: String) {
        val game = getGame(gameName)
        game.startSubtitle = subtitle
    }

    fun setWinTitle(gameName: String, title: String) {
        val game = getGame(gameName)
        game.winTitle = title
    }

    fun setWinSubtitle(gameName: String, subtitle: String) {
        val game = getGame(gameName)
        game.winSubtitle = subtitle
    }

    fun setLoseTitle(gameName: String, title: String) {
        val game = getGame(gameName)
        game.loseTitle = title
    }

    fun setLoseSubtitle(gameName: String, subtitle: String) {
        val game = getGame(gameName)
        game.loseSubtitle = subtitle
    }

    fun getGame(gameName: String): Game {
        return games[gameName] ?: throw GameTrackerException("Unable to find game with name: $gameName")
    }

    private fun getPlayer(playerName: String): Player {
        return gameTracker.server.getPlayer(playerName) ?: throw GameTrackerException("Unable to find player with name: $playerName")
    }

    private fun getRule(game: Game, ruleName: String): Rule {
        return game.rules[ruleName] ?: throw GameTrackerException("Unable to find rule with name: $ruleName")
    }

}