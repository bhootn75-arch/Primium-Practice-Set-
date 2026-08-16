package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
fun QuickRevisionScreen(
  viewModel: PracticeSetViewModel,
  modifier: Modifier = Modifier
) {
  val questions by viewModel.activeRevisionQuestions.collectAsStateWithLifecycle()
  val activeSet by viewModel.activeRevisionSet.collectAsStateWithLifecycle()
  val currentIndex by viewModel.revisionIndex.collectAsStateWithLifecycle()
  val isAnswerRevealed by viewModel.isAnswerRevealed.collectAsStateWithLifecycle()

  var fontScale by remember { mutableFloatStateOf(1.0f) }
  var showJumpSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()

  val currentQuestion = questions.getOrNull(currentIndex)

  if (currentQuestion == null) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("No question available for revision.")
    }
    return
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    TopAppBar(
      title = {
        Column {
          Text(
            text = "Quick Revision • ${activeSet?.title ?: "Practice Set"}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
          )
          Text(
            text = "Card ${currentIndex + 1} of ${questions.size}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      navigationIcon = {
        IconButton(
          onClick = { viewModel.navigateTo(ActiveScreen.Home) },
          modifier = Modifier.testTag("btn_revision_close")
        ) {
          Icon(Icons.Default.Close, contentDescription = "Exit Revision")
        }
      },
      actions = {
        // Reshuffle Questions & Options Toggle
        IconButton(
          onClick = { viewModel.reshuffleRevision() },
          modifier = Modifier.testTag("btn_revision_reshuffle")
        ) {
          Icon(Icons.Default.Shuffle, contentDescription = "Reshuffle Questions & Options", tint = MaterialTheme.colorScheme.primary)
        }

        // Font Magnifier Toggle
        IconButton(
          onClick = {
            fontScale = if (fontScale >= 1.3f) 1.0f else fontScale + 0.15f
          },
          modifier = Modifier.testTag("btn_toggle_font_size")
        ) {
          Icon(Icons.Default.FormatSize, contentDescription = "Text Size")
        }

        // Jump to question
        IconButton(
          onClick = { showJumpSheet = true },
          modifier = Modifier.testTag("btn_revision_jump")
        ) {
          Icon(Icons.Default.GridView, contentDescription = "Jump Matrix")
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
      )
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Question Flashcard
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("revision_card_${currentIndex}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = Color(0xFFFFB300),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "Flashcard ${currentIndex + 1}",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF0A192F),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Surface(
              color = MaterialTheme.colorScheme.surfaceVariant,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = currentQuestion.scriptTag,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Question Content
          Text(
            text = currentQuestion.questionText,
            style = MaterialTheme.typography.titleMedium.copy(
              lineHeight = (26 * fontScale).sp,
              fontSize = (18 * fontScale).sp
            ),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(18.dp))

          // Options List
          val options = listOf(
            Triple("A", currentQuestion.optionA, currentQuestion.correctOption.equals("A", ignoreCase = true)),
            Triple("B", currentQuestion.optionB, currentQuestion.correctOption.equals("B", ignoreCase = true)),
            Triple("C", currentQuestion.optionC, currentQuestion.correctOption.equals("C", ignoreCase = true)),
            Triple("D", currentQuestion.optionD, currentQuestion.correctOption.equals("D", ignoreCase = true))
          )

          options.forEach { (key, text, isCorrect) ->
            val optBg = if (isAnswerRevealed && isCorrect) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            val optBorder = if (isAnswerRevealed && isCorrect) Color(0xFF2E7D32) else Color.Transparent

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(optBg)
                .then(if (isAnswerRevealed && isCorrect) Modifier.border(BorderStroke(1.5.dp, optBorder), RoundedCornerShape(10.dp)) else Modifier)
                .padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(if (isAnswerRevealed && isCorrect) Color(0xFF2E7D32) else MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = key,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (isAnswerRevealed && isCorrect) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Text(
                text = text,
                fontSize = (15 * fontScale).sp,
                fontWeight = if (isAnswerRevealed && isCorrect) FontWeight.Bold else FontWeight.Normal,
                color = if (isAnswerRevealed && isCorrect) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
              )

              if (isAnswerRevealed && isCorrect) {
                Text(
                  text = "✓ Correct",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = Color(0xFF2E7D32)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Reveal Answer Button
          Button(
            onClick = { viewModel.toggleRevealAnswer() },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("btn_reveal_answer"),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isAnswerRevealed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
              contentColor = if (isAnswerRevealed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
              imageVector = if (isAnswerRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isAnswerRevealed) "Hide Answer & Notes" else "Reveal Answer & Explanation (ᱥᱟᱹᱨᱤ ᱛᱮᱞᱟ)",
              fontWeight = FontWeight.Bold
            )
          }

          // Explanation Section
          AnimatedVisibility(
            visible = isAnswerRevealed,
            enter = fadeIn(),
            exit = fadeOut()
          ) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
              Surface(
                color = Color(0xFFFFF8E1),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "Jagu Sir's Study Explanation:",
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = Color(0xFFE65100)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = if (currentQuestion.explanation.isNotBlank()) currentQuestion.explanation else "Correct Option: ${currentQuestion.correctOption}",
                      fontSize = (14 * fontScale).sp,
                      lineHeight = 20.sp,
                      color = Color(0xFF3E2723)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // Bottom Navigation
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(
          onClick = { viewModel.setRevisionIndex(currentIndex - 1) },
          enabled = currentIndex > 0,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("btn_rev_prev")
        ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
          Spacer(modifier = Modifier.width(4.dp))
          Text("Prev")
        }

        Text(
          text = "${currentIndex + 1} / ${questions.size}",
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
          onClick = {
            if (currentIndex < questions.size - 1) {
              viewModel.setRevisionIndex(currentIndex + 1)
            } else {
              viewModel.navigateTo(ActiveScreen.Home)
            }
          },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("btn_rev_next")
        ) {
          Text(if (currentIndex < questions.size - 1) "Next" else "Finish")
          Spacer(modifier = Modifier.width(4.dp))
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
        }
      }
    }
  }

  // Jump to Question Bottom Sheet
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
        Text(
          text = "Jump to Flashcard (ᱠᱩᱠᱞᱤ ᱵᱟᱪᱷᱟᱣ ᱢᱮ)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.foundation.layout.FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          questions.forEachIndexed { idx, _ ->
            val isCurr = idx == currentIndex
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (isCurr) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                  viewModel.setRevisionIndex(idx)
                  scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showJumpSheet = false
                  }
                },
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${idx + 1}",
                fontWeight = FontWeight.Bold,
                color = if (isCurr) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
