package com.example.ktmyreceiptbook.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_table")
class Receipt(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val shortDesc: String,
    val fullDesc: String,
    val imageUri: String
)