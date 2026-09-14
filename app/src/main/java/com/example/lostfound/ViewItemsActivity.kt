package com.example.lostfound

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_view_items)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val databaseHelper =
            DatabaseHelper(this)

        val itemList =
            databaseHelper.getAllItems()

        // Make a separate copy
        val allItems =
            ArrayList(itemList)

        val rv =
            findViewById<RecyclerView>(
                R.id.recyclerView
            )

        val searchView =
            findViewById<SearchView>(
                R.id.searchView
            )

        val btnAll =
            findViewById<Button>(
                R.id.btnAll
            )

        val btnLost =
            findViewById<Button>(
                R.id.btnLost
            )

        val btnFound =
            findViewById<Button>(
                R.id.btnFound
            )

        rv.layoutManager =
            LinearLayoutManager(this)

        val adapter =
            ItemAdapter(itemList)

        rv.adapter = adapter

        var selectedType = "All"


        // FILTER FUNCTION

        fun filterItems() {

            val searchText =
                searchView.query
                    .toString()
                    .lowercase()

            val filteredList =
                ArrayList<Item>()

            for (item in allItems) {

                val searchMatch =
                    item.itemName
                        .lowercase()
                        .contains(searchText) ||

                            item.description
                                .lowercase()
                                .contains(searchText) ||

                            item.location
                                .lowercase()
                                .contains(searchText)

                val typeMatch =
                    selectedType == "All" ||
                            item.type == selectedType

                if (searchMatch && typeMatch) {

                    filteredList.add(item)
                }
            }

            adapter.itemList.clear()

            adapter.itemList.addAll(
                filteredList
            )

            adapter.notifyDataSetChanged()
        }


        // SEARCH

        searchView.setOnQueryTextListener(
            object :
                SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(
                    query: String?
                ): Boolean {

                    return false
                }

                override fun onQueryTextChange(
                    newText: String?
                ): Boolean {

                    filterItems()

                    return true
                }
            }
        )


        // ALL BUTTON

        btnAll.setOnClickListener {

            selectedType = "All"

            filterItems()
        }


        // LOST BUTTON

        btnLost.setOnClickListener {

            selectedType = "Lost"

            filterItems()
        }


        // FOUND BUTTON

        btnFound.setOnClickListener {

            selectedType = "Found"

            filterItems()
        }
    }
}