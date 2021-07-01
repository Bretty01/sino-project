package com.example.sino.data.nightmare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NightmareViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<NightmareRelation>>
    val nightmareLocation = MutableLiveData<Int>()
    private val nightmareRepository: NightmareAccess

    init {
        val nightmareDao = NightmareDatabase.getNightmareDatabase(application).nightmareDao()
        nightmareRepository = NightmareAccess(nightmareDao)
        readAllData = nightmareRepository.readNightmareData
    }

    fun setNightmareData() : LiveData<List<NightmareRelation>> {
        return nightmareRepository.getNightmareInfo(nightmareLocation)
    }
}