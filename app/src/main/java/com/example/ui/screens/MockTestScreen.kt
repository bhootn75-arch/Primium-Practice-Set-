package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.ActiveScreen
import com.example.ui.viewmodel.PracticeSetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MockTestScreen(
  viewModel: PracticeSetViewModel,
  modifier: Modifier = Modifier
) {
  val state by viewModel.mockTestState.collectAsStateWithLifecycle()
  val scope = rememberCoroutineScope()
  var showJumpSheet by remember { mutableStateOf(false) }
  var showSubmitDialog by remember { mutableStateOf(false) }
  var showExitConfirmDialog by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState()

  val questions = state.questions
  val currentIndex = state.currentIndex
  val currentQuestion = questions.getOrNull(currentIndex)

  val mins = state.remainingSeconds / 60
  val secs = state.remainingSeconds % 60
  val timerText = String.format("%02d:%02d", mins, secs)
  val isTimerLow = state.remainingSeconds < 120 // Under 2 mins

  if (currentQuestion == null) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Loading test questions...")
    }
    return
  }

  val selectedOption = state.selectedAnswers[currentIndex]
  val isMarked = state.markedForReview.contains(currentIndex)

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar
    TopAppBar(
      title = {
        Column {
          Text(
            text = state.set?.title ?: "Mock Test",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
          )
          Text(
            text = "Q ${currentIndex + 1} of ${questions.size}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      navigationIcon = {
        IconButton(
          onClick = { showExitConfirmDialog = true },
          modifier = Modifier.testTag("btn_test_close")
        ) {
          Icon(Icons.Default.Close, contentDescription = "Exit Test")
        }
      },
      actions = {
        // Timer Pill
        Surface(
          color = if (isTimerLow) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier.padding(end = 6.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Timer,
              contentDescription = "Timer",
              modifier = Modifier.size(16.dp),
              tint = if (isTimerLow) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = timerText,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = if (isTimerLow) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
          }
        }

        // Jump to Question button
        IconButton(
          onClick = { showJumpSheet = true },
          modifier = Modifier.testTag("btn_jump_to_questions")
        ) {
          Icon(Icons.Default.GridView, contentDescription = "Question Matrix")
        }

        // Submit Button
        Button(
          onClick = { showSubmitDialog = true },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .padding(end = 8.dp)
            .testTag("btn_submit_test"),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2E7D32)
          ),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Text("Submit", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
      )
    )

    // Main Question Body (Scrollable)
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Question Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("question_card_${currentIndex}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = MaterialTheme.colorScheme.primary,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "Question ${currentIndex + 1}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontWeight = FontWeight.Bold
              )
            }

            if (isMarked) {
              Surface(
                color = Color(0xFFFFF3E0),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFFFA000))
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "Review",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Question Text
          Text(
            text = currentQuestion.questionText,
            style = MaterialTheme.typography.titleMedium.copy(
              lineHeight = 26.sp,
              fontSize = 17.sp
            ),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Options A, B, C, D
      val options = listOf(
        Pair("A", currentQuestion.optionA),
        Pair("B", currentQuestion.optionB),
        Pair("C", currentQuestion.optionC),
        Pair("D", currentQuestion.optionD)
      )

      options.forEach { (optKey, optText) ->
        val isSelected = selectedOption == optKey
        OptionSelectCard(
          optionKey = optKey,
          optionText = optText,
          isSelected = isSelected,
          onClick = { viewModel.selectAnswer(currentIndex, optKey) }
        )
        Spacer(modifier = Modifier.height(10.dp))
      }
    }

    // Bottom Navigation Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Prev button
        OutlinedButton(
          onClick = { viewModel.setQuestionIndex(currentIndex - 1) },
          enabled = currentIndex > 0,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("btn_test_prev")
        ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
          Spacer(modifier = Modifier.width(4.dp))
          Text("Prev")
        }

        // Mark for Review toggle
        IconButton(
          onClick = { viewModel.toggleMarkForReview(currentIndex) },
          modifier = Modifier.testTag("btn_mark_review")
        ) {
          Icon(
            imageVector = if (isMarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = "Mark for Review",
            tint = if (isMarked) Color(0xFFFFA000) else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Clear Selection
        if (selectedOption != null) {
          IconButton(
            onClick = { viewModel.selectAnswer(currentIndex, selectedOption) },
            modifier = Modifier.testTag("btn_clear_answer")
          ) {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = "Clear Answer",
              tint = MaterialTheme.colorScheme.error
            )
          }
        }

        // Next or Submit button
        if (currentIndex < questions.size - 1) {
          Button(
            onClick = { viewModel.setQuestionIndex(currentIndex + 1) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("btn_test_next")
          ) {
            Text("Next")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
          }
        } else {
          Button(
            onClick = { showSubmitDialog = true },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            modifier = Modifier.testTag("btn_test_finish")
          ) {
            Text("Finish")
          }
        }
      }
    }
  }

  // Jump to Questions Bottom Sheet
  if (showJumpSheet) {
    ModalBottomSheet(
      onDismissRequest = { showJumpSheet = false },
      sheetState = sheetState
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Jump to Question (ᱥᱟᱱᱟᱢ ᱠᱩᱠᱞᱤ)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Legend
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          LegendPill(color = Color(0xFF2E7D32), label = "Answered (${state.selectedAnswers.size})")
          LegendPill(color = Color(0xFFFFA000), label = "Review (${state.markedForReview.size})")
          LegendPill(color = Color(0xFFE0E0E0), label = "Pending (${questions.size - state.selectedAnswers.size})")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question Matrix
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          questions.forEachIndexed { qIdx, _ ->
            val isAns = state.selectedAnswers.containsKey(qIdx)
            val isRev = state.markedForReview.contains(qIdx)
            val isCurr = qIdx == currentIndex

            val bgColor = when {
              isRev -> Color(0xFFFFA000)
              isAns -> Color(0xFF2E7D32)
              isCurr -> MaterialTheme.colorScheme.primary
              else -> MaterialTheme.colorScheme.surfaceVariant
            }
            val textColor = if (isRev || isAns || isCurr) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(bgColor)
                .border(
                  width = if (isCurr) 2.dp else 0.dp,
                  color = if (isCurr) MaterialTheme.colorScheme.primary else Color.Transparent,
                  shape = CircleShape
                )
                .clickable {
                  viewModel.setQuestionIndex(qIdx)
                  scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showJumpSheet = false
                  }
                }
                .testTag("jump_q_${qIdx + 1}"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${qIdx + 1}",
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontSize = 14.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }

  // Submit Test Dialog
  if (showSubmitDialog) {
    val answeredCount = state.selectedAnswers.size
    val reviewCount = state.markedForReview.size
    val unansweredCount = questions.size - answeredCount

    AlertDialog(
      onDismissRequest = { showSubmitDialog = false },
      title = { Text("Submit Practice Test?") },
      text = {
        Column {
          Text("Are you ready to submit your test answers for instant grading?")
          Spacer(modifier = Modifier.height(12.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Answered:", fontWeight = FontWeight.SemiBold, color = Color(0xFF2E7D32))
            Text("$answeredCount / ${questions.size}", fontWeight = FontWeight.Bold)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Marked for Review:", fontWeight = FontWeight.SemiBold, color = Color(0xFFFFA000))
            Text("$reviewCount", fontWeight = FontWeight.Bold)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Unanswered:", fontWeight = FontWeight.SemiBold, color = Color(0xFF757575))
            Text("$unansweredCount", fontWeight = FontWeight.Bold)
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showSubmitDialog = false
            viewModel.submitTest()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
          modifier = Modifier.testTag("btn_confirm_submit_test")
        ) {
          Text("Submit Now")
        }
      },
      dismissButton = {
        TextButton(onClick = { showSubmitDialog = false }) {
          Text("Continue Test")
        }
      }
    )
  }

  // Exit Confirmation Dialog
  if (showExitConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showExitConfirmDialog = false },
      title = { Text("Exit Mock Test?") },
      text = { Text("Your test progress will not be saved. Are you sure you want to exit to Home?") },
      confirmButton = {
        Button(
          onClick = {
            showExitConfirmDialog = false
            viewModel.navigateTo(ActiveScreen.Home)
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Exit")
        }
      },
      dismissButton = {
        TextButton(onClick = { showExitConfirmDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun OptionSelectCard(
  optionKey: String,
  optionText: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
  val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .testTag("option_$optionKey"),
    shape = RoundedCornerShape(12.dp),
    border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
    colors = CardDefaults.cardColors(containerColor = containerColor)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = optionKey,
          fontWeight = FontWeight.Bold,
          color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 14.sp
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Text(
        text = optionText,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontSize = 15.sp,
          lineHeight = 22.sp
        ),
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f)
      )
    }
  }
}

@Composable
private fun LegendPill(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(10.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
  }
}
