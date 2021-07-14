package com.example.sino.data.armor

import androidx.room.*

@Entity(tableName = "armor_support_skill")
data class ArmorSupportSkill(
    @PrimaryKey(autoGenerate = true)
    val skill_id: Int,
    val skill_name: String,
    val skill_description: String,
    val element: Int,
    val weapon: Int
)

@Entity(tableName = "armor_type")
data class ArmorType(
    @PrimaryKey(autoGenerate = true)
    val type_id: Int,
    val type_name: String,
    val type_icon: String
)

@Entity(tableName = "armor_set")
data class ArmorSet(
    @PrimaryKey(autoGenerate = true)
    val set_id: Int,
    val set_name: String,
    val set_description: String
)

@Entity(tableName = "armor", foreignKeys =
    [ForeignKey(entity = ArmorType::class, parentColumns = arrayOf("type_id"),
        childColumns = arrayOf("type")),
    ForeignKey(entity = ArmorSet::class, parentColumns = arrayOf("set_id"),
        childColumns = arrayOf("set_skill")),
    ForeignKey(entity = ArmorSupportSkill::class, parentColumns = arrayOf("skill_id"),
        childColumns = arrayOf("story_skill"))])
data class Armor(
    @PrimaryKey(autoGenerate = true)
    val armor_id: Int,
    val name: String,
    val type: Int,
    val story_skill: Int,
    val set_skill: Int
)

@Entity(tableName = "armor_stats", primaryKeys = ["rank_id", "armor_id"],foreignKeys =
    [ForeignKey(entity = Armor::class, parentColumns = arrayOf("armor_id"),
        childColumns = arrayOf("armor_id"))])
data class ArmorStats(
    val rank_id: Int,
    val armor_id: Int,
    val rank: String,
    val stats_icon: String,
    val min_pdef: Int,
    val min_mdef: Int,
    val max_pdef: Int,
    val max_mdef: Int
)
/**
@Entity(tableName = "armor_element")
data class ArmorElement(
    @PrimaryKey(autoGenerate = true)
    val element_id: Int,
    val name: String,
    val icon: String
)
*/
data class StatsRelation(
    @Embedded val armorStats: ArmorStats,
    @Relation(
        parentColumn = "armor_id",
        entityColumn = "armor_id"
    )
    val armor: List<ArmorRelation>
)

@DatabaseView("SELECT armor.armor_id,armor.name, armor_type.type_name, armor_type.type_icon, " +
        "armor_set.set_name, armor_set.set_description, armor_support_skill.skill_name, " +
        "armor_support_skill.skill_description, armor_support_skill.element " +
        "FROM armor INNER JOIN armor_type ON armor.type = armor_type.type_id " +
        "INNER JOIN armor_set ON armor.set_skill = armor_set.set_id " +
        "INNER JOIN armor_support_skill ON armor.story_skill = armor_support_skill.skill_id")
data class ArmorRelation(
    val armor_id: Int,
    val name: String,
    val type_name: String,
    val type_icon: String,
    val set_name: String,
    val set_description: String,
    val skill_name: String,
    val skill_description: String,
    val element: Int,
)
