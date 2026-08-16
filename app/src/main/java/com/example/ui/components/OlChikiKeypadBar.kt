package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OlChikiKeypadBar(
  onInsertChar: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val olChikiLetters = listOf(
    // Vowels & Basic 30
    "ᱚ", "ᱛ", "ᱜ", "ᱝ", "ᱞ",
    "ᱟ", "ᱠ", "ᱡ", "ᱢ", "ᱣ",
    "ᱤ", "ᱥ", "ᱦ", "ᱧ", "ᱨ",
    "ᱩ", "ᱪ", "ᱫ", "ᱬ", "ᱭ",
    "ᱮ", "ᱯ", "ᱰ", "ᱱ", "ᱲ",
    "ᱳ", "ᱴ", "ᱵ", "ᱶ", "ᱷ",
    // Modifiers & Punctuation
    "ᱸ", "ᱹ", "ᱺ", "ᱻ", "ᱼ", "ᱽ", "᱾", "᱿",
    // Ol Chiki Digits
    "᱐", "᱑", "᱒", "᱓", "᱔", "᱕", "᱖", "᱗", "᱘", "᱙"
  )

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surfaceVariant,
    shape = RoundedCornerShape(12.dp),
    tonalElevation = 2.dp
  ) {
    Column(modifier = Modifier.padding(8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ᱚᱞ ᱪᱤᱠᱤ ᱠᱤᱵᱳᱨᱰ (Ol Chiki Keypad Toolbar)",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = "Tap to Insert",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        olChikiLetters.forEach { letter ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surface)
              .clickable { onInsertChar(letter) }
              .padding(horizontal = 12.dp, vertical = 8.dp)
              .testTag("key_$letter"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = letter,
              fontSize = 18.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }
  }
}
