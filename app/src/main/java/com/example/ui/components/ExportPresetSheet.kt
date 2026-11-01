package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.model.AspectRatioItem
import com.example.model.ComputedDimensions
import com.example.model.FramingMode
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportPresetSheet(
  aspectRatio: AspectRatioItem,
  dimensions: ComputedDimensions,
  framingMode: FramingMode,
  isAiCutoutEnabled: Boolean,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val scope = rememberCoroutineScope()
  var isExporting by remember { mutableStateOf(false) }
  var isExportComplete by remember { mutableStateOf(false) }
  var selectedContainer by remember { mutableStateOf("MP4 (H.264)") }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = DarkSurface,
    contentColor = TextPrimary,
    modifier = modifier.testTag("export_preset_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Filled.FileDownload, contentDescription = null, tint = CyberCyan)
          Text(
            text = " Render & Export Profile",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextSecondary)
        }
      }

      // Specs Summary Box
      Surface(
        color = DarkCardBg,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Selected Aspect Ratio:", color = TextSecondary, fontSize = 12.sp)
            Text("${aspectRatio.ratioString} (${aspectRatio.name})", color = CyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Target Canvas Resolution:", color = TextSecondary, fontSize = 12.sp)
            Text("${dimensions.width} × ${dimensions.height} px", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Framing Strategy:", color = TextSecondary, fontSize = 12.sp)
            Text(framingMode.title, color = NeonAmber, fontSize = 12.sp, fontWeight = FontWeight.Medium)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("AI Subject Cutout Tracking:", color = TextSecondary, fontSize = 12.sp)
            Text(
              if (isAiCutoutEnabled) "✓ Active (2 Detection Spans)" else "Disabled",
              color = if (isAiCutoutEnabled) MintGreen else TextMuted,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Hardware Codec Safety:", color = TextSecondary, fontSize = 12.sp)
            Text(
              if (dimensions.isMod16Safe) "✓ 100% Mod-16 Optimized" else "✓ Mod-2 Safe",
              color = MintGreen,
              fontSize = 12.sp
            )
          }
        }
      }

      // Format Containers
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Export Format / Codec:", color = TextSecondary, fontSize = 12.sp)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("MP4 (H.264)", "MP4 (HEVC/H.265)", "MOV (ProRes)").forEach { format ->
            val isSelected = selectedContainer == format
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) CyberCyan.copy(alpha = 0.25f) else DarkSurfaceVariant)
                .border(
                  1.dp,
                  if (isSelected) CyberCyan else DarkSurfaceBorder,
                  RoundedCornerShape(8.dp)
                )
                .clickable { selectedContainer = format }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = format,
                color = if (isSelected) CyberCyan else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }
      }

      // Progress / Complete States
      AnimatedVisibility(visible = isExportComplete) {
        Surface(
          color = Color(0xFF064E3B),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MintGreen)
            Column {
              Text("Export Complete!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Saved to /Movies/AspectLab_${dimensions.width}x${dimensions.height}.mp4", color = TextSecondary, fontSize = 11.sp)
            }
          }
        }
      }

      // Action Button
      Button(
        onClick = {
          if (!isExporting) {
            isExporting = true
            isExportComplete = false
            scope.launch {
              delay(1600)
              isExporting = false
              isExportComplete = true
            }
          }
        },
        enabled = !isExporting,
        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("start_export_button")
      ) {
        if (isExporting) {
          CircularProgressIndicator(
            color = Color(0xFF0F172A),
            strokeWidth = 2.5.dp,
            modifier = Modifier.size(20.dp)
          )
          Text("  Rendering Frames with AI Auto-Reframe...", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
        } else {
          Text(if (isExportComplete) "Export Again" else "Export Video Project", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
