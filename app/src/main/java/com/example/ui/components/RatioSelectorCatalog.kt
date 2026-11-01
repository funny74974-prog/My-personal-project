package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatioItem
import com.example.model.AspectRatioRepository
import com.example.model.RatioCategory
import com.example.model.RatioOrientation
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatioSelectorCatalog(
  selectedRatio: AspectRatioItem,
  selectedCategory: RatioCategory,
  searchQuery: String,
  orientationFilter: RatioOrientation?,
  onSelectRatio: (AspectRatioItem) -> Unit,
  onSelectCategory: (RatioCategory) -> Unit,
  onSearchChange: (String) -> Unit,
  onOrientationFilterChange: (RatioOrientation?) -> Unit,
  modifier: Modifier = Modifier
) {
  // Filter presets based on category or search
  val rawList = if (searchQuery.isNotBlank()) {
    AspectRatioRepository.allAspectRatios.filter {
      it.name.contains(searchQuery, ignoreCase = true) ||
          it.ratioString.contains(searchQuery, ignoreCase = true) ||
          it.description.contains(searchQuery, ignoreCase = true) ||
          it.formattedDecimal.contains(searchQuery, ignoreCase = true)
    }
  } else {
    when (selectedCategory) {
      RatioCategory.SOCIAL -> AspectRatioRepository.socialPresets
      RatioCategory.CINEMA -> AspectRatioRepository.cinemaPresets
      RatioCategory.DISPLAYS -> AspectRatioRepository.displayPresets
      RatioCategory.PHOTOGRAPHY -> AspectRatioRepository.photoPresets
      RatioCategory.CUSTOM -> AspectRatioRepository.mathematicalVariations
    }
  }

  val displayList = if (orientationFilter != null) {
    rawList.filter { it.orientation == orientationFilter }
  } else {
    rawList
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag("ratio_catalog_section")
  ) {
    // Search bar with item count badge
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Search 100+ ratios (e.g. 9:16, cinema, imax, 4:5)...", fontSize = 12.sp, color = TextMuted) },
        leadingIcon = {
          Icon(Icons.Filled.Search, contentDescription = "Search", tint = CyberCyan, modifier = Modifier.size(18.dp))
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
            }
          }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DarkSurface,
          unfocusedContainerColor = DarkSurface,
          focusedBorderColor = CyberCyan,
          unfocusedBorderColor = DarkSurfaceBorder,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .weight(1f)
          .height(46.dp)
          .testTag("search_ratio_input")
      )

      Spacer(modifier = Modifier.width(8.dp))

      // Catalog Count Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurfaceVariant)
          .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(8.dp))
          .padding(horizontal = 8.dp, vertical = 10.dp)
      ) {
        Text(
          text = "${displayList.size} Ratios",
          color = CyberCyan,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // Category Tabs (Only if not searching)
    if (searchQuery.isBlank()) {
      ScrollableTabRow(
        selectedTabIndex = selectedCategory.ordinal,
        containerColor = Color.Transparent,
        contentColor = CyberCyan,
        edgePadding = 12.dp,
        indicator = { tabPositions ->
          if (selectedCategory.ordinal < tabPositions.size) {
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCategory.ordinal]),
              color = CyberCyan,
              height = 2.5.dp
            )
          }
        },
        divider = {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(1.dp)
              .background(DarkSurfaceBorder)
          )
        }
      ) {
        RatioCategory.values().forEach { category ->
          val isSelected = selectedCategory == category
          val count = when (category) {
            RatioCategory.SOCIAL -> AspectRatioRepository.socialPresets.size
            RatioCategory.CINEMA -> AspectRatioRepository.cinemaPresets.size
            RatioCategory.DISPLAYS -> AspectRatioRepository.displayPresets.size
            RatioCategory.PHOTOGRAPHY -> AspectRatioRepository.photoPresets.size
            RatioCategory.CUSTOM -> AspectRatioRepository.mathematicalVariations.size
          }

          Tab(
            selected = isSelected,
            onClick = { onSelectCategory(category) },
            text = {
              Text(
                text = "${category.displayName} ($count)",
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) CyberCyan else TextSecondary
              )
            },
            modifier = Modifier.testTag("tab_${category.name.lowercase()}")
          )
        }
      }
    }

    // Quick Orientation Filter Chips
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      item {
        FilterChip(
          selected = orientationFilter == null,
          onClick = { onOrientationFilterChange(null) },
          label = { Text("All Orientations", fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
            selectedLabelColor = CyberCyan,
            containerColor = DarkSurface,
            labelColor = TextSecondary
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = orientationFilter == null,
            borderColor = DarkSurfaceBorder,
            selectedBorderColor = CyberCyan
          )
        )
      }
      RatioOrientation.values().forEach { orientation ->
        val isSelected = orientationFilter == orientation
        item {
          FilterChip(
            selected = isSelected,
            onClick = { onOrientationFilterChange(if (isSelected) null else orientation) },
            label = {
              Text(
                text = when (orientation) {
                  RatioOrientation.LANDSCAPE -> "▰ Landscape"
                  RatioOrientation.PORTRAIT -> "▱ Portrait"
                  RatioOrientation.SQUARE -> "■ Square (1:1)"
                },
                fontSize = 11.sp
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
              selectedLabelColor = CyberCyan,
              containerColor = DarkSurface,
              labelColor = TextSecondary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = DarkSurfaceBorder,
              selectedBorderColor = CyberCyan
            )
          )
        }
      }
    }

    // Aspect Ratio Items Grid
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f, fill = false),
      contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(displayList, key = { it.id }) { item ->
        val isSelected = selectedRatio.id == item.id || (selectedRatio.x == item.x && selectedRatio.y == item.y)
        AspectRatioCard(
          item = item,
          isSelected = isSelected,
          onSelect = { onSelectRatio(item) }
        )
      }
    }
  }
}

