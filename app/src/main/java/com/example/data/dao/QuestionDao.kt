package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
  @Query("SELECT * FROM questions WHERE setId = :setId ORDER BY questionNumber ASC")
  fun getQuestionsForSet(setId: Long): Flow<List<QuestionEntity>>

  @Query("SELECT * FROM questions WHERE setId = :setId ORDER BY questionNumber ASC")
  suspend fun getQuestionsListForSet(setId: Long): List<QuestionEntity>

  @Query("SELECT COUNT(*) FROM questions WHERE setId = :setId")
  suspend fun getQuestionCountForSet(setId: Long): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestion(question: QuestionEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long>

  @Update
  suspend fun updateQuestion(question: QuestionEntity)

  @Delete
  suspend fun deleteQuestion(question: QuestionEntity)

  @Query("DELETE FROM questions WHERE id = :id")
  suspend fun deleteQuestionById(id: Long)

  @Query("DELETE FROM questions WHERE setId = :setId")
  suspend fun deleteAllQuestionsForSet(setId: Long)
}
