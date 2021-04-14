package no.uia.ikt205.todolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import no.uia.ikt205.todolist.todos.CatCollectionAdapter
import no.uia.ikt205.todolist.todos.CatDepositoryManager
import no.uia.ikt205.todolist.todos.CatDetailsActivity
import no.uia.ikt205.todolist.todos.data.Cat
import no.uia.ikt205.todolist.databinding.ActivityMainBinding

class CatHolder{
    companion object{
        var PickedCat:Cat? = null
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivityCat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bookListing.layoutManager = LinearLayoutManager(this)
        binding.bookListing.adapter = CatCollectionAdapter(emptyList<Cat>(), this::onCatClicked)

        CatDepositoryManager.instance.onCats = {
            (binding.bookListing.adapter as CatCollectionAdapter).updateCollection(it)
        }

        CatDepositoryManager.instance.load(this)

        binding.saveBt.setOnClickListener {
            val category = binding.title.text.toString()

            binding.title.setText("")

            addCat(category)

            val ipm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ipm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun addCat(category: String) {
        val cat = Cat(category)
        val db = Firebase.firestore

        val ex = hashMapOf(
            "exists" to 1
        )

        db.collection("Categories").document(category)
            .set(ex)
            .addOnSuccessListener {
                Log.d(TAG, "Exists ref added with ID: $category")
                db.collection("Categories").document(category)
                    .set(cat)
                    .addOnSuccessListener {
                        Log.d(TAG, "Cat added with ID: $cat")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding Cat", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding exists ref", e)
            }

        CatDepositoryManager.instance.addCat(cat)
    }

    private fun onCatClicked(cat: Cat): Unit {
        CatHolder.PickedCat = cat

        val intent =Intent(this, CatDetailsActivity::class.java)

        startActivity(intent)
    }
}