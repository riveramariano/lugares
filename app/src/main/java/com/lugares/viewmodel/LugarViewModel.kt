package com.lugares.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lugares.data.LugarDao
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepository

class LugarViewModel(application: Application) : AndroidViewModel(application) {
    val getAllData: MutableLiveData<List<Lugar>>

    private val repository: LugarRepository = LugarRepository(LugarDao())

    init {
        getAllData = repository.getAllData
    }

    fun addLugar(lugar: Lugar) {
        repository.addLugar(lugar)
    }

    fun updateLugar(lugar: Lugar) {
        repository.updateLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        repository.deleteLugar(lugar)
    }
}