package com.redfootdev.gametracker.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Subcommand
import com.redfootdev.gametracker.GameTracker
import com.redfootdev.gametracker.game.Game
import net.kyori.adventure.text.format.NamedTextColor.*
import net.kyori.adventure.text.format.TextDecoration.*
import org.bukkit.command.CommandSender;

@CommandAlias("gametracker|gamet|gtracker|gt")
@CommandPermission("gametracker.gamemaster")
class GTGameCommand(gameTracker: GameTracker): BaseCommand() {

    private val comp = gameTracker.comp

    @Dependency
    private val gameManager = gameTracker.gameManager


    @Subcommand("game list")
    fun onGameList(sender: CommandSender) {
        val gameCommandMap = gameManager.listGames().associate { game ->
            val name = game.name
            val command = "gt game info $name"
            Pair(name,command)
        }
        val message = comp.listWithCommands("Games: ", gameCommandMap, "(Click games for more info)")
        sender.sendMessage(message)
    }

    @Subcommand("game info")
    fun onGameInfo(sender: CommandSender, game: Game) {
        val message = gameManager.displayGameInfo(game)
        sender.sendMessage(message)
    }

}