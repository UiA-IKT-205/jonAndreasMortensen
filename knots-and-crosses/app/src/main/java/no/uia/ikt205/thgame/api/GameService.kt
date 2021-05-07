package no.uia.ikt205.knotsandcrosses.api


import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import no.uia.ikt205.knotsandcrosses.App
import no.uia.ikt205.knotsandcrosses.R
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.api.data.GameState
import no.uia.ikt205.thgame.GameManager
import org.json.JSONArray
import org.json.JSONObject


typealias GameServiceCallback = (state:Game?, errorCode:Int? ) -> Unit

/*  NOTE:
    Using object expression to make GameService a Singleton.
    Why? Because there should only be one active GameService ever.
 */

object GameService {

    val TAG:String = "GameService"

    var state: String = "[[\"0\",\"0\",\"0\"],[\"0\",\"0\",\"0\"],[\"0\",\"0\",\"0\"]]"

    /// NOTE: Do not want to have App.context all over the code. Also it is nice if we later want to support different contexts
    private val context = App.context

    /// NOTE: Good practice to use a que for performing requests.
    private val requestQue:RequestQueue = Volley.newRequestQueue(context)

    /// NOTE: One possible way of constructing a list of API url. You want to construct the urls so that you can support different environments (i.e. Debug, Test, Prod etc)
    private enum class APIEndpoints(val url:String) {
        CREATE_GAME("%1s%2s%3s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(R.string.base_path))),
        JOIN_GAME("%1s%2s%3s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(R.string.join_game_path)))
    }


    fun createGame(playerId:String, state: GameState, callback:GameServiceCallback) {
        val url = APIEndpoints.CREATE_GAME.url

        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("state", JSONArray(state))

        val request = object : JsonObjectRequest(Request.Method.POST,url, requestData,
            {
                // Success game created.

                val body = it.toString(0)
                val json = JSONObject(body)
                val players = json.getString("players")
                val gameId = json.getString("gameId")
                val stateFromRequest = json.getString("state")

                Log.d(TAG,"Players: $players")

                GameManager.players = players

                Log.d(TAG,"Game ID: $gameId")

                GameManager.gameId = gameId

                Log.d(TAG,"State: $stateFromRequest")

                val game = Gson().fromJson(body, Game::class.java)
                callback(game,null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

    fun joinGame(playerId:String, gameId:String, callback: GameServiceCallback){
        val URL = "https://generic-game-service.herokuapp.com/Game/$gameId/join"

        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("gameId", gameId)

        val request = object : JsonObjectRequest(Request.Method.POST,URL, requestData,
            {
                // Success game joined.

                val body = it.toString(0)
                val json = JSONObject(body)
                val players = json.getString("players")
                val gameIdFromRequest = json.getString("gameId")
                val stateFromRequest = json.getString("state")

                Log.d(TAG,"Players: $players")

                GameManager.players = players

                Log.d(TAG,"Game ID: $gameIdFromRequest")

                GameManager.gameId = gameIdFromRequest

                Log.d(TAG,"State: $stateFromRequest")

                val result = it.toString(0)
                println(result)
                val game = Gson().fromJson(result, Game::class.java)
                callback(game,null)
            }, {
                // Error joining game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

    fun updateGame(gameId:String, gameState: GameState, callback: GameServiceCallback){
        val URL = "https://generic-game-service.herokuapp.com/Game/$gameId/update"

        val requestData = JSONObject()
        requestData.put("gameId", gameId)
        requestData.put("state", JSONArray(gameState))

        val request = object : JsonObjectRequest(Request.Method.POST,URL, requestData,
            {
                // Success game updated.

                val body = it.toString(0)
                val json = JSONObject(body)
                val players = json.getString("players")
                val gameIdFromRequest = json.getString("gameId")
                val stateFromRequest = json.getString("state")

                Log.d(TAG,"Players: $players")

                Log.d(TAG,"Game ID: $gameIdFromRequest")

                Log.d(TAG,"State: $stateFromRequest")

                val game = Gson().fromJson(it.toString(0), Game::class.java)
                callback(game,null)
            }, {
                // Error updating game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

    fun pollGame(gameId: String,callback:GameServiceCallback){
        val URL = "https://generic-game-service.herokuapp.com/Game/$gameId/poll"

        val requestData = JSONObject()
        requestData.put("gameId", gameId)

        val request = object : JsonObjectRequest(Request.Method.GET,URL, requestData,
            {
                // Success game polled.

                val body = it.toString(0)
                val json = JSONObject(body)
                val players = json.getString("players")
                val gameIdFromRequest = json.getString("gameId")
                val stateFromRequest = json.getString("state")

                Log.d(TAG,"Players: $players")

                Log.d(TAG,"Game ID: $gameIdFromRequest")

                Log.d(TAG,"State: $stateFromRequest")

                state = stateFromRequest

                val game = Gson().fromJson(it.toString(0), Game::class.java)
                callback(game,null)
            }, {
                // Error polling game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

}