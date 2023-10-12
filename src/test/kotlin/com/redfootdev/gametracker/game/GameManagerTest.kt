package com.redfootdev.gametracker.game

import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.MockBukkit
import com.redfootdev.gametracker.GameTracker
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test



class GameManagerTest {
    lateinit var server: ServerMock
    lateinit var plugin: GameTracker

    @BeforeEach
    fun setUp() {
        try {
            // Start the mock server
            server = MockBukkit.mock()
            // Load your plugin
            plugin = MockBukkit.load(GameTracker::class.java)
        } catch(error: Exception) {
            println(error)
        }

    }

    @AfterEach
    fun tearDown() {
        try {
            println("got here")
            // Stop the mock server
            MockBukkit.unmock()
        } catch(error: Exception) {
            println(error)
        }

    }

    @Test
    fun healthcheck() : Unit {
        Assertions.assertEquals(0, 0)
    }

    @Test
    fun createAndFindGame() {
        try {
            val testGameName = "testGame"
            val gameManager = GameManager(plugin)
            gameManager.createGame(testGameName)
            val testGame = gameManager.getGame(testGameName)
            Assertions.assertEquals(testGameName, testGame.name)
            Assertions.assertEquals(0, testGame.rules.size)
            Assertions.assertEquals(0, testGame.gamePlayers.size)
            Assertions.assertEquals(0, testGame.teams.size)
        } catch(error: Exception) {
            println(error)
        }

    }
}