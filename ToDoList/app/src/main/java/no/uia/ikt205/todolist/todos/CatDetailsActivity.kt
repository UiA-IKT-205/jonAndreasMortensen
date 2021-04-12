package no.uia.ikt205.todolist.todos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_cat_details.*
import no.uia.ikt205.todolist.CategoryHolder
import no.uia.ikt205.todolist.todos.data.Category
import no.uia.ikt205.todolist.todos.data.Tasks
import no.uia.ikt205.todolist.databinding.ActivityCatDetailsBinding

var receivedCategoryFormatted = ""
val TAG = "CatDetailsActivity"

class CatDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatDetailsBinding
    lateinit var category:Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTodoItems.layoutManager = LinearLayoutManager(this)
        binding.rvTodoItems.adapter = TasksCollectionAdapter(emptyList<Tasks>())

        TaskDepositoryManager.instance.onTasks = {
            (binding.rvTodoItems.adapter as TasksCollectionAdapter).updateTaskCollection(it)
        }

        TaskDepositoryManager.instance.loadTasks(this)

        val db = Firebase.firestore

        if(CategoryHolder.pickedCategory != null){
            category = CategoryHolder.pickedCategory!!
            Log.i("Details view", category.toString())

            receivedCategoryFormatted = category.toString().replace("Category(category=", "")

            // Importerer oppgaver fra Firestore
            db.collection("Categories")
                .document(receivedCategoryFormatted.replace(")", ""))
                .collection(receivedCategoryFormatted.replace(")", ""))
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val dataForm = document.data.toString().replace("{done=", "")
                        val formattedDone = dataForm.replace("}", "")
                        println(formattedDone)
                        var taskFirebase: Tasks
                        if (formattedDone == "true") {
                            taskFirebase = Tasks(document.id, isChecked = true)
                        } else {
                            taskFirebase = Tasks(document.id, isChecked = false)
                        }

                        TaskDepositoryManager.instance.addTask(taskFirebase)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

            binding.catName.text = receivedCategoryFormatted.replace(")", "")

        } else{
            setResult(RESULT_CANCELED, Intent().apply {
            })
            finish()
        }

        // Oppdaterer fremgangsbaren
        db.collection("Progress")
            .document(receivedCategoryFormatted.replace(")", ""))
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: ${snapshot.data}")
                    val progress = snapshot.data.toString().replace("{progress=", "")
                    val formattedProgress = progress.replace("}", "")
                    binding.progressBarSecond.progress = formattedProgress.toInt() // Updates progress bar value
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }

        // Legger en oppgave til Firestore og liste
        btnAddTodo.setOnClickListener {
            val todoTitle = etTodoTitle.text.toString()

            if(todoTitle.isNotEmpty()) {
                var todo = Tasks(todoTitle, false)
                val receivedCatFormatted = category.toString().replace("Cat(category=", "")
                val todoy = hashMapOf(

                    "done" to false
                )

                db.collection("Categories")
                    .document(receivedCatFormatted.replace(")", ""))
                    .collection(receivedCatFormatted.replace(")", ""))
                    .document(todoTitle)
                    .set(todoy)
                    .addOnSuccessListener {
                        Log.d(TAG, "To-Do task added with ID: $todoTitle")
                        todo = Tasks(todoTitle, false)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding task", e)
                    }


                TaskDepositoryManager.instance.addTask(todo)
                etTodoTitle.text.clear()
            }
        }

        // Sletter avkryssede oppgaver fra Firestore og liste
        btnDeleteDoneTodos.setOnClickListener {
            val receivedBookFormatted = category.toString().replace("Cat(category=", "")

            TaskDepositoryManager.instance.deleteDoneTasks()

            db.collection("Categories")
                .document(receivedBookFormatted.replace(")", ""))
                .collection(receivedBookFormatted.replace(")", ""))
                .whereEqualTo("done", true)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("Categories")
                            .document(receivedBookFormatted.replace(")", ""))
                            .collection(receivedBookFormatted.replace(")", ""))
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}