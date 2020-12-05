package com.example.cars_secured_dealership.repository

import com.example.cars_secured_dealership.service.TriviaApi
import com.example.cars_secured_dealership.service.TriviaApiService

class TriviaRepository {
    private val triviaApi : TriviaApiService = TriviaApi.createApi()

    fun getRandomNumberTrivia(number: Int) = triviaApi.getRandomNumberTrivia(number)
}
