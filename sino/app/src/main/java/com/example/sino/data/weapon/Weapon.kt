package com.example.sino.data.weapon

import androidx.room.*

//TODO("Fix variable formatting with @ColumnInfo for all underscored variables")
@Entity(tableName = "weapons", foreignKeys =
    [ForeignKey(entity = WeaponType::class, parentColumns = arrayOf("type_id"),
        childColumns = arrayOf("type")),
    ForeignKey(entity = WeaponStorySkill::class, parentColumns = arrayOf("skill_id"),
        childColumns = arrayOf("story_skill")),
    ForeignKey(entity = WeaponColoSkill::class, parentColumns = arrayOf("skill_id"),
        childColumns = arrayOf("colo_skill")),
    ForeignKey(entity = WeaponSupportSkill::class, parentColumns = arrayOf("skill_id"),
        childColumns = arrayOf("support_skill")),
    ForeignKey(entity = WeaponElement::class, parentColumns = arrayOf("element_id"),
        childColumns = arrayOf("element"))])
data class Weapons(
    @PrimaryKey(autoGenerate = true)
    val weapon_id: Int,
    val name: String,
    val type: Int,
    val element: Int,
    val cost: Int,
    val story_skill: Int,
    val colo_skill: Int,
    val support_skill: Int
)

@Entity(tableName = "weapon_stats", primaryKeys = ["rank_id", "weapon_id"], foreignKeys =
    [ForeignKey(entity = Weapons::class, parentColumns = arrayOf("weapon_id"),
        childColumns = arrayOf("weapon_id"))])
data class WeaponStats(
    val rank_id: Int,
    val weapon_id: Int,
    val stats_rank: String,
    val stats_icon: String,
    val min_patk: Int,
    val min_matk: Int,
    val min_pdef: Int,
    val min_mdef: Int,
    val max_patk: Int,
    val max_matk: Int,
    val max_pdef: Int,
    val max_mdef: Int,
    val mlb_patk: Int,
    val mlb_matk: Int,
    val mlb_pdef: Int,
    val mlb_mdef: Int
)

@Entity(tableName = "weapon_type")
data class WeaponType(
    @PrimaryKey(autoGenerate = true)
    val type_id: Int,
    val type_name: String,
    val type_icon: String
)

@Entity(tableName = "weapon_story_skill")
data class WeaponStorySkill(
    @PrimaryKey(autoGenerate = true)
    val skill_id: Int,
    val story_name: String,
    val story_description: String,
    val sp_cost: Int
)

@Entity(tableName = "weapon_colo_skill")
data class WeaponColoSkill(
    @PrimaryKey(autoGenerate = true)
    val skill_id: Int,
    val colo_name: String,
    val colo_description: String,
    val colo_sp: Int
)

@Entity(tableName = "weapon_support_skill")
data class WeaponSupportSkill(
    @PrimaryKey(autoGenerate = true)
    val skill_id: Int,
    val support_name: String,
    val support_description: String,
)

@Entity(tableName = "weapon_element")
data class WeaponElement(
    @PrimaryKey(autoGenerate = true)
    val element_id: Int,
    val element_name: String,
    val element_icon: String
)

data class WeaponStatsRelation(
    @Embedded val weaponStats: WeaponStats,
    @Relation(
        parentColumn = "weapon_id",
        entityColumn = "weapon_id"
    )
    val weapon: List<WeaponTypeRelation>,
)

@DatabaseView("SELECT weapons.weapon_id, weapons.type, weapons.name, weapons.cost, " +
        "weapon_type.type_icon, weapon_story_skill.story_name, weapon_story_skill.story_description, " +
        "weapon_story_skill.sp_cost, weapon_colo_skill.colo_name, weapon_colo_skill.colo_description, " +
        "weapon_colo_skill.colo_sp, weapon_support_skill.support_name, weapon_support_skill.support_description, " +
        "weapon_element.element_name, weapon_element.element_icon " +
        "FROM weapons INNER JOIN weapon_type ON weapons.type = weapon_type.type_id " +
        "INNER JOIN weapon_support_skill ON weapons.support_skill = weapon_support_skill.skill_id " +
        "INNER JOIN weapon_colo_skill ON weapons.colo_skill = weapon_colo_skill.skill_id " +
        "INNER JOIN weapon_story_skill ON weapons.story_skill = weapon_story_skill.skill_id " +
        "INNER JOIN weapon_element ON weapons.element = weapon_element.element_id")
data class WeaponTypeRelation(
    val weapon_id: Int,
    val type: Int,
    val name: String,
    val cost: Int,
    val type_icon: String,
    val story_name: String,
    val story_description: String,
    val sp_cost: Int,
    val colo_name: String,
    val colo_description: String,
    val colo_sp: Int,
    val support_name: String,
    val support_description: String,
    val element_name: String,
    val element_icon: String

)