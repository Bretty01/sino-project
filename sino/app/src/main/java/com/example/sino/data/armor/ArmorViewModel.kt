package com.example.sino.data.armor

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.sino.QueryStringBuilder

class ArmorViewModel(application: Application) : AndroidViewModel(application) {
    var where = false
    val readAllData: LiveData<List<StatsRelation>>
    private val armorRepository: ArmorAccess
    val armorLocation = MutableLiveData<Int>()

    init {
        val armorDao = ArmorDatabase.getArmorDatabase(application).armorDao()
        armorRepository = ArmorAccess(armorDao)
        readAllData = armorRepository.readArmorData
    }

    fun setArmorData(): LiveData<List<StatsRelation>> {
        return armorRepository.readCurrentArmor(armorLocation.value!!)
    }

    fun filterArmor(queryString: String) : LiveData<List<StatsRelation>> {
        val query = SimpleSQLiteQuery(queryString)
        return armorRepository.sortFilterArmor(query)
    }
}
