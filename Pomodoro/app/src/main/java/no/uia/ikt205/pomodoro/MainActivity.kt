package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var startButtonThree:Button
    lateinit var startButtonSix:Button
    lateinit var startButtonNine:Button
    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 1000L
    val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            startCountDown(it)

        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

        startButtonThree = findViewById<Button>(R.id.startCountdownButtonThree)
        startButtonThree.setOnClickListener(){
            timeToCountDownInMs = 1800000
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

        startButtonSix = findViewById<Button>(R.id.startCountdownButtonSix)
        startButtonSix.setOnClickListener(){
            timeToCountDownInMs = 3600000
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

        startButtonNine = findViewById<Button>(R.id.startCountdownButtonNine)
        startButtonNine.setOnClickListener(){
            timeToCountDownInMs = 5400000
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

        startButton = findViewById<Button>(R.id.startCountdownButtonTwo)
        startButton.setOnClickListener(){
            timeToCountDownInMs = 7200000
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }

    fun startCountDown(v: View){

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}