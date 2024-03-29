package com.example.sino.data.nightmare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery

class NightmareAccess(private val nightmareDao: NightmareDao) {
    val readNightmareData: LiveData<List<NightmareRelation>> = nightmareDao.getNightmareList()
    fun getNightmareInfo(nightmareLocation: MutableLiveData<Int>): LiveData<List<NightmareRelation>> {
        return nightmareDao.getSpecificNightmare(nightmareLocation.value!!)
    }

    fun sortFilterNightmare(queryString: SimpleSQLiteQuery): LiveData<List<NightmareRelation>> {
        return nightmareDao.sortFilterNightmare(queryString)
    }
}