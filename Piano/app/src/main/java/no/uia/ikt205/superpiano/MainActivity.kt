package no.uia.ikt205.superpiano

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import no.uia.ikt205.superpiano.databinding.ActivityMainBinding
import android.content.ContentValues.TAG
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage


class MainActivity : AppCompatActivity() {

    private val TAG:String = "SuperPiano:MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth

    private lateinit var piano:PianoLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        signInAnonymously()

        piano = supportFragmentManager.findFragmentById(binding.piano.id) as PianoLayout

        piano.onSave = {
            this.upload(it)
        }
    }

    private fun upload(file: Uri){
        Log.d(TAG, "Upload file $file")

        val ref = FirebaseStorage.getInstance().reference.child("melodies/${file.lastPathSegment}")
        var uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
            Log.d(TAG,"Saved file ${it.toString()}")
        }.addOnFailureListener{
            Log.e(TAG,"Error saving the file to fb", it)
        }
    }

    private fun signInAnonymously(){
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Login Success ${it.user.toString()}")
        }.addOnFailureListener {
            Log.d(TAG, "Login Success", it)
        }
    }
}