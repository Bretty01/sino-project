package com.example.sino.utilities

import android.util.Log
import android.view.View
import android.widget.ToggleButton

/**
 * Class: QueryStringBuilder
 * Purpose: To build a query based on the input received.
 * @param queryGroup Used to determine which base query string to use
 */
class QueryStringBuilder(queryGroup: String) {
    //Used to tell if you need to parse an AND at the end of the string
    private var andSwitch = false
    //The variable used to hold the the query string (except the ORDER BY portion of the string)
    private var returnString : String? = null
    //Used to hold the ORDER BY portion of the string
    private var orderString: String? = null
    private var queryGroup: String = queryGroup

    /**
     * Function: buildFilter
     * Purpose: To create the filter portion of the query (and any other portion of the query if need be).
     * @param queryString The portion of the query to be parsed into the full returnString.
     */
    private fun buildFilter(queryString : String) {
        //Parse an AND if the andSwitch is flipped to true
        if(returnString != null && andSwitch) {
            returnString += ") AND ("
            andSwitch = false
        }
        //Otherwise parse an OR
        else if(returnString != null) {
            returnString += " OR"
        }
        //If the returnString is null, the base portion of the string must be created
        else {
            createBaseString(true)
        }
        //Add on the portion of the query to the full string
        returnString += " $queryString"
    }

    /**
     * Function: buildSort
     * Purpose: To create the sort portion of the query (and any other portion of the query if need be).
     * @param queryString The portion of the query to be parsed into the full returnString.
     */
    fun buildSort(queryString: String) {
        //If no query string exists, create one based off only the sort
        if(returnString == null) {
            createBaseString(false)
        }
        //Add on the portion of the query to the ORDER BY string
        orderString = " ORDER BY $queryString"
    }

    /**
     * Function: createBaseString
     * Purpose: To create the initial query string if returnString is null.
     * @param filter Used to determine if the base string is to be used for a filter or a sort
     */
    private fun createBaseString(filter: Boolean) {
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
        //If the base returnString is for a filter, parse an AND on the end
        if(filter) {
            returnString += "AND ("
        }
    }

    /**
     * Function: createFilterQuery
     * Purpose: To check if the buttons are pressed and parse in the query based on the buttons pressed.
     * @param filterGroup The 2d array of button references.
     * @param popupView A reference to the filter popup view.
     */
    fun createFilterQuery(filterGroup: Array<Array<Int>>, popupView: View) {
        //Loop through all the buttons. If they are pressed, get the tag information to create the query
        for(filters in filterGroup)
        {
            for(filterTag in filters)
            {
                if(popupView.findViewById<ToggleButton>(filterTag).isChecked) {
                    buildFilter(popupView.findViewById<ToggleButton>(filterTag).tag.toString())
                }
            }
            parseAnd()
        }
    }

    /**
     * Function: parseAnd
     * Purpose: To ensure that an AND will be parsed on to the string the next time buildFilter is called.
     */
    private fun parseAnd() {
        if(returnString != null)
        {
            andSwitch = true
        }
    }

    /**
     * Function: parseRightBracket
     * Purpose: To add on a right bracket to the queryString.
     */
    fun parseRightBracket() {
        if (returnString != null) {
            returnString += ")"
        }
    }

    /**
     * Function: clearQuery
     * Purpose: To clear out the query in returnString. This is to avoid messing up any existing filters.
     */
    fun clearQuery() {
        returnString = null
        andSwitch = false
    }

    /**
     * Function: getString
     * Purpose: To grab the completed query string.
     * @return A completed query string based on whether or not a filter and sort were used.
     */
    fun getString(): String? {
        if(orderString != null && returnString != null) {
            return returnString + orderString
        }
        return returnString
    }

}