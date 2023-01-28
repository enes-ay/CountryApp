package com.example.countryapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.countryapp.model.Country
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = arrayOf(Country::class), version = 1)
abstract class CountryDatabase:RoomDatabase() {

      abstract fun countryDao():CountryDAO

      companion object{
          @Volatile private var instance:CountryDatabase?=null
          private val lock=Any()
          @OptIn(InternalCoroutinesApi::class)

          operator fun invoke(context: Context) =instance?: synchronized(lock){
              instance?: makeDatabase(context).also {
                  instance=it
              }
          }
          private fun makeDatabase(context:Context)=Room.databaseBuilder(context.applicationContext,
              CountryDatabase::class.java,"CountryDatabase")
              .build()

      }

      // singleton

}