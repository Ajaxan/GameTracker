package com.redfootdev.gametracker.common

import net.kyori.adventure.text.Component

interface Displayable {
    fun displayType() : Component
    fun displayInfo() : Component
    fun display() : Component
}