package com.example.sino.data.nightmare
import androidx.room.*

@Entity(tableName = "nightmares")
data class Nightmares(
    @PrimaryKey(autoGenerate = true)
    val nightmare_id: Int,
    val name: String,
    val summon_cost: Int
)

@Entity(tableName = "nightmare_story")
data class NightmareStorySkill(
    @PrimaryKey(autoGenerate = true)
    val skill_id: Int,
    val story_name: String,
    val story_description: String,
    val story_preparation: Int,
    val story_duration: Int
)

@Entity(tableName = "nightmare_colo")
data class NightmareColoSkill(
    @PrimaryKey(autoGenerate = true)
    val skill_id: Int,
    val colo_name: String,
    val colo_description: String,
    val colo_preparation: Int,
    val colo_duration: Int
)

@Entity(tableName = "nightmare_stats", primaryKeys = ["rank_id", "nightmare_id"], foreignKeys =
    [ForeignKey(entity = Nightmares::class, parentColumns = arrayOf("nightmare_id"),
        childColumns = arrayOf("nightmare_id")),
    ForeignKey(entity = NightmareStorySkill::class, parentColumns = arrayOf("skill_id"),
        childColumns = arrayOf("story_skill")),
    ForeignKey(entity = NightmareColoSkill::class, parentColumns = arrayOf("skill_id"),
    childColumns = arrayOf("colo_skill"))])
data class NightmareStats(
    val rank_id: Int,
    val nightmare_id: Int,
    val stats_rank: String,
    val stats_icon: String,
    val story_skill: Int,
    val colo_skill: Int,
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

@DatabaseView("SELECT nightmares.name, nightmares.summon_cost, nightmare_stats.nightmare_id," +
        " nightmare_stats.rank_id, nightmare_stats.stats_rank, nightmare_stats.stats_icon, nightmare_stats.min_patk, " +
        "nightmare_stats.min_matk, nightmare_stats.min_pdef, nightmare_stats.min_mdef, nightmare_stats.max_patk, " +
        "nightmare_stats.max_matk, nightmare_stats.max_pdef, nightmare_stats.max_mdef, nightmare_stats.mlb_patk, " +
        "nightmare_stats.mlb_matk, nightmare_stats.mlb_pdef, nightmare_stats.mlb_mdef, nightmare_story.story_name, " +
        "nightmare_story.story_description, nightmare_story.story_preparation, nightmare_story.story_duration, " +
        "nightmare_colo.colo_name, nightmare_colo.colo_description, nightmare_colo.colo_preparation, " +
        "nightmare_colo.colo_duration FROM nightmare_stats INNER JOIN nightmares USING(nightmare_id) " +
        "INNER JOIN nightmare_story ON nightmare_stats.story_skill = nightmare_story.skill_id " +
        "INNER JOIN nightmare_colo ON nightmare_stats.colo_skill = nightmare_colo.skill_id")
data class NightmareRelation(
    val name: String,
    val summon_cost: Int,
    val nightmare_id: Int,
    val rank_id: Int,
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
    val mlb_mdef: Int,
    val story_name: String,
    val story_description: String,
    val story_preparation: Int,
    val story_duration: Int,
    val colo_name: String,
    val colo_description: String,
    val colo_preparation: Int,
    val colo_duration: Int
)

