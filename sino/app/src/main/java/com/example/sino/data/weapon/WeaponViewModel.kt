package com.example.sino.data.weapon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WeaponViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<WeaponStatsRelation>>
    val weaponLocation = MutableLiveData<Int>()
    private val weaponRepository: WeaponAccess

    init {
        val weaponDao = WeaponDatabase.getWeaponDatabase(application).weaponDao()
        weaponRepository = WeaponAccess(weaponDao)
        readAllData = weaponRepository.readWeaponData
    }

    fun setWeaponData() : LiveData<List<WeaponStatsRelation>> {
        return weaponRepository.getWeaponInfo(weaponLocation)
    }
}