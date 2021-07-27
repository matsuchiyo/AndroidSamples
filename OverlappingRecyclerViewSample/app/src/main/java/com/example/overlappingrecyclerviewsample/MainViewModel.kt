package com.example.overlappingrecyclerviewsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Item(val name: String)

class MainViewModel: ViewModel() {
    val items: LiveData<List<Item>> = MutableLiveData<List<Item>>(
        listOf(
            Item("aaa"),
            Item("bbb"),
            Item("ccc"),
            Item("ddd"),
            Item("eee"),
            Item("aaa"),
            Item("bbb"),
            Item("ccc"),
            Item("ddd"),
            Item("eee"),
            Item("aaa"),
            Item("bbb"),
            Item("ccc"),
            Item("ddd"),
            Item("eee"),
            Item("aaa"),
            Item("bbb"),
            Item("ccc"),
            Item("ddd"),
            Item("eee"),
            Item("aaa"),
            Item("bbb"),
            Item("ccc"),
            Item("ddd"),
            Item("eee"),
            Item("aaa"),
            Item("bbb"),
            Item("ccc"),
            Item("ddd"),
            Item("eee"),
        )
    )
}
