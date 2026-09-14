package com.example.lostfound

class Item(
    val id: Int,
    var itemName: String,
    var description: String,
    var location: String,
    var contact: String,
    var type: String,
    var photo: String?,
    var date: String?
) {
}