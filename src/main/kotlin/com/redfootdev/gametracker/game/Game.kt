package com.redfootdev.gametracker.game

import com.redfootdev.gametracker.GameTracker
import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.game.commandset.CommandType
import com.redfootdev.gametracker.game.commandset.set
import com.redfootdev.gametracker.game.commandset.TextCommandSet
import com.redfootdev.gametracker.game.rule.Rule
import com.redfootdev.gametracker.utils.ParseUtil
import com.redfootdev.gametracker.utils.color
import com.redfootdev.gametracker.utils.showTitle
import com.redfootdev.gametracker.utils.text
import net.kyori.adventure.text.format.NamedTextColor.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent


data class GamePlayer (
    val player: Player,
    var team: Team,
    var isAlive: Boolean = true,
    var deathTime: Long? = null
)

class Game (val gameTracker: GameTracker, val name: String) : Listener {

    var startTitle = "Begin"
    var startSubtitle = "The game has begun!"
    var winTitle = "Victory"
    var winSubtitle = "The day has been won!"
    var loseTitle = "Defeat"
    var loseSubtitle = "The day has been lost..."

    val gamePlayers: HashMap<Player, GamePlayer> = hashMapOf()
    val livingPlayers: HashMap<Player, GamePlayer> = hashMapOf()
    val deadPlayers: HashMap<Player, GamePlayer> = hashMapOf()
    val teams: HashMap<String, Team> = hashMapOf()
    val rules: HashMap<String, Rule> = hashMapOf()
    var commandSet: TextCommandSet = TextCommandSet(CommandType::class.java)

    fun addPlayer(player: Player) {
        val playerTeam = Team(player)
        val gamePlayer = GamePlayer(player, playerTeam)
        playerTeam.addPlayer(gamePlayer)
        gamePlayers[player] = gamePlayer
    }

    fun removePlayer(player: Player) {
        val gamePlayer = gamePlayers[player] ?: return
        gamePlayers.remove(player)
        val playerTeam = gamePlayer.team
        if(playerTeam.players.isEmpty()) teams.remove(playerTeam.name)
    }

    fun addRule(rule: Rule) {
        rule.onAdded()
        rules[rule.name] = rule
    }

    fun removeRule(ruleName: String) {
        val rule = rules.remove(ruleName) ?: throw GameTrackerException("No rule found with name: $ruleName")
        rule.onRemoved()
    }

    fun switchTeam(gamePlayer: GamePlayer, newTeam: Team) {
        val oldTeam = gamePlayer.team
        if(!teams.containsValue(newTeam)) teams[newTeam.name] = newTeam
        oldTeam.removePlayer(gamePlayer)
        newTeam.addPlayer(gamePlayer)
        gamePlayer.team = newTeam
        if(oldTeam.players.isEmpty()) teams.remove(oldTeam.name)
    }

    fun start() {
        livingPlayers.putAll(gamePlayers)
        gamePlayers.values.forEach { gamePlayer -> gamePlayer.player.showTitle(startTitle.color(LIGHT_PURPLE), startSubtitle.color(GOLD)) }
        rules.values.forEach { rule -> rule.onStart() }
        runCommands(commandSet.set(CommandType.START), gamePlayers.values.map{ gamePlayer -> gamePlayer.player })
        gameTracker.server.pluginManager.registerEvents(this, gameTracker)

    }

    fun end(winners: Collection<GamePlayer>, losers: Collection<GamePlayer>) {
        winners.forEach { winner -> winner.player.showTitle(GREEN.text(winTitle), GOLD.text(winSubtitle)) }
        losers.forEach { loser -> loser.player.showTitle(RED.text(loseTitle), GOLD.text(loseSubtitle)) }
        rules.values.forEach { rule -> rule.onEnd() }
        runCommands(commandSet.set(CommandType.END_WIN), winners.map{it.player})
        runCommands(commandSet.set(CommandType.END_LOSE), losers.map{it.player})
        HandlerList.unregisterAll(this)
    }

    fun death(player: Player) {
        val gamePlayer = livingPlayers.remove(player) ?: throw GameTrackerException("death method called on a player not in this game: ${player.name}")
        deadPlayers[player] = gamePlayer
        rules.values.forEach { rule -> rule.onDeath(gamePlayer) }
        runCommands(commandSet.set(CommandType.DEATH), listOf(player))

    }

    fun respawn(player: Player) {
        val gamePlayer = deadPlayers.remove(player) ?: throw GameTrackerException("respawn method called on a player not in this game: ${player.name}")
        livingPlayers[player] = gamePlayer
        rules.values.forEach { rule -> rule.onRespawn(gamePlayer) }
        runCommands(commandSet.set(CommandType.RESPAWN), listOf(player))
    }

    fun runCommands(commands: List<String>, players: Collection<Player>) {
        val parsedCommands = ParseUtil.parseCommandsForPlayers(commands, players)
        parsedCommands.forEach {command -> gameTracker.server.dispatchCommand(gameTracker.server.consoleSender, command) }
    }

    @EventHandler
    fun onGamePlayerDeath(event: PlayerDeathEvent) {
        val deadPlayer = event.player
        if(gamePlayers[deadPlayer] == null) return
        death(deadPlayer)
    }

    @EventHandler
    fun onGamePlayerDamage(event: EntityDamageByEntityEvent) {
        if(event.damager !is Player || event.entity !is Player) return
        val attacker = gamePlayers[event.damager as Player] ?: return
        val defender = gamePlayers[event.entity as Player] ?: return
        if(attacker.team.players.contains(defender) && !attacker.team.friendlyFire) event.isCancelled = true
    }


}