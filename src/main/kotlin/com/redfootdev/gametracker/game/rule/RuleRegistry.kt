package com.redfootdev.gametracker.game.rule

import com.redfootdev.gametracker.common.DefaultOptions
import com.redfootdev.gametracker.common.EmptyOptions
import com.redfootdev.gametracker.common.OptionsMap
import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.result.Results
import com.redfootdev.gametracker.game.rule.type.*
import com.redfootdev.gametracker.game.rule.type.Timer
import com.redfootdev.gametracker.utils.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor.*
import java.util.*

/*enum class RuleType(val displayName: String, val description: String, val defaults: DefaultOptions) {
    TIMER("Timer", "A timer in minutes from the start of the game that triggers Results", TimerOptions),
    ALL_DEAD("AllDead", "When all players in a game are dead it triggers Results", EmptyOptions),
    ONE_TEAM("OneTeam", "All players are put on a single team at the start. This can trigger Results", OneTeamOptions);
    //TODO Add new types here

    fun createRule(game: Game, ruleData: RuleData): Rule {
        val results = ruleData.results.map { resultData -> resultData.type.createResult(game, resultData) }
        return when(this) {
            TIMER -> Timer(game, ruleData.name, results, ruleData.options)
            ALL_DEAD -> AllDead(game, ruleData.name, results, ruleData.options)
            ONE_TEAM -> OneTeam(game, ruleData.name, results, ruleData.options)
            //TODO build new types here
        }
    }

    fun displayType(): Component {
        return GREEN.text("Type: ").append(GOLD.text(this.displayName))
            .append(GREEN.text(" | Desc: ")).append(GOLD.text(this.description))
    }


    companion object {
        fun getType(typeName: String): RuleType {
            try { return valueOf(typeName.uppercase()) }
            catch (e: IllegalArgumentException) { throw GameTrackerException("No Rule with type: $typeName found") }
        }

        fun getTypeList(): List<String> {
            return Arrays.stream(RuleType.values())
                .map { t -> t.displayName }.toList()
        }

        fun getTypeListString(): String {
            return Arrays.stream(RuleType.values())
                .map { t -> t.displayName }
                .collect(Collectors.joining(", "))
        }
    }
}*/

data class RuleBuilder( val builder: (game: Game, ruleName: String, results: Results, options: OptionsMap) -> Rule)

data class RuleSchematic(val internalTypeName: String, val displayTypeName: String, val description: String, val defaultOptions: DefaultOptions, val ruleBuilder: RuleBuilder) {
    fun displaySchematic(): Component {
        return GREEN.text("Type: ").append(GOLD.text(displayTypeName))
            .append(GREEN.text(" | Desc: ")).append(GOLD.text(description))
    }
}

class RuleRegistry() {
    val ruleSchematics: HashMap<String, RuleSchematic> = hashMapOf()

    init {
        ruleSchematics[Timer.NAME] = RuleSchematic(Timer.NAME,"Timer",
            "A timer in minutes from the start of the game that triggers Results",
            TimerOptions,
            RuleBuilder { game, ruleName, results, options -> Timer(game, Timer.NAME, ruleName, results, options) }
        )
        ruleSchematics[AllDead.NAME] = RuleSchematic(AllDead.NAME,"All Dead",
            "When all players in a game are dead it triggers Results",
            EmptyOptions,
            RuleBuilder { game, ruleName, results, options -> AllDead(game, AllDead.NAME, ruleName, results, options) }
        )
        ruleSchematics[OneTeam.NAME] = RuleSchematic(OneTeam.NAME,"One Team",
            "All players are put on a single team at the start. This can trigger Results",
            OneTeamOptions,
            RuleBuilder { game, ruleName, results, options -> OneTeam(game, OneTeam.NAME, ruleName, results, options) }
            )
    }

    fun addSchematic(schematic: RuleSchematic) {
        ruleSchematics[schematic.internalTypeName] = schematic
    }

    fun buildSchematic(game: Game, ruleData: RuleData): Rule {
        val results = ruleData.results.map { resultData -> resultData.schematic.resultBuilder.builder(game, resultData.name, resultData.options) }
        val schematic = ruleData.schematic
        return schematic.ruleBuilder.builder(game, ruleData.name, results, ruleData.options)
    }

    fun getSchematic(internalTypeName: String): RuleSchematic {
        return ruleSchematics[internalTypeName] ?: throw GameTrackerException("No rule Schematic found with internalRuleName: $internalTypeName")
    }

}