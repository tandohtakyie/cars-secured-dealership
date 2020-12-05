package com.example.cars_secured_dealership.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cars_secured_dealership.model.Trivia
import com.example.cars_secured_dealership.repository.TriviaRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TriviaViewModel(application : Application) : AndroidViewModel(application) {
    private val triviaRepository = TriviaRepository()
    val trivia = MutableLiveData<Trivia>()
    val error = MutableLiveData<String>()


    fun getRandomTrivia(number : Int){
        triviaRepository.getRandomNumberTrivia(number).enqueue(object : Callback<Trivia> {
            override fun onResponse(call: Call<Trivia>, response: Response<Trivia>) {
                if (response.isSuccessful) trivia.value = response.body()
                else error.value = "There was an error while fetching a quote."
            }

            override fun onFailure(call: Call<Trivia>, t: Throwable) {
                error.value = t.message
            }
        })
    }
}
