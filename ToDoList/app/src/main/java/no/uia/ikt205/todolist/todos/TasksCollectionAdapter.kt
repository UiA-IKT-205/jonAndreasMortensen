package no.uia.ikt205.todolist.todos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.task_layout.view.*
import no.uia.ikt205.todolist.todos.data.Tasks
import no.uia.ikt205.todolist.databinding.TaskLayoutBinding

var progressTasks: Int = 0

class TasksCollectionAdapter(private var tasks:List<Tasks>) : RecyclerView.Adapter<TasksCollectionAdapter.ViewHolder>() {

    class ViewHolder(private val binding: TaskLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Tasks) {
            binding.tvTodoTitle.text = task.task
        }
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        val db = Firebase.firestore
        var countTrue = 0
        var totalTasks: Int
        var progressValue: Float = 0.00F
        holder.bind(task)

        holder.itemView.apply {
            tvTodoTitle.text = task.task
            cbDone.isChecked = task.isChecked

            cbDone.setOnCheckedChangeListener { _, isChecked ->
                task.isChecked = !task.isChecked

                db.collection("Categories")
                    .document(receivedCatFormatted.replace(")", ""))
                    .collection(receivedCatFormatted.replace(")", ""))
                    .document(tvTodoTitle.text as String)
                    .update("done", cbDone.isChecked)
                    .addOnSuccessListener {
                        println("Changed status successfully!")
                    }
                    .addOnFailureListener { e ->
                        println("Failed to change status!")
                    }

                db.collection("Categories")
                    .document(receivedCatFormatted.replace(")", ""))
                    .collection(receivedCatFormatted.replace(")", ""))
                    .whereEqualTo("done", true)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            countTrue++
                        }
                        totalTasks = tasks.size
                        progressValue = (countTrue.toFloat() / totalTasks.toFloat()) * 100

                        progressTasks = progressValue.toInt()

                        println("Progress: $progressTasks")

                        val doc = hashMapOf(
                            "progress" to progressTasks
                        )

                        db.collection("Progress")
                            .document(receivedCatFormatted.replace(")", ""))
                            .set(doc)
                            .addOnSuccessListener {
                                println("Changed progress")
                            }
                            .addOnFailureListener {
                                println("Failed to change progress!")
                            }

                        // Set values back to 0
                        countTrue = 0
                        totalTasks = 0
                        progressValue = 0.0F
                    }
                    .addOnFailureListener { e ->
                        println("Failed to get progress!")
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TaskLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun updateTaskCollection(newTasks:List<Tasks>){
        tasks = newTasks
        notifyDataSetChanged()
    }

}