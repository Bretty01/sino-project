package com.example.sino.data.armor

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

class ArmorAccess(private val armorDao: ArmorDao)
{

    val readArmorData: LiveData<List<StatsRelation>> = armorDao.getArmorList()

    fun readCurrentArmor(armorLocation: Int): LiveData<List<StatsRelation>> {
        return armorDao.getSpecificArmor(armorLocation)
    }

    fun sortFilterArmor(queryString: SimpleSQLiteQuery) : LiveData<List<StatsRelation>> {
        return armorDao.sortFilterArmor(queryString)
    }
}