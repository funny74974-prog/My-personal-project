package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.model.AspectRatioRepository
import com.example.model.ComputedDimensions
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomRatioBuilder(
  customX: Int,
  customY: Int,
  customDecimal: String,
  computedDimensions: ComputedDimensions,
  baseStandard: Int,
  onUpdateX: (Int) -> Unit,
  onUpdateY: (Int) -> Unit,
  onUpdateDecimal: (String) -> Unit,
  onInvertRatio: () -> Unit,
  onSetBaseStandard: (Int) -> Unit,
  onSavePreset: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val (reducedX, reducedY) = AspectRatioRepository.simplifyRatio(customX, customY)
  val scrollState = rememberScrollState()
  var showSaveInput by remember { mutableStateOf(false) }
  var presetNameInput by remember { mutableStateOf("Custom $customX:$customY") }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .verticalScroll(scrollState)
      .padding(12.dp)
      .testTag("custom_ratio_builder"),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Top summary card with giant live ratio display, fraction reduction, and invert button
    Surface(
      color = DarkCardBg,
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Active Custom Aspect",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )

          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "$customX:$customY",
              color = CyberCyan,
              fontSize = 28.sp,
              fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "(${String.format("%.3f:1", if (customY != 0) customX.toFloat() / customY else 1f)})",
              color = NeonAmber,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(bottom = 4.dp)
            )
          }

          if (reducedX != customX || reducedY != customY) {
            Text(
              text = "Reduced Fraction: $reducedX:$reducedY (GCD: ${AspectRatioRepository.gcd(customX, customY)})",
              color = MintGreen,
              fontSize = 11.sp
            )
          }
        }

        // Action buttons: Swap X/Y and Save Preset
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          IconButton(
            onClick = onInvertRatio,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(DarkSurfaceVariant)
              .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(8.dp))
              .size(42.dp)
              .testTag("invert_ratio_button")
          ) {
            Icon(
              imageVector = Icons.Filled.SwapHoriz,
              contentDescription = "Invert Orientation",
              tint = CyberCyan,
              modifier = Modifier.size(22.dp)
            )
          }

          IconButton(
            onClick = { showSaveInput = !showSaveInput },
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(DarkSurfaceVariant)
              .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(8.dp))
              .size(42.dp)
              .testTag("save_preset_button")
          ) {
            Icon(
              imageVector = Icons.Filled.BookmarkBorder,
              contentDescription = "Save Custom Preset",
              tint = NeonAmber,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // Save Preset Dialog Row
    if (showSaveInput) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurface)
          .border(1.dp, NeonAmber, RoundedCornerShape(8.dp))
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = presetNameInput,
          onValueChange = { presetNameInput = it },
          label = { Text("Preset Name", fontSize = 10.sp) },
          singleLine = true,
          modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
          onClick = {
            onSavePreset(presetNameInput)
            showSaveInput = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = NeonAmber)
        ) {
          Text("Save", color = Color(0xFF1E293B), fontWeight = FontWeight.Bold)
        }
      }
    }

    // Dual-Axis Controls: Width (X) and Height (Y) Steppers & Sliders
    Surface(
      color = DarkCardBg,
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Dual-Axis Stepper & Slider (1 to 100 Variations)",
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )

        // Axis X (Horizontal Ratio)
        AxisControlRow(
          axisLabel = "Width (X)",
          value = customX,
          onValueChange = onUpdateX,
          accentColor = CyberCyan,
          testTagPrefix = "axis_x"
        )

        // Axis Y (Vertical Ratio)
        AxisControlRow(
          axisLabel = "Height (Y)",
          value = customY,
          onValueChange = onUpdateY,
          accentColor = NeonAmber,
          testTagPrefix = "axis_y"
        )
      }
    }

    // Decimal Aspect Ratio Direct Input (e.g., 2.39, 1.85, 1.414, 0.562)
    Surface(
      color = DarkCardBg,
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "Direct Decimal Ratio Input (e.g. 2.39, 1.85, 0.56)",
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = customDecimal,
            onValueChange = onUpdateDecimal,
            label = { Text("Decimal (X / Y)", fontSize = 11.sp, color = TextMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = DarkSurface,
              unfocusedContainerColor = DarkSurface,
              focusedBorderColor = CyberCyan,
              unfocusedBorderColor = DarkSurfaceBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("decimal_ratio_input")
          )

          Spacer(modifier = Modifier.width(8.dp))

          // Quick decimal shortcuts
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              QuickDecimalChip("2.39", "Scope", onUpdateDecimal)
              QuickDecimalChip("1.85", "Flat", onUpdateDecimal)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              QuickDecimalChip("1.78", "16:9", onUpdateDecimal)
              QuickDecimalChip("0.56", "9:16", onUpdateDecimal)
            }
          }
        }
      }
    }

    // Video Resolution & Codec Safety Calculator Card
    Surface(
      color = DarkCardBg,
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Video Codec & Resolution Calculator",
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )

          // Mod 2 / Mod 16 Safety Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (computedDimensions.isMod16Safe) MintGreen.copy(alpha = 0.2f) else CyberCyan.copy(alpha = 0.2f))
              .border(
                1.dp,
                if (computedDimensions.isMod16Safe) MintGreen else CyberCyan,
                RoundedCornerShape(6.dp)
              )
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = if (computedDimensions.isMod16Safe) "✓ Mod-16 Safe" else "✓ Mod-2 Safe",
              color = if (computedDimensions.isMod16Safe) MintGreen else CyberCyan,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Base Standard Selector Chips
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          AspectRatioRepository.resolutionPresets.forEach { preset ->
            val isSelected = baseStandard == preset.baseStandard
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSelected) CyberCyan else DarkSurface)
                .border(
                  1.dp,
                  if (isSelected) CyberCyan else DarkSurfaceBorder,
                  RoundedCornerShape(6.dp)
                )
                .clickable { onSetBaseStandard(preset.baseStandard) }
                .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
              Text(
                text = preset.name,
                color = if (isSelected) Color(0xFF0F172A) else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }

        // Dimension Results Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(DarkSurfaceVariant)
            .padding(10.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Exact Export Canvas:", color = TextSecondary, fontSize = 12.sp)
              Text(
                "${computedDimensions.width} × ${computedDimensions.height} px",
                color = CyberCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Megapixels:", color = TextSecondary, fontSize = 12.sp)
              Text(
                String.format("%.2f MP", computedDimensions.megapixels),
                color = TextPrimary,
                fontSize = 12.sp
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Estimated Bitrate (30fps):", color = TextSecondary, fontSize = 12.sp)
              Text(
                String.format("%.1f Mbps (H.264/HEVC)", computedDimensions.estBitrateMbps),
                color = NeonAmber,
                fontSize = 12.sp
              )
            }

            Text(
              text = computedDimensions.codecRecommendation,
              color = MintGreen,
              fontSize = 10.sp,
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }
      }
    }

    // Quick Matrix of 20+ Mathematical Variations
    Surface(
      color = DarkCardBg,
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "Quick Mathematical Fractions Matrix",
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )

        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val quickMatrix = listOf(
            1 to 1, 4 to 3, 3 to 4, 16 to 9, 9 to 16,
            16 to 10, 10 to 16, 21 to 9, 32 to 9, 18 to 9,
            5 to 4, 4 to 5, 3 to 2, 2 to 3, 7 to 5,
            5 to 3, 17 to 9, 21 to 10, 24 to 10, 2 to 1
          )

          quickMatrix.forEach { (x, y) ->
            val isCurrent = customX == x && customY == y
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isCurrent) CyberCyan.copy(alpha = 0.3f) else DarkSurface)
                .border(
                  1.dp,
                  if (isCurrent) CyberCyan else DarkSurfaceBorder,
                  RoundedCornerShape(6.dp)
                )
                .clickable {
                  onUpdateX(x)
                  onUpdateY(y)
                }
                .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
              Text(
                text = "$x:$y",
                color = if (isCurrent) CyberCyan else TextPrimary,
                fontSize = 11.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun AxisControlRow(
  axisLabel: String,
  value: Int,
  onValueChange: (Int) -> Unit,
  accentColor: Color,
  testTagPrefix: String
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = axisLabel,
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // -5 step
        QuickStepButton("-5") { onValueChange(value - 5) }
        // -1 step
        IconButton(
          onClick = { onValueChange(value - 1) },
          modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(DarkSurfaceVariant)
            .testTag("${testTagPrefix}_decrement")
        ) {
          Icon(Icons.Filled.Remove, contentDescription = "Minus", tint = TextPrimary, modifier = Modifier.size(14.dp))
        }

        // Value text
        Box(
          modifier = Modifier
            .width(44.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(DarkSurface)
            .border(1.dp, accentColor, RoundedCornerShape(4.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "$value",
            color = accentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // +1 step
        IconButton(
          onClick = { onValueChange(value + 1) },
          modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(DarkSurfaceVariant)
            .testTag("${testTagPrefix}_increment")
        ) {
          Icon(Icons.Filled.Add, contentDescription = "Plus", tint = TextPrimary, modifier = Modifier.size(14.dp))
        }
        // +5 step
        QuickStepButton("+5") { onValueChange(value + 5) }
      }
    }

    Slider(
      value = value.toFloat(),
      onValueChange = { onValueChange(it.toInt()) },
      valueRange = 1f..100f,
      colors = SliderDefaults.colors(
        thumbColor = accentColor,
        activeTrackColor = accentColor,
        inactiveTrackColor = DarkSurfaceVariant
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(32.dp)
        .testTag("${testTagPrefix}_slider")
    )
  }
}

@Composable
private fun QuickStepButton(label: String, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(4.dp))
      .background(DarkSurfaceVariant)
      .clickable(onClick = onClick)
      .padding(horizontal = 6.dp, vertical = 4.dp)
  ) {
    Text(label, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
  }
}

@Composable
private fun QuickDecimalChip(value: String, name: String, onSelect: (String) -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(4.dp))
      .background(DarkSurfaceVariant)
      .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(4.dp))
      .clickable { onSelect(value) }
      .padding(horizontal = 6.dp, vertical = 2.dp)
  ) {
    Text("$name $value", color = CyberCyan, fontSize = 10.sp)
  }
}
