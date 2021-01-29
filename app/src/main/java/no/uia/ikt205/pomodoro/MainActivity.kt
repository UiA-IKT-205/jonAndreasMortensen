package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import kotlin.properties.Delegates



class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 1000L
    var pauseToCountDownInMs = 1000L
    val timeTicks = 1000L
    var currentMS: Long = 0
    var b = 0
    var c = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){

            startCountDown()
            var c = true

        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

        // Endring 1:
        val seek = findViewById<SeekBar>(R.id.seekBarSetTime)
        seek?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // does nothing
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // does nothing
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                Toast.makeText(this@MainActivity,
                        "Time is set to: " + seek.progress + " min",
                        Toast.LENGTH_SHORT).show()
                timeToCountDownInMs = (seek.progress * 60000).toLong()
            }
        })

        val seekPause = findViewById<SeekBar>(R.id.seekBarPause)
        seekPause?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // does nothing
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // does nothing
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                Toast.makeText(this@MainActivity,
                        "Pause is set to: " + seek.progress + " min",
                        Toast.LENGTH_SHORT).show()
                pauseToCountDownInMs = (seek.progress * 60000).toLong()

            }
        })


    }

    fun startCountDown(){
        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@MainActivity,"Nå er det pause!", Toast.LENGTH_SHORT).show()
                pauseCountDown()
            }

            override fun onTick(millisUntilFinished: Long) {
                currentMS = millisUntilFinished
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()
    }

    fun pauseCountDown(){
        var a = findViewById<TextView>(R.id.text).text.toString().toIntOrNull()
        if (c == true) {
            if (a != null) {
                b = a
            }
            c = false
        }
        timer = object : CountDownTimer(pauseToCountDownInMs,timeTicks) {

            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Pausetid er ferdig", Toast.LENGTH_SHORT).show()

                when (b) {
                    null -> Toast.makeText(this@MainActivity,"Finished!!", Toast.LENGTH_SHORT).show()
                    0 -> Toast.makeText(this@MainActivity,"Finished!!", Toast.LENGTH_SHORT).show()
                    else -> {
                        b--
                        Toast.makeText(this@MainActivity,"Repetisjoner igjen: $b", Toast.LENGTH_SHORT).show()
                        startCountDown()
                    }
                }

            }

            override fun onTick(millisUntilFinished: Long) {
                currentMS = millisUntilFinished
                updateCountDownDisplay(millisUntilFinished)

            }
        }

        timer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }
}