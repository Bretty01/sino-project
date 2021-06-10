package com.example.sino.data.armor

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArmorDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addArmorSkill(skill: ArmorSupportSkill)

    @Query("SELECT * FROM armor_support_skill ORDER BY skill_id ASC")
    fun allArmorSkill(): LiveData<List<ArmorSupportSkill>>
}