package no.uia.ikt205.todolist.todos

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import no.uia.ikt205.todolist.todos.data.Cat

class CatDepositoryManager {

    private lateinit var catColection: MutableList<Cat>

    var onCats: ((List<Cat>) -> Unit)? = null
    var onCatUpdate: ((cat: Cat) -> Unit)? = null

    fun load(context: Context) {

        catColection = mutableListOf()

        val TAG = "ToDoListCategories"
        val db = Firebase.firestore

        db.collection("Categories")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val bookFirebase = Cat(document.id)
                    addCat(bookFirebase)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        onCats?.invoke(catColection)
    }

    fun addCat(cat: Cat) {
        catColection.add(cat)
        onCats?.invoke(catColection)
    }

    fun removeCat(cat: Cat) {
        catColection.remove(cat)
        onCats?.invoke(catColection)
    }

    companion object {
        val instance = CatDepositoryManager()
    }

}