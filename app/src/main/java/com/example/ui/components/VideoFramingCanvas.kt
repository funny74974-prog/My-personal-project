package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatioItem
import com.example.model.FramingMode
import com.example.model.SubjectDetectionSpan
import com.example.model.VideoEffectMarker
import com.example.ui.theme.ActionSafeColor
import com.example.ui.theme.CutoutMaskBorder
import com.example.ui.theme.CutoutMaskFill
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkCanvasBg
import com.example.ui.theme.GridGuideColor
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.TitleSafeColor

@Composable
fun VideoFramingCanvas(
  aspectRatio: AspectRatioItem,
  framingMode: FramingMode,
  showAiCutoutMask: Boolean,
  showGuidesGrid: Boolean,
  showSafeZoneOverlay: Boolean,
  activeSubjectSpan: SubjectDetectionSpan?,
  activeEffectMarker: VideoEffectMarker?,
  timelinePosition: Float,
  matteOpacity: Float,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseGlow by infiniteTransition.animateFloat(
    initialValue = 0.6f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseGlow"
  )

  BoxWithConstraints(
    modifier = modifier
      .background(DarkCanvasBg)
      .testTag("video_framing_canvas"),
    contentAlignment = Alignment.Center
  ) {
    val canvasW = constraints.maxWidth.toFloat()
    val canvasH = constraints.maxHeight.toFloat()

    // Calculate aspect bounding box within canvas
    val targetAspect = aspectRatio.decimal.coerceIn(0.15f, 6.0f)
    val canvasAspect = if (canvasH > 0) canvasW / canvasH else 1.0f

    val targetW: Float
    val targetH: Float
    if (canvasAspect > targetAspect) {
      // Height constrained
      targetH = canvasH * 0.94f
      targetW = targetH * targetAspect
    } else {
      // Width constrained
      targetW = canvasW * 0.94f
      targetH = targetW / targetAspect
    }

    val targetLeft = (canvasW - targetW) / 2f
    val targetTop = (canvasH - targetH) / 2f
    val targetRect = Rect(targetLeft, targetTop, targetLeft + targetW, targetTop + targetH)

    Canvas(modifier = Modifier.fillMaxSize()) {
      // 1. Draw Simulated Video Footage (Native 16:9 source footage)
      drawSimulatedVideoFootage(
        canvasW = canvasW,
        canvasH = canvasH,
        targetRect = targetRect,
        framingMode = framingMode,
        activeSubjectSpan = activeSubjectSpan,
        timelinePosition = timelinePosition
      )

      // 2. Draw AI Cutout subject highlight & bounding box if active
      if (showAiCutoutMask && activeSubjectSpan != null) {
        drawSubjectAiCutout(
          canvasW = canvasW,
          canvasH = canvasH,
          targetRect = targetRect,
          framingMode = framingMode,
          span = activeSubjectSpan,
          glowAlpha = pulseGlow
        )
      }

      // 3. Draw Matte (Letterbox/Pillarbox borders) outside targetRect
      drawMatteBars(
        canvasW = canvasW,
        canvasH = canvasH,
        targetRect = targetRect,
        matteOpacity = matteOpacity
      )

      // 4. Draw Framing Frame Outline & Corner brackets
      drawFramingGuides(
        targetRect = targetRect,
        aspectRatio = aspectRatio
      )

      // 5. Draw Composition Guides (Rule of thirds)
      if (showGuidesGrid) {
        drawRuleOfThirds(targetRect)
      }

      // 6. Draw Safe Zone Overlays (Action Safe 90%, Title Safe 80%, and Social Overlays)
      if (showSafeZoneOverlay) {
        drawSafeZones(targetRect, isPortraitSocial = aspectRatio.x < aspectRatio.y)
      }

      // 7. Active transition / effect flash
      if (activeEffectMarker != null) {
        drawEffectFlash(targetRect, activeEffectMarker)
      }
    }

    // Top overlay badge for Current Aspect Ratio and Dimensions
    Box(
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(10.dp)
        .background(Color(0xDD111827), RoundedCornerShape(8.dp))
        .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
        .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Text(
        text = "${aspectRatio.name}  •  ${aspectRatio.ratioString} (${aspectRatio.formattedDecimal})",
        color = CyberCyan,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }

    // Active AI Cutout Indicator Badge
    if (activeSubjectSpan != null && showAiCutoutMask) {
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(10.dp)
          .background(Color(0xEE052E16), RoundedCornerShape(8.dp))
          .border(1.dp, CutoutMaskBorder.copy(alpha = pulseGlow), RoundedCornerShape(8.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Text(
          text = "⚡ AI CUTOUT TRACKING [${String.format("%.1f", activeSubjectSpan.start)}s - ${String.format("%.1f", activeSubjectSpan.end)}s]",
          color = CutoutMaskBorder,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // Active Effect Marker Badge
    if (activeEffectMarker != null) {
      Box(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 12.dp)
          .background(Color(0xEE1E1B4B), RoundedCornerShape(20.dp))
          .border(1.dp, NeonAmber, RoundedCornerShape(20.dp))
          .padding(horizontal = 12.dp, vertical = 5.dp)
      ) {
        Text(
          text = "✨ ${activeEffectMarker.action}: ${activeEffectMarker.effectName}",
          color = NeonAmber,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }
  }
}

// Drawing simulated video camera footage with realistic subject & studio background
private fun DrawScope.drawSimulatedVideoFootage(
  canvasW: Float,
  canvasH: Float,
  targetRect: Rect,
  framingMode: FramingMode,
  activeSubjectSpan: SubjectDetectionSpan?,
  timelinePosition: Float
) {
  // Video backdrop gradient
  val bgBrush = Brush.linearGradient(
    colors = listOf(
      Color(0xFF0F172A),
      Color(0xFF1E293B),
      Color(0xFF0F172A),
      Color(0xFF1A1F36)
    ),
    start = Offset(0f, 0f),
    end = Offset(canvasW, canvasH)
  )
  drawRect(brush = bgBrush, size = Size(canvasW, canvasH))

  // Determine subject center offset based on framing mode
  val subjCenterX: Float
  val subjCenterY: Float
  if (framingMode == FramingMode.AI_AUTO_REFRAME && activeSubjectSpan != null) {
    // Lock subject directly to center of targetRect
    subjCenterX = targetRect.center.x
    subjCenterY = targetRect.center.y + (targetRect.height * 0.08f)
  } else {
    // Standard center
    subjCenterX = canvasW * 0.5f
    subjCenterY = canvasH * 0.52f
  }

  // Draw studio key light cone
  val lightBrush = Brush.radialGradient(
    colors = listOf(
      Color(0x3300E5FF),
      Color(0x1100E5FF),
      Color.Transparent
    ),
    center = Offset(subjCenterX, subjCenterY - 120f),
    radius = 320f
  )
  drawCircle(brush = lightBrush, center = Offset(subjCenterX, subjCenterY - 120f), radius = 320f)

  // Draw Human Subject Silhouette (Head and Shoulders for AI Cutout)
  if (activeSubjectSpan != null) {
    val headRadius = targetRect.height * 0.14f
    val headCenter = Offset(subjCenterX, subjCenterY - (targetRect.height * 0.22f))

    // Head
    drawCircle(
      color = Color(0xFF334155),
      radius = headRadius,
      center = headCenter
    )

    // Face highlight / rim light
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFF475569), Color(0xFF1E293B)),
        center = headCenter,
        radius = headRadius
      ),
      radius = headRadius * 0.95f,
      center = headCenter
    )

    // Torso / Shoulders
    val bodyPath = Path().apply {
      val torsoTop = headCenter.y + headRadius * 0.75f
      val shoulderW = headRadius * 3.2f
      moveTo(subjCenterX - shoulderW / 2, torsoTop + (headRadius * 2.2f))
      quadraticBezierTo(
        subjCenterX - shoulderW / 2, torsoTop + headRadius * 0.3f,
        subjCenterX, torsoTop
      )
      quadraticBezierTo(
        subjCenterX + shoulderW / 2, torsoTop + headRadius * 0.3f,
        subjCenterX + shoulderW / 2, torsoTop + (headRadius * 2.2f)
      )
      close()
    }
    drawPath(
      path = bodyPath,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF3B485C), Color(0xFF1E2430)),
        startY = headCenter.y,
        endY = headCenter.y + headRadius * 3f
      )
    )
  }
}

