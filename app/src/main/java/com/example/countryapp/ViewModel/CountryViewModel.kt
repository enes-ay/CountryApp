package com.example.countryapp.ViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.countryapp.model.Country
import com.example.countryapp.service.CountryDatabase
import kotlinx.coroutines.launch

class CountryViewModel(application: Application):BaseViewModel(application) {

    var country=MutableLiveData<Country>()


    fun getSpecificDataFromRoom(countryId:Int){
        launch {
            val dao=CountryDatabase(getApplication()).countryDao()
            country.value= dao.getCountry(countryId)

        }

    }

}