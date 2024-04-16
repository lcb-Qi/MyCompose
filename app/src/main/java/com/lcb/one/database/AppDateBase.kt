package com.lcb.one.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lcb.one.ui.screen.mcAssistant.repo.model.McDay
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.screen.mcAssistant.repo.McDayDao

private const val TAG = "AppDateBase"

val appDatabase by lazy {
    KotlinVersion.CURRENT
    Room.databaseBuilder(MyApp.getAppContext(), AppDatabase::class.java, "db_salt_fish")
        .build()
}

@Database(
    entities = [McDay::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMcDayDao(): McDayDao
}