// Draw the AI Cutout subject tracking contour & bounding box
private fun DrawScope.drawSubjectAiCutout(
  canvasW: Float,
  canvasH: Float,
  targetRect: Rect,
  framingMode: FramingMode,
  span: SubjectDetectionSpan,
  glowAlpha: Float
) {
  val boxW = targetRect.width * span.boundingBox.width
  val boxH = targetRect.height * span.boundingBox.height
  val boxCenterX = if (framingMode == FramingMode.AI_AUTO_REFRAME) {
    targetRect.center.x
  } else {
    canvasW * span.boundingBox.centerX
  }
  val boxCenterY = if (framingMode == FramingMode.AI_AUTO_REFRAME) {
    targetRect.center.y
  } else {
    canvasH * span.boundingBox.centerY
  }

  val boxLeft = boxCenterX - boxW / 2f
  val boxTop = boxCenterY - boxH / 2f
  val boxRect = Rect(boxLeft, boxTop, boxLeft + boxW, boxTop + boxH)

  // Neon glowing bounding box
  drawRoundRect(
    color = CutoutMaskFill,
    topLeft = Offset(boxRect.left, boxRect.top),
    size = Size(boxRect.width, boxRect.height),
    cornerRadius = CornerRadius(12f, 12f)
  )

  drawRoundRect(
    color = CutoutMaskBorder.copy(alpha = glowAlpha),
    topLeft = Offset(boxRect.left, boxRect.top),
    size = Size(boxRect.width, boxRect.height),
    cornerRadius = CornerRadius(12f, 12f),
    style = Stroke(
      width = 2.5f,
      pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 8f), 0f)
    )
  )

  // Four corner brackets
  val bracketLen = 22f
  val strokeW = 3.5f
  val cornerColor = CutoutMaskBorder

  // Top-Left
  drawLine(cornerColor, Offset(boxRect.left, boxRect.top), Offset(boxRect.left + bracketLen, boxRect.top), strokeW)
  drawLine(cornerColor, Offset(boxRect.left, boxRect.top), Offset(boxRect.left, boxRect.top + bracketLen), strokeW)

  // Top-Right
  drawLine(cornerColor, Offset(boxRect.right, boxRect.top), Offset(boxRect.right - bracketLen, boxRect.top), strokeW)
  drawLine(cornerColor, Offset(boxRect.right, boxRect.top), Offset(boxRect.right, boxRect.top + bracketLen), strokeW)

  // Bottom-Left
  drawLine(cornerColor, Offset(boxRect.left, boxRect.bottom), Offset(boxRect.left + bracketLen, boxRect.bottom), strokeW)
  drawLine(cornerColor, Offset(boxRect.left, boxRect.bottom), Offset(boxRect.left, boxRect.bottom - bracketLen), strokeW)

  // Bottom-Right
  drawLine(cornerColor, Offset(boxRect.right, boxRect.bottom), Offset(boxRect.right - bracketLen, boxRect.bottom), strokeW)
  drawLine(cornerColor, Offset(boxRect.right, boxRect.bottom), Offset(boxRect.right, boxRect.bottom - bracketLen), strokeW)
}

