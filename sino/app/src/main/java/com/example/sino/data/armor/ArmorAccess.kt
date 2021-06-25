package com.example.sino.data.armor

import androidx.lifecycle.LiveData

class ArmorAccess(private val armorDao: ArmorDao)
{

    val readArmorData: LiveData<List<StatsRelation>> = armorDao.getArmorList()

    fun readCurrentArmor(armorLocation: Int): LiveData<List<StatsRelation>> {
        return armorDao.getSpecificArmor(armorLocation)
    }

    fun readArmorSupport(armorLocation: LiveData<Int>): LiveData<List<SkillRelation>> {
        return armorDao.getSupportSkill(armorLocation.value)
    }

    fun readArmorSet(armorLocation: LiveData<Int>): LiveData<List<SetRelation>> {
        return armorDao.getSetSkill(armorLocation.value)
    }

    fun readArmorType(armorLocation: LiveData<Int>): LiveData<List<TypeRelation>> {
        return armorDao.getArmorType(armorLocation.value)
    }


}