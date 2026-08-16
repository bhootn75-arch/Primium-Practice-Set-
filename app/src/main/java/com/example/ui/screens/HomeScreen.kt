package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
fun HomeScreen(viewModel: PracticeSetViewModel) {
  val allCourses by viewModel.allCourses.collectAsState()
  val enrolledCourses by viewModel.enrolledCourses.collectAsState()
  val allSets by viewModel.allSets.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()
  val currentTab by viewModel.currentHomeTab.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val isAdminMode by viewModel.isAdminMode.collectAsState()

  var showProfileSheet by remember { mutableStateOf(false) }
  var showAddChoiceDialog by remember { mutableStateOf(false) }
  var setDeleteConfirmId by remember { mutableStateOf<Long?>(null) }

  val categories = listOf("All", "JSSC", "Jharkhand Police", "SSC GD", "Santali Special", "Railway")

  // Filtered lists
  val filteredCourses = remember(allCourses, selectedCategory, searchQuery) {
    allCourses.filter { course ->
      val matchesCat = selectedCategory == "All" || course.examCategory.contains(selectedCategory, ignoreCase = true)
      val matchesSearch = searchQuery.isBlank() || course.title.contains(searchQuery, ignoreCase = true)
      matchesCat && matchesSearch
    }
  }

  val filteredSets = remember(allSets, selectedCategory, searchQuery) {
    allSets.filter { set ->
      val matchesCat = selectedCategory == "All" ||
          set.subject.contains(selectedCategory, ignoreCase = true) ||
          (set.courseTitle?.contains(selectedCategory, ignoreCase = true) == true)
      val matchesSearch = searchQuery.isBlank() ||
          set.title.contains(searchQuery, ignoreCase = true) ||
          set.subject.contains(searchQuery, ignoreCase = true)
      matchesCat && matchesSearch
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { showProfileSheet = true },
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color(0xFF0D47A1), modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (isAdminMode) "Jagu Sir (Admin)" else "नमस्ते, ${userProfile.name.split(" ").firstOrNull() ?: "विद्यार्थी"}",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                if (isAdminMode) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFFD54F)) {
                    Text("👑 ADMIN", color = Color(0xFF3E2723), fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                  }
                }
              }
              Text(
                text = if (isAdminMode) "कंटेंट व टेस्ट मैनेजर" else "लक्ष्य: ${userProfile.targetExam}",
                fontSize = 11.sp,
                color = Color(0xFFFFD54F),
                fontWeight = FontWeight.Medium
              )
            }
          }
        },
        actions = {
          if (isAdminMode) {
            IconButton(onClick = { viewModel.exitAdminMode() }) {
              Icon(Icons.Default.Logout, contentDescription = "Exit Admin", tint = Color(0xFFFFCC80))
            }
          } else {
            IconButton(onClick = { viewModel.openAdminPinDialog() }) {
              Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin Portal", tint = Color.White.copy(alpha = 0.85f))
            }
          }
          IconButton(onClick = { viewModel.openRatingDialog() }) {
            Icon(Icons.Default.Star, contentDescription = "Rate", tint = Color(0xFFFFD54F))
          }
          IconButton(onClick = { viewModel.navigateTo(ActiveScreen.Leaderboard()) }) {
            Icon(Icons.Default.Leaderboard, contentDescription = "AIR Rank", tint = Color.White)
          }
          IconButton(onClick = { viewModel.navigateTo(ActiveScreen.History) }) {
            Icon(Icons.Default.History, contentDescription = "History", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D47A1))
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
      ) {
        NavigationBarItem(
          selected = currentTab == 0,
          onClick = { viewModel.setHomeTab(0) },
          icon = { Icon(Icons.Default.MenuBook, contentDescription = "Courses") },
          label = { Text("कोर्स (Courses)", fontSize = 11.sp, fontWeight = if (currentTab == 0) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF0D47A1), indicatorColor = Color(0xFFE3F2FD))
        )
        NavigationBarItem(
          selected = currentTab == 1,
          onClick = { viewModel.setHomeTab(1) },
          icon = { Icon(Icons.Default.Quiz, contentDescription = "Test Series") },
          label = { Text("टेस्ट सीरीज", fontSize = 11.sp, fontWeight = if (currentTab == 1) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF0D47A1), indicatorColor = Color(0xFFE3F2FD))
        )
        NavigationBarItem(
          selected = currentTab == 2,
          onClick = { viewModel.setHomeTab(2) },
          icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Enrolled") },
          label = { Text("मेरे बैच (${enrolledCourses.size})", fontSize = 11.sp, fontWeight = if (currentTab == 2) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF0D47A1), indicatorColor = Color(0xFFE3F2FD))
        )
        NavigationBarItem(
          selected = currentTab == 3,
          onClick = { viewModel.navigateTo(ActiveScreen.Leaderboard()) },
          icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "AIR Rank") },
          label = { Text("ऑल इंडिया रैंक", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF0D47A1), indicatorColor = Color(0xFFE3F2FD))
        )
      }
    },
    floatingActionButton = {
      // FAB is strictly only available for Admin
      if (isAdminMode) {
        FloatingActionButton(
          onClick = { showAddChoiceDialog = true },
          containerColor = Color(0xFFE65100),
          contentColor = Color.White,
          modifier = Modifier.testTag("home_add_fab")
        ) {
          Icon(Icons.Default.Add, contentDescription = "Admin Add")
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF7F9FC))
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Admin Active Notice Banner
      if (isAdminMode) {
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB74D))
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(24.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text("👑 एडमिन मोड सक्रिय (Admin Panel)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFE65100))
                Text("आप नए कोर्स, 50-60 Qs के टेस्ट जोड़ व संपादित कर सकते हैं।", fontSize = 11.sp, color = Color(0xFF5D4037))
              }
              Button(
                onClick = { viewModel.exitAdminMode() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("एग्जिट", fontSize = 11.sp)
              }
            }
          }
        }
      }

      // 1. Promotional Hero Banner (RWA Style)
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  listOf(Color(0xFF0D47A1), Color(0xFF1565C0))
                )
              )
              .padding(16.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFFD54F)) {
                  Text(
                    text = "🎯 LIVE TEST SERIES 2026",
                    color = Color(0xFF3E2723),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                Text(
                  text = "RWA Exam Pattern",
                  color = Color.White.copy(alpha = 0.9f),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "JSSC कक्षपाल, झारखंड पुलिस व SSC GD स्पेशल",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )

              Text(
                text = "सभी प्रश्नों के चारों ऑप्शन, विस्तृत व्याख्या व ट्रिक समाधान सहित",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
              )

              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                  onClick = {
                    val scienceSet = allSets.find { it.title.contains("रासायनिक", ignoreCase = true) } ?: allSets.firstOrNull()
                    if (scienceSet != null) viewModel.openPreTestInstructions(scienceSet.id)
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("फ्री मॉक टेस्ट दें", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                  onClick = { viewModel.setHomeTab(0) },
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                  Text("सभी कोर्स देखें", fontSize = 12.sp)
                }
              }
            }
          }
        }
      }

      // 2. Rating Banner (Prompt)
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.openRatingDialog() },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("ऐप को 5-Star रेटिंग दें!", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFE65100))
              Text("शानदार टेस्ट सीरीज के लिए अपना रिव्यू सबमिट करें", fontSize = 11.sp, color = Color(0xFF5D4037))
            }
            Text("★ 5.0", fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color(0xFFE65100))
          }
        }
      }

      // 3. Search & Exam Category Filters
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("कोर्स, विषय या टेस्ट खोजें (उदा. कक्षपाल, विज्ञान)...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                  Icon(Icons.Default.Close, contentDescription = "Clear")
                }
              }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
              FilterChip(
                selected = selectedCategory == cat,
                onClick = { viewModel.setSelectedCategory(cat) },
                label = { Text(cat, fontSize = 12.sp, fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = Color(0xFF0D47A1),
                  selectedLabelColor = Color.White
                )
              )
            }
          }
        }
      }

      // 4. Content depending on Tab
      when (currentTab) {
        0 -> {
          // --- TAB 0: ALL COURSES STORE ---
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "उपलब्ध ऑनलाइन कोर्स (${filteredCourses.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1A237E)
              )

              if (isAdminMode) {
                TextButton(onClick = { viewModel.navigateTo(ActiveScreen.CourseEditor()) }) {
                  Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("+ नया कोर्स जोड़ें", fontSize = 12.sp)
                }
              }
            }
          }

          if (filteredCourses.isEmpty()) {
            item {
              EmptyStateCard(message = if (isAdminMode) "कोई कोर्स नहीं मिला। ऊपर दिए '+ नया कोर्स जोड़ें' से असीमित कोर्स बनाएं।" else "वर्तमान में कोई कोर्स उपलब्ध नहीं है।")
            }
          } else {
            items(filteredCourses) { course ->
              CourseStoreCard(
                course = course,
                isAdmin = isAdminMode,
                onViewDetails = { viewModel.navigateTo(ActiveScreen.CourseDetail(course.id)) },
                onEdit = { viewModel.navigateTo(ActiveScreen.CourseEditor(course.id)) },
                onEnroll = { viewModel.enrollInCourse(course.id, course.title) }
              )
            }
          }
        }

        1 -> {
          // --- TAB 1: ALL PRACTICE SETS & MOCK TESTS ---
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "सभी ऑनलाइन प्रैक्टिस सेट (${filteredSets.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1A237E)
              )

              if (isAdminMode) {
                TextButton(onClick = { viewModel.navigateTo(ActiveScreen.SetEditor()) }) {
                  Icon(Icons.Default.PlaylistAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("+ 50-60 Qs पेस्ट करें", fontSize = 12.sp)
                }
              }
            }
          }

          if (filteredSets.isEmpty()) {
            item {
              EmptyStateCard(message = if (isAdminMode) "कोई प्रैक्टिस सेट नहीं मिला। '+ 50-60 Qs पेस्ट करें' पर क्लिक करें।" else "वर्तमान में कोई प्रैक्टिस सेट उपलब्ध नहीं है।")
            }
          } else {
            items(filteredSets) { set ->
              PracticeSetHomeCard(
                set = set,
                isAdmin = isAdminMode,
                onStart = { viewModel.openPreTestInstructions(set.id) },
                onRevision = { viewModel.startQuickRevision(set.id) },
                onEdit = { viewModel.navigateTo(ActiveScreen.SetEditor(set.id)) },
                onDelete = { setDeleteConfirmId = set.id },
                onToggleFavorite = { viewModel.toggleFavorite(set.id) }
              )
            }
          }
        }

        2 -> {
          // --- TAB 2: MY ENROLLED COURSES ---
          item {
            Text(
              text = "मेरे नामांकित कोर्स व टेस्ट सीरीज (${enrolledCourses.size})",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = Color(0xFF1A237E)
            )
          }

          if (enrolledCourses.isEmpty()) {
            item {
              EmptyStateCard(message = "आपने अभी तक कोई कोर्स नहीं खरीदा है। 'कोर्स' टैब में जाकर किसी भी कोर्स पर 'खरीदें' पर क्लिक करें।")
            }
          } else {
            items(enrolledCourses) { course ->
              CourseStoreCard(
                course = course,
                isAdmin = isAdminMode,
                onViewDetails = { viewModel.navigateTo(ActiveScreen.CourseDetail(course.id)) },
                onEdit = { viewModel.navigateTo(ActiveScreen.CourseEditor(course.id)) },
                onEnroll = {}
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(50.dp))
      }
    }
  }

  // --- Delete Confirmation Dialog (Admin Only) ---
  setDeleteConfirmId?.let { delId ->
    AlertDialog(
      onDismissRequest = { setDeleteConfirmId = null },
      title = { Text("प्रैक्टिस सेट हटाएं?") },
      text = { Text("क्या आप वाकई इस प्रैक्टिस सेट और इसके सभी प्रश्नों को हटाना चाहते हैं?") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteSet(delId)
            setDeleteConfirmId = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("हटाएं")
        }
      },
      dismissButton = {
        TextButton(onClick = { setDeleteConfirmId = null }) {
          Text("रद्द करें")
        }
      }
    )
  }

  // --- Add Choice Dialog ---
  if (showAddChoiceDialog && isAdminMode) {
    AlertDialog(
      onDismissRequest = { showAddChoiceDialog = false },
      title = { Text("नया क्या जोड़ना चाहते हैं?", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0D47A1)) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(
            onClick = {
              showAddChoiceDialog = false
              viewModel.navigateTo(ActiveScreen.SetEditor())
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
          ) {
            Icon(Icons.Default.PlaylistAdd, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("प्रैक्टिस सेट (50-60 Qs बल्क पेस्ट)")
          }

          Button(
            onClick = {
              showAddChoiceDialog = false
              viewModel.navigateTo(ActiveScreen.CourseEditor())
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
          ) {
            Icon(Icons.Default.School, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("नया कोर्स बनाएं (Unlimited Courses)")
          }
        }
      },
      confirmButton = {},
      dismissButton = {
        TextButton(onClick = { showAddChoiceDialog = false }) {
          Text("रद्द करें")
        }
      }
    )
  }

  // --- User Profile Bottom Sheet ---
  if (showProfileSheet) {
    ModalBottomSheet(onDismissRequest = { showProfileSheet = false }) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color(0xFF0D47A1)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = if (isAdminMode) "Jagu Sir (Admin)" else userProfile.name,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = Color(0xFF1A237E)
        )
        Text(userProfile.phone, fontSize = 13.sp, color = Color.Gray)
        Text(
          text = if (isAdminMode) "👑 भूमिका: सुपर एडमिन / शिक्षक" else "Student ID: ${userProfile.studentId}",
          fontSize = 12.sp,
          color = Color(0xFFE65100),
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FA))
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("लक्ष्य परीक्षा: ${userProfile.targetExam}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text("सक्रिय नामांकित बैच: ${enrolledCourses.size} कोर्स", fontSize = 13.sp, color = Color(0xFF2E7D32))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Admin Mode Toggle Button in Profile
        if (!isAdminMode) {
          OutlinedButton(
            onClick = {
              showProfileSheet = false
              viewModel.openAdminPinDialog()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D47A1))
          ) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("🔒 एडमिन लॉगिन (Admin Portal / पिन दर्ज करें)")
          }
        } else {
          Button(
            onClick = {
              showProfileSheet = false
              viewModel.exitAdminMode()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
          ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("👑 एडमिन से बाहर निकलें (Switch to Student)")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedButton(
            onClick = {
              showProfileSheet = false
              viewModel.openRatingDialog()
            },
            modifier = Modifier.weight(1f)
          ) {
            Text("रेटिंग दें")
          }

          Button(
            onClick = {
              showProfileSheet = false
              viewModel.logoutUser()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
          ) {
            Text("लॉगआउट")
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@Composable
private fun CourseStoreCard(
  course: CourseEntity,
  isAdmin: Boolean = false,
  onViewDetails: () -> Unit,
  onEdit: () -> Unit = {},
  onEnroll: () -> Unit
) {
  Card(
    modifier = Modifier
      .clickable { onViewDetails() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFF3E0)) {
          Text(
            text = course.badge.uppercase(),
            color = Color(0xFFE65100),
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = course.examCategory,
            color = Color(0xFF1565C0),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
          if (isAdmin) {
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
              Icon(Icons.Default.Edit, contentDescription = "Edit Course", tint = Color(0xFF0D47A1), modifier = Modifier.size(16.dp))
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = course.title,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color(0xFF212121),
        lineHeight = 22.sp
      )

      Text(
        text = course.description,
        fontSize = 12.sp,
        color = Color.Gray,
        maxLines = 2,
        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
      )

      HorizontalDivider(color = Color(0xFFEEEEEE))

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "₹${course.price}",
              fontWeight = FontWeight.Black,
              fontSize = 18.sp,
              color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "₹${course.originalPrice}",
              fontSize = 12.sp,
              color = Color.Gray,
              textDecoration = TextDecoration.LineThrough
            )
          }
          Text(text = "वैधता: ${course.validityMonths} माह", fontSize = 10.sp, color = Color.DarkGray)
        }

        if (course.isEnrolled) {
          Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE8F5E9)) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("नामांकित (Enrolled)", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }
        } else {
          Button(
            onClick = onEnroll,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Text("कोर्स खरीदें", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
private fun PracticeSetHomeCard(
  set: PracticeSetEntity,
  isAdmin: Boolean = false,
  onStart: () -> Unit,
  onRevision: () -> Unit,
  onEdit: () -> Unit = {},
  onDelete: () -> Unit = {},
  onToggleFavorite: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onStart() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE3F2FD)) {
          Text(
            text = set.subject,
            color = Color(0xFF0D47A1),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (isAdmin) {
            IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
              Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF0D47A1), modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
              Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
            }
          }
          IconButton(onClick = onToggleFavorite, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = if (set.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = null,
              tint = if (set.isFavorite) Color(0xFFE65100) else Color.Gray,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = set.title,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = Color(0xFF212121),
        lineHeight = 20.sp
      )

      if (set.courseTitle != null) {
        Text(
          text = "बैच: ${set.courseTitle}",
          fontSize = 11.sp,
          color = Color(0xFFE65100),
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(top = 2.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${set.totalQuestionsCount} प्रश्न • ${set.durationMinutes} मिनट • +${set.marksPerQuestion}/-${set.negativeMarking}",
          fontSize = 11.sp,
          color = Color.Gray
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          OutlinedButton(
            onClick = onRevision,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(2.dp))
            Text("रिवीजन", fontSize = 11.sp)
          }

          Button(
            onClick = onStart,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(2.dp))
            Text("टेस्ट दें", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
private fun EmptyStateCard(message: String) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Box(
      modifier = Modifier.padding(32.dp).fillMaxWidth(),
      contentAlignment = Alignment.Center
    ) {
      Text(message, color = Color.Gray, fontSize = 13.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
  }
}
