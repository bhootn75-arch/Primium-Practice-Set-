package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.model.CourseEntity
import com.example.data.model.PracticeSetEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.TestResultEntity
import com.example.data.repository.PracticeSetRepository
import com.example.data.seed.DefaultPracticeSets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ActiveScreen {
  object Home : ActiveScreen
  data class CourseDetail(val courseId: Long) : ActiveScreen
  data class PreTestInstructions(val setId: Long) : ActiveScreen
  data class MockTest(val setId: Long) : ActiveScreen
  data class QuickRevision(val setId: Long) : ActiveScreen
  data class SetEditor(val setId: Long? = null, val targetCourseId: Long? = null) : ActiveScreen
  data class CourseEditor(val courseId: Long? = null) : ActiveScreen
  data class TestResult(
    val resultId: Long? = null,
    val result: TestResultEntity? = null,
    val questions: List<QuestionEntity> = emptyList(),
    val userAnswers: Map<Int, String> = emptyMap()
  ) : ActiveScreen
  data class Leaderboard(val setId: Long? = null) : ActiveScreen
  object Auth : ActiveScreen
  object History : ActiveScreen
  object About : ActiveScreen
}

data class UserProfile(
  val name: String = "Jagu Student",
  val phone: String = "9876543210",
  val email: String = "student@smartstudy.com",
  val targetExam: String = "JSSC कक्षपाल / झारखंड पुलिस",
  val isLoggedIn: Boolean = true,
  val studentId: String = "SS-2026-9842"
)

data class LeaderboardItem(
  val rank: Int,
  val name: String,
  val score: Float,
  val maxScore: Float,
  val accuracy: Float,
  val timeTaken: String,
  val isCurrentUser: Boolean = false,
  val avatarColorHex: Long = 0xFF1976D2
)

data class MockTestState(
  val set: PracticeSetEntity? = null,
  val questions: List<QuestionEntity> = emptyList(),
  val currentIndex: Int = 0,
  val selectedAnswers: Map<Int, String> = emptyMap(), // questionIndex -> "A", "B", "C", "D"
  val markedForReview: Set<Int> = emptySet(),
  val visitedQuestions: Set<Int> = setOf(0),
  val remainingSeconds: Int = 0,
  val totalSeconds: Int = 0,
  val isTimerRunning: Boolean = false,
  val isSubmitted: Boolean = false,
  val selectedLanguage: String = "Hindi & Ol Chiki"
)

class PracticeSetViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: PracticeSetRepository
  private val prefs = application.getSharedPreferences("smart_study_prefs", Context.MODE_PRIVATE)

  val allCourses: StateFlow<List<CourseEntity>>
  val enrolledCourses: StateFlow<List<CourseEntity>>
  val allSets: StateFlow<List<PracticeSetEntity>>
  val allResults: StateFlow<List<TestResultEntity>>

  private val _activeScreen = MutableStateFlow<ActiveScreen>(ActiveScreen.Home)
  val activeScreen: StateFlow<ActiveScreen> = _activeScreen.asStateFlow()

  private val _currentHomeTab = MutableStateFlow(0) // 0: Home/Courses, 1: Test Series, 2: Leaderboard, 3: Profile
  val currentHomeTab: StateFlow<Int> = _currentHomeTab.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  // User Authentication & Profile
  private val _userProfile = MutableStateFlow(
    UserProfile(
      name = prefs.getString("user_name", "Student (झारखंड अभ्यर्थी)") ?: "Student",
      phone = prefs.getString("user_phone", "+91 9876543210") ?: "+91 9876543210",
      email = prefs.getString("user_email", "student@smartstudy.com") ?: "student@smartstudy.com",
      targetExam = prefs.getString("target_exam", "JSSC कक्षपाल / झारखंड पुलिस") ?: "JSSC कक्षपाल / झारखंड पुलिस",
      isLoggedIn = prefs.getBoolean("is_logged_in", true),
      studentId = prefs.getString("student_id", "SS-2026-9842") ?: "SS-2026-9842"
    )
  )
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  // Admin Access Control (Jagu Sir / Teacher Portal)
  private val _isAdminMode = MutableStateFlow(prefs.getBoolean("is_admin_mode", false))
  val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

  private val _showAdminPinDialog = MutableStateFlow(false)
  val showAdminPinDialog: StateFlow<Boolean> = _showAdminPinDialog.asStateFlow()

  // Rating Dialog State
  private val _showRatingDialog = MutableStateFlow(false)
  val showRatingDialog: StateFlow<Boolean> = _showRatingDialog.asStateFlow()

  private val _userRating = MutableStateFlow(5)
  val userRating: StateFlow<Int> = _userRating.asStateFlow()

  // Mock test session
  private val _mockTestState = MutableStateFlow(MockTestState())
  val mockTestState: StateFlow<MockTestState> = _mockTestState.asStateFlow()
  private var timerJob: Job? = null

  // Active Pre-Test Instructions Set
  private val _instructionSet = MutableStateFlow<PracticeSetEntity?>(null)
  val instructionSet: StateFlow<PracticeSetEntity?> = _instructionSet.asStateFlow()

  // Revision state
  private val _revisionIndex = MutableStateFlow(0)
  val revisionIndex: StateFlow<Int> = _revisionIndex.asStateFlow()
  private val _isAnswerRevealed = MutableStateFlow(false)
  val isAnswerRevealed: StateFlow<Boolean> = _isAnswerRevealed.asStateFlow()
  private val _activeRevisionQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
  val activeRevisionQuestions: StateFlow<List<QuestionEntity>> = _activeRevisionQuestions.asStateFlow()
  private val _activeRevisionSet = MutableStateFlow<PracticeSetEntity?>(null)
  val activeRevisionSet: StateFlow<PracticeSetEntity?> = _activeRevisionSet.asStateFlow()

  // Toast / notification message
  private val _snackbarMessage = MutableStateFlow<String?>(null)
  val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

  init {
    val db = AppDatabase.getDatabase(application, viewModelScope)
    repository = PracticeSetRepository(db)
    allCourses = repository.allCourses.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )
    enrolledCourses = repository.enrolledCourses.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )
    allSets = repository.allSets.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )
    allResults = repository.allResults.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    // Ensure initial data exists
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val sampleSet = repository.getSetById(1)
        if (sampleSet == null) {
          DefaultPracticeSets.populateInitialData(db)
        }
      } catch (e: Exception) {
        // ignore
      }
    }
  }

  fun navigateTo(screen: ActiveScreen) {
    if (screen !is ActiveScreen.MockTest) {
      timerJob?.cancel()
    }
    // Guard Admin Screens from unauthorized direct access
    if ((screen is ActiveScreen.SetEditor || screen is ActiveScreen.CourseEditor) && !_isAdminMode.value) {
      _showAdminPinDialog.value = true
      showMessage("🔒 यह अनुभाग केवल एडमिन (Jagu Sir / Teacher) के लिए सुरक्षित है। कृपया एडमिन पिन दर्ज करें।")
      return
    }
    _activeScreen.value = screen
  }

  // --- Admin Access Control Methods ---
  fun openAdminPinDialog() {
    _showAdminPinDialog.value = true
  }

  fun dismissAdminPinDialog() {
    _showAdminPinDialog.value = false
  }

  fun getSavedAdminPin(): String {
    return prefs.getString("admin_pin", "7890") ?: "7890"
  }

  fun verifyAndLoginAdmin(enteredPin: String): Boolean {
    val savedPin = getSavedAdminPin()
    return if (enteredPin.trim() == savedPin) {
      _isAdminMode.value = true
      prefs.edit().putBoolean("is_admin_mode", true).apply()
      _showAdminPinDialog.value = false
      showMessage("👑 एडमिन मोड सक्रिय! अब आप नए कोर्स व 50-60 प्रश्नों के प्रैक्टिस सेट जोड़/संपादित कर सकते हैं।")
      true
    } else {
      showMessage("❌ गलत एडमिन पिन! केवल अधिकृत शिक्षक/एडमिन ही कंटेंट बदल सकते हैं।")
      false
    }
  }

  fun exitAdminMode() {
    _isAdminMode.value = false
    prefs.edit().putBoolean("is_admin_mode", false).apply()
    showMessage("विद्यार्थी मोड (Student Mode) सक्रिय हो गया।")
    if (_activeScreen.value is ActiveScreen.SetEditor || _activeScreen.value is ActiveScreen.CourseEditor) {
      navigateTo(ActiveScreen.Home)
    }
  }

  fun changeAdminPin(currentPin: String, newPin: String): Boolean {
    if (currentPin != getSavedAdminPin()) {
      showMessage("❌ वर्तमान पिन गलत है!")
      return false
    }
    if (newPin.trim().length < 4) {
      showMessage("❌ नया पिन कम से कम 4 अंकों का होना चाहिए!")
      return false
    }
    prefs.edit().putString("admin_pin", newPin.trim()).apply()
    showMessage("✅ एडमिन सुरक्षा पिन सफलतापूर्वक बदल दिया गया!")
    return true
  }

  fun setHomeTab(tab: Int) {
    _currentHomeTab.value = tab
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSelectedCategory(cat: String) {
    _selectedCategory.value = cat
  }

  fun showMessage(msg: String) {
    _snackbarMessage.value = msg
  }

  fun clearMessage() {
    _snackbarMessage.value = null
  }

  // --- Rating Flow ---
  fun openRatingDialog() {
    _showRatingDialog.value = true
  }

  fun dismissRatingDialog() {
    _showRatingDialog.value = false
  }

  fun setUserRating(rating: Int) {
    _userRating.value = rating
  }

  fun submitRating(feedback: String = "") {
    _showRatingDialog.value = false
    showMessage("धन्यवाद! 5-Star रेटिंग व रिव्यू सफलतापूर्वक सबमिट हुआ।")
  }

  // --- Auth Flow ---
  fun loginUser(phone: String, name: String, targetExam: String) {
    val newProfile = UserProfile(
      name = name.ifBlank { "अभ्यर्थी" },
      phone = phone,
      email = "$phone@smartstudy.com",
      targetExam = targetExam,
      isLoggedIn = true,
      studentId = "SS-2026-${(1000..9999).random()}"
    )
    _userProfile.value = newProfile
    prefs.edit()
      .putString("user_name", newProfile.name)
      .putString("user_phone", newProfile.phone)
      .putString("user_email", newProfile.email)
      .putString("target_exam", newProfile.targetExam)
      .putBoolean("is_logged_in", true)
      .putString("student_id", newProfile.studentId)
      .apply()

    showMessage("सत्यापन सफल! आपका स्वागत है।")
    navigateTo(ActiveScreen.Home)
  }

  fun loginWithGoogle(
    email: String = "bhootn75@gmail.com",
    name: String = "विद्यार्थी (JSSC Aspirant)",
    targetExam: String = "JSSC कक्षपाल (Warder)"
  ) {
    val newProfile = UserProfile(
      name = name,
      phone = "+91 9876543210",
      email = email,
      targetExam = targetExam,
      isLoggedIn = true,
      studentId = "SS-2026-${(1000..9999).random()}"
    )
    _userProfile.value = newProfile
    prefs.edit()
      .putString("user_name", newProfile.name)
      .putString("user_phone", newProfile.phone)
      .putString("user_email", newProfile.email)
      .putString("target_exam", newProfile.targetExam)
      .putBoolean("is_logged_in", true)
      .putString("student_id", newProfile.studentId)
      .apply()

    showMessage("Google खाता ($email) से लॉगिन सफल!")
    navigateTo(ActiveScreen.Home)
  }

  fun skipAuthToHome() {
    navigateTo(ActiveScreen.Home)
  }

  fun logoutUser() {
    _userProfile.value = _userProfile.value.copy(isLoggedIn = false)
    prefs.edit().putBoolean("is_logged_in", false).apply()
    navigateTo(ActiveScreen.Auth)
  }

  // --- Course Operations ---
  fun enrollInCourse(courseId: Long, courseTitle: String) {
    viewModelScope.launch {
      repository.enrollInCourse(courseId)
      showMessage("बधाई! '$courseTitle' कोर्स अनलॉक हो गया है।")
    }
  }

  fun saveCourse(course: CourseEntity, onComplete: () -> Unit) {
    viewModelScope.launch {
      repository.insertCourse(course)
      showMessage("कोर्स सफलतापूर्वक सेव हुआ!")
      onComplete()
    }
  }

  fun deleteCourse(courseId: Long) {
    viewModelScope.launch {
      repository.deleteCourse(courseId)
      showMessage("कोर्स हटा दिया गया।")
    }
  }

  suspend fun getCourseById(courseId: Long): CourseEntity? {
    return repository.getCourseById(courseId)
  }

  // --- Pre-Test Instructions ---
  fun openPreTestInstructions(setId: Long) {
    viewModelScope.launch {
      val set = repository.getSetById(setId)
      if (set != null) {
        _instructionSet.value = set
        navigateTo(ActiveScreen.PreTestInstructions(setId))
      } else {
        showMessage("Practice Set not found.")
      }
    }
  }

  // --- Helper to Shuffle Questions & Options for Anti-Guessing Practice ---
  private fun prepareShuffledQuestions(rawQuestions: List<QuestionEntity>): List<QuestionEntity> {
    return rawQuestions.shuffled().mapIndexed { index, q ->
      val standardCorrectKey = when (q.correctOption.trim().uppercase()) {
        "A", "1" -> "A"
        "B", "2" -> "B"
        "C", "3" -> "C"
        "D", "4" -> "D"
        else -> "A"
      }

      val pairs = listOf(
        "A" to q.optionA,
        "B" to q.optionB,
        "C" to q.optionC,
        "D" to q.optionD
      ).shuffled()

      val newCorrectOption = when {
        pairs[0].first == standardCorrectKey -> "A"
        pairs[1].first == standardCorrectKey -> "B"
        pairs[2].first == standardCorrectKey -> "C"
        pairs[3].first == standardCorrectKey -> "D"
        else -> "A"
      }

      q.copy(
        questionNumber = index + 1,
        optionA = pairs[0].second,
        optionB = pairs[1].second,
        optionC = pairs[2].second,
        optionD = pairs[3].second,
        correctOption = newCorrectOption
      )
    }
  }

  // --- Mock Test Operations (RWA Style) ---
  fun startMockTest(setId: Long) {
    viewModelScope.launch {
      val set = repository.getSetById(setId)
      val rawQuestions = repository.getQuestionsListForSet(setId)
      if (rawQuestions.isEmpty()) {
        showMessage("इस सेट में कोई प्रश्न नहीं है! कृपया पहले प्रश्न जोड़ें।")
        return@launch
      }
      // Shuffling questions and all options for fresh non-predictable practice
      val shuffledQuestions = prepareShuffledQuestions(rawQuestions)
      val totalSec = (set?.durationMinutes ?: 15) * 60
      _mockTestState.value = MockTestState(
        set = set,
        questions = shuffledQuestions,
        currentIndex = 0,
        selectedAnswers = emptyMap(),
        markedForReview = emptySet(),
        visitedQuestions = setOf(0),
        remainingSeconds = totalSec,
        totalSeconds = totalSec,
        isTimerRunning = true,
        isSubmitted = false
      )
      navigateTo(ActiveScreen.MockTest(setId))
      startTimer()
    }
  }

  private fun startTimer() {
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_mockTestState.value.remainingSeconds > 0 && !_mockTestState.value.isSubmitted) {
        delay(1000L)
        _mockTestState.value = _mockTestState.value.copy(
          remainingSeconds = _mockTestState.value.remainingSeconds - 1
        )
      }
      if (_mockTestState.value.remainingSeconds <= 0 && !_mockTestState.value.isSubmitted) {
        submitTest()
      }
    }
  }

  fun selectAnswer(questionIndex: Int, option: String) {
    val current = _mockTestState.value.selectedAnswers.toMutableMap()
    if (current[questionIndex] == option) {
      current.remove(questionIndex)
    } else {
      current[questionIndex] = option
    }
    _mockTestState.value = _mockTestState.value.copy(selectedAnswers = current)
  }

  fun clearAnswer(questionIndex: Int) {
    val current = _mockTestState.value.selectedAnswers.toMutableMap()
    current.remove(questionIndex)
    _mockTestState.value = _mockTestState.value.copy(selectedAnswers = current)
  }

  fun toggleMarkForReview(questionIndex: Int) {
    val current = _mockTestState.value.markedForReview.toMutableSet()
    if (current.contains(questionIndex)) {
      current.remove(questionIndex)
    } else {
      current.add(questionIndex)
    }
    _mockTestState.value = _mockTestState.value.copy(markedForReview = current)
  }

  fun setQuestionIndex(index: Int) {
    if (index in 0 until _mockTestState.value.questions.size) {
      val visited = _mockTestState.value.visitedQuestions.toMutableSet()
      visited.add(index)
      _mockTestState.value = _mockTestState.value.copy(
        currentIndex = index,
        visitedQuestions = visited
      )
    }
  }

  fun saveAndNext() {
    val state = _mockTestState.value
    if (state.currentIndex < state.questions.size - 1) {
      setQuestionIndex(state.currentIndex + 1)
    }
  }

  fun markAndNext() {
    val state = _mockTestState.value
    toggleMarkForReview(state.currentIndex)
    if (state.currentIndex < state.questions.size - 1) {
      setQuestionIndex(state.currentIndex + 1)
    }
  }

  fun submitTest() {
    timerJob?.cancel()
    val state = _mockTestState.value
    val set = state.set ?: return
    val questions = state.questions
    val answers = state.selectedAnswers

    var correct = 0
    var wrong = 0
    var skipped = 0

    questions.forEachIndexed { index, q ->
      val userAns = answers[index]
      if (userAns == null) {
        skipped++
      } else if (userAns.equals(q.correctOption, ignoreCase = true)) {
        correct++
      } else {
        wrong++
      }
    }

    val marksPerQ = set.marksPerQuestion
    val negPerQ = set.negativeMarking
    val rawScore = (correct * marksPerQ) - (wrong * negPerQ)
    val score = if (rawScore < 0) 0f else rawScore
    val maxScore = questions.size * marksPerQ
    val accuracy = if (correct + wrong > 0) (correct.toFloat() / (correct + wrong)) * 100f else 0f
    val timeTaken = state.totalSeconds - state.remainingSeconds

    val result = TestResultEntity(
      setId = set.id,
      setTitle = set.title,
      totalQuestions = questions.size,
      correctCount = correct,
      wrongCount = wrong,
      skippedCount = skipped,
      score = score,
      maxScore = maxScore,
      accuracyPercentage = accuracy,
      timeTakenSeconds = timeTaken
    )

    viewModelScope.launch {
      val resId = repository.saveTestResult(result)
      _mockTestState.value = state.copy(isSubmitted = true, isTimerRunning = false)
      navigateTo(
        ActiveScreen.TestResult(
          resultId = resId,
          result = result,
          questions = questions,
          userAnswers = answers
        )
      )
    }
  }

  // --- Leaderboard Generation ---
  fun getLeaderboardForTest(
    result: TestResultEntity?,
    userScore: Float,
    userAccuracy: Float,
    timeTakenSecs: Int
  ): List<LeaderboardItem> {
    val maxMarks = result?.maxScore ?: 50f
    val currentUserName = _userProfile.value.name
    val timeStr = "${timeTakenSecs / 60}m ${timeTakenSecs % 60}s"

    val list = mutableListOf(
      LeaderboardItem(
        rank = 1,
        name = "रोहित कुमार मुर्मू (Dumka)",
        score = (maxMarks * 0.96f),
        maxScore = maxMarks,
        accuracy = 98.2f,
        timeTaken = "11m 42s",
        avatarColorHex = 0xFF2E7D32
      ),
      LeaderboardItem(
        rank = 2,
        name = "अंजलि सोरेन (Ranchi)",
        score = (maxMarks * 0.92f),
        maxScore = maxMarks,
        accuracy = 95.0f,
        timeTaken = "13m 15s",
        avatarColorHex = 0xFF1976D2
      ),
      LeaderboardItem(
        rank = 3,
        name = "सोनू हांसदा (Jamshedpur)",
        score = (maxMarks * 0.88f),
        maxScore = maxMarks,
        accuracy = 92.4f,
        timeTaken = "14m 02s",
        avatarColorHex = 0xFF8E24AA
      ),
      LeaderboardItem(
        rank = 4,
        name = "विकास मरांडी (Godda)",
        score = (maxMarks * 0.84f),
        maxScore = maxMarks,
        accuracy = 89.0f,
        timeTaken = "14m 30s",
        avatarColorHex = 0xFFE65100
      ),
      LeaderboardItem(
        rank = 5,
        name = "$currentUserName (आप)",
        score = userScore,
        maxScore = maxMarks,
        accuracy = userAccuracy,
        timeTaken = timeStr,
        isCurrentUser = true,
        avatarColorHex = 0xFF0288D1
      ),
      LeaderboardItem(
        rank = 6,
        name = "पंकज बेसरा (Pakur)",
        score = (maxMarks * 0.76f),
        maxScore = maxMarks,
        accuracy = 82.5f,
        timeTaken = "15m 10s",
        avatarColorHex = 0xFF5D4037
      )
    )

    // Sort by score descending
    return list.sortedByDescending { it.score }.mapIndexed { idx, item ->
      item.copy(rank = idx + 1)
    }
  }

  // --- Quick Revision Mode ---
  fun startQuickRevision(setId: Long) {
    viewModelScope.launch {
      val set = repository.getSetById(setId)
      val rawQuestions = repository.getQuestionsListForSet(setId)
      if (rawQuestions.isEmpty()) {
        showMessage("इस सेट में रिविज़न के लिए प्रश्न नहीं हैं।")
        return@launch
      }
      val shuffledQuestions = prepareShuffledQuestions(rawQuestions)
      _activeRevisionSet.value = set
      _activeRevisionQuestions.value = shuffledQuestions
      _revisionIndex.value = 0
      _isAnswerRevealed.value = false
      navigateTo(ActiveScreen.QuickRevision(setId))
    }
  }

  fun reshuffleRevision() {
    val currentQuestions = _activeRevisionQuestions.value
    if (currentQuestions.isNotEmpty()) {
      _activeRevisionQuestions.value = prepareShuffledQuestions(currentQuestions)
      _revisionIndex.value = 0
      _isAnswerRevealed.value = false
      showMessage("🔀 सभी प्रश्न व विकल्प पुनः शफल कर दिए गए!")
    }
  }

  fun setRevisionIndex(index: Int) {
    if (index in 0 until _activeRevisionQuestions.value.size) {
      _revisionIndex.value = index
      _isAnswerRevealed.value = false
    }
  }

  fun toggleRevealAnswer() {
    _isAnswerRevealed.value = !_isAnswerRevealed.value
  }

  fun toggleFavorite(setId: Long) {
    viewModelScope.launch {
      repository.toggleFavorite(setId)
    }
  }

  fun deleteSet(setId: Long) {
    viewModelScope.launch {
      repository.deleteSet(setId)
      showMessage("प्रैक्टिस सेट हटा दिया गया।")
      if (_activeScreen.value is ActiveScreen.SetEditor) {
        navigateTo(ActiveScreen.Home)
      }
    }
  }

  // --- JSON & Set Import/Export ---
  fun exportSet(setId: Long, onExported: (String) -> Unit) {
    viewModelScope.launch {
      val json = repository.exportSetAsJson(setId)
      onExported(json)
    }
  }

  fun importSetFromJson(json: String) {
    viewModelScope.launch {
      try {
        val newId = repository.importSetFromJson(json)
        showMessage("प्रैक्टिस सेट सफलतापूर्वक लोड हुआ!")
        navigateTo(ActiveScreen.Home)
      } catch (e: Exception) {
        showMessage("त्रुटि: अमान्य JSON फॉर्मेट।")
      }
    }
  }

  // --- Set Creator & Questions ---
  suspend fun getSetDetails(setId: Long): Pair<PracticeSetEntity?, List<QuestionEntity>> {
    val set = repository.getSetById(setId)
    val questions = repository.getQuestionsListForSet(setId)
    return Pair(set, questions)
  }

  fun savePracticeSetWithQuestions(
    set: PracticeSetEntity,
    questions: List<QuestionEntity>,
    onComplete: () -> Unit
  ) {
    viewModelScope.launch {
      val savedSetId = repository.insertOrUpdateSet(set.copy(totalQuestionsCount = questions.size))
      repository.saveQuestions(savedSetId, questions)
      showMessage("प्रैक्टिस सेट सफलतापूर्वक सेव हुआ!")
      onComplete()
    }
  }
}
