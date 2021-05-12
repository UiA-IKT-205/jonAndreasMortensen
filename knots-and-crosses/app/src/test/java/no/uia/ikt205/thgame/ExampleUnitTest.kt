package no.uia.ikt205.thgame

import no.uia.ikt205.thgame.api.GameService
import no.uia.ikt205.thgame.api.data.Game
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {

    private var gameState: Game? = null
    private val firstPlayer:String = "Christian"
    private val secondPlayer:String = "Christian"
    private val initState = listOf(mutableListOf('0','0','0'), mutableListOf('0','0','0'), mutableListOf('0','0','0')) // listOf(listOf(0,0,0), listOf(0,0,0), listOf(0,0,0))

    @Test
    fun createGame(){
        GameService.createGame(firstPlayer,initState ){ state:Game?, err:Int? ->
            gameState = state
            assertNotNull(state)
            assertNotNull(state?.gameId)
            assertEquals(firstPlayer, state?.players?.get(0))
        }
    }


    fun JoinGame(){
        gameState?.let {
            GameService.joinGame(secondPlayer, it.gameId) { state:Game?, err:Int? ->
                gameState = state
                assertEquals(firstPlayer, state?.players?.get(0))
            }
        }
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}