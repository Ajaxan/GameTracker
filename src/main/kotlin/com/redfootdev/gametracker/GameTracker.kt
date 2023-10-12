package com.redfootdev.gametracker

import com.redfootdev.gametracker.api.GameTrackerAPI
import com.redfootdev.gametracker.api.GameTrackerService
import com.redfootdev.gametracker.commands.CommandManager
import com.redfootdev.gametracker.game.GameManager
import com.redfootdev.gametracker.game.commandset.CommandSetManager
import com.redfootdev.gametracker.game.result.ResultManager
import com.redfootdev.gametracker.game.rule.RuleManager
import com.redfootdev.gametracker.utils.ComponentUtil
import com.redfootdev.gametracker.utils.text
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.format.NamedTextColor.*
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin


open class GameTracker : JavaPlugin() {

    lateinit var adventure: BukkitAudiences
    lateinit var console: ConsoleCommandSender
    lateinit var gameManager: GameManager
    lateinit var ruleManager: RuleManager
    lateinit var resultManager: ResultManager
    lateinit var commandSetManager: CommandSetManager
    lateinit var commandManager: CommandManager

    private val mainColor = GREEN
    private val secondaryColor = GOLD
    private val auxiliaryColor = LIGHT_PURPLE
    lateinit var comp: ComponentUtil


    override fun onEnable() {
        comp = ComponentUtil(mainColor, secondaryColor, auxiliaryColor)
        console = server.consoleSender
        console.sendMessage(comp.title("⋘⋘⋘", "GameTracker Starting Up", "⋙⋙⋙"))
        console.sendMessage(comp.line("[$name]", "---------------------------------"))
        console.sendMessage(comp.line("[$name]", "Version: ${this.description.version}"))
        console.sendMessage(comp.line("[$name]", "---------------------------------"))

        initializeConfig()
        initializeAPI()
        initializeManagers()
        initializeCommands()
    }

    override fun onDisable() {
        console.sendMessage(GOLD.text("⋘⋘⋘").append(GREEN.text("GameTracker Shutting Down")).append(GOLD.text("⋙⋙⋙")))
        /*adventure.close()*/
    }

    private fun initializeConfig() {
        //saveDefaultConfig()
    }

    private fun initializeAPI() {
        /*adventure = BukkitAudiences.create(this);*/
        server.servicesManager.register(GameTrackerAPI::class.java, GameTrackerService(this), this, ServicePriority.Normal)

    }

    private fun initializeCommands() {
        commandManager = CommandManager(this)
        commandManager.initialize()

    }

    private fun initializeManagers() {
        gameManager = GameManager(this)
        ruleManager = RuleManager(this)
        resultManager = ResultManager(this)
        commandSetManager = CommandSetManager(this)
    }

    /*fun adventure(): BukkitAudiences? {
        checkNotNull(this.adventure) { "Tried to access Adventure when the plugin was disabled!" }
        return this.adventure
    }*/

    /*fun logInfo(info: String) {
        console.sendMessage(NamedTextColor.WHITE.text("[${this::class.java.name}] ").append(info))
    }

    fun logWarning(warning: String) {
        console.sendMessage(NamedTextColor.YELLOW.text("[${this::class.java.name}] ").append(warning))
    }

    fun logError(error: String) {
        console.sendMessage(NamedTextColor.RED.text("[${this::class.java.name}] ").append(error))
    }*/
}