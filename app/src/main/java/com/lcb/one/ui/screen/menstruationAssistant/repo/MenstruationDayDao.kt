package com.lcb.one.ui.screen.menstruationAssistant.repo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lcb.one.ui.screen.menstruationAssistant.repo.model.MenstruationDay
import kotlinx.coroutines.flow.Flow

@Dao
interface MenstruationDayDao {

    @Query("SELECT * FROM mc_day ORDER BY start_time")
    fun queryAll(): Flow<List<MenstruationDay>>

    @Query("SELECT * FROM mc_day ORDER BY start_time DESC LIMIT 1")
    suspend fun getLastMcDay(): MenstruationDay?

    @Query("SELECT * FROM mc_day WHERE finish = false")
    suspend fun getRunningMcDay(): MenstruationDay?

    @Update
    suspend fun update(mcDay: MenstruationDay)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: MenstruationDay)

    @Query("DELETE FROM mc_day")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(mcDay: MenstruationDay)
}
