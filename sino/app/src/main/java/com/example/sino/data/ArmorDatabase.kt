package com.example.sino.data.armor

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArmorSupportSkill::class], version = 1, exportSchema = true)
abstract class ArmorDatabase: RoomDatabase()
{
    abstract fun armorDao(): ArmorDao

    companion object
    {
        @Volatile
        private var INSTANCE: ArmorDatabase? = null

        fun getSupportDatabase(context: Context): ArmorDatabase
        {
            //Variable to check if the database exists
            //If the database exists, return it
            val tempInstance = INSTANCE
            if(tempInstance != null)
            {
                return tempInstance
            }
            //If the database does not exist, create the database.
            synchronized(this)
            {
               val instance = Room.databaseBuilder(
                   context.applicationContext, ArmorDatabase::class.java,
                   "armor_support_skill"
               ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}