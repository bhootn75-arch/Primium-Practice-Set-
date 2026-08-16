package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "practice_sets")
data class PracticeSetEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0L,
  val title: String,
  val subject: String,
  val description: String,
  val author: String = "Jagu Sir (Santali Smart Study)",
  val durationMinutes: Int = 15,
  val marksPerQuestion: Float = 1.0f,
  val negativeMarking: Float = 0.25f,
  val createdAt: Long = System.currentTimeMillis(),
  val isFavorite: Boolean = false,
  val totalQuestionsCount: Int = 0
)
