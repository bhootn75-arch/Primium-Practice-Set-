package com.example.data.repository

import com.example.data.AppDatabase
import com.example.data.model.PracticeSetEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.TestResultEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class PracticeSetRepository(private val database: AppDatabase) {
  private val setDao = database.practiceSetDao()
  private val questionDao = database.questionDao()
  private val resultDao = database.testResultDao()

  val allSets: Flow<List<PracticeSetEntity>> = setDao.getAllSets()
  val allResults: Flow<List<TestResultEntity>> = resultDao.getAllResults()

  suspend fun getSetById(id: Long): PracticeSetEntity? = withContext(Dispatchers.IO) {
    setDao.getSetById(id)
  }

  fun getQuestionsForSet(setId: Long): Flow<List<QuestionEntity>> = questionDao.getQuestionsForSet(setId)

  suspend fun getQuestionsListForSet(setId: Long): List<QuestionEntity> = withContext(Dispatchers.IO) {
    questionDao.getQuestionsListForSet(setId)
  }

  suspend fun insertOrUpdateSet(set: PracticeSetEntity): Long = withContext(Dispatchers.IO) {
    if (set.id == 0L) {
      setDao.insertSet(set)
    } else {
      setDao.updateSet(set)
      set.id
    }
  }

  suspend fun deleteSet(id: Long) = withContext(Dispatchers.IO) {
    questionDao.deleteAllQuestionsForSet(id)
    setDao.deleteSetById(id)
  }

  suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.IO) {
    setDao.toggleFavorite(id)
  }

  suspend fun saveQuestion(question: QuestionEntity): Long = withContext(Dispatchers.IO) {
    val qId = if (question.id == 0L) {
      questionDao.insertQuestion(question)
    } else {
      questionDao.updateQuestion(question)
      question.id
    }
    setDao.updateQuestionCount(question.setId)
    qId
  }

  suspend fun saveQuestions(setId: Long, questions: List<QuestionEntity>) = withContext(Dispatchers.IO) {
    questionDao.deleteAllQuestionsForSet(setId)
    val indexedQuestions = questions.mapIndexed { index, q ->
      q.copy(id = 0L, setId = setId, questionNumber = index + 1)
    }
    questionDao.insertQuestions(indexedQuestions)
    setDao.updateQuestionCount(setId)
  }

  suspend fun deleteQuestion(question: QuestionEntity) = withContext(Dispatchers.IO) {
    questionDao.deleteQuestion(question)
    setDao.updateQuestionCount(question.setId)
  }

  suspend fun saveTestResult(result: TestResultEntity): Long = withContext(Dispatchers.IO) {
    resultDao.insertResult(result)
  }

  suspend fun exportSetAsJson(setId: Long): String = withContext(Dispatchers.IO) {
    val set = setDao.getSetById(setId) ?: return@withContext ""
    val questions = questionDao.getQuestionsListForSet(setId)
    val root = JSONObject()
    root.put("title", set.title)
    root.put("subject", set.subject)
    root.put("description", set.description)
    root.put("author", set.author)
    root.put("durationMinutes", set.durationMinutes)
    root.put("marksPerQuestion", set.marksPerQuestion.toDouble())
    root.put("negativeMarking", set.negativeMarking.toDouble())

    val qArray = JSONArray()
    for (q in questions) {
      val qObj = JSONObject()
      qObj.put("number", q.questionNumber)
      qObj.put("question", q.questionText)
      qObj.put("optionA", q.optionA)
      qObj.put("optionB", q.optionB)
      qObj.put("optionC", q.optionC)
      qObj.put("optionD", q.optionD)
      qObj.put("correctOption", q.correctOption)
      qObj.put("explanation", q.explanation)
      qObj.put("scriptTag", q.scriptTag)
      qArray.put(qObj)
    }
    root.put("questions", qArray)
    root.toString(2)
  }

  suspend fun importSetFromJson(jsonString: String): Long = withContext(Dispatchers.IO) {
    val root = JSONObject(jsonString)
    val title = root.optString("title", "Imported Practice Set")
    val subject = root.optString("subject", "General")
    val description = root.optString("description", "Imported practice set")
    val author = root.optString("author", "Jagu Sir (Santali Smart Study)")
    val durationMinutes = root.optInt("durationMinutes", 15)
    val marks = root.optDouble("marksPerQuestion", 1.0).toFloat()
    val negative = root.optDouble("negativeMarking", 0.25).toFloat()

    val qArray = root.optJSONArray("questions") ?: JSONArray()
    val newSetId = setDao.insertSet(
      PracticeSetEntity(
        title = title,
        subject = subject,
        description = description,
        author = author,
        durationMinutes = durationMinutes,
        marksPerQuestion = marks,
        negativeMarking = negative,
        totalQuestionsCount = qArray.length()
      )
    )

    val questionList = mutableListOf<QuestionEntity>()
    for (i in 0 until qArray.length()) {
      val qObj = qArray.getJSONObject(i)
      questionList.add(
        QuestionEntity(
          setId = newSetId,
          questionNumber = i + 1,
          questionText = qObj.optString("question", "Question ${i + 1}"),
          optionA = qObj.optString("optionA", "Option A"),
          optionB = qObj.optString("optionB", "Option B"),
          optionC = qObj.optString("optionC", "Option C"),
          optionD = qObj.optString("optionD", "Option D"),
          correctOption = qObj.optString("correctOption", "A"),
          explanation = qObj.optString("explanation", ""),
          scriptTag = qObj.optString("scriptTag", "Ol Chiki & Hindi")
        )
      )
    }
    questionDao.insertQuestions(questionList)
    newSetId
  }
}
