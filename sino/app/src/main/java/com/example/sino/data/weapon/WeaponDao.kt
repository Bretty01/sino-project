package com.example.sino.data.weapon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WeaponDao {
    @Transaction
    @Query("SELECT * FROM weapon_stats INNER JOIN weapons USING(weapon_id) WHERE weapon_stats.rank_id = 1")
    fun getWeaponList(): LiveData<List<WeaponStatsRelation>>

    @Transaction
    @Query("SELECT * FROM weapon_stats INNER JOIN weapons USING(weapon_id) INNER JOIN weapon_type ON " +
            "weapons.type = weapon_type.type_id WHERE weapon_stats.weapon_id = :weaponLocation")
    fun getSpecificWeapon(weaponLocation: Int): LiveData<List<WeaponStatsRelation>>

}