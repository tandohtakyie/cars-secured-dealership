package com.example.cars_secured_dealership.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cars_secured_dealership.dao.CarDealDao
import com.example.cars_secured_dealership.model.CarDeal

@Database(entities = [CarDeal::class], version = 1, exportSchema = false)
abstract class CarDealRoomDatabase : RoomDatabase(){
    abstract fun cardealDao() : CarDealDao

    companion object{
        private const val DATABASE_NAME = "CAR_DEAL_DATABASE"

        @Volatile
        private var carDealRoomDatabaseInstance : CarDealRoomDatabase? = null

        fun getDatabase(context: Context) : CarDealRoomDatabase? {
            if (carDealRoomDatabaseInstance == null){
                synchronized(CarDealRoomDatabase::class.java){
                    if (carDealRoomDatabaseInstance == null){
                        carDealRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            CarDealRoomDatabase::class.java, DATABASE_NAME
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return carDealRoomDatabaseInstance
        }
    }
}
