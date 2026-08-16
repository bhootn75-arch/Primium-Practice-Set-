package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.viewmodel.PracticeSetViewModel

@Composable
fun AdminPinDialog(viewModel: PracticeSetViewModel) {
  val showDialog by viewModel.showAdminPinDialog.collectAsState()
  val isAdminMode by viewModel.isAdminMode.collectAsState()

  var pinInput by remember { mutableStateOf("") }
  var isPinVisible by remember { mutableStateOf(false) }
  var isChangePinMode by remember { mutableStateOf(false) }
  var oldPinInput by remember { mutableStateOf("") }
  var newPinInput by remember { mutableStateOf("") }
  var confirmNewPinInput by remember { mutableStateOf("") }

  if (showDialog) {
    Dialog(onDismissRequest = {
      viewModel.dismissAdminPinDialog()
      pinInput = ""
      isChangePinMode = false
    }) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
      ) {
        Column(
          modifier = Modifier.padding(22.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Color(0xFFE8EAF6)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isChangePinMode) Icons.Default.Key else Icons.Default.Lock,
              contentDescription = null,
              tint = Color(0xFF0D47A1),
              modifier = Modifier.size(28.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = if (isChangePinMode) "एडमिन पिन बदलें (Change PIN)" else "🔒 एडमिन कंट्रोल पैनल (Admin Access)",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            textAlign = TextAlign.Center
          )

          Text(
            text = if (isChangePinMode) {
              "वर्तमान सुरक्षा पिन और नया 4-अंकीय पिन दर्ज करें।"
            } else {
              "यह अनुभाग केवल अधिकृत एडमिन (Jagu Sir / शिक्षक) के लिए है। नए कोर्स, 50-60 प्रश्नों के टेस्ट जोड़ने व मैनेज करने हेतु एडमिन पिन दर्ज करें।"
            },
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp),
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          if (!isChangePinMode) {
            // Standard PIN Verification
            OutlinedTextField(
              value = pinInput,
              onValueChange = { if (it.length <= 8) pinInput = it },
              label = { Text("एडमिन पिन (Admin PIN)") },
              placeholder = { Text("उदा. 7890") },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
              visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = { isPinVisible = !isPinVisible }) {
                  Icon(
                    imageVector = if (isPinVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle PIN Visibility"
                  )
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_pin_input"),
              shape = RoundedCornerShape(12.dp)
            )

            Text(
              text = "ℹ️ डिफ़ॉल्ट पिन: 7890 (डिफ़ॉल्ट मास्टर की)",
              fontSize = 11.sp,
              color = Color(0xFFE65100),
              fontWeight = FontWeight.Medium,
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 4.dp)
            )
          } else {
            // Change PIN Form
            OutlinedTextField(
              value = oldPinInput,
              onValueChange = { oldPinInput = it },
              label = { Text("वर्तमान पिन (Current PIN)") },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
              visualTransformation = PasswordVisualTransformation(),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = newPinInput,
              onValueChange = { newPinInput = it },
              label = { Text("नया पिन (New PIN - 4+ Digits)") },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
              visualTransformation = PasswordVisualTransformation(),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = confirmNewPinInput,
              onValueChange = { confirmNewPinInput = it },
              label = { Text("नया पिन दोहराएं (Confirm New PIN)") },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
              visualTransformation = PasswordVisualTransformation(),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Action Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = {
                viewModel.dismissAdminPinDialog()
                pinInput = ""
                isChangePinMode = false
              },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("रद्द करें")
            }

            Button(
              onClick = {
                if (!isChangePinMode) {
                  if (viewModel.verifyAndLoginAdmin(pinInput)) {
                    pinInput = ""
                  }
                } else {
                  if (newPinInput != confirmNewPinInput) {
                    viewModel.showMessage("❌ नए पिन मेल नहीं खा रहे हैं!")
                  } else {
                    if (viewModel.changeAdminPin(oldPinInput, newPinInput)) {
                      isChangePinMode = false
                      oldPinInput = ""
                      newPinInput = ""
                      confirmNewPinInput = ""
                    }
                  }
                }
              },
              modifier = Modifier
                .weight(1f)
                .testTag("admin_unlock_btn"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
              Text(if (isChangePinMode) "पिन सेव करें" else "अनलॉक करें", fontWeight = FontWeight.Bold)
            }
          }

          if (isAdminMode && !isChangePinMode) {
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = { isChangePinMode = true }) {
              Text("🔑 एडमिन पिन बदलना चाहते हैं? यहां क्लिक करें", fontSize = 11.sp, color = Color(0xFF1565C0))
            }
          }
        }
      }
    }
  }
}
