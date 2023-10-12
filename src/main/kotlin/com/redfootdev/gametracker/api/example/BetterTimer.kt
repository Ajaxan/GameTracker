package com.redfootdev.gametracker.api.example

import com.redfootdev.gametracker.common.DefaultOptions
import com.redfootdev.gametracker.common.OptionData
import com.redfootdev.gametracker.common.OptionsData
import com.redfootdev.gametracker.common.OptionsMap
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.result.Results
import com.redfootdev.gametracker.game.rule.Rule
import com.redfootdev.gametracker.utils.TimeUtil
import org.bukkit.Bukkit


object BetterTimerOptions : DefaultOptions {
    override val optionsData: OptionsData = listOf(
            OptionData(BetterTimer.TIME, "Better Timer Time Limit", 15L, "# of minutes till timer runs out"),
            OptionData(BetterTimer.DISPLAY,"Better Display Timer Enabled", false, "true/false if timer should display on screen")
        )
}

class BetterTimer(
    game: Game,
    schematicName: String,
    name: String,
    results: Results,
    options: OptionsMap,
) : Rule(game, schematicName, name, results, options) {

    companion object {
        const val NAME = "bettertimer"
        const val TIME = "bettertime"
        const val DISPLAY = "betterdisplay"
    }

    private val time: Long = parseOptionLong(TIME)
    private val display: Boolean = parseOptionBoolean(DISPLAY)

    private var taskNumber: Int? = null


    override fun onStart() {
        taskNumber = Bukkit.getServer().scheduler.scheduleSyncDelayedTask(game.gameTracker, {
            results.forEach { it.activate() }
        }, TimeUtil.minutesToTicks(time))
    }

    override fun onRemoved() {
        taskNumber?.let {
            Bukkit.getServer().scheduler.cancelTask(it)
        }
    }

}