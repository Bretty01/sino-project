package com.example.sino.data.nightmare

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Nightmares::class, NightmareStorySkill::class, NightmareColoSkill::class, NightmareStats::class],
    views = [NightmareRelation::class], version = 1, exportSchema = false)
abstract class NightmareDatabase : RoomDatabase() {
    abstract fun nightmareDao(): NightmareDao

    companion object
    {
        private var dbInstance: NightmareDatabase? = null

        fun getNightmareDatabase(context: Context): NightmareDatabase
        {
            //Variable to check if the database exists
            //If the database exists, return it
            val tempInstance = dbInstance
            if(tempInstance != null)
            {
                return tempInstance
            }
            //If the database does not exist, create the database.
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext, NightmareDatabase::class.java,
                    "nightmare.db")
                    .createFromAsset("nightmares/database/nightmare.db")
                    .build()
                dbInstance = instance
                return instance
            }
        }

    }
}