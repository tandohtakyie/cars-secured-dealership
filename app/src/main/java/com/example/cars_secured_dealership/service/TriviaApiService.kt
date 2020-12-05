package com.example.cars_secured_dealership.service

import com.example.cars_secured_dealership.model.Trivia
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TriviaApiService {

    @GET("/{number}?json")
    fun getRandomNumberTrivia(@Path("number") number: Int) : Call<Trivia>
}
