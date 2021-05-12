package no.uia.ikt205.thgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import no.uia.ikt205.knotsandcrosses.databinding.ActivityMainBinding
import no.uia.ikt205.thgame.dialogs.CreateGameDialog
import no.uia.ikt205.thgame.dialogs.GameDialogListener
import no.uia.ikt205.thgame.dialogs.JoinGameDialog

class MainActivity : AppCompatActivity() , GameDialogListener {

    private val TAG:String = "MainActivity"

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            createNewGame()
        }

        binding.joinGameButton.setOnClickListener {
            joinGame()
        }

    }

    private fun createNewGame(){
        val dlg = CreateGameDialog()
        dlg.show(supportFragmentManager,"CreateGameDialogFragment")
    }

    private fun joinGame(){
        val dlg = JoinGameDialog()
        dlg.show(supportFragmentManager,"CreateGameDialogFragment")
    }

    override fun onDialogCreateGame(player: String) {
        Log.d(TAG,player)

        GameManager.createGame(player)

        Thread.sleep(1_000)

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, player)
        }
        startActivity(intent)
    }

    override fun onDialogJoinGame(player: String, gameId: String) {
        Log.d(TAG, "$player $gameId")

        GameManager.joinGame(player,gameId)

        Thread.sleep(1_000)

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, player)
        }
        startActivity(intent)
    }

}