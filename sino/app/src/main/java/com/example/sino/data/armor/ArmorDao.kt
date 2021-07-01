package com.example.sino.data.armor

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArmorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addArmorSkill(skill: ArmorSupportSkill)

    @Transaction
    @Query("SELECT * FROM armor_support_skill ORDER BY skill_id ASC")
    fun allArmorSkill(): LiveData<List<ArmorSupportSkill>>

    @Transaction
    @Query("SELECT * FROM armor_stats INNER JOIN armor USING(armor_id) WHERE armor_stats.rank_id = 1")
    fun getArmorList(): LiveData<List<StatsRelation>>

    @Transaction
    @Query("SELECT * FROM armor_stats INNER JOIN armor USING(armor_id) WHERE armor_stats.armor_id = :armorLocation")
    fun getSpecificArmor(armorLocation: Int): LiveData<List<StatsRelation>>


    @Transaction
    @Query("SELECT * FROM armor INNER JOIN armor_support_skill ON armor.story_skill=armor_support_skill.skill_id" +
            " WHERE armor.armor_id=:armorLocation")
    fun getSupportSkill(armorLocation: Int?): LiveData<List<SkillRelation>>


    @Transaction
    @Query("SELECT * FROM armor INNER JOIN armor_set ON armor.set_skill=armor_set.set_id " +
            "WHERE armor.armor_id=:armorLocation")
    fun getSetSkill(armorLocation: Int?): LiveData<List<SetRelation>>

    @Transaction
    @Query("SELECT * FROM armor INNER JOIN armor_type ON armor.type=armor_type.type_id " +
            "WHERE armor.armor_id=:armorLocation")
    fun getArmorType(armorLocation: Int?): LiveData<List<TypeRelation>>

    /**
    @Transaction
    @Query("SELECT * FROM armor_support_skill INNER JOIN armor ON armor_support_skill.skill_id=armor.story_skill" +
            " INNER JOIN armor_element ON armor_support_skill.element=armor_element.element_id " +
            "WHERE armor.armor_id=:armorLocation")
    fun getSupportSkill(armorLocation: Int?):LiveData<List<ElementRelation>>
*/
}