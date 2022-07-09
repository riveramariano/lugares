package com.lugares.repository

import androidx.lifecycle.MutableLiveData
import com.lugares.data.LugarDao
import com.lugares.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    val getAllData: MutableLiveData<List<Lugar>> = lugarDao.getAllData()

     fun addLugar(lugar: Lugar) {
        lugarDao.addLugar(lugar)
    }

    fun updateLugar(lugar: Lugar) {
        lugarDao.updateLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        lugarDao.deleteLugar(lugar)
    }
}