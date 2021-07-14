package com.example.sino.data.armor

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface ArmorDao {
    @Transaction
    @Query("SELECT * FROM armor_support_skill ORDER BY skill_id ASC")
    fun allArmorSkill(): LiveData<List<ArmorSupportSkill>>

    @Transaction
    @Query("SELECT * FROM armor_stats INNER JOIN armor USING(armor_id) WHERE armor_stats.rank_id = 1")
    fun getArmorList(): LiveData<List<StatsRelation>>

    @Transaction
    @Query("SELECT * FROM armor_stats INNER JOIN armor USING(armor_id) WHERE armor_stats.armor_id = :armorLocation")
    fun getSpecificArmor(armorLocation: Int): LiveData<List<StatsRelation>>

    @RawQuery
    fun sortFilterArmor(query: SupportSQLiteQuery) : LiveData<List<StatsRelation>>
}