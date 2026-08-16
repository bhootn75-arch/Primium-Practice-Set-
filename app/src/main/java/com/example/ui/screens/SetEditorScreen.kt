package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PracticeSetEntity
import com.example.data.model.QuestionEntity
import com.example.ui.components.OlChikiKeypadBar
import com.example.ui.viewmodel.ActiveScreen
import com.example.ui.viewmodel.PracticeSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetEditorScreen(
  setId: Long?,
  viewModel: PracticeSetViewModel,
  modifier: Modifier = Modifier
) {
  var title by remember { mutableStateOf("") }
  var subject by remember { mutableStateOf("Santali Ol Chiki") }
  var description by remember { mutableStateOf("") }
  var durationMins by remember { mutableStateOf("15") }
  var marksPerQ by remember { mutableStateOf("1.0") }
  var negativeMark by remember { mutableStateOf("0.25") }

  val questions = remember { mutableStateListOf<QuestionEntity>() }
  var activeQuestionDialogIndex by remember { mutableStateOf<Int?>(null) }
  var showBulkImportDialog by remember { mutableStateOf(false) }
  var bulkImportText by remember { mutableStateOf("") }

  // Load existing data if editing
  LaunchedEffect(setId) {
    if (setId != null && setId > 0L) {
      val (set, qList) = viewModel.getSetDetails(setId)
      if (set != null) {
        title = set.title
        subject = set.subject
        description = set.description
        durationMins = set.durationMinutes.toString()
        marksPerQ = set.marksPerQuestion.toString()
        negativeMark = set.negativeMarking.toString()
      }
      questions.clear()
      questions.addAll(qList)
    } else {
      // Default template for new set
      if (questions.isEmpty()) {
        questions.add(
          QuestionEntity(
            setId = 0L,
            questionNumber = 1,
            questionText = "ᱯᱩᱭᱞᱩ ᱠᱩᱠᱞᱤ (First Question): ",
            optionA = "ᱵᱟᱪᱷᱟᱣ ᱮ (Option A)",
            optionB = "ᱵᱟᱪᱷᱟᱣ ᱵᱤ (Option B)",
            optionC = "ᱵᱟᱪᱷᱟᱣ ᱥᱤ (Option C)",
            optionD = "ᱵᱟᱪᱷᱟᱣ ᱰᱤ (Option D)",
            correctOption = "A",
            explanation = "ᱛᱮᱞᱟ ᱵᱤᱵᱚᱨᱚᱬ (Explanation notes here)",
            scriptTag = "Ol Chiki & Hindi"
          )
        )
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    TopAppBar(
      title = {
        Text(
          text = if (setId != null && setId > 0L) "Edit Practice Set" else "Practice Set Maker (ᱠᱩᱠᱞᱤ ᱵᱮᱱᱟᱣ)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = { viewModel.navigateTo(ActiveScreen.Home) },
          modifier = Modifier.testTag("btn_editor_back")
        ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
      },
      actions = {
        IconButton(
          onClick = { showBulkImportDialog = true },
          modifier = Modifier.testTag("btn_bulk_import_questions")
        ) {
          Icon(Icons.Default.PlaylistAdd, contentDescription = "Bulk Add Questions")
        }

        Button(
          onClick = {
            if (title.isBlank()) {
              viewModel.showMessage("Please provide a title for the practice set.")
              return@Button
            }
            if (questions.isEmpty()) {
              viewModel.showMessage("Please add at least one question.")
              return@Button
            }
            val setToSave = PracticeSetEntity(
              id = setId ?: 0L,
              title = title.trim(),
              subject = subject.trim(),
              description = description.trim(),
              author = "Jagu Sir (Santali Smart Study)",
              durationMinutes = durationMins.toIntOrNull() ?: 15,
              marksPerQuestion = marksPerQ.toFloatOrNull() ?: 1.0f,
              negativeMarking = negativeMark.toFloatOrNull() ?: 0.25f,
              totalQuestionsCount = questions.size
            )
            viewModel.savePracticeSetWithQuestions(setToSave, questions.toList()) {
              viewModel.navigateTo(ActiveScreen.Home)
            }
          },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .padding(end = 8.dp)
            .testTag("btn_save_practice_set")
        ) {
          Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Save Set", fontWeight = FontWeight.Bold)
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    )

    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
      // Set Configuration Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Set Details & Exam Settings",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              label = { Text("Practice Set Title (ᱥᱮᱴ ᱧᱩᱛᱩᱢ)*") },
              placeholder = { Text("e.g. Santali Language & Grammar Mock Set 01") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_set_title"),
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject/Category") },
                modifier = Modifier
                  .weight(1f)
                  .testTag("input_set_subject"),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
              )

              OutlinedTextField(
                value = durationMins,
                onValueChange = { durationMins = it },
                label = { Text("Duration (Mins)") },
                modifier = Modifier
                  .weight(0.8f)
                  .testTag("input_set_duration"),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedTextField(
                value = marksPerQ,
                onValueChange = { marksPerQ = it },
                label = { Text("Marks/Question") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
              )

              OutlinedTextField(
                value = negativeMark,
                onValueChange = { negativeMark = it },
                label = { Text("Negative Mark") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = description,
              onValueChange = { description = it },
              label = { Text("Short Description / Instructions") },
              modifier = Modifier.fillMaxWidth(),
              maxLines = 2,
              shape = RoundedCornerShape(10.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Questions Section Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Questions List (${questions.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )

          Button(
            onClick = {
              val newNum = questions.size + 1
              questions.add(
                QuestionEntity(
                  setId = setId ?: 0L,
                  questionNumber = newNum,
                  questionText = "",
                  optionA = "",
                  optionB = "",
                  optionC = "",
                  optionD = "",
                  correctOption = "A",
                  explanation = "",
                  scriptTag = "Ol Chiki & Hindi"
                )
              )
              activeQuestionDialogIndex = questions.size - 1
            },
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.testTag("btn_add_question")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Question")
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
      }

      // Question Items
      itemsIndexed(questions) { index, q ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { activeQuestionDialogIndex = index }
            .testTag("editor_q_item_${index + 1}"),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${index + 1}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 13.sp
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (q.questionText.isNotBlank()) q.questionText else "Untitled Question",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Ans: Option ${q.correctOption} • ${q.scriptTag}",
                fontSize = 11.sp,
                color = Color(0xFF2E7D32)
              )
            }

            // Action icons
            Row {
              // Move Up
              if (index > 0) {
                IconButton(
                  onClick = {
                    val item = questions.removeAt(index)
                    questions.add(index - 1, item)
                  },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up", modifier = Modifier.size(16.dp))
                }
              }

              // Move Down
              if (index < questions.size - 1) {
                IconButton(
                  onClick = {
                    val item = questions.removeAt(index)
                    questions.add(index + 1, item)
                  },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(Icons.Default.ArrowDownward, contentDescription = "Move Down", modifier = Modifier.size(16.dp))
                }
              }

              // Duplicate
              IconButton(
                onClick = {
                  questions.add(index + 1, q.copy(id = 0L, questionNumber = index + 2))
                },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", modifier = Modifier.size(16.dp))
              }

              // Delete
              IconButton(
                onClick = { questions.removeAt(index) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
              }
            }
          }
        }
      }
    }
  }

  // Edit Single Question Dialog with Ol Chiki Keypad Bar
  activeQuestionDialogIndex?.let { editIndex ->
    val editingQ = questions.getOrNull(editIndex)
    if (editingQ != null) {
      QuestionEditorDialog(
        question = editingQ,
        questionIndex = editIndex + 1,
        onSave = { updatedQ ->
          questions[editIndex] = updatedQ
          activeQuestionDialogIndex = null
        },
        onDismiss = { activeQuestionDialogIndex = null }
      )
    }
  }

  // Bulk Import Questions Dialog
  if (showBulkImportDialog) {
    AlertDialog(
      onDismissRequest = { showBulkImportDialog = false },
      title = { Text("Bulk Add Questions (Text Paste)") },
      text = {
        Column {
          Text(
            text = "Paste questions in format:\nQ: Question text\nA: Option A\nB: Option B\nC: Option C\nD: Option D\nAns: A\nExp: Explanation notes\n---",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = bulkImportText,
            onValueChange = { bulkImportText = it },
            placeholder = { Text("Q: ᱚᱞ ᱪᱤᱠᱤ...\nA: ᱚప్ᱥᱚᱱ ᱑\nB: ...\nAns: A") },
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
              .testTag("input_bulk_text"),
            maxLines = 8
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (bulkImportText.isNotBlank()) {
              val parsed = parseBulkQuestions(bulkImportText, setId ?: 0L, questions.size)
              if (parsed.isNotEmpty()) {
                questions.addAll(parsed)
                viewModel.showMessage("Added ${parsed.size} questions!")
                showBulkImportDialog = false
                bulkImportText = ""
              } else {
                viewModel.showMessage("Could not parse questions. Check format.")
              }
            }
          },
          modifier = Modifier.testTag("btn_confirm_bulk_import")
        ) {
          Text("Add All")
        }
      },
      dismissButton = {
        TextButton(onClick = { showBulkImportDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestionEditorDialog(
  question: QuestionEntity,
  questionIndex: Int,
  onSave: (QuestionEntity) -> Unit,
  onDismiss: () -> Unit
) {
  var qText by remember { mutableStateOf(question.questionText) }
  var optA by remember { mutableStateOf(question.optionA) }
  var optB by remember { mutableStateOf(question.optionB) }
  var optC by remember { mutableStateOf(question.optionC) }
  var optD by remember { mutableStateOf(question.optionD) }
  var correctOpt by remember { mutableStateOf(question.correctOption) }
  var explanation by remember { mutableStateOf(question.explanation) }
  var scriptTag by remember { mutableStateOf(question.scriptTag) }

  // Target field for Ol Chiki insertion
  var focusedField by remember { mutableStateOf("question") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Question $questionIndex Editor",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
      ) {
        // Quick Ol Chiki Keypad Bar at the top of editor
        OlChikiKeypadBar(
          onInsertChar = { char ->
            when (focusedField) {
              "question" -> qText += char
              "optionA" -> optA += char
              "optionB" -> optB += char
              "optionC" -> optC += char
              "optionD" -> optD += char
              "explanation" -> explanation += char
            }
          }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = qText,
          onValueChange = {
            qText = it
            focusedField = "question"
          },
          label = { Text("Question Text (ᱠᱩᱠᱞᱤ)*") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_edit_question_text"),
          maxLines = 4,
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = optA,
          onValueChange = {
            optA = it
            focusedField = "optionA"
          },
          label = { Text("Option A*") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_edit_opt_a"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
          value = optB,
          onValueChange = {
            optB = it
            focusedField = "optionB"
          },
          label = { Text("Option B*") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_edit_opt_b"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
          value = optC,
          onValueChange = {
            optC = it
            focusedField = "optionC"
          },
          label = { Text("Option C*") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_edit_opt_c"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
          value = optD,
          onValueChange = {
            optD = it
            focusedField = "optionD"
          },
          label = { Text("Option D*") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_edit_opt_d"),
          singleLine = true,
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Correct Option Selection Row
        Text(
          text = "Correct Answer (ᱥᱟᱹᱨᱤ ᱛᱮᱞᱟ)*",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("A", "B", "C", "D").forEach { opt ->
            val isSelected = correctOpt.equals(opt, ignoreCase = true)
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFF2E7D32) else MaterialTheme.colorScheme.surfaceVariant)
                .clickable { correctOpt = opt }
                .padding(vertical = 8.dp)
                .testTag("select_correct_$opt"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "Option $opt",
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = explanation,
          onValueChange = {
            explanation = it
            focusedField = "explanation"
          },
          label = { Text("Explanation / Solution Notes (ᱛᱮᱞᱟ ᱵᱤᱵᱚᱨᱚᱬ)") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_edit_explanation"),
          maxLines = 3,
          shape = RoundedCornerShape(10.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSave(
            question.copy(
              questionText = qText.trim(),
              optionA = optA.trim(),
              optionB = optB.trim(),
              optionC = optC.trim(),
              optionD = optD.trim(),
              correctOption = correctOpt,
              explanation = explanation.trim(),
              scriptTag = scriptTag
            )
          )
        },
        modifier = Modifier.testTag("btn_save_question_dialog")
      ) {
        Text("Done")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

private fun parseBulkQuestions(rawText: String, setId: Long, startIndex: Int): List<QuestionEntity> {
  val result = mutableListOf<QuestionEntity>()
  val blocks = rawText.split(Regex("---|===|\\n\\n+"))
  var currentNum = startIndex + 1

  for (block in blocks) {
    val lines = block.lines().map { it.trim() }.filter { it.isNotBlank() }
    if (lines.isEmpty()) continue

    var qText = ""
    var optA = ""
    var optB = ""
    var optC = ""
    var optD = ""
    var ans = "A"
    var exp = ""

    for (line in lines) {
      when {
        line.startsWith("Q:", ignoreCase = true) || line.startsWith("Q.", ignoreCase = true) -> {
          qText = line.substring(2).trim()
        }
        line.startsWith("A:", ignoreCase = true) || line.startsWith("A)", ignoreCase = true) || line.startsWith("1.", ignoreCase = true) -> {
          optA = line.substring(2).trim()
        }
        line.startsWith("B:", ignoreCase = true) || line.startsWith("B)", ignoreCase = true) || line.startsWith("2.", ignoreCase = true) -> {
          optB = line.substring(2).trim()
        }
        line.startsWith("C:", ignoreCase = true) || line.startsWith("C)", ignoreCase = true) || line.startsWith("3.", ignoreCase = true) -> {
          optC = line.substring(2).trim()
        }
        line.startsWith("D:", ignoreCase = true) || line.startsWith("D)", ignoreCase = true) || line.startsWith("4.", ignoreCase = true) -> {
          optD = line.substring(2).trim()
        }
        line.startsWith("Ans:", ignoreCase = true) || line.startsWith("Answer:", ignoreCase = true) -> {
          val clean = line.replace(Regex("(?i)Ans(wer)?:"), "").trim().uppercase()
          ans = if (clean.contains("B")) "B" else if (clean.contains("C")) "C" else if (clean.contains("D")) "D" else "A"
        }
        line.startsWith("Exp:", ignoreCase = true) || line.startsWith("Explanation:", ignoreCase = true) -> {
          exp = line.replace(Regex("(?i)Exp(lanation)?:"), "").trim()
        }
      }
    }

    if (qText.isNotBlank()) {
      result.add(
        QuestionEntity(
          setId = setId,
          questionNumber = currentNum++,
          questionText = qText,
          optionA = if (optA.isNotBlank()) optA else "Option A",
          optionB = if (optB.isNotBlank()) optB else "Option B",
          optionC = if (optC.isNotBlank()) optC else "Option C",
          optionD = if (optD.isNotBlank()) optD else "Option D",
          correctOption = ans,
          explanation = exp,
          scriptTag = "Ol Chiki & Hindi"
        )
      )
    }
  }
  return result
}