@Composable
fun AspectRatioCard(
  item: AspectRatioItem,
  isSelected: Boolean,
  onSelect: () -> Unit
) {
  val borderColor = if (isSelected) CyberCyan else DarkSurfaceBorder
  val bgColor = if (isSelected) Color(0xFF132738) else DarkCardBg

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(bgColor)
      .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(10.dp))
      .clickable(onClick = onSelect)
      .padding(10.dp)
      .testTag("ratio_card_${item.id}")
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Miniature visual aspect ratio box showing geometric proportion
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0xFF0A0F17)),
        contentAlignment = Alignment.Center
      ) {
        val aspect = item.decimal.coerceIn(0.2f, 5.0f)
        val maxDim = 32f
        val w: Float
        val h: Float
        if (aspect >= 1.0f) {
          w = maxDim
          h = (maxDim / aspect).coerceAtLeast(6f)
        } else {
          h = maxDim
          w = (maxDim * aspect).coerceAtLeast(6f)
        }

        Box(
          modifier = Modifier
            .size(w.dp, h.dp)
            .background(if (isSelected) CyberCyan.copy(alpha = 0.4f) else Color(0xFF334155))
            .border(
              1.dp,
              if (isSelected) CyberCyan else Color(0xFF64748B),
              RoundedCornerShape(2.dp)
            )
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Ratio Title, Description, and Badges
      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = item.ratioString,
            color = if (isSelected) CyberCyan else TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = "(${item.formattedDecimal})",
            color = TextSecondary,
            fontSize = 12.sp
          )

          if (item.badge != null) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (item.badge.contains("Top") || item.badge.contains("Hit")) NeonAmber.copy(alpha = 0.25f) else Color(0xFF1E293B))
                .border(
                  0.5.dp,
                  if (item.badge.contains("Top") || item.badge.contains("Hit")) NeonAmber else CyberCyan.copy(alpha = 0.5f),
                  RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
              Text(
                text = item.badge,
                color = if (item.badge.contains("Top") || item.badge.contains("Hit")) NeonAmber else CyberCyan,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Text(
          text = item.name,
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Text(
          text = item.description,
          color = TextMuted,
          fontSize = 11.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      // Checkmark / Select Pill
      if (isSelected) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CyberCyan)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = "ACTIVE",
            color = Color(0xFF00363F),
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }
      }
    }
  }
}
