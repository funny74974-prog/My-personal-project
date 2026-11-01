package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.FramingMode
import com.example.ui.components.CustomRatioBuilder
import com.example.ui.components.ExportPresetSheet
import com.example.ui.components.LogicExplainerSheet
import com.example.ui.components.RatioSelectorCatalog
import com.example.ui.components.TimelineScrubber
import com.example.ui.components.VideoFramingCanvas
import com.example.ui.theme.CutoutMaskBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkCanvasBg
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AspectRatioViewModel

enum class StudioBottomNavTab {
  CATALOG_100_PLUS,
  CUSTOM_XY_BUILDER,
  AI_CUTOUT_INSPECTOR
}

@Composable
fun AspectRatioStudioScreen(
  viewModel: AspectRatioViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var currentTab by remember { mutableStateOf(StudioBottomNavTab.CATALOG_100_PLUS) }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .background(DarkCanvasBg)
      .statusBarsPadding()
      .navigationBarsPadding(),
    containerColor = DarkCanvasBg,
    bottomBar = {
      NavigationBar(
        containerColor = DarkSurface,
        contentColor = CyberCyan,
        modifier = Modifier
          .fillMaxWidth()
          .border(
            width = 1.dp,
            color = DarkSurfaceBorder,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
          )
      ) {
        NavigationBarItem(
          selected = currentTab == StudioBottomNavTab.CATALOG_100_PLUS,
          onClick = { currentTab = StudioBottomNavTab.CATALOG_100_PLUS },
          icon = { Icon(Icons.Filled.GridView, contentDescription = "100+ Catalog") },
          label = { Text("100+ Ratios", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF0F172A),
            selectedTextColor = CyberCyan,
            indicatorColor = CyberCyan,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
          ),
          modifier = Modifier.testTag("nav_tab_catalog")
        )

        NavigationBarItem(
          selected = currentTab == StudioBottomNavTab.CUSTOM_XY_BUILDER,
          onClick = { currentTab = StudioBottomNavTab.CUSTOM_XY_BUILDER },
          icon = { Icon(Icons.Filled.Tune, contentDescription = "Custom X:Y") },
          label = { Text("X:Y Builder", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF0F172A),
            selectedTextColor = NeonAmber,
            indicatorColor = NeonAmber,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
          ),
          modifier = Modifier.testTag("nav_tab_custom")
        )

        NavigationBarItem(
          selected = currentTab == StudioBottomNavTab.AI_CUTOUT_INSPECTOR,
          onClick = { currentTab = StudioBottomNavTab.AI_CUTOUT_INSPECTOR },
          icon = { Icon(Icons.Filled.Person, contentDescription = "AI Cutout") },
          label = { Text("AI Cutout", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF0F172A),
            selectedTextColor = MintGreen,
            indicatorColor = MintGreen,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
          ),
          modifier = Modifier.testTag("nav_tab_cutout")
        )
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // 1. Studio Top Bar
      StudioHeaderBar(
        activeRatio = uiState.selectedRatio.ratioString,
        onOpenExplainer = { viewModel.openExplainerSheet(true) },
        onOpenExport = { viewModel.openExportSheet(true) }
      )

      // 2. Interactive Video Framing Canvas (Fixed Height 220dp for compact mobile view)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(210.dp)
          .padding(horizontal = 10.dp, vertical = 2.dp)
          .clip(RoundedCornerShape(12.dp))
          .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
      ) {
        VideoFramingCanvas(
          aspectRatio = uiState.selectedRatio,
          framingMode = uiState.framingMode,
          showAiCutoutMask = uiState.showAiCutoutMask,
          showGuidesGrid = uiState.showGuidesGrid,
          showSafeZoneOverlay = uiState.showSafeZoneOverlay,
          activeSubjectSpan = uiState.activeSubjectSpan,
          activeEffectMarker = uiState.activeEffectMarker,
          timelinePosition = uiState.timelineCurrentTime,
          matteOpacity = uiState.matteOpacity,
          modifier = Modifier.fillMaxSize()
        )
      }

      // 3. Framing Mode & Overlay Control Bar
      FramingControlsStrip(
        framingMode = uiState.framingMode,
        isAiCutoutOn = uiState.showAiCutoutMask,
        isGridOn = uiState.showGuidesGrid,
        isSafeZoneOn = uiState.showSafeZoneOverlay,
        onSetFramingMode = viewModel::setFramingMode,
        onToggleCutout = viewModel::toggleAiCutoutMask,
        onToggleGrid = viewModel::toggleGuidesGrid,
        onToggleSafeZone = viewModel::toggleSafeZones
      )

      // 4. Interactive Timeline Scrubber with markers
      TimelineScrubber(
        currentTime = uiState.timelineCurrentTime,
        isPlaying = uiState.isPlaying,
        subjectSpans = uiState.subjectDetectionSpans,
        effectMarkers = uiState.effectMarkers,
        onTimeChange = viewModel::setTimelinePosition,
        onTogglePlay = viewModel::togglePlayback,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
      )

      // 5. Active Tab Content (100+ Catalog, Custom Builder, or AI Cutout Inspector)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
      ) {
        when (currentTab) {
          StudioBottomNavTab.CATALOG_100_PLUS -> {
            RatioSelectorCatalog(
              selectedRatio = uiState.selectedRatio,
              selectedCategory = uiState.selectedCategory,
              searchQuery = uiState.searchQuery,
              orientationFilter = uiState.orientationFilter,
              onSelectRatio = viewModel::selectRatio,
              onSelectCategory = viewModel::selectCategory,
              onSearchChange = viewModel::setSearchQuery,
              onOrientationFilterChange = viewModel::setOrientationFilter,
              modifier = Modifier.fillMaxSize()
            )
          }

          StudioBottomNavTab.CUSTOM_XY_BUILDER -> {
            CustomRatioBuilder(
              customX = uiState.customX,
              customY = uiState.customY,
              customDecimal = uiState.customDecimalInput,
              computedDimensions = uiState.computedDimensions,
              baseStandard = uiState.baseResolutionStandard,
              onUpdateX = viewModel::updateCustomX,
              onUpdateY = viewModel::updateCustomY,
              onUpdateDecimal = viewModel::updateCustomDecimal,
              onInvertRatio = viewModel::invertRatio,
              onSetBaseStandard = viewModel::setBaseResolution,
              onSavePreset = viewModel::saveCurrentCustomPreset,
              modifier = Modifier.fillMaxSize()
            )
          }

          StudioBottomNavTab.AI_CUTOUT_INSPECTOR -> {
            AiCutoutInspector(
              activeSpan = uiState.activeSubjectSpan,
              currentTime = uiState.timelineCurrentTime,
              framingMode = uiState.framingMode,
              isCutoutActive = uiState.showAiCutoutMask,
              onToggleCutout = viewModel::toggleAiCutoutMask,
              onSetFramingMode = viewModel::setFramingMode,
              modifier = Modifier.fillMaxSize()
            )
          }
        }
      }
    }
  }

  // Explainer Bottom Sheet (Math & UI Design Logic)
  if (uiState.showExplainerSheet) {
    LogicExplainerSheet(
      onDismiss = { viewModel.openExplainerSheet(false) }
    )
  }

  // Export Sheet
  if (uiState.showExportSheet) {
    ExportPresetSheet(
      aspectRatio = uiState.selectedRatio,
      dimensions = uiState.computedDimensions,
      framingMode = uiState.framingMode,
      isAiCutoutEnabled = uiState.showAiCutoutMask,
      onDismiss = { viewModel.openExportSheet(false) }
    )
  }
}

