package com.example.cars_secured_dealership.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.cars_secured_dealership.dao.CarDealDao
import com.example.cars_secured_dealership.database.CarDealRoomDatabase
import com.example.cars_secured_dealership.model.CarDeal
import com.example.cars_secured_dealership.model.Country

class CarDealRepository(context: Context) {
    private var carDealDao: CarDealDao

    init {
        val carDealRoomDatabase = CarDealRoomDatabase.getDatabase(context)
        carDealDao = carDealRoomDatabase!!.cardealDao()
    }

    fun getCarDealsMain(): LiveData<List<CarDeal>>{
        return carDealDao.getCarDealsMain()
    }

    // All
    fun getAll(): LiveData<List<CarDeal>> {
        return carDealDao.getAll()
    }

    // Per country
    fun getCarDealsPerCountry(country: String): List<CarDeal> {
        return carDealDao.getCarDealPerCountry(country)
    }

    // Sums

    fun getSumAll(): Double {
        return carDealDao.getSumAll()
    }

    fun getSumOwnPlace(): Double {
        return carDealDao.getSumOwnPlace()
    }

    fun getSumPerCountry(query: String): Double {
        return carDealDao.getSumPerCountry(query)
    }



    // Insert, Update, Delete

    fun insertCarDeal(carDeal: CarDeal) {
        carDealDao.insertCarDeal(carDeal)
    }

    suspend fun updateCarDeal(carDeal: CarDeal) {
        carDealDao.updateCarDeal(carDeal)
    }

    fun deleteCarDeal(carDeal: CarDeal) {
        carDealDao.deleteCarDeal(carDeal)
    }

    suspend fun deleteAllCarDeals() {
        carDealDao.deleteAllCarDeals()
    }

    suspend fun deleteCarDealPerCountry(country: String) {
        carDealDao.deleteCarDealsOfCountry(country)
    }
}
