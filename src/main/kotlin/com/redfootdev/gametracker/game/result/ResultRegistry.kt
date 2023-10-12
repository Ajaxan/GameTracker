package com.redfootdev.gametracker.game.result

import com.redfootdev.gametracker.common.DefaultOptions
import com.redfootdev.gametracker.common.EmptyOptions
import com.redfootdev.gametracker.common.OptionsMap
import com.redfootdev.gametracker.exceptions.GameTrackerException
import com.redfootdev.gametracker.game.Game
import com.redfootdev.gametracker.game.result.type.AllLose
import com.redfootdev.gametracker.game.result.type.AllWin
import com.redfootdev.gametracker.game.result.type.LivingWin
import com.redfootdev.gametracker.utils.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*

/*
enum class ResultType(val displayName: String, val description: String, val defaults: DefaultOptions) {
    ALL_LOSE("AllLose", "When triggered all game players lose.", EmptyOptions),
    ALL_WIN("AllWin", "When triggered all game players win", EmptyOptions),
    LIVING_WIN("LivingWin", "When triggered all living players win", EmptyOptions);
    //TODO Add new types here

    fun createResult(game: Game, resultData: ResultData): Result {
        return when(this) {
            ALL_LOSE -> AllLose(game, resultData.name, resultData.options)
            ALL_WIN -> AllWin(game, resultData.name, resultData.options)
            LIVING_WIN -> LivingWin(game, resultData.name, resultData.options)
            //TODO Add new types here
        }
    }

    fun displayType(): Component {
        return NamedTextColor.GREEN.text("Type: ").append(NamedTextColor.GOLD.text(this.displayName))
            .append(NamedTextColor.GREEN.text(" | Desc: ")).append(NamedTextColor.GOLD.text(this.description))
    }

    companion object {
        fun getType(typeName: String): ResultType {
            try { return valueOf(typeName.uppercase()) }
            catch (e: IllegalArgumentException) { throw GameTrackerException("No Result with type: $typeName found") }
        }

        fun getTypeList(): List<String> {
            return Arrays.stream(ResultType.values())
                .map { t -> t.displayName }.toList()
        }

        fun getTypeListString(): String {
            return Arrays.stream(ResultType.values())
                .map { t -> t.displayName }
                .collect(Collectors.joining(", "))
        }
    }

}*/

data class ResultBuilder( val builder: (game: Game, resultName: String, options: OptionsMap) -> Result)

data class ResultSchematic(val internalTypeName: String, val displayTypeName: String, val description: String, val defaultOptions: DefaultOptions, val resultBuilder: ResultBuilder) {
    fun displaySchematic(): Component {
        return NamedTextColor.GREEN.text("Type: ").append(NamedTextColor.GOLD.text(displayTypeName))
            .append(NamedTextColor.GREEN.text(" | Desc: ")).append(NamedTextColor.GOLD.text(description))
    }
}

class ResultRegistry() {
    val resultSchematics: HashMap<String, ResultSchematic> = hashMapOf()

    init {

        resultSchematics[LivingWin.NAME] = ResultSchematic(
            LivingWin.NAME,
            "All Lose",
            "When triggered all game players lose.",
            EmptyOptions,
            ResultBuilder { game, resultName, options -> AllLose(game, LivingWin.NAME, resultName, options) }
        )
        resultSchematics[AllWin.NAME] = ResultSchematic(
            AllWin.NAME,
            "All Win",
            "When triggered all game players win",
            EmptyOptions,
            ResultBuilder { game, resultName, options -> AllWin(game, AllWin.NAME, resultName, options) }
        )
        resultSchematics[LivingWin.NAME] = ResultSchematic(
            LivingWin.NAME,
            "Living Win",
            "When triggered all living players win",
            EmptyOptions,
            ResultBuilder { game, resultName, options -> LivingWin(game, LivingWin.NAME, resultName, options) }
        )
    }

    fun addSchematic(schematic: ResultSchematic) {
        resultSchematics[schematic.internalTypeName] = schematic
    }

    fun buildSchematic(game: Game, resultData: ResultData): Result {
        val schematic = resultData.schematic
        return schematic.resultBuilder.builder(game, resultData.name, resultData.options)
    }

    fun getSchematic(internalTypeName: String): ResultSchematic {
        return resultSchematics[internalTypeName]
            ?: throw GameTrackerException("No result Schematic found with internalResultName: $internalTypeName")
    }
}
    
