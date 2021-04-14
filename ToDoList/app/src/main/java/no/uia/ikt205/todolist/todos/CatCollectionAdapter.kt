package no.uia.ikt205.todolist.todos

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.cat_layout.view.*
import no.uia.ikt205.todolist.todos.data.Cat
import no.uia.ikt205.todolist.databinding.CatLayoutBinding

class CatCollectionAdapter(private var cats:List<Cat>, private val onCatClicked:(Cat) -> Unit) : RecyclerView.Adapter<CatCollectionAdapter.ViewHolder>(){

    class ViewHolder(private val binding:CatLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Cat, onCatClicked:(Cat) -> Unit) {
            binding.title.text = cat.category

            val TAG = "CatCollectionAdapter"
            val db = Firebase.firestore

            db.collection("Progress")
                .document(cat.category)
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
                onCatClicked(cat)
            }
        }
    }

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = cats[position]
        holder.bind(cat,onCatClicked)

        holder.itemView.apply {
            title.text = cat.category

            deleteBt.setOnClickListener {
                val TAG = "ToDoListTasks"

                val db = Firebase.firestore

                val doc = hashMapOf(
                    "progress" to 0
                )

                db.collection("Categories")
                    .document(title.text as String)
                    .collection(title.text as String)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("Categories")
                                .document(title.text as String)
                                .collection(title.text as String)
                                .document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    db.collection("Categories")
                                        .document(title.text as String)
                                        .delete()
                                        .addOnSuccessListener {
                                            db.collection("Progress")
                                                .document(title.text as String)
                                                .set(doc)
                                                .addOnSuccessListener {
                                                    db.collection("Progress")
                                                        .document(title.text as String)
                                                        .delete()
                                                        .addOnSuccessListener { Log.d(TAG, "Category progress deleted!") }
                                                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting category progress", e) }

                                                    Log.d(TAG, "Category progress deleted!")
                                                }
                                                .addOnFailureListener { e -> Log.w(TAG, "Error deleting category progress", e) }

                                            Log.d(TAG, "DocumentSnapshot successfully deleted! (cat)")
                                        }
                                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

                                    Log.d(TAG, "DocumentSnapshot successfully deleted! (doc)") }
                                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                        }

                        Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                val remove = Cat(title.text as String)
                CatDepositoryManager.instance.removeCat(remove)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CatLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun updateCollection(newCats:List<Cat>){
        cats = newCats
        notifyDataSetChanged()
    }


}