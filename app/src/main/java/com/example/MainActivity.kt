package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.PracticeSetTheme
import com.example.ui.viewmodel.ActiveScreen
import com.example.ui.viewmodel.PracticeSetViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: PracticeSetViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      PracticeSetTheme {
        val activeScreen by viewModel.activeScreen.collectAsStateWithLifecycle()
        val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(snackbarMessage) {
          snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
          }
        }

        // Back navigation handling
        BackHandler(enabled = activeScreen !is ActiveScreen.Home) {
          viewModel.navigateTo(ActiveScreen.Home)
        }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
          Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val screen = activeScreen) {
              is ActiveScreen.Home -> {
                HomeScreen(viewModel = viewModel)
              }
              is ActiveScreen.Auth -> {
                AuthScreen(viewModel = viewModel)
              }
              is ActiveScreen.CourseDetail -> {
                CourseDetailScreen(courseId = screen.courseId, viewModel = viewModel)
              }
              is ActiveScreen.CourseEditor -> {
                CourseEditorScreen(courseId = screen.courseId, viewModel = viewModel)
              }
              is ActiveScreen.PreTestInstructions -> {
                PreTestInstructionsScreen(setId = screen.setId, viewModel = viewModel)
              }
              is ActiveScreen.MockTest -> {
                MockTestScreen(viewModel = viewModel)
              }
              is ActiveScreen.QuickRevision -> {
                QuickRevisionScreen(viewModel = viewModel)
              }
              is ActiveScreen.SetEditor -> {
                SetEditorScreen(
                  setId = screen.setId,
                  targetCourseId = screen.targetCourseId,
                  viewModel = viewModel
                )
              }
              is ActiveScreen.TestResult -> {
                TestResultScreen(
                  result = screen.result,
                  questions = screen.questions,
                  userAnswers = screen.userAnswers,
                  viewModel = viewModel
                )
              }
              is ActiveScreen.Leaderboard -> {
                LeaderboardScreen(
                  setId = screen.setId,
                  viewModel = viewModel
                )
              }
              is ActiveScreen.History -> {
                HistoryScreen(viewModel = viewModel)
              }
              is ActiveScreen.About -> {
                AboutScreen(viewModel = viewModel)
              }
            }

            // Global 5-Star Rating Prompt
            RatingDialog(viewModel = viewModel)

            // Global Admin PIN Prompt
            AdminPinDialog(viewModel = viewModel)
          }
        }
      }
    }
  }
}
