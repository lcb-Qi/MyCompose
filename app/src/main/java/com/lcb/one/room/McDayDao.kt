package com.lcb.one.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lcb.one.bean.McDay
import kotlinx.coroutines.flow.Flow

@Dao
interface McDayDao {

    @Query("SELECT * FROM mc_day ORDER BY start_time")
    fun queryAll(): Flow<List<McDay>>

    @Query("SELECT * FROM mc_day ORDER BY start_time DESC LIMIT 1")
    suspend fun getLastMcDay(): McDay?

    @Query("SELECT * FROM mc_day WHERE finish = false")
    suspend fun getRunningMcDay(): McDay?

    @Update
    suspend fun update(mcDay: McDay)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: McDay)

    @Query("DELETE FROM mc_day")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(mcDay: McDay)
}
