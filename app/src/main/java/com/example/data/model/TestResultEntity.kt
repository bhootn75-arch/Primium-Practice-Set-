package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0L,
  val setId: Long,
  val setTitle: String,
  val totalQuestions: Int,
  val correctCount: Int,
  val wrongCount: Int,
  val skippedCount: Int,
  val score: Float,
  val maxScore: Float,
  val accuracyPercentage: Float,
  val timeTakenSeconds: Int,
  val completedAt: Long = System.currentTimeMillis()
)