// Letterbox / Pillarbox Matte bars
private fun DrawScope.drawMatteBars(
  canvasW: Float,
  canvasH: Float,
  targetRect: Rect,
  matteOpacity: Float
) {
  val matteColor = Color.Black.copy(alpha = matteOpacity)

  // Top bar
  if (targetRect.top > 0) {
    drawRect(color = matteColor, topLeft = Offset(0f, 0f), size = Size(canvasW, targetRect.top))
  }
  // Bottom bar
  if (targetRect.bottom < canvasH) {
    drawRect(color = matteColor, topLeft = Offset(0f, targetRect.bottom), size = Size(canvasW, canvasH - targetRect.bottom))
  }
  // Left bar
  if (targetRect.left > 0) {
    drawRect(color = matteColor, topLeft = Offset(0f, targetRect.top), size = Size(targetRect.left, targetRect.height))
  }
  // Right bar
  if (targetRect.right < canvasW) {
    drawRect(color = matteColor, topLeft = Offset(targetRect.right, targetRect.top), size = Size(canvasW - targetRect.right, targetRect.height))
  }
}

// Outer framing bounds & stylish camera frame
private fun DrawScope.drawFramingGuides(
  targetRect: Rect,
  aspectRatio: AspectRatioItem
) {
  // Border line around aspect ratio canvas
  drawRect(
    color = CyberCyan,
    topLeft = Offset(targetRect.left, targetRect.top),
    size = Size(targetRect.width, targetRect.height),
    style = Stroke(width = 2.0f)
  )

  // Crosshair center mark
  val chLen = 14f
  val cx = targetRect.center.x
  val cy = targetRect.center.y
  val chColor = CyberCyan.copy(alpha = 0.7f)
  drawLine(chColor, Offset(cx - chLen, cy), Offset(cx + chLen, cy), 1.5f)
  drawLine(chColor, Offset(cx, cy - chLen), Offset(cx, cy + chLen), 1.5f)
}

