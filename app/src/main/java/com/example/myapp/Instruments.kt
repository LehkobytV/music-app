package com.example.myapp

data class Instruments(
    val type: String,
    val brand: String,
    val model: String,
    val countryOfOrigin: String,
    val price: Double
) {
    var id: Int = 0

    override fun toString(): String {
        return "$type - $brand - $model - $countryOfOrigin - $price"
    }
}
