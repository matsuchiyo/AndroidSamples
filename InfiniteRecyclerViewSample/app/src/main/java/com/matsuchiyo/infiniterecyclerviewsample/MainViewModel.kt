package com.matsuchiyo.infiniterecyclerviewsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable

data class Item(val name: String): Serializable

class MainViewModel: ViewModel() {
    val items: LiveData<List<Item>> = MutableLiveData<List<Item>>(
        listOf(
            Item("aaa"),
            Item("bbb"),
            Item("ccc"),
            Item("ddd"),
            Item("eee"),
        )
    )
}
