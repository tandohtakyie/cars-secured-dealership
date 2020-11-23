package com.example.cars_secured_dealership.model

import com.google.gson.annotations.SerializedName

data class Trivia(
    @SerializedName("text") var text: String,
    @SerializedName("number") var number: String,
    @SerializedName("found") var found: Boolean,
    @SerializedName("type") var type: String
)
