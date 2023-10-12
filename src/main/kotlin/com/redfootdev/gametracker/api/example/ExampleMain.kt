package com.redfootdev.gametracker.api.example

import com.redfootdev.gametracker.api.GameTrackerAPI
import com.redfootdev.gametracker.game.rule.RuleBuilder
import com.redfootdev.gametracker.game.rule.RuleSchematic
import com.redfootdev.gametracker.game.rule.type.Timer
import org.bukkit.plugin.java.JavaPlugin

// I don't actually run this code internally, so if it doesn't work, let me know.
class ExampleMain() : JavaPlugin() {

    private val gameTrackerApi = this.server.servicesManager.load(GameTrackerAPI::class.java) ?: throw Exception("Failed to load GameTracker API")

    init {
        // Register a Rule that can be used in games
        val schematicBuilder = RuleBuilder { game, ruleName, results, options -> BetterTimer(game, Timer.NAME, ruleName, results, options) }
        val ruleSchematic = RuleSchematic(BetterTimer.NAME, "Better Timer", "This is a better version of the default timer", BetterTimerOptions, schematicBuilder)
        gameTrackerApi.registerRuleSchematic(ruleSchematic)

        // Unregister a rule
        gameTrackerApi.unregisterRuleSchematic(BetterTimer.NAME)
    }

}