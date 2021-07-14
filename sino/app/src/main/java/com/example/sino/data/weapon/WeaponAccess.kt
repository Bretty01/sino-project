package com.example.sino.data.weapon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery

class WeaponAccess(private val weaponDao: WeaponDao) {

    fun getWeaponInfo(weaponLocation: MutableLiveData<Int>): LiveData<List<WeaponStatsRelation>> {
        return weaponDao.getSpecificWeapon(weaponLocation.value!!)
    }

    val readWeaponData: LiveData<List<WeaponStatsRelation>> = weaponDao.getWeaponList()

    fun sortFilterWeapon(queryString: SimpleSQLiteQuery): LiveData<List<WeaponStatsRelation>> {
        return weaponDao.sortFilterWeapon(queryString)
    }
}