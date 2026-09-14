package com.example.lostfound

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        const val DATABASE_NAME = "LostFound.db"
        const val DATABASE_VERSION = 3

        const val TABLE_NAME = "items"

        const val ID = "id"
        const val ITEM_NAME = "item_name"
        const val DESCRIPTION = "description"
        const val LOCATION = "location"
        const val CONTACT = "contact"
        const val TYPE = "type"
        const val PHOTO = "photo"

        const val DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val query = """
            CREATE TABLE $TABLE_NAME (
                $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $ITEM_NAME TEXT,
                $DESCRIPTION TEXT,
                $LOCATION TEXT,
                $CONTACT TEXT,
                $TYPE TEXT,
                $PHOTO TEXT,
                $DATE TEXT
            )
        """.trimIndent()

        db?.execSQL(query)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (oldVersion < 2) {
            db?.execSQL(
                "ALTER TABLE $TABLE_NAME ADD COLUMN $PHOTO TEXT"
            )
        }

        if (oldVersion < 3) {
            db?.execSQL(
                "ALTER TABLE $TABLE_NAME ADD COLUMN $DATE TEXT"
            )
        }
    }

    fun insertItem(
        itemName: String,
        description: String,
        location: String,
        contact: String,
        type: String,
        photo: String?,
        date: String?
    ): Boolean {

        val db = this.writableDatabase

        val values = android.content.ContentValues()

        values.put(ITEM_NAME, itemName)
        values.put(DESCRIPTION, description)
        values.put(LOCATION, location)
        values.put(CONTACT, contact)
        values.put(TYPE, type)
        values.put(PHOTO, photo)
        values.put(DATE, date)

        val result =
            db.insert(TABLE_NAME, null, values)

        return result != -1L
    }

    fun getAllItems(): ArrayList<Item> {

        val itemList = ArrayList<Item>()

        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                val id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ID)
                )

                val itemName = cursor.getString(
                    cursor.getColumnIndexOrThrow(ITEM_NAME)
                )

                val description = cursor.getString(
                    cursor.getColumnIndexOrThrow(DESCRIPTION)
                )

                val location = cursor.getString(
                    cursor.getColumnIndexOrThrow(LOCATION)
                )

                val contact = cursor.getString(
                    cursor.getColumnIndexOrThrow(CONTACT)
                )

                val type = cursor.getString(
                    cursor.getColumnIndexOrThrow(TYPE)
                )

                val photo = cursor.getString(
                    cursor.getColumnIndexOrThrow(PHOTO)
                )
                val date = cursor.getString(
                    cursor.getColumnIndexOrThrow(DATE)
                )
                val item = Item(
                    id,
                    itemName,
                    description,
                    location,
                    contact,
                    type,
                    photo,
                    date
                )

                itemList.add(item)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return itemList
    }

    fun deleteItem(id: Int): Boolean {

        val db = this.writableDatabase

        val result = db.delete(
            TABLE_NAME,
            "$ID = ?",
            arrayOf(id.toString())
        )

        return result > 0
    }
}