@Composable
private fun StudioHeaderBar(
  activeRatio: String,
  onOpenExplainer: () -> Unit,
  onOpenExport: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // App Branding
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF00363F))
          .border(1.dp, CyberCyan, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
      ) {
        Text("📐", fontSize = 16.sp)
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "AspectLab",
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black
          )
          Spacer(modifier = Modifier.width(6.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(CyberCyan.copy(alpha = 0.2f))
              .padding(horizontal = 5.dp, vertical = 1.dp)
          ) {
            Text("PRO", color = CyberCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          }
        }
        Text(
          text = "100+ Video Aspect Ratio Studio",
          color = TextMuted,
          fontSize = 10.sp
        )
      }
    }

    // Top Action Buttons
    Row(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Logic Explainer Button
      Button(
        onClick = onOpenExplainer,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF2E2205),
          contentColor = NeonAmber
        ),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonAmber),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        modifier = Modifier
          .height(34.dp)
          .testTag("explainer_button")
      ) {
        Icon(Icons.Filled.Lightbulb, contentDescription = null, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("लॉजिक समझें", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }

      // Export Button
      Button(
        onClick = onOpenExport,
        colors = ButtonDefaults.buttonColors(
          containerColor = CyberCyan,
          contentColor = Color(0xFF0F172A)
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        modifier = Modifier
          .height(34.dp)
          .testTag("export_button")
      ) {
        Text("Export", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
private fun FramingControlsStrip(
  framingMode: FramingMode,
  isAiCutoutOn: Boolean,
  isGridOn: Boolean,
  isSafeZoneOn: Boolean,
  onSetFramingMode: (FramingMode) -> Unit,
  onToggleCutout: () -> Unit,
  onToggleGrid: () -> Unit,
  onToggleSafeZone: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 10.dp, vertical = 2.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Framing Modes Drop/Chips
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
      FramingMode.values().forEach { mode ->
        val isSelected = framingMode == mode
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) CyberCyan else DarkSurface)
            .border(
              1.dp,
              if (isSelected) CyberCyan else DarkSurfaceBorder,
              RoundedCornerShape(6.dp)
            )
            .clickable { onSetFramingMode(mode) }
            .padding(horizontal = 7.dp, vertical = 4.dp)
        ) {
          Text(
            text = when (mode) {
              FramingMode.FIT_LETTERBOX -> "Fit"
              FramingMode.FILL_CROP -> "Fill"
              FramingMode.STRETCH -> "Stretch"
              FramingMode.AI_AUTO_REFRAME -> "AI Reframe"
            },
            color = if (isSelected) Color(0xFF0F172A) else TextSecondary,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )
        }
      }
    }

    // Overlay Toggles: AI Cutout, Grid, Safe Zones
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
      ToggleBadgeButton(
        label = "Cutout",
        isActive = isAiCutoutOn,
        activeColor = CutoutMaskBorder,
        onClick = onToggleCutout,
        testTag = "toggle_cutout"
      )
      ToggleBadgeButton(
        label = "Grid",
        isActive = isGridOn,
        activeColor = CyberCyan,
        onClick = onToggleGrid,
        testTag = "toggle_grid"
      )
      ToggleBadgeButton(
        label = "Safe",
        isActive = isSafeZoneOn,
        activeColor = NeonAmber,
        onClick = onToggleSafeZone,
        testTag = "toggle_safe"
      )
    }
  }
}

