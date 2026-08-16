package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionEntity
import com.example.data.model.TestResultEntity
import com.example.ui.viewmodel.ActiveScreen
import com.example.ui.viewmodel.PracticeSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestResultScreen(
  result: TestResultEntity?,
  questions: List<QuestionEntity>,
  userAnswers: Map<Int, String>,
  viewModel: PracticeSetViewModel,
  modifier: Modifier = Modifier
) {
  if (result == null) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("No test result available.")
    }
    return
  }

  val percent = if (result.maxScore > 0) (result.score / result.maxScore) * 100f else 0f
  val timeMins = result.timeTakenSeconds / 60
  val timeSecs = result.timeTakenSeconds % 60
  val timeStr = String.format("%02d:%02d min", timeMins, timeSecs)

  val gradeColor = when {
    percent >= 75f -> Color(0xFF2E7D32)
    percent >= 50f -> Color(0xFFFFA000)
    else -> Color(0xFFD32F2F)
  }

  val gradeLabel = when {
    percent >= 85f -> "Excellent! ᱥᱟᱹᱜᱩᱱ ᱫᱟᱨᱟᱢ"
    percent >= 70f -> "Very Good! ᱟᱹᱰᱤ ᱱᱟᱯᱟᱭ"
    percent >= 50f -> "Good Effort! ᱟᱨᱦᱚᱸ ᱠᱩᱨᱩᱢᱩᱴᱩᱭ ᱢᱮ"
    else -> "Needs Revision! ᱟᱨᱦᱚᱸ ᱯᱟᱲᱦᱟᱣ ᱢᱮ"
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "Test Scorecard (ᱨᱮᱡᱚᱞᱴ)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = { viewModel.navigateTo(ActiveScreen.Home) },
          modifier = Modifier.testTag("btn_result_back_home")
        ) {
          Icon(Icons.Default.Home, contentDescription = "Home")
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
      )
    )

    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 12.dp)
    ) {
      // Scorecard Hero Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("scorecard_hero_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = result.setTitle,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Score Circle
            Box(
              modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(gradeColor.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = String.format("%.1f", result.score),
                  fontSize = 30.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = gradeColor
                )
                Text(
                  text = "out of ${result.maxScore.toInt()}",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = gradeLabel,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = gradeColor
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Stat breakdown row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              ResultStatItem(
                icon = Icons.Default.CheckCircle,
                iconColor = Color(0xFF2E7D32),
                label = "Correct",
                value = "${result.correctCount}"
              )
              ResultStatItem(
                icon = Icons.Default.Close,
                iconColor = Color(0xFFD32F2F),
                label = "Wrong",
                value = "${result.wrongCount}"
              )
              ResultStatItem(
                icon = Icons.Default.HelpOutline,
                iconColor = Color(0xFF757575),
                label = "Skipped",
                value = "${result.skippedCount}"
              )
              ResultStatItem(
                icon = Icons.Default.Timer,
                iconColor = MaterialTheme.colorScheme.primary,
                label = "Time",
                value = timeStr
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Retake / Actions Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = { viewModel.startMockTest(result.setId) },
            modifier = Modifier
              .weight(1f)
              .testTag("btn_retake_test"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
          ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Retake Test", fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = { viewModel.startQuickRevision(result.setId) },
            modifier = Modifier
              .weight(1f)
              .testTag("btn_revise_from_result"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Quick Revision", fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "Detailed Solutions & Explanations (ᱥᱟᱹᱨᱤ ᱛᱮᱞᱟ)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))
      }

      // Detailed question cards
      itemsIndexed(questions) { index, q ->
        val userAns = userAnswers[index]
        val isCorrect = userAns != null && userAns.equals(q.correctOption, ignoreCase = true)
        val isSkipped = userAns == null

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("review_q_${index + 1}"),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = BorderStroke(
            1.dp,
            when {
              isCorrect -> Color(0xFF2E7D32).copy(alpha = 0.5f)
              isSkipped -> MaterialTheme.colorScheme.outline
              else -> Color(0xFFD32F2F).copy(alpha = 0.5f)
            }
          )
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Question ${index + 1}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )

              Surface(
                color = when {
                  isCorrect -> Color(0xFFE8F5E9)
                  isSkipped -> Color(0xFFF5F5F5)
                  else -> Color(0xFFFFEBEE)
                },
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = when {
                    isCorrect -> "✓ Correct (+${if (q.correctOption.isNotBlank()) "2" else "1"})"
                    isSkipped -> "— Skipped"
                    else -> "✗ Wrong"
                  },
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = when {
                    isCorrect -> Color(0xFF2E7D32)
                    isSkipped -> Color(0xFF616161)
                    else -> Color(0xFFD32F2F)
                  },
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = q.questionText,
              style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 22.sp,
                fontSize = 15.sp
              ),
              fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Options with indicators
            val options = listOf(
              Triple("A", q.optionA, q.correctOption.equals("A", ignoreCase = true)),
              Triple("B", q.optionB, q.correctOption.equals("B", ignoreCase = true)),
              Triple("C", q.optionC, q.correctOption.equals("C", ignoreCase = true)),
              Triple("D", q.optionD, q.correctOption.equals("D", ignoreCase = true))
            )

            options.forEach { (key, text, isAns) ->
              val isSelectedByUser = userAns.equals(key, ignoreCase = true)
              val optBg = when {
                isAns -> Color(0xFFE8F5E9)
                isSelectedByUser && !isAns -> Color(0xFFFFEBEE)
                else -> Color.Transparent
              }

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(optBg)
                  .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "$key.",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (isAns) Color(0xFF2E7D32) else if (isSelectedByUser) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = text,
                  fontSize = 14.sp,
                  color = if (isAns) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.weight(1f)
                )
                if (isAns) {
                  Text(
                    text = "Correct Answer",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                  )
                } else if (isSelectedByUser) {
                  Text(
                    text = "Your Choice",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                  )
                }
              }
            }

            if (q.explanation.isNotBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              Surface(
                color = Color(0xFFFFF8E1),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(
                      text = "Jagu Sir's Explanation (ᱛᱮᱞᱟ ᱵᱤᱵᱚᱨᱚᱬ):",
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFE65100)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = q.explanation,
                      fontSize = 13.sp,
                      lineHeight = 18.sp,
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
  }
}

@Composable
private fun ResultStatItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconColor: Color,
  label: String,
  value: String
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
  }
}
