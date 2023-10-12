package com.redfootdev.gametracker.utils

import org.bukkit.entity.Player

class ParseUtil {


    companion object {
        fun parsePhrase(phrase: String, token: String, replacement: String): String {
            return phrase.replace(token, replacement)
        }

        fun parsePhraseList(phrases: List<String>, token: String, replacement: String) : List<String> {
            return phrases.map { phrase -> parsePhrase(phrase,token,replacement) }
        }

        fun parseCommandsForPlayer(commands: List<String>, player: Player): List<String> {
            return parsePhraseList(commands, "%%p", player.name)
        }

        fun parseCommandsForPlayers(commands: List<String>, players: Collection<Player>): List<String> {
            return players.flatMap { player -> parseCommandsForPlayer(commands, player) }
        }
    }
}