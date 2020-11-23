package com.example.cars_secured_dealership.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "carDeals_table")
data class CarDeal(

    @ColumnInfo(name = "carDeal") var carDeal: String,
    @ColumnInfo(name = "country") var country: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "total") var total: Double,

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long? = null

) : Parcelable
