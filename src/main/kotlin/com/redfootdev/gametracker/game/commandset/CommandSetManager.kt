package com.redfootdev.gametracker.game.commandset

import com.redfootdev.gametracker.GameTracker
import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.utils.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor.*
import java.util.EnumMap

enum class CommandType(val description: String) {
    START("Run at the start of a game for all game players."),
    END_WIN("Run at the end of a game for winning players."),
    END_LOSE("Run at the end of a game for losing players."),
    DEATH("Run when a player in a game dies."),
    RESPAWN("Run when a player in a game respawns.");

    companion object {
        fun getType(typeName: String): CommandType {
            try {
                return CommandType.valueOf(typeName.uppercase())
            } catch (e: IllegalArgumentException) {
                throw GameTrackerException("No Rule with type: $typeName found")
            }
        }

        fun displayTypes(): Component {
            val display = GREEN.text("Text Command Types").appendNewline()
            CommandType.values().forEach { type ->
                display.append(GREEN.text("${type.name}: ").append(GOLD.text(type.description))).appendNewline()
            }
            return display
        }
    }
}


typealias TextCommandSet = EnumMap<CommandType, ArrayList<String>>

fun TextCommandSet.set(type: CommandType): ArrayList<String> {
    return this[type] ?: throw GameTrackerException("$type CommandType missing from textCommandSet")
}



class CommandSetManager(private val gameTracker: GameTracker) {
    val textCommandSets: HashMap<String, TextCommandSet> = hashMapOf()

    fun displayTextCommandTypes(): Component {
        return CommandType.displayTypes()
    }

    fun listTextCommandSets(): List<TextCommandSet> {
        return textCommandSets.values.toList()
    }

    fun displayTextCommandsInSet(textCommandSetName: String) : Component {
        val textCommandSet = getTextCommandSet(textCommandSetName)
        val display = GREEN.text("Text Commands:").appendNewline()
        CommandType.values().forEach { type ->
            display.append(GREEN.text(" - ${type.name}:")).appendNewline()
            textCommandSet[type]?.forEachIndexed { index, command ->
                display.append(GREEN.text("   ($index): ").append(GOLD.text(command))).appendNewline()
            } ?: throw GameTrackerException("Tried to find commandType that wasn't found: ${type.name}")
        }
        return display
    }

    fun createTextCommandSet(textCommandSetName: String) {
        textCommandSets[textCommandSetName] = TextCommandSet(CommandType::class.java)
    }

    fun removeTextCommandSet(textCommandSetName: String) {
        textCommandSets.remove(textCommandSetName) ?: throw GameTrackerException("No command set found with name: $textCommandSetName")
    }

    fun addTextCommand(textCommandSetName: String, typeName: String, command: String) {
        val textCommandSet = getTextCommandSet(textCommandSetName)
        val commandType = CommandType.getType(typeName)
        textCommandSet[commandType]?.add(command) ?: throw GameTrackerException("Tried to find commandType that wasn't found: $commandType")
    }

    fun removeTextCommand(textCommandSetName: String, typeName: String, commandNumberString: String) {
        val textCommandSet = getTextCommandSet(textCommandSetName)
        val commandType = CommandType.getType(typeName)
        try {
            val commandNumber = commandNumberString.toInt()
            textCommandSet[commandType]?.removeAt(commandNumber) ?: throw GameTrackerException("Tried to find commandType that wasn't found: $commandType")
        }
        catch (e:NumberFormatException) { throw GameTrackerException("Tried to remove command with a proper command number: $commandNumberString") }
    }

    fun getTextCommandSet(textCommandSetName: String): TextCommandSet {
        return textCommandSets[textCommandSetName] ?: throw GameTrackerException("No command set found with name: $textCommandSetName")
    }


}