@Composable
private fun ToggleBadgeButton(
  label: String,
  isActive: Boolean,
  activeColor: Color,
  onClick: () -> Unit,
  testTag: String
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(if (isActive) activeColor.copy(alpha = 0.2f) else DarkSurface)
      .border(
        1.dp,
        if (isActive) activeColor else DarkSurfaceBorder,
        RoundedCornerShape(6.dp)
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 6.dp, vertical = 4.dp)
      .testTag(testTag)
  ) {
    Text(
      text = label,
      color = if (isActive) activeColor else TextMuted,
      fontSize = 10.sp,
      fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
    )
  }
}

@Composable
private fun AiCutoutInspector(
  activeSpan: com.example.model.SubjectDetectionSpan?,
  currentTime: Float,
  framingMode: FramingMode,
  isCutoutActive: Boolean,
  onToggleCutout: () -> Unit,
  onSetFramingMode: (FramingMode) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Surface(
      color = DarkCardBg,
      shape = RoundedCornerShape(10.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, CutoutMaskBorder.copy(alpha = 0.5f)),
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
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (activeSpan != null) CutoutMaskBorder else TextMuted)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (activeSpan != null) "Subject Detected (Tracking Live)" else "No Subject in Current Frame",
              color = if (activeSpan != null) CutoutMaskBorder else TextMuted,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }

          Button(
            onClick = onToggleCutout,
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isCutoutActive) CutoutMaskBorder else DarkSurfaceVariant,
              contentColor = if (isCutoutActive) Color(0xFF0F172A) else TextSecondary
            ),
            shape = RoundedCornerShape(6.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            modifier = Modifier.height(28.dp)
          ) {
            Text(if (isCutoutActive) "Cutout ON" else "Cutout OFF", fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }

        Text(
          text = "Computer Vision AI Analysis Details (from prompt):",
          color = TextSecondary,
          fontSize = 11.sp
        )

        // Time intervals breakdown
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          DetectionSpanCard(
            title = "Span 1: Entry",
            range = "0.0s — 12.4s",
            isCurrent = currentTime in 0.0f..12.4f,
            confidence = "96%",
            modifier = Modifier.weight(1f)
          )

          DetectionSpanCard(
            title = "Span 2: Main Speech",
            range = "15.2s — 45.8s",
            isCurrent = currentTime in 15.2f..45.8f,
            confidence = "99%",
            modifier = Modifier.weight(1f)
          )
        }

        // Bounding Box Norm Coordinates
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(DarkSurfaceVariant)
            .padding(8.dp)
        ) {
          Column {
            Text("Active Normalized Bounding Box:", color = TextSecondary, fontSize = 10.sp)
            Text(
              if (activeSpan != null)
                "Left: 0.32  Top: 0.15  Right: 0.68  Bottom: 0.88 (Center Lock: 0.50, 0.51)"
              else "No bounding box active at timestamp ${String.format("%.1f", currentTime)}s",
              color = CyberCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }

    // Explanatory note
    Surface(
      color = DarkCardBg,
      shape = RoundedCornerShape(10.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text("AI Auto-Reframe Smart Pan & Scan", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(
          text = "जब आप 16:9 के वीडियो को 9:16 (रील्स / शॉर्ट्स) में बदलते हैं, तो 'AI Auto-Reframe' मोड मुख्य व्यक्ति को हमेशा फ्रेम के मध्य में रखता है ताकि बैकग्राउंड कटआउट या मोशन ब्लर करते समय व्यक्ति कभी कटे नहीं।",
          color = TextMuted,
          fontSize = 11.sp,
          lineHeight = 16.sp
        )
      }
    }
  }
}

@Composable
private fun DetectionSpanCard(
  title: String,
  range: String,
  isCurrent: Boolean,
  confidence: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(if (isCurrent) CutoutMaskBorder.copy(alpha = 0.2f) else DarkSurface)
      .border(
        1.dp,
        if (isCurrent) CutoutMaskBorder else DarkSurfaceBorder,
        RoundedCornerShape(6.dp)
      )
      .padding(8.dp)
  ) {
    Column {
      Text(title, color = if (isCurrent) CutoutMaskBorder else TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
      Text(range, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
      Text("Confidence: $confidence", color = MintGreen, fontSize = 9.sp)
    }
  }
}
