package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {

    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")

    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    init {
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // this means that if error is not null, there is some kind of error,
                // so stop the function from running using return
                return@addSnapshotListener
            }

            _cities.clear()

            // ? helps deal with it even if its empty/null
            snapshot?.documents?.forEach { document ->
                // why did we use :: , I know it's to convert document to city object though
                val city = document.toObject(City::class.java)
                if (city != null) {
                    _cities.add(city)
                }
            }
        }
    }

    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        // _cities.add(city)
        citiesRef.document(city.name).set(city)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        citiesRef.document(oldCity.name).set(updatedCity)
    }

    fun deleteCity(city: City) {
        citiesRef.document(city.name).delete()
    }
}