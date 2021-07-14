package com.example.sino.data.armor

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArmorSupportSkill::class, ArmorType::class, ArmorSet::class,
    Armor::class, ArmorStats::class], views = [ArmorRelation::class], version = 1, exportSchema = true)
abstract class ArmorDatabase : RoomDatabase() {
    abstract fun armorDao(): ArmorDao

    companion object
    {
        @Volatile
        private var INSTANCE: ArmorDatabase? = null

        fun getArmorDatabase(context: Context): ArmorDatabase
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
                   "armor.db")
                   .createFromAsset("armor/database/armor.db")
                   .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}