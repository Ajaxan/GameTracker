package com.redfootdev.gametracker.game.result.type

import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.result.Result

class AllLose(
    game: Game,
    schematicName: String,
    name:String,
    options: HashMap<String, String>
) : Result(game, schematicName, name, options) {

    companion object {
        const val NAME = "allLose"
    }

    override fun activate() {
        game.end(emptyList(), game.gamePlayers.values)
    }
}