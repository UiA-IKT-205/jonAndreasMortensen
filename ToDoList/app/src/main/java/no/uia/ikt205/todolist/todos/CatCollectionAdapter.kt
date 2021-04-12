package no.uia.ikt205.todolist.todos

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.cat_layout.view.*
import no.uia.ikt205.todolist.todos.data.Category
import no.uia.ikt205.todolist.databinding.CatLayoutBinding

class CatCollectionAdapter(private var categories:List<Category>, private val onCatClicked:(Category) -> Unit) : RecyclerView.Adapter<CatCollectionAdapter.ViewHolder>(){

    class ViewHolder(private val binding:CatLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, onCatClicked:(Category) -> Unit) {
            binding.title.text = category.category
            // binding.progressBar.progress = 66

            val TAG = "CatCollectionAdapter"
            val db = Firebase.firestore

            db.collection("Progress")
                .document(category.category)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: ${snapshot.data}")
                        val progress = snapshot.data.toString().replace("{progress=", "")
                        val formattedProgress = progress.replace("}", "")
                        binding.progressBar.progress = formattedProgress.toInt()
                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }

            binding.card.setOnClickListener {
                onCatClicked(category)
            }
        }
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = categories[position]
        holder.bind(cat,onCatClicked)

        holder.itemView.apply {
            title.text = cat.category

            deleteBt.setOnClickListener {
                val TAG = "ToDoListTasks"

                val db = Firebase.firestore

                // Deletes category from Firestore //
                // --------------------------------------------------------------------------------------- //
                db.collection("Categories")
                    .document(title.text as String)
                    .delete()
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                // --------------------------------------------------------------------------------------- //
                val remove = Category(title.text as String)
                CatDepositoryManager.instance.removeCat(remove)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CatLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun updateCollection(newCategories:List<Category>){
        categories = newCategories
        notifyDataSetChanged()
    }


}