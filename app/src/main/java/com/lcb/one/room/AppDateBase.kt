package com.lcb.one.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lcb.one.bean.McDay
import com.lcb.one.ui.MyApp

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
