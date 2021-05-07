package no.uia.ikt205.knotsandcrosses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*
import no.uia.ikt205.knotsandcrosses.databinding.ActivityGameBinding
import no.uia.ikt205.thgame.GameManager
import no.uia.ikt205.knotsandcrosses.api.GameService
import kotlin.random.Random
import kotlin.system.exitProcess

// var turn = Random.nextInt(0,2)

class GameActivity : AppCompatActivity() {

    private lateinit var binding:ActivityGameBinding

    val TAG = "GameActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameId = GameManager.gameId.toString()
        var players = GameManager.players.toString()
        // val player = intent.getStringExtra(EXTRA_MESSAGE)

        val gameSigns = arrayOf('X','O')
        val playerSign = gameSigns.random()

        findViewById<TextView>(R.id.players_id).apply {
            val regex = Regex("[^A-Za-z0-9]")
            val result = regex.replace(players, " ")
            text = result
        }

        findViewById<TextView>(R.id.player_id).apply {
            text = gameId
        }

        var rowOne = charArrayOf('0','0','0')
        var rowTwo = charArrayOf('0','0','0')
        var rowThree = charArrayOf('0','0','0')

        var gameState = arrayOf(rowOne,rowTwo,rowThree)

        // var turn = 1

        val handler: Handler = Handler()
        var runnable: Runnable? = null
        val delay = 100

        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())

            GameManager.pollGame(gameId)

            players = GameManager.players.toString()

            findViewById<TextView>(R.id.players_id).apply {
                val regex = Regex("[^A-Za-z0-9]")
                val result = regex.replace(players, " ")
                text = result
            }

            if (checkWin(GameService.state)){
                exitProcess(0)
            }

            val one = GameService.state.elementAt(3)
            val two = GameService.state.elementAt(7)
            val three = GameService.state.elementAt(11)
            val four = GameService.state.elementAt(17)
            val five = GameService.state.elementAt(21)
            val six = GameService.state.elementAt(25)
            val seven = GameService.state.elementAt(31)
            val eight = GameService.state.elementAt(35)
            val nine = GameService.state.elementAt(39)
            Log.d(TAG,"--------------------------------------------------------------\n" +
                    "$one , $two, $three, $four, $five, $six, $seven, $eight, $nine\n" +
                    "--------------------------------------------------------------")

            rowOne = charArrayOf(one,two,three)
            rowTwo = charArrayOf(four,five,six)
            rowThree = charArrayOf(seven,eight,nine)

            gameState = arrayOf(rowOne,rowTwo,rowThree)

            updateStateFromPoll(one,null_null)
            updateStateFromPoll(two,null_one)
            updateStateFromPoll(three,null_two)
            updateStateFromPoll(four,one_null)
            updateStateFromPoll(five,one_one)
            updateStateFromPoll(six,one_two)
            updateStateFromPoll(seven,two_null)
            updateStateFromPoll(eight,two_one)
            updateStateFromPoll(nine,two_two)

        }.also { runnable = it }, delay.toLong())

        // 0x0
        null_null.setOnClickListener {
            rowOne[0] = playerSign
            null_null.text = playerSign.toString()
            null_null.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }

        // 0x1
        null_one.setOnClickListener {
            rowOne[1] = playerSign
            null_one.text = playerSign.toString()
            null_one.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }

        // 0x2
        null_two.setOnClickListener {
            rowOne[2] = playerSign
            null_two.text = playerSign.toString()
            null_two.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }

        // 1x0
        one_null.setOnClickListener {
            rowTwo[0] = playerSign
            one_null.text = playerSign.toString()
            one_null.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }

        // 1x1
        one_one.setOnClickListener {
            rowTwo[1] = playerSign
            one_one.text = playerSign.toString()
            one_one.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }

        // 1x2
        one_two.setOnClickListener {
            rowTwo[2] = playerSign
            one_two.text = playerSign.toString()
            one_two.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }

        // 2x0
        two_null.setOnClickListener {
            rowThree[0] = playerSign
            two_null.text = playerSign.toString()
            two_null.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }

        // 2x1
        two_one.setOnClickListener {
            rowThree[1] = playerSign
            two_one.text = playerSign.toString()
            two_one.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }

        // 2x2
        two_two.setOnClickListener {
            rowThree[2] = playerSign
            two_two.text = playerSign.toString()
            two_two.isClickable = false
            GameManager.updateGame(gameId,gameState)
        }
    }

    private fun checkWin(state:String): Boolean {
        val one = state.elementAt(3)
        val two = state.elementAt(7)
        val three = state.elementAt(11)
        val four = state.elementAt(17)
        val five = state.elementAt(21)
        val six = state.elementAt(25)
        val seven = state.elementAt(31)
        val eight = state.elementAt(35)
        val nine = state.elementAt(39)

        if ((one == 'O') && (two == 'O') && (three == 'O')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "O won!")
            }
            startActivity(intent)
            return true
        } else if ((one == 'X') && (two == 'X') && (three == 'X')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "X won!")
            }
            startActivity(intent)
            return true
        } else if ((four == 'O') && (five == 'O') && (six == 'O')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "O won!")
            }
            startActivity(intent)
            return true
        } else if ((four == 'X') && (five == 'X') && (six == 'X')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "X won!")
            }
            startActivity(intent)
            return true
        } else if ((seven == 'O') && (eight == 'O') && (nine == 'O')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "O won!")
            }
            startActivity(intent)
            return true
        } else if ((seven == 'X') && (eight == 'X') && (nine == 'X')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "X won!")
            }
            startActivity(intent)
            return true
        } else if ((one == 'O') && (five == 'O') && (nine == 'O')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "O won!")
            }
            startActivity(intent)
            return true
        } else if ((one == 'X') && (five == 'X') && (nine == 'X')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "X won!")
            }
            startActivity(intent)
            return true
        } else if ((three == 'O') && (five == 'O') && (seven == 'O')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "O won!")
            }
            startActivity(intent)
            return true
        } else if ((three == 'X') && (five == 'X') && (seven == 'X')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "X won!")
            }
            startActivity(intent)
            return true
        } else if ((one == 'O') && (four == 'O') && (seven == 'O')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "O won!")
            }
            startActivity(intent)
            return true
        } else if ((one == 'X') && (four == 'X') && (seven == 'X')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "X won!")
            }
            startActivity(intent)
            return true
        } else if ((two == 'O') && (five == 'O') && (eight == 'O')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "O won!")
            }
            startActivity(intent)
            return true
        } else if ((two == 'X') && (five == 'X') && (eight == 'X')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "X won!")
            }
            startActivity(intent)
            return true
        } else if ((three == 'O') && (six == 'O') && (nine == 'O')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "O won!")
            }
            startActivity(intent)
            return true
        } else if ((three == 'X') && (six == 'X') && (nine == 'X')){
            val intent = Intent(this, WinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "X won!")
            }
            startActivity(intent)
            return true
        }

        return false
    }

    private fun updateStateFromPoll (pollPosition:Char, button:Button){
        when (pollPosition) {
            '0' -> button.text = ""
            'O' -> button.text = "O"
            'X' -> button.text = "X"
            else -> {
                Log.d(TAG,"Err updating state from poll!")
            }
        }
    }
}
