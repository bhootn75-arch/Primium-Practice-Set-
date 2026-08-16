package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ActiveScreen
import com.example.ui.viewmodel.PracticeSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreTestInstructionsScreen(
  setId: Long,
  viewModel: PracticeSetViewModel
) {
  val set by viewModel.instructionSet.collectAsState()
  var isAgreed by remember { mutableStateOf(false) }
  var selectedLanguage by remember { mutableStateOf("हिंदी व संथाली (Hindi/Ol Chiki)") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "सामान्य निर्देश (Exam Instructions)",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = set?.title ?: "Practice Set",
              fontSize = 12.sp,
              color = Color.White.copy(alpha = 0.85f),
              maxLines = 1
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = { viewModel.navigateTo(ActiveScreen.Home) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D47A1))
      )
    },
    bottomBar = {
      Surface(
        shadowElevation = 12.dp,
        color = Color.White
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Checkbox(
              checked = isAgreed,
              onCheckedChange = { isAgreed = it },
              modifier = Modifier.testTag("instruction_agree_checkbox")
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "मैंने सभी निर्देश पढ़ और समझ लिए हैं। मैं परीक्षा शुरू करने के लिए सहमत हूँ।",
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF333333),
              lineHeight = 16.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = {
              if (isAgreed) {
                viewModel.startMockTest(setId)
              } else {
                viewModel.showMessage("कृपया आगे बढ़ने के लिए निर्देशों को स्वीकार करें।")
              }
            },
            enabled = isAgreed,
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("start_test_now_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF2E7D32),
              disabledContainerColor = Color(0xFFBDBDBD)
            )
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("टेस्ट शुरू करें (START TEST)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .background(Color(0xFFF7F9FC))
        .padding(16.dp)
    ) {
      // Test Overview Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Badge(containerColor = Color(0xFFE3F2FD)) {
              Text(
                text = set?.subject ?: "Exam Special",
                color = Color(0xFF0D47A1),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
            if (set?.courseTitle != null) {
              Text(
                text = set?.courseTitle ?: "",
                fontSize = 11.sp,
                color = Color(0xFFE65100),
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = set?.title ?: "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 4 Key Stats Box
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            InfoChip(
              title = "कुल प्रश्न",
              value = "${set?.totalQuestionsCount ?: 0} Qs",
              icon = Icons.Default.Quiz,
              color = Color(0xFF1565C0),
              modifier = Modifier.weight(1f)
            )
            InfoChip(
              title = "कुल समय",
              value = "${set?.durationMinutes ?: 15} Min",
              icon = Icons.Default.Timer,
              color = Color(0xFF00897B),
              modifier = Modifier.weight(1f)
            )
            InfoChip(
              title = "कुल अंक",
              value = "${(set?.totalQuestionsCount ?: 0) * (set?.marksPerQuestion ?: 1f)} M",
              icon = Icons.Default.Score,
              color = Color(0xFF8E24AA),
              modifier = Modifier.weight(1f)
            )
            InfoChip(
              title = "निगेटिव",
              value = "-${set?.negativeMarking ?: 0.25f}",
              icon = Icons.Default.Warning,
              color = Color(0xFFD32F2F),
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Language Select Section
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("डिफ़ॉल्ट भाषा चुनें (Select Default Language):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("हिंदी (Hindi)", "संथाली (Ol Chiki)", "English").forEach { lang ->
              FilterChip(
                selected = selectedLanguage.contains(lang.take(4)),
                onClick = { selectedLanguage = lang },
                label = { Text(lang, fontSize = 12.sp) }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Question Status Palette Legend (RWA Standard)
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "प्रश्न संकेत व रंग प्रणाली (Question Palette Symbols):",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1)
          )

          Spacer(modifier = Modifier.height(12.dp))

          PaletteLegendItem(
            color = Color(0xFF2E7D32),
            label = "Answered (उत्तर दिया)",
            desc = "आपने इस प्रश्न का उत्तर सफलतापूर्वक सुरक्षित कर दिया है।"
          )
          PaletteLegendItem(
            color = Color(0xFFD32F2F),
            label = "Not Answered (उत्तर नहीं दिया)",
            desc = "आपने इस प्रश्न को देखा है परंतु कोई उत्तर नहीं चुना है।"
          )
          PaletteLegendItem(
            color = Color(0xFF7B1FA2),
            label = "Marked for Review (समीक्षा के लिए चिह्नित)",
            desc = "आपने प्रश्न को बाद में पुनः जांचने के लिए चिह्नित किया है।"
          )
          PaletteLegendItem(
            color = Color(0xFF9E9E9E),
            label = "Not Visited (नहीं देखा गया)",
            desc = "आप अभी तक इस प्रश्न पर नहीं पहुंचे हैं।"
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Guidelines & Rules
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "महत्वपूर्ण परीक्षा नियम:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
          )

          Spacer(modifier = Modifier.height(8.dp))

          val rules = listOf(
            "1. प्रत्येक सही उत्तर के लिए +${set?.marksPerQuestion ?: 1.0f} अंक प्रदान किए जाएंगे।",
            "2. प्रत्येक गलत उत्तर के लिए -${set?.negativeMarking ?: 0.25f} अंक काटे जाएंगे (नेगेटिव मार्किंग)।",
            "3. 🔀 स्मार्ट शफलिंग सक्रिय: प्रत्येक बार टेस्ट शुरू करने पर सभी प्रश्न एवं चारों ऑप्शन्स (A, B, C, D) नए रैंडम क्रम में आएंगे ताकि रटने की बजाय सटीक ज्ञान की जांच हो सके।",
            "4. समय समाप्त होते ही आपका टेस्ट स्वतः सबमिट हो जाएगा।",
            "5. टेस्ट सबमिट करने के तुरंत बाद ऑल इंडिया रैंक (AIR), पर्सेंटाइल और चारों बॉक्सेस (Total, Correct, Wrong, Skipped) का संपूर्ण विश्लेषण व्याख्या सहित मिलेगा।"
          )

          rules.forEach { rule ->
            Text(
              text = rule,
              fontSize = 12.sp,
              color = Color(0xFF555555),
              lineHeight = 18.sp,
              modifier = Modifier.padding(vertical = 3.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun InfoChip(
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(color.copy(alpha = 0.08f))
      .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
      .padding(vertical = 10.dp, horizontal = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
      Text(title, fontSize = 10.sp, color = Color.Gray)
    }
  }
}

@Composable
private fun PaletteLegendItem(color: Color, label: String, desc: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(color),
      contentAlignment = Alignment.Center
    ) {
      Text("1", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column {
      Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF222222))
      Text(desc, fontSize = 11.sp, color = Color.Gray, lineHeight = 14.sp)
    }
  }
}
