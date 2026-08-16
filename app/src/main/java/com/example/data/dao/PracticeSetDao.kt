package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PracticeSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticeSetDao {
  @Query("SELECT * FROM practice_sets ORDER BY createdAt DESC")
  fun getAllSets(): Flow<List<PracticeSetEntity>>

  @Query("SELECT * FROM practice_sets WHERE id = :id")
  suspend fun getSetById(id: Long): PracticeSetEntity?

  @Query("SELECT * FROM practice_sets WHERE subject = :subject ORDER BY createdAt DESC")
  fun getSetsBySubject(subject: String): Flow<List<PracticeSetEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSet(set: PracticeSetEntity): Long

  @Update
  suspend fun updateSet(set: PracticeSetEntity)

  @Delete
  suspend fun deleteSet(set: PracticeSetEntity)

  @Query("DELETE FROM practice_sets WHERE id = :id")
  suspend fun deleteSetById(id: Long)

  @Query("UPDATE practice_sets SET isFavorite = NOT isFavorite WHERE id = :id")
  suspend fun toggleFavorite(id: Long)

  @Query("UPDATE practice_sets SET totalQuestionsCount = (SELECT COUNT(*) FROM questions WHERE setId = :id) WHERE id = :id")
  suspend fun updateQuestionCount(id: Long)
}
