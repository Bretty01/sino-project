package com.example.sino.data.armor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "armor_support_skill")
data class ArmorSupportSkill(
    @PrimaryKey(autoGenerate = true)
    val skill_id: Int,
    val skill_name: String,
    val skill_description: String
)

