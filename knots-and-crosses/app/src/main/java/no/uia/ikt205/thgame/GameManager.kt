package no.uia.ikt205.thgame

import android.util.Log
import no.uia.ikt205.knotsandcrosses.api.GameService
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.api.data.GameState

object GameManager {

    val TAG:String = "GameManager"

    var players:String? = null
    var state:String? = null
    var gameId:String? = null

    val StartingGameState: GameState = arrayOf(charArrayOf('0','0','0'), charArrayOf('0','0','0'), charArrayOf('0','0','0'))

    fun createGame(player:String){
        GameService.createGame(player,StartingGameState) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                Log.e(TAG,"Failed to create game!")
            } else {
                /// TODO("We have a game. What to do?)
                Log.d(TAG,"Game created with player ID: $player")
                if (game != null) {
                    Log.d(TAG,"Game created with game ID: $gameId")
                }
            }
        }
    }

    fun joinGame(player:String, gameId:String){
        GameService.joinGame(player,gameId) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                Log.e(TAG,"Failed to join game!")
            } else {
                /// TODO("We have a game. What to do?)
                Log.d(TAG,"Game joined with player ID: $player and Game ID: $gameId")
            }
        }
    }

    fun updateGame(gameId: String, gameState: GameState){

        GameService.updateGame(gameId,gameState) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                Log.e(TAG,"Failed to update game!")
            } else {
                /// TODO("We have a game. What to do?)
                Log.d(TAG,"Game with ID: $gameId updated")
            }
        }
    }

    fun pollGame(gameId: String){

        GameService.pollGame(gameId) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                Log.e(TAG,"Failed to poll game!")
            } else {
                /// TODO("We have a game. What to do?)
                Log.d(TAG,"Polled game with ID: $gameId")
            }
        }
    }

}