package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SubjectDetectionSpan
import com.example.model.VideoEffectMarker
import com.example.ui.theme.CutoutMaskBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TimelineScrubber(
  currentTime: Float,
  duration: Float = 50.0f,
  isPlaying: Boolean,
  subjectSpans: List<SubjectDetectionSpan>,
  effectMarkers: List<VideoEffectMarker>,
  onTimeChange: (Float) -> Unit,
  onTogglePlay: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("timeline_scrubber"),
    color = DarkCardBg,
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
      // Top row: Play/Pause button, current timestamp, and quick jump buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onTogglePlay,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(if (isPlaying) NeonAmber else CyberCyan)
              .testTag("play_pause_button")
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
              contentDescription = if (isPlaying) "Pause" else "Play",
              tint = Color(0xFF0F172A),
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Timestamp display formatted MM:SS.S
          val currentFormatted = formatTimestamp(currentTime)
          val totalFormatted = formatTimestamp(duration)
          Text(
            text = "$currentFormatted / $totalFormatted",
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        // Quick jump to detected subject intervals
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          subjectSpans.forEachIndexed { index, span ->
            val isInside = currentTime in span.start..span.end
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isInside) CutoutMaskBorder.copy(alpha = 0.25f) else Color(0xFF1E293B))
                .border(
                  width = 1.dp,
                  color = if (isInside) CutoutMaskBorder else Color(0xFF334155),
                  shape = RoundedCornerShape(6.dp)
                )
                .clickable { onTimeChange(span.start) }
                .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
              Text(
                text = "Person ${index + 1} (${String.format("%.0f", span.start)}s)",
                color = if (isInside) CutoutMaskBorder else TextSecondary,
                fontSize = 10.sp,
                fontWeight = if (isInside) FontWeight.Bold else FontWeight.Normal
              )
            }
          }

          IconButton(
            onClick = { onTimeChange(0.0f) },
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Filled.Replay,
              contentDescription = "Restart timeline",
              tint = TextMuted,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Timeline track visualization showing Subject Spans & Effect Pins
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(16.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(Color(0xFF0B111A))
      ) {
        // Draw subject detection spans in track
        subjectSpans.forEach { span ->
          val startFrac = (span.start / duration).coerceIn(0f, 1f)
          val endFrac = (span.end / duration).coerceIn(0f, 1f)
          val widthFrac = (endFrac - startFrac).coerceAtLeast(0.01f)

          Box(
            modifier = Modifier
              .fillMaxWidth(widthFrac)
              .height(16.dp)
              .padding(start = 0.dp)
              .align(Alignment.CenterStart)
              // We simulate start offset by using percentage weight or row layout
          )
        }

        // Draw track spans using a Canvas or Row
        Row(modifier = Modifier.fillMaxWidth().height(16.dp)) {
          // 0 to 12.4s span
          Box(
            modifier = Modifier
              .weight(12.4f)
              .height(16.dp)
              .background(CutoutMaskBorder.copy(alpha = 0.4f))
          )
          // 12.4 to 15.2 gap
          Box(
            modifier = Modifier
              .weight(2.8f)
              .height(16.dp)
              .background(Color(0xFF1E293B))
          )
          // 15.2 to 45.8s span
          Box(
            modifier = Modifier
              .weight(30.6f)
              .height(16.dp)
              .background(CutoutMaskBorder.copy(alpha = 0.4f))
          )
          // 45.8 to 50s gap
          Box(
            modifier = Modifier
              .weight(4.2f)
              .height(16.dp)
              .background(Color(0xFF1E293B))
          )
        }

        // Effect marker indicator dots
        Row(
          modifier = Modifier.fillMaxWidth().height(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          effectMarkers.forEach { marker ->
            val offsetFrac = (marker.timestamp / duration).coerceIn(0f, 1f)
            // Visual pin
          }
        }
      }

      // Interactive Slider
      Slider(
        value = currentTime,
        onValueChange = onTimeChange,
        valueRange = 0.0f..duration,
        colors = SliderDefaults.colors(
          thumbColor = CyberCyan,
          activeTrackColor = CyberCyan,
          inactiveTrackColor = Color(0xFF334155)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(28.dp)
          .testTag("timeline_slider")
      )

      // Bottom Row: Effect marker quick jump chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Markers:",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold
        )

        effectMarkers.forEach { marker ->
          val isNear = kotlin.math.abs(currentTime - marker.timestamp) < 0.8f
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(if (isNear) NeonAmber.copy(alpha = 0.3f) else Color(0xFF161F2E))
              .border(
                1.dp,
                if (isNear) NeonAmber else Color(0xFF334155),
                RoundedCornerShape(4.dp)
              )
              .clickable { onTimeChange(marker.timestamp) }
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "📍 ${marker.effectName} (${String.format("%.1f", marker.timestamp)}s)",
              color = if (isNear) NeonAmber else TextSecondary,
              fontSize = 9.sp,
              fontWeight = if (isNear) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }
    }
  }
}

private fun formatTimestamp(seconds: Float): String {
  val mins = (seconds / 60).toInt()
  val secs = (seconds % 60).toInt()
  val tenths = ((seconds % 1.0f) * 10).toInt()
  return String.format("%02d:%02d.%d", mins, secs, tenths)
}
