package com.redfootdev.gametracker.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.format.TextDecoration.*

import net.kyori.adventure.title.Title
import org.bukkit.entity.Player

fun String.c(): Component {
    return Component.text(this)
}

fun String.color(color: NamedTextColor = NamedTextColor.WHITE): Component {
    return this.c().style(Style.style(color))
}

fun String.command(command: String): Component {
    return this.c().clickEvent(ClickEvent.runCommand(command))
}

fun String.decorate(decoration: TextDecoration): Component {
    return this.c().decorate(decoration)
}

fun Component.append(text: String): Component {
    return this.append(Component.text(text))
}

fun NamedTextColor.text(text: String): Component {
    return Component.text(text, Style.style(this))
}

fun NamedTextColor.textCommand(text: String, command: String): Component {
    return Component.text(text, Style.style(this)).clickEvent(ClickEvent.runCommand(command))
}

fun Player.showTitle(title: Component, subtitle: Component) {
    this.showTitle(Title.title(title, title))
}

class ComponentUtil(
    private val mainColor: NamedTextColor,
    private val secondaryColor: NamedTextColor,
    private val auxiliaryColor: NamedTextColor
) {
    fun title(preTitle: String, title: String, postTitle: String): Component {
        return secondaryColor.text(preTitle).append(mainColor.text(title)).append(secondaryColor.text(postTitle))
    }

    fun line(mainText: String): Component {
        return mainColor.text(mainText).appendNewline()
    }

    fun line(mainText: String, secondaryText: String): Component {
        return mainColor.text(mainText).append(secondaryColor.text(secondaryText)).appendNewline()
    }

    fun line(mainText: String, secondaryComp: Component): Component {
        return mainColor.text(mainText).append(secondaryComp.color(secondaryColor)).appendNewline()
    }

    fun lineAux(mainText: String, auxiliaryText: String): Component {
        return mainColor.text(mainText).append(auxiliaryColor.text(auxiliaryText)).appendNewline()
    }

    fun lineAux(mainText: String, auxiliaryComp: Component): Component {
        return mainColor.text(mainText).append(auxiliaryComp.color(auxiliaryColor)).appendNewline()
    }

    fun line(mainText: String, secondaryText: String, auxiliaryText: String): Component {
        return mainColor.text(mainText).append(secondaryColor.text(secondaryText)).append(auxiliaryColor.text(auxiliaryText))
            .appendNewline()
    }

    fun list(listTitle: String, listItems: Collection<String>, helperText: String?): Component {
        val display = makeListTitle(listTitle, helperText)
        listItems.forEach { item ->
            display.append(mainColor.text(" - "))
                .append(secondaryColor.text(item))
                .appendNewline()
        }
        return display
    }

    fun listWithCommands(listTitle: String, itemsWithCommands: Map<String, String>, helperText: String?): Component {
        val display = makeListTitle(listTitle, helperText)
        itemsWithCommands.forEach { (item, command) ->
            display.append(mainColor.text(" - "))
                .append(secondaryColor.text(item)
                    .decorate(BOLD)
                    .clickEvent(ClickEvent.runCommand(command))
                ).appendNewline()
        }
        return display
    }

    fun listWithDescription(listTitle: String, itemsWithDesc: Map<String, String>, helperText: String?): Component {
        val display = makeListTitle(listTitle, helperText)
        itemsWithDesc.forEach { (item, desc) ->
            display.append(mainColor.text(" - "))
                .append(secondaryColor.text(item))
                .append(auxiliaryColor.text(desc)
                    .decorate(ITALIC))
                .appendNewline()
        }
        return display
    }

    private fun makeListTitle(listTitle: String, helperText: String?): Component {
        val helpText = helperText ?: ""
        return lineAux(listTitle, helpText.decorate(ITALIC).color(auxiliaryColor))
    }
}