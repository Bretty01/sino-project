package com.example.sino.data.weapon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface WeaponDao {
    @Transaction
    @Query("SELECT * FROM weapon_stats INNER JOIN weapons USING(weapon_id) WHERE weapon_stats.rank_id = 1")
    fun getWeaponList(): LiveData<List<WeaponStatsRelation>>

    @Transaction
    @Query("SELECT * FROM weapon_stats INNER JOIN weapons USING(weapon_id) INNER JOIN weapon_type ON " +
            "weapons.type = weapon_type.type_id WHERE weapon_stats.weapon_id = :weaponLocation")
    fun getSpecificWeapon(weaponLocation: Int): LiveData<List<WeaponStatsRelation>>

    @RawQuery(observedEntities = [Weapons::class, WeaponStats::class, WeaponType::class, WeaponStorySkill::class,
        WeaponColoSkill::class, WeaponSupportSkill::class, WeaponElement::class])
    fun sortFilterWeapon(queryString: SupportSQLiteQuery): LiveData<List<WeaponStatsRelation>>
}