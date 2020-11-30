package com.example.cars_secured_dealership.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cars_secured_dealership.model.CarDeal
import com.example.cars_secured_dealership.repository.CarDealRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val carDealRepository = CarDealRepository(application.applicationContext)

    val carDealsPerCountry = MutableLiveData<List<CarDeal>>()

    val mainCarDeals : LiveData<List<CarDeal>> = carDealRepository.getCarDealsMain()
    val allCarDeals : LiveData<List<CarDeal>> = carDealRepository.getAll()

    // Per Country
    fun getCarDealsPerCountry(country: String){
        carDealsPerCountry.value = carDealRepository.getCarDealsPerCountry(country)
    }

    // Sums
    fun getSumAll(): Double{
        return carDealRepository.getSumAll()
    }

    fun getSumOwnPlace(): Double{
        return carDealRepository.getSumOwnPlace()
    }

    fun getSumPerCountry(query: String): Double{
        return carDealRepository.getSumPerCountry(query)
    }

    // Basic insert, update, delete

    fun insertCarDeal(carDeal: CarDeal) {
        ioScope.launch {
            carDealRepository.insertCarDeal(carDeal)
        }
    }

    fun updateCarDeal(carDeal: CarDeal){
        ioScope.launch {
            carDealRepository.updateCarDeal(carDeal)
        }
    }

    fun deleteCarDeal(carDeal: CarDeal) {
        ioScope.launch {
            carDealRepository.deleteCarDeal(carDeal)
        }
    }

    fun deleteAllCarDeals() {
        ioScope.launch {
            carDealRepository.deleteAllCarDeals()
        }
    }

    fun deleteCarDealsOfCountry(country: String) {
        ioScope.launch {
            carDealRepository.deleteCarDealPerCountry(country)
        }
    }




}
