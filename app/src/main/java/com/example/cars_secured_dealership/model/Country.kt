package com.example.cars_secured_dealership.model

data class Country(
    var country: String
){
    companion object{
        val COUNTRY_NAMES = listOf(
            "China",
            "United States",
            "Japan",
            "South Korea",
            "Spain",
            "France",
            "Canada",
            "Italy",
            "Germany",
            "United Kingdom",
            "Netherlands",
            "Australia",
            "Belgium",
            "South Africa",
            "Own Place"
        )
    }
}
