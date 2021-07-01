package com.example.sino.data.armor

import androidx.room.*

@Entity(tableName = "armor_support_skill")
data class ArmorSupportSkill(
    @PrimaryKey(autoGenerate = true)
    val skill_id: Int,
    val name: String,
    val description: String,
    val element: Int
)

@Entity(tableName = "armor_type")
data class ArmorType(
    @PrimaryKey(autoGenerate = true)
    val type_id: Int,
    val name: String,
    val icon: String
)

@Entity(tableName = "armor_set")
data class ArmorSet(
    @PrimaryKey(autoGenerate = true)
    val set_id: Int,
    val name: String,
    val description: String
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
    val icon: String,
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
data class TypeRelation(
    @Embedded val armor: Armor,
    @Relation(
        parentColumn = "type",
        entityColumn = "type_id"
    )
    val type: ArmorType
)

data class SetRelation(
    @Embedded val armor: Armor,
    @Relation(
        parentColumn = "set_skill",
        entityColumn = "set_id"
    )
    val armorRelation: ArmorSet
)

data class SkillRelation(
    @Embedded val armor: Armor,
    @Relation(
        parentColumn = "story_skill",
        entityColumn = "skill_id"
    )
    val armorRelation: ArmorSupportSkill
)

data class StatsRelation(
    @Embedded val armorStats: ArmorStats,
    @Relation(
        parentColumn = "armor_id",
        entityColumn = "armor_id"
    )
    val armor: List<Armor>
)

data class SpecificStats(
    val armor_id: Int,
    val max_mdef: Int,
    val name: String
)
