package com.lcb.one.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.screen.menstruationAssistant.repo.MenstruationDayDao

val appDatabase by lazy {
    Room.databaseBuilder(MyApp.get(), AppDatabase::class.java, "db_salt_fish")
        .build()
}

@Database(
    entities = [MenstruationDay::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMenstruationDayDao(): MenstruationDayDao
}
