package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "questions",
  foreignKeys = [
    ForeignKey(
      entity = PracticeSetEntity::class,
      parentColumns = ["id"],
      childColumns = ["setId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index(value = ["setId"])]
)
data class QuestionEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0L,
  val setId: Long,
  val questionNumber: Int,
  val questionText: String,
  val optionA: String,
  val optionB: String,
  val optionC: String,
  val optionD: String,
  val correctOption: String, // "A", "B", "C", "D"
  val explanation: String = "",
  val scriptTag: String = "Ol Chiki & Hindi" // "Ol Chiki", "Hindi", "English"
)
