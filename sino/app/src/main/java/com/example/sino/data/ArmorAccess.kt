package com.example.sino.data.assets.armor

import androidx.lifecycle.LiveData
import com.example.sino.data.armor.ArmorDao
import com.example.sino.data.armor.ArmorSupportSkill

class ArmorAccess(private val armorDao: ArmorDao)
{
    val readArmorData: LiveData<List<ArmorSupportSkill>> = armorDao.allArmorSkill()
    fun addArmor(skill: ArmorSupportSkill)
    {
        armorDao.addArmorSkill(skill)
    }
}