// Rule of Thirds grid
private fun DrawScope.drawRuleOfThirds(targetRect: Rect) {
  val oneThirdW = targetRect.width / 3f
  val oneThirdH = targetRect.height / 3f
  val gridColor = GridGuideColor

  // Vertical lines
  drawLine(gridColor, Offset(targetRect.left + oneThirdW, targetRect.top), Offset(targetRect.left + oneThirdW, targetRect.bottom), 1f)
  drawLine(gridColor, Offset(targetRect.left + oneThirdW * 2f, targetRect.top), Offset(targetRect.left + oneThirdW * 2f, targetRect.bottom), 1f)

  // Horizontal lines
  drawLine(gridColor, Offset(targetRect.left, targetRect.top + oneThirdH), Offset(targetRect.right, targetRect.top + oneThirdH), 1f)
  drawLine(gridColor, Offset(targetRect.left, targetRect.top + oneThirdH * 2f), Offset(targetRect.right, targetRect.top + oneThirdH * 2f), 1f)
}

// Safe zones: Action Safe (90%) and Title Safe (80%)
private fun DrawScope.drawSafeZones(targetRect: Rect, isPortraitSocial: Boolean) {
  // Action Safe: 90%
  val actionW = targetRect.width * 0.90f
  val actionH = targetRect.height * 0.90f
  val actionLeft = targetRect.left + (targetRect.width - actionW) / 2f
  val actionTop = targetRect.top + (targetRect.height - actionH) / 2f

  drawRect(
    color = ActionSafeColor,
    topLeft = Offset(actionLeft, actionTop),
    size = Size(actionW, actionH),
    style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f))
  )

  // Title Safe: 80%
  val titleW = targetRect.width * 0.80f
  val titleH = targetRect.height * 0.80f
  val titleLeft = targetRect.left + (targetRect.width - titleW) / 2f
  val titleTop = targetRect.top + (targetRect.height - titleH) / 2f

  drawRect(
    color = TitleSafeColor,
    topLeft = Offset(titleLeft, titleTop),
    size = Size(titleW, titleH),
    style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
  )

  // If portrait social (like TikTok/Reels 9:16), draw platform UI safe overlays
  if (isPortraitSocial) {
    val iconMarginRight = targetRect.right - (targetRect.width * 0.14f)
    val socialIconColor = Color(0x66FFFFFF)

    // Mock Right side engagement buttons (Like, Comment, Bookmark, Share, Audio Disc)
    for (i in 0..3) {
      val cy = targetRect.top + (targetRect.height * (0.50f + i * 0.08f))
      drawCircle(color = socialIconColor, radius = 9f, center = Offset(iconMarginRight, cy))
    }

    // Mock Bottom caption & music bar safe zone
    val bottomPillH = targetRect.height * 0.12f
    drawRoundRect(
      color = Color(0x22FFFFFF),
      topLeft = Offset(targetRect.left + 16f, targetRect.bottom - bottomPillH - 8f),
      size = Size(targetRect.width * 0.68f, bottomPillH),
      cornerRadius = CornerRadius(8f, 8f)
    )
  }
}

// Flash or Glow effect
private fun DrawScope.drawEffectFlash(targetRect: Rect, effect: VideoEffectMarker) {
  val flashColor = when (effect.effectName) {
    "Flash_White" -> Color.White.copy(alpha = 0.45f)
    "Cinematic_Glow" -> CyberCyan.copy(alpha = 0.35f)
    else -> Color.Transparent
  }
  if (flashColor != Color.Transparent) {
    drawRect(
      color = flashColor,
      topLeft = Offset(targetRect.left, targetRect.top),
      size = Size(targetRect.width, targetRect.height)
    )
  }
}
