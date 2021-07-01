package com.example.sino.data.weapon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Weapons::class, WeaponStats::class, WeaponType::class, WeaponStorySkill::class,
    WeaponColoSkill::class, WeaponSupportSkill::class, WeaponElement::class],
    views = [WeaponTypeRelation::class], version = 1, exportSchema = false)
abstract class WeaponDatabase : RoomDatabase() {
    abstract fun weaponDao(): WeaponDao

    companion object
    {
        private var dbInstance: WeaponDatabase? = null

        fun getWeaponDatabase(context: Context): WeaponDatabase
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
                    context.applicationContext, WeaponDatabase::class.java,
                    "weapon.db")
                    .createFromAsset("weapons/database/weapon.db")
                    .build()
                dbInstance = instance
                return instance
            }
        }

    }
}