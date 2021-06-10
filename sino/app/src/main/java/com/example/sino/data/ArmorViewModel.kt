package com.example.sino.data.armor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.sino.data.assets.armor.ArmorAccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArmorViewModel(application: Application): AndroidViewModel(application)
{
    private val readAllData: LiveData<List<ArmorSupportSkill>>
    private val armorRepository: ArmorAccess

    init
    {
        val armorDao = ArmorDatabase.getSupportDatabase(application).armorDao()
        armorRepository = ArmorAccess(armorDao)
        readAllData = armorRepository.readArmorData
    }

    fun addArmorSupport(armor:ArmorSupportSkill)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            armorRepository.addArmor(armor)
        }

    }
}