package no.uia.ikt205.thgame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_win.*
import no.uia.ikt205.knotsandcrosses.R
import no.uia.ikt205.knotsandcrosses.databinding.ActivityWinBinding

class WinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWinBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val winner = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE).toString()

        // Viser "X won!", "O won!" eller "Draw!" på skjermen
        findViewById<TextView>(R.id.win_text).apply {
            text = winner
        }

        backGameButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}