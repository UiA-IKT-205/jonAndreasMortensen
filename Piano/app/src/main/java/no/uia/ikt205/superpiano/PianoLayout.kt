package no.uia.ikt205.superpiano

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.*
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.*
import kotlinx.android.synthetic.main.fragment_piano.*
import kotlinx.android.synthetic.main.fragment_piano.view.*
import no.uia.ikt205.superpiano.data.Note
import no.uia.ikt205.superpiano.databinding.FragmentPianoBinding
import java.io.File
import java.io.FileOutputStream
import java.nio.file.*
import kotlin.collections.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PianoLayout : Fragment() {

    var onSave:((file: Uri) -> Unit)? = null

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C","D","E","F","G","A","B","C2","D2","E2","F2","G2")

    private val halfTones = listOf("C#", "D#", "F#", "G#", "A#", "C#2", "D#2", "F#2", "G#2", "A#2")

    private var score:MutableList<Note> = mutableListOf<Note>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        fullTones.forEach { orgNoteValue ->
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(orgNoteValue)
            var startPlayFull:Long = 0 // Start time relative to device in nano seconds
            var fullNoteStartTime:String = ""


            fullTonePianoKey.onKeyDown =  { note ->
                startPlayFull = System.nanoTime()

                val currentDateTime = LocalDateTime.now()
                fullNoteStartTime = currentDateTime.format(DateTimeFormatter.ISO_TIME)
                println("Piano key down $note at $fullNoteStartTime")
            }

            fullTonePianoKey.onKeyUp = {
                val endPlayFull = System.nanoTime() // Stop time relative to device in nano seconds
                var totalFullTone:Double = 0.0
                var timeFullTone:Long = 0
                timeFullTone = endPlayFull - startPlayFull // Calculates the time the Full tone key was pressed (in nano seconds)
                totalFullTone = timeFullTone.toDouble() / 1000000000 // Converts timeFullTone to seconds
                val note = Note(it,fullNoteStartTime,totalFullTone)

                score.add(note) // Adds it to file
                Toast.makeText(activity,"Note: $it, Start time: $fullNoteStartTime, Duration $totalFullTone seconds", Toast.LENGTH_SHORT).show()
                println("Piano key up $it, Start time: $fullNoteStartTime, Duration $totalFullTone seconds")
                println("(RAW) $it was pressed for $timeFullTone nano seconds (RAW)") // Used for debugging
                println("(PROCESSED) $it was pressed for $totalFullTone seconds (PROCESSED)") // Used for debugging
            }

            ft.add(view.whitePianoKeysLayout.id,fullTonePianoKey,"note_$orgNoteValue")
        }


        halfTones.forEach { orgNoteValue ->
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(orgNoteValue)
            var startPlayHalf:Long = 0 // Start time relative to device in nano seconds
            var halfNoteStartTime:String = ""

            halfTonePianoKey.onKeyPressed =  { note ->
                startPlayHalf = System.nanoTime()
                val currentDateTime = LocalDateTime.now()
                halfNoteStartTime = currentDateTime.format(DateTimeFormatter.ISO_TIME)
                println("Piano key down $note at $halfNoteStartTime")
                //println("Piano key down $note")
            }

            halfTonePianoKey.onKeyReleased = {
                val endPlayHalf = System.nanoTime() // Stop time relative to device in nano seconds
                var totalHalfTone:Double = 0.0
                var timeHalfTone:Long = 0

                timeHalfTone = endPlayHalf - startPlayHalf // Calculates the time the Half tone key was pressed (in nano seconds)
                totalHalfTone = timeHalfTone.toDouble() / 1000000000 // Converts timeHalfTone to seconds
                val note = Note(it,halfNoteStartTime,totalHalfTone)

                score.add(note) // Adds the "Note" content to file
                Toast.makeText(activity,"Note: $it, Start time: $halfNoteStartTime, Duration $totalHalfTone seconds", Toast.LENGTH_SHORT).show()
                println("Piano key up $it, Start time: $halfNoteStartTime, Duration $totalHalfTone seconds")
                println("(RAW) $it was pressed for $timeHalfTone nano seconds (RAW)") // Used for debugging
                println("(PROCESSED) $it was pressed for $totalHalfTone seconds (PROCESSED)") // Used for debugging
            }

            ft.add(view.blackPianoKeysLayout.id,halfTonePianoKey,"note_$orgNoteValue")
        }

        ft.commit()

        view.saveScoreBt.setOnClickListener {
            var fileName = view.fileNameTextEdit.text.toString()
            fileName = "$fileName.musikk"
            val path = this.activity?.getExternalFilesDir(null)
            val file = File(path, fileName)
            val fileExists = file.exists()

            // Checks is the file already exists
               if (score.count() > 0 && fileName.isNotEmpty() && path != null){
                       if(fileExists){
                           println("$fileName already exist! Enter different file name.")
                           Toast.makeText(activity,"$fileName already exist! Enter different file name.", Toast.LENGTH_SHORT).show()
                       } else{
                           val content:String = score.map{
                               it.toString()
                           }.reduce{acc, s-> acc + s + "\n"}
                           saveFile(fileName,content)
                           println("$fileName is saved")
                           Toast.makeText(activity,"$fileName is saved", Toast.LENGTH_SHORT).show()
                       }
                    }

        }

        return view
    }

    private fun saveFile(fileName:String,content:String){
        val path = this.activity?.getExternalFilesDir(null)
        if (path != null){
            val file = File(path, fileName)
            FileOutputStream(file, true).bufferedWriter().use { writer ->
                writer.write(content)
            }

            this.onSave?.invoke(file.toUri());

        } else {
            // User warning
            println("Could not get external path.")
            Toast.makeText(activity,"Could not get external path.", Toast.LENGTH_SHORT).show()
        }
    }

}
