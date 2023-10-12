package com.redfootdev.gametracker.game.rule.type

import com.redfootdev.gametracker.common.DefaultOptions
import com.redfootdev.gametracker.common.OptionData
import com.redfootdev.gametracker.common.OptionsData
import com.redfootdev.gametracker.common.OptionsMap
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.Team
import com.redfootdev.gametracker.game.result.Results
import com.redfootdev.gametracker.game.result.activate
import com.redfootdev.gametracker.game.rule.Rule
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

private const val TEAM_COLOR = "teamColor"
private const val TEAM_PATTERN = "teamPattern"
private const val COLOR_VISIBLE = "colorVisible"
private const val FRIENDLY_FIRE = "friendlyFire"

object OneTeamOptions : DefaultOptions {
    override val optionsData: OptionsData
        get() = listOf(
            OptionData(TEAM_COLOR,"Team Color", TrimMaterial.GOLD, "Team color to use. Can be any of the armor trim materials."),
            OptionData(TEAM_PATTERN,"Team Pattern", TrimPattern.COAST, "Team pattern to use. Can be any of the armor trim patterns"),
            OptionData(COLOR_VISIBLE,"Team Color Visible", false, "true/false if team color should be applied to players."),
            OptionData(FRIENDLY_FIRE, "Friendly Fire Enabled", false, "true/false if team members can attack each other."),
        )
}

class OneTeam(
    game: Game,
    schematicName: String,
    name: String,
    results: Results,
    options: OptionsMap,
) : Rule(game, schematicName, name, results, options) {

    companion object {
        const val NAME = "oneTeam"
        const val TEAM_COLOR = "teamColor"
        const val TEAM_PATTERN = "teamPattern"
        const val COLOR_VISIBLE = "colorVisible"
        const val FRIENDLY_FIRE = "friendlyFire"
    }

    private val teamColor: TrimMaterial = parseOptionTrimMaterial(TEAM_COLOR)
    private val teamPattern: TrimPattern = parseOptionTrimPattern(TEAM_PATTERN)
    private val colorVisible: Boolean = parseOptionBoolean(COLOR_VISIBLE)
    private val friendlyFire: Boolean = parseOptionBoolean(FRIENDLY_FIRE)

    override fun onStart() {
        val teamName = "allPlayerTeam"
        val allPlayerTeam = Team(teamName, teamColor, teamPattern, colorVisible, friendlyFire)
        game.gamePlayers.values.forEach { gamePlayer ->
            game.switchTeam(gamePlayer, allPlayerTeam)
        }
        results.activate()
    }

}