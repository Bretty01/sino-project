package com.example.sino.data.assets.armor

import androidx.lifecycle.LiveData

class ArmorAccess(private val armorDao: ArmorDao)
{
    val readArmorData: LiveData<List<ArmorSupportSkill>> = armorDao.allArmorSkill()
    fun addArmor(skill: ArmorSupportSkill)
    {
        armorDao.addArmorSkill(skill)
    }
}