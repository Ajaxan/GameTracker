package com.redfootdev.gametracker.game.rule.type

import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.GamePlayer
import com.redfootdev.gametracker.game.result.Result
import com.redfootdev.gametracker.game.rule.Rule

class AllDead(
    game: Game,
    schematicName: String,
    name: String,
    results: List<Result>,
    options: HashMap<String, String>,
) : Rule(game, schematicName, name, results, options) {

    companion object {
        const val NAME = "allDead"
    }

    override fun onDeath(gamePlayer: GamePlayer) {
        val player = gamePlayer.player
        game.gamePlayers.containsKey(player)
        if(game.livingPlayers.isEmpty()) {
            results.forEach {result -> result.activate()}
        }
    }
}