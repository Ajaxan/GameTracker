package com.redfootdev.gametracker.game.rule.type

import com.redfootdev.gametracker.common.DefaultOptions
import com.redfootdev.gametracker.common.OptionData
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.result.Result
import com.redfootdev.gametracker.game.rule.Rule
import com.redfootdev.gametracker.utils.TimeUtil
import org.bukkit.Bukkit


object TimerOptions : DefaultOptions {
    override val optionsData: List<OptionData>
        get() = listOf(
            OptionData(Timer.TIME, "Timer Time Limit", 15L, "# of minutes till timer runs out"),
            OptionData(Timer.DISPLAY,"Display Timer Enabled", false, "true/false if timer should display on screen")
        )
}

class Timer(
    game: Game,
    schematicName: String,
    name: String,
    results: List<Result>,
    options: HashMap<String, String>,
) : Rule(game, schematicName, name, results, options) {

    companion object {
        const val NAME = "timer"
        const val TIME = "time"
        const val DISPLAY = "display"
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