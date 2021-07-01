package com.example.sino.data.armor

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ArmorViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<StatsRelation>>
    private val armorRepository: ArmorAccess
    val armorLocation = MutableLiveData<Int>()
    init {
        val armorDao = ArmorDatabase.getArmorDatabase(application).armorDao()
        armorRepository = ArmorAccess(armorDao)
        readAllData = armorRepository.readArmorData
    }
    fun setArmorData(): LiveData<List<StatsRelation>>{
        return armorRepository.readCurrentArmor(armorLocation.value!!)
    }

    fun readArmorSupport(): LiveData<List<SkillRelation>> {
        return armorRepository.readArmorSupport(armorLocation)
    }

    fun readArmorSet(): LiveData<List<SetRelation>> {
        return armorRepository.readArmorSet(armorLocation)
    }

    fun readArmorType(): LiveData<List<TypeRelation>> {
        return armorRepository.readArmorType(armorLocation)
    }

}