package com.example.sino.data.nightmare

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface NightmareDao {
    @Transaction
    @Query("SELECT * FROM nightmare_stats INNER JOIN nightmares USING(nightmare_id) " +
            "INNER JOIN nightmare_story ON nightmare_stats.story_skill = nightmare_story.skill_id " +
            "INNER JOIN nightmare_colo ON nightmare_stats.colo_skill = nightmare_colo.skill_id " +
            "WHERE nightmare_stats.rank_id = 1")
    fun getNightmareList(): LiveData<List<NightmareRelation>>

    @Transaction
    @Query("SELECT * FROM nightmare_stats INNER JOIN nightmares USING(nightmare_id) " +
            "INNER JOIN nightmare_story ON nightmare_stats.story_skill = nightmare_story.skill_id " +
            "INNER JOIN nightmare_colo ON nightmare_stats.colo_skill = nightmare_colo.skill_id " +
            "WHERE nightmare_id = :nightmareLocation")
    fun getSpecificNightmare(nightmareLocation: Int): LiveData<List<NightmareRelation>>
}