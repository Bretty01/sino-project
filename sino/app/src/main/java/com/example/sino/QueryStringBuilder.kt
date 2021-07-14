package com.example.sino

import android.util.Log
import android.view.View
import android.widget.ToggleButton
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder

class QueryStringBuilder {
    private var andSwitch = false
    private var returnString : String? = null
    private var orderString: String? = null
    fun buildFilter(queryString : String, queryGroup : String) {
        if(returnString != null && andSwitch) {
            returnString += ") AND ("
            andSwitch = false
        }
        else if(returnString != null) {
            returnString += " OR"
        }
        else {
            createBaseString(queryGroup, true)
        }
        returnString += " $queryString"
    }

    fun buildSort(queryString: String, queryGroup: String) {
        if(returnString == null) {
            createBaseString(queryGroup, false)
        }
        orderString = " ORDER BY $queryString"
    }

    private fun createBaseString(queryGroup: String, filter: Boolean) {
        when(queryGroup) {
            "armor" -> returnString = "SELECT * FROM armor_stats INNER JOIN armor USING(armor_id) " +
                    "INNER JOIN armor_type ON armor.type = armor_type.type_id " +
                    "INNER JOIN armor_support_skill ON armor.story_skill = armor_support_skill.skill_id " +
                    "INNER JOIN armor_set ON armor.set_skill = armor_set.set_id WHERE armor_stats.rank_id = 1 "
            "nightmare" -> returnString = "SELECT * FROM nightmare_stats " +
                    "INNER JOIN nightmares USING(nightmare_id) "+
                    "INNER JOIN nightmare_story ON nightmare_stats.story_skill = nightmare_story.skill_id " +
                    "INNER JOIN nightmare_colo ON nightmare_stats.colo_skill = nightmare_colo.skill_id WHERE " +
                    "nightmare_stats.rank_id = 1 "
            "weapon" -> returnString = "SELECT * FROM weapon_stats INNER JOIN weapons USING(weapon_id)" +
                    "INNER JOIN weapon_type ON weapons.type = weapon_type.type_id " +
                    "INNER JOIN weapon_support_skill ON weapons.support_skill = weapon_support_skill.skill_id " +
                    "INNER JOIN weapon_colo_skill ON weapons.colo_skill = weapon_colo_skill.skill_id " +
                    "INNER JOIN weapon_story_skill ON weapons.story_skill = weapon_story_skill.skill_id " +
                    "INNER JOIN weapon_element ON weapons.element = weapon_element.element_id WHERE " +
                    "weapon_stats.rank_id = 1 "
        }
        if(filter) {
            returnString += "AND ("
        }
    }

    fun createFilterQuery(filterGroup: Array<Array<Int>>, popupView: View, queryGroup: String) {
        for(filters in filterGroup)
        {
            for(filterTag in filters)
            {
                if(popupView.findViewById<ToggleButton>(filterTag).isChecked) {
                    buildFilter(popupView.findViewById<ToggleButton>(filterTag).tag.toString(), queryGroup)
                }
            }
            parseAnd()
        }
    }

    fun parseAnd() {
        if(returnString != null)
        {
            andSwitch = true
        }
    }

    fun parseRightBracket() {
        if (returnString != null) {
            returnString += ")"
        }
    }

    fun clearQuery() {
        returnString = null
        andSwitch = false
    }

    fun getString(): String? {
        if(orderString != null && returnString != null) {
            return returnString + orderString
        }
        return returnString
    }

}