package no.uia.ikt205.todolist.todos

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import no.uia.ikt205.todolist.todos.data.Category

class CatDepositoryManager {

    private lateinit var categoryColection: MutableList<Category>

    var onCats: ((List<Category>) -> Unit)? = null
    var onCatUpdate: ((category: Category) -> Unit)? = null

    fun load(context: Context) {

        categoryColection = mutableListOf()

        val TAG = "ToDoListCategories"
        val db = Firebase.firestore

        // Importerer kategorier fra Firestore
        db.collection("Categories")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val bookFirebase = Category(document.id)
                    addCat(bookFirebase)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        onCats?.invoke(categoryColection)
    }

    fun addCat(category: Category) {
        categoryColection.add(category)
        onCats?.invoke(categoryColection)
    }

    fun removeCat(category: Category) {
        categoryColection.remove(category)
        onCats?.invoke(categoryColection)
    }

    companion object {
        val instance = CatDepositoryManager()
    }

}