package com.example.cars_secured_dealership.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cars_secured_dealership.model.CarDeal

@Dao
interface CarDealDao {

    @Query("SELECT * FROM cardeals_table ORDER BY id DESC LIMIT 14")
    fun getCarDealsMain(): LiveData<List<CarDeal>>

    @Query("SELECT * FROM carDeals_table ORDER BY date DESC, country ASC, carDeal ASC")
    fun getAll() : LiveData<List<CarDeal>>

    // Per country
    @Query("SELECT * FROM carDeals_table WHERE country=:country ORDER BY date DESC")
    fun getCarDealPerCountry(country: String): List<CarDeal>

    // get sum for all but your own place
    @Query("SELECT SUM(total) FROM carDeals_table WHERE NOT country='Own Place'")
    fun getSumAll(): Double

    // get sum for your own place
    @Query("SELECT SUM(total) FROM carDeals_table WHERE country='Own Place'")
    fun getSumOwnPlace(): Double

    // get sum per country
    @Query("SELECT SUM(total) FROM carDeals_table WHERE country=:query")
    fun getSumPerCountry(query: String): Double

    // Insert, Update, Delete

    @Insert
    fun insertCarDeal(carDeal: CarDeal)

    @Update
    suspend fun updateCarDeal(carDeal: CarDeal)

    // delete one car deal
    @Delete
    fun deleteCarDeal(carDeal: CarDeal)

    // delete all car deals
    @Query("DELETE FROM carDeals_table")
    suspend fun deleteAllCarDeals()

    // delete all car deals within a country
    @Query("DELETE FROM carDeals_table WHERE country=:country")
    suspend fun deleteCarDealsOfCountry(country: String)
}
