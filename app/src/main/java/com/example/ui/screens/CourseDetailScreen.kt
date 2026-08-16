package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseEntity
import com.example.data.model.PracticeSetEntity
import com.example.ui.viewmodel.ActiveScreen
import com.example.ui.viewmodel.PracticeSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
  courseId: Long,
  viewModel: PracticeSetViewModel
) {
  val allCourses by viewModel.allCourses.collectAsState()
  val allSets by viewModel.allSets.collectAsState()
  val isAdminMode by viewModel.isAdminMode.collectAsState()
  val course = allCourses.find { it.id == courseId }
  val courseSets = allSets.filter { it.courseId == courseId || (courseId == 1L && it.courseId == null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "कोर्स विवरण (Course Details)",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color.White
          )
        },
        navigationIcon = {
          IconButton(onClick = { viewModel.navigateTo(ActiveScreen.Home) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        actions = {
          if (isAdminMode) {
            IconButton(onClick = {
              viewModel.navigateTo(ActiveScreen.SetEditor(targetCourseId = courseId))
            }) {
              Icon(Icons.Default.Add, contentDescription = "Add Test", tint = Color.White)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D47A1))
      )
    },
    bottomBar = {
      if (course != null) {
        Surface(shadowElevation = 10.dp, color = Color.White) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "₹${course.price}",
                  fontSize = 22.sp,
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "₹${course.originalPrice}",
                  fontSize = 14.sp,
                  color = Color.Gray,
                  textDecoration = TextDecoration.LineThrough
                )
              }
              Text(
                text = "वैधता: ${course.validityMonths} माह • संपूर्ण टेस्ट अनलॉक",
                fontSize = 11.sp,
                color = Color.DarkGray
              )
            }

            if (course.isEnrolled) {
              Button(
                onClick = { viewModel.showMessage("आप पहले से ही इस बैच में नामांकित हैं!") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("नामांकित (Enrolled)", fontWeight = FontWeight.Bold)
              }
            } else {
              Button(
                onClick = { viewModel.enrollInCourse(course.id, course.title) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("buy_course_now_btn")
              ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("कोर्स खरीदें (Buy Now)", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  ) { paddingValues ->
    if (course == null) {
      Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .background(Color(0xFFF7F9FB))
          .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Hero Course Header Card
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .background(
                  Brush.verticalGradient(
                    listOf(Color(0xFF0D47A1), Color(0xFF1565C0))
                  )
                )
                .padding(18.dp)
            ) {
              Column {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color(0xFFFFD54F)
                ) {
                  Text(
                    text = course.badge.uppercase(),
                    color = Color(0xFF3E2723),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = course.title,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = course.description,
                  fontSize = 13.sp,
                  color = Color.White.copy(alpha = 0.9f),
                  lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  CourseFeatureChip(text = "🔥 ${courseSets.size} प्रैक्टिस सेट")
                  CourseFeatureChip(text = "🛡️ ऑल इंडिया रैंक")
                  CourseFeatureChip(text = "💡 ट्रिक समाधान")
                }
              }
            }
          }
        }

        // Features List
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "कोर्स की मुख्य विशेषताएं (Course Features):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1A237E)
              )

              Spacer(modifier = Modifier.height(10.dp))

              val featureItems = course.features.split(",")
              featureItems.forEach { feat ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(feat.trim(), fontSize = 13.sp, color = Color(0xFF333333))
                }
              }
            }
          }
        }

        // Attached Practice Sets Header
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "शामिल प्रैक्टिस सेट (${courseSets.size})",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Color(0xFF1A237E)
            )

            if (isAdminMode) {
              TextButton(onClick = { viewModel.navigateTo(ActiveScreen.SetEditor(targetCourseId = course.id)) }) {
                Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("नया सेट जोड़ें", fontSize = 12.sp)
              }
            }
          }
        }

        if (courseSets.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
              Box(modifier = Modifier.padding(24.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                  text = if (isAdminMode) "इस कोर्स में अभी कोई टेस्ट नहीं जुड़ा है। '+ नया सेट जोड़ें' पर क्लिक करके 50-60 प्रश्न बल्क में पेस्ट करें।" else "इस कोर्स में अभी कोई टेस्ट उपलब्ध नहीं है।",
                  color = Color.Gray,
                  fontSize = 13.sp,
                  textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
              }
            }
          }
        } else {
          items(courseSets) { set ->
            CourseTestItemCard(
              set = set,
              isUnlocked = course.isEnrolled || !set.isPaid,
              onStart = { viewModel.openPreTestInstructions(set.id) },
              onRevision = { viewModel.startQuickRevision(set.id) }
            )
          }
        }

        item {
          Spacer(modifier = Modifier.height(30.dp))
        }
      }
    }
  }
}

@Composable
private fun CourseFeatureChip(text: String) {
  Surface(
    shape = RoundedCornerShape(6.dp),
    color = Color.White.copy(alpha = 0.2f)
  ) {
    Text(
      text = text,
      color = Color.White,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}

@Composable
private fun CourseTestItemCard(
  set: PracticeSetEntity,
  isUnlocked: Boolean,
  onStart: () -> Unit,
  onRevision: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { if (isUnlocked) onStart() },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(if (isUnlocked) Color(0xFFE8F5E9) else Color(0xFFEEEEEE)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isUnlocked) Icons.Default.Quiz else Icons.Default.Lock,
          contentDescription = null,
          tint = if (isUnlocked) Color(0xFF2E7D32) else Color.Gray,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = set.title,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = Color(0xFF212121),
          maxLines = 2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${set.totalQuestionsCount} प्रश्न • ${set.durationMinutes} मिनट • +${set.marksPerQuestion} / -${set.negativeMarking}",
          fontSize = 11.sp,
          color = Color.Gray
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      if (isUnlocked) {
        Button(
          onClick = onStart,
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text("टेस्ट दें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      } else {
        Badge(containerColor = Color(0xFFFFEBEE)) {
          Text("Locked", color = Color(0xFFD32F2F), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
        }
      }
    }
  }
}
