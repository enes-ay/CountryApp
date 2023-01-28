package com.example.countryapp.ViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.countryapp.model.Country
import com.example.countryapp.service.CountryAPIservice
import com.example.countryapp.service.CountryDatabase
import com.example.countryapp.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {
    private var countryAPIservice = CountryAPIservice()
    private val disposable = CompositeDisposable()
    private val customPreferences=CustomSharedPreferences(getApplication())
    private val REFRESH_TIME=10*60*1000*10*1000L

    val countries = MutableLiveData<List<Country>>()
    val countryLoading = MutableLiveData<Boolean>()
    val countryErrMessage = MutableLiveData<Boolean>()

    fun refreshData() {
        val updateTime=customPreferences.getTime()
     //   getDataFromAPI()

        if((updateTime != null) && (updateTime != 0L) && ((System.nanoTime() - updateTime) > REFRESH_TIME)) {
            getDataFromAPI()
        }
        else{
            getDataFromSQLite()
        }

    }

    fun getDataFromAPI() {
        countryLoading.value = true

        disposable.add(
            countryAPIservice.getData().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"countries from internet",Toast.LENGTH_SHORT).show()

                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryErrMessage.value = true
                        e.printStackTrace()
                    }

                }
                ))

    }

    private fun showCountries(t: List<Country>) {
        countries.value = t
        countryLoading.value = false
        countryErrMessage.value = false
    }

    private fun storeInSQLite(list:List<Country>) {
        launch {
            val dao=CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
           var listLong=dao.insertCountry(*list.toTypedArray())
            for (i in 0..listLong.size-1){
                list[i].uuid=listLong[i].toInt()
            }
            showCountries(list)
        }
     customPreferences.saveTime(System.nanoTime())
    }
    private fun getDataFromSQLite(){
        launch {
            val countryList = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countryList)
            Toast.makeText(getApplication(),"countries from sqlite",Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}