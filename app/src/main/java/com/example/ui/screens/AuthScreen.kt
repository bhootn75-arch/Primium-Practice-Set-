package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.PracticeSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(viewModel: PracticeSetViewModel) {
  var phone by remember { mutableStateOf("") }
  var name by remember { mutableStateOf("") }
  var otp by remember { mutableStateOf("") }
  var isOtpSent by remember { mutableStateOf(false) }
  var selectedTargetExam by remember { mutableStateOf("JSSC कक्षपाल (Warder)") }
  var isExamExpanded by remember { mutableStateOf(false) }
  var showGooglePicker by remember { mutableStateOf(false) }
  var customGmailInput by remember { mutableStateOf("") }

  val targetExams = listOf(
    "JSSC कक्षपाल (Warder)",
    "JSSC झारखंड पुलिस (Jharkhand Police)",
    "SSC GD 2026",
    "JSSC CGL (संथाली पेपर-2)",
    "Railway RPF & Group D",
    "All Jharkhand Govt Exams"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF0D47A1), // Deep Royal Navy
            Color(0xFF1565C0),
            Color(0xFFF5F9FF)
          ),
          startY = 0f,
          endY = 700f
        )
      )
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Skip Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color.White.copy(alpha = 0.2f)
        ) {
          Text(
            text = "🌟 Live Portal",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }

        TextButton(
          onClick = { viewModel.skipAuthToHome() }
        ) {
          Text("डायरेक्ट ऐप देखें (Skip) ➔", color = Color(0xFFFFD54F), fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Logo / Hero Badge
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(Color.White),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.School,
          contentDescription = "App Logo",
          tint = Color(0xFF0D47A1),
          modifier = Modifier.size(42.dp)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "SANTALI SMART STUDY",
        fontSize = 22.sp,
        fontWeight = FontWeight.Black,
        color = Color.White,
        letterSpacing = 1.sp
      )
      Text(
        text = "ऑनलाइन टेस्ट सीरीज व कोर्स पोर्टल (RWA Pattern)",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White.copy(alpha = 0.9f)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Card Form
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = if (!isOtpSent) "विद्यार्थी लॉगिन / साइन-अप" else "OTP सत्यापन दर्ज करें",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
          )
          Text(
            text = if (!isOtpSent)
              "Google से तुरंत 1-क्लिक लॉगिन करें या मोबाइल नंबर दर्ज करें"
            else
              "+91 $phone पर भेजा गया 4-अंकीय OTP दर्ज करें (Demo: 1234)",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
          )

          // 1. PRIMARY FAST GOOGLE SIGN IN BUTTON
          Button(
            onClick = {
              showGooglePicker = true
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA4335))
          ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Google से तुरंत लॉगिन करें (Gmail)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Divider
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
            Text("  या मोबाइल नंबर से  ", fontSize = 11.sp, color = Color.Gray)
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
          }

          Spacer(modifier = Modifier.height(14.dp))

          if (!isOtpSent) {
            // Full Name Input
            OutlinedTextField(
              value = name,
              onValueChange = { name = it },
              label = { Text("आपका पूरा नाम (Full Name)") },
              leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1565C0))
              },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_name_input"),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mobile Number Input
            OutlinedTextField(
              value = phone,
              onValueChange = { if (it.length <= 10) phone = it },
              label = { Text("मोबाइल नंबर (10 Digits)") },
              leadingIcon = {
                Text(
                  text = "+91 ",
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1565C0),
                  modifier = Modifier.padding(start = 12.dp)
                )
              },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_phone_input"),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Target Exam Selector
            ExposedDropdownMenuBox(
              expanded = isExamExpanded,
              onExpandedChange = { isExamExpanded = !isExamExpanded }
            ) {
              OutlinedTextField(
                value = selectedTargetExam,
                onValueChange = {},
                readOnly = true,
                label = { Text("लक्ष्य परीक्षा (Target Exam)") },
                leadingIcon = {
                  Icon(Icons.Default.Stars, contentDescription = null, tint = Color(0xFFE65100))
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExamExpanded) },
                modifier = Modifier
                  .fillMaxWidth()
                  .menuAnchor(),
                shape = RoundedCornerShape(12.dp)
              )
              ExposedDropdownMenu(
                expanded = isExamExpanded,
                onDismissRequest = { isExamExpanded = false }
              ) {
                targetExams.forEach { exam ->
                  DropdownMenuItem(
                    text = { Text(exam, fontSize = 14.sp) },
                    onClick = {
                      selectedTargetExam = exam
                      isExamExpanded = false
                    }
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = {
                if (phone.length >= 10) {
                  isOtpSent = true
                } else {
                  // If phone is blank, log in directly with guest info
                  viewModel.loginUser("9876543210", name.ifBlank { "विद्यार्थी" }, selectedTargetExam)
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("auth_send_otp_btn"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
              Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("OTP प्राप्त करें / लॉगिन करें", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
          } else {
            // OTP Field
            OutlinedTextField(
              value = otp,
              onValueChange = { if (it.length <= 4) otp = it },
              label = { Text("Enter 4-digit OTP (उदा. 1234)") },
              leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF1565C0))
              },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_otp_input"),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = {
                viewModel.loginUser(phone, name, selectedTargetExam)
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("auth_verify_btn"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("सत्यापित करें व प्रवेश करें", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { isOtpSent = false }) {
              Text("नंबर बदलें (Edit Phone Number)", color = Color(0xFF1565C0))
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedButton(
            onClick = {
              viewModel.skipAuthToHome()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF0D47A1))
            Spacer(modifier = Modifier.width(8.dp))
            Text("बिना लॉगिन डायरेक्ट ऐप देखें (Guest Mode)", color = Color(0xFF0D47A1), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "Rojgar & Education First Architecture • Santali Smart Study",
        fontSize = 11.sp,
        color = Color(0xFF757575),
        textAlign = TextAlign.Center
      )
    }
  }

  // Google Account Chooser Bottom Sheet
  if (showGooglePicker) {
    ModalBottomSheet(
      onDismissRequest = { showGooglePicker = false }
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color(0xFFEA4335), modifier = Modifier.size(44.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Google खाता चुनें (Choose Account)",
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = Color(0xFF1A237E)
        )
        Text(
          text = "Santali Smart Study में तुरंत प्रवेश करने के लिए खाता चुनें",
          fontSize = 12.sp,
          color = Color.Gray,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(bottom = 16.dp)
        )

        // Account 1: User's actual email (bhootn75@gmail.com)
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              showGooglePicker = false
              viewModel.loginWithGoogle("bhootn75@gmail.com", "विद्यार्थी (JSSC Aspirant)", selectedTargetExam)
            },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF0D47A1)),
              contentAlignment = Alignment.Center
            ) {
              Text("B", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("विद्यार्थी (झारखंड अभ्यर्थी)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text("bhootn75@gmail.com", fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Account 2: Jagu Sir (Admin)
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              showGooglePicker = false
              viewModel.loginWithGoogle("jagusir.santali@gmail.com", "Jagu Sir (Admin)", selectedTargetExam)
            },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE65100)),
              contentAlignment = Alignment.Center
            ) {
              Text("J", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Jagu Sir (शिक्षक / एडमिन)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFE65100))
              Text("jagusir.santali@gmail.com", fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Custom Gmail Input Option
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = customGmailInput,
            onValueChange = { customGmailInput = it },
            placeholder = { Text("अन्य Gmail ID दर्ज करें...", fontSize = 12.sp) },
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              val email = if (customGmailInput.contains("@")) customGmailInput else "${customGmailInput.ifBlank { "student" }}@gmail.com"
              showGooglePicker = false
              viewModel.loginWithGoogle(email, email.substringBefore("@"), selectedTargetExam)
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
          ) {
            Text("लॉगिन")
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
