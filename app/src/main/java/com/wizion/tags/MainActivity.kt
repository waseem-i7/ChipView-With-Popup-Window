package com.wizion.tags


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var popupWindow: PopupWindow
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var popupSearchView : SearchView
    private lateinit var chipGroup : ChipGroup
    private lateinit var itemsList : ArrayList<TagsModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemsList = ArrayList()
        itemsList.add(TagsModel("Apple"))
        itemsList.add(TagsModel("Banana"))
        itemsList.add(TagsModel("Cherry"))
        itemsList.add(TagsModel("Durian"))
        itemsList.add(TagsModel("Elderberry"))
        itemsList.add(TagsModel("Apple"))
        itemsList.add(TagsModel("Banana"))
        itemsList.add(TagsModel("Cherry"))
        itemsList.add(TagsModel("Durian"))
        itemsList.add(TagsModel("Elderberry"))
        itemsList.add(TagsModel("Apple"))
        itemsList.add(TagsModel("Banana"))
        itemsList.add(TagsModel("Cherry"))
        itemsList.add(TagsModel("Durian"))
        itemsList.add(TagsModel("Elderberry"))
        itemsList.add(TagsModel("Apple"))
        itemsList.add(TagsModel("Banana"))
        itemsList.add(TagsModel("Cherry"))
        itemsList.add(TagsModel("Durian"))
        itemsList.add(TagsModel("Elderberry"))


        popupSearchView = findViewById(R.id.search_view)
        chipGroup = findViewById(R.id.chip_group)

        recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CustomAdapter( itemsList) { item ->
            addChip(item)
          //  popupWindow.dismiss()
        }
        recyclerView.adapter = adapter


        popupWindow = PopupWindow(
            recyclerView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.isFocusable = false
        popupSearchView.setBackgroundColor(ContextCompat.getColor(this,R.color.white))

        popupSearchView.setOnSearchClickListener {
            popupWindow.showAsDropDown(it)
        }
        popupSearchView.setOnCloseListener{
            popupWindow.dismiss()
            return@setOnCloseListener false
        }

        popupSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }
    private fun addChip(currentItem: TagsModel) {
        val chip = Chip(chipGroup.context)
        chip.text = currentItem.tag
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
            itemsList.add(currentItem)
            adapter.notifyDataSetChanged()
            popupSearchView.setQuery("",true)
        }
        chipGroup.addView(chip)
        itemsList.remove(currentItem)
        adapter.notifyDataSetChanged()
        popupSearchView.setQuery("",true)
    }
    class CustomAdapter(private var items: List<TagsModel>, private val itemClickListener: (TagsModel) -> Unit) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(), Filterable {

        private var filteredItems: List<TagsModel> = items

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_tags, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = filteredItems[position]
            holder.textView.text = item.tag
            holder.itemView.setOnClickListener { itemClickListener(item) }
        }

        override fun getItemCount(): Int {
            return filteredItems.size
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val filterResults = FilterResults()
                    val query = constraint?.toString()?.toLowerCase(Locale.getDefault())
                    val filteredList  = if (query.isNullOrEmpty()) {
                        items
                    } else {
                        items.filter { it.tag.toLowerCase(Locale.getDefault()).contains(query) }
                    }
                    filterResults.values = filteredList
                    filterResults.count = filteredList.size
                    return filterResults
                }

                override fun publishResults(
                    constraint: CharSequence?,
                    results: FilterResults?
                ) {
                    filteredItems = results?.values as List<TagsModel>
                    notifyDataSetChanged()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.tv1)
        }
    }
}