package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.TestResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestResultDao {
  @Query("SELECT * FROM test_results ORDER BY completedAt DESC")
  fun getAllResults(): Flow<List<TestResultEntity>>

  @Query("SELECT * FROM test_results WHERE setId = :setId ORDER BY completedAt DESC")
  fun getResultsForSet(setId: Long): Flow<List<TestResultEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertResult(result: TestResultEntity): Long

  @Query("DELETE FROM test_results WHERE id = :id")
  suspend fun deleteResultById(id: Long)

  @Query("DELETE FROM test_results")
  suspend fun clearAllResults()
}
