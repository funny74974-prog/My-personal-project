package com.example.model

enum class RatioCategory(val displayName: String, val iconName: String) {
  SOCIAL("Social & Reels", "share"),
  CINEMA("Cinema & Film", "movie"),
  DISPLAYS("Screens & Displays", "tv"),
  PHOTOGRAPHY("Photo & Print", "camera"),
  CUSTOM("Custom X:Y Builder", "tune")
}

enum class RatioOrientation(val label: String) {
  LANDSCAPE("Landscape (Horizontal)"),
  PORTRAIT("Portrait (Vertical)"),
  SQUARE("Square (1:1)")
}

enum class FramingMode(val title: String, val description: String) {
  FIT_LETTERBOX("Fit (Letterbox)", "Preserves complete original image with matte borders"),
  FILL_CROP("Fill (Crop & Pan)", "Fills viewport completely, cropping edges"),
  STRETCH("Stretch", "Distorts video geometry to match ratio"),
  AI_AUTO_REFRAME("AI Auto-Reframe", "Locks and tracks human subject in safe cutout zone")
}

data class AspectRatioItem(
  val id: String,
  val x: Int,
  val y: Int,
  val name: String,
  val category: RatioCategory,
  val description: String,
  val orientation: RatioOrientation = when {
    x > y -> RatioOrientation.LANDSCAPE
    x < y -> RatioOrientation.PORTRAIT
    else -> RatioOrientation.SQUARE
  },
  val decimal: Float = if (y != 0) x.toFloat() / y.toFloat() else 1.0f,
  val badge: String? = null,
  val safeZoneNotice: String? = null,
  val isCustomUserCreated: Boolean = false
) {
  val ratioString: String
    get() = "$x:$y"

  val formattedDecimal: String
    get() = String.format("%.2f:1", decimal)
}

data class SubjectDetectionSpan(
  val start: Float,
  val end: Float,
  val label: String = "Main Subject",
  val confidence: Float = 0.98f,
  val boundingBox: RectFNorm = RectFNorm(0.32f, 0.15f, 0.68f, 0.88f)
)

data class VideoEffectMarker(
  val timestamp: Float,
  val action: String,
  val effectName: String,
  val colorHex: Long = 0xFF00E5FF
)

data class RectFNorm(
  val left: Float,
  val top: Float,
  val right: Float,
  val bottom: Float
) {
  val width: Float get() = right - left
  val height: Float get() = bottom - top
  val centerX: Float get() = (left + right) / 2f
  val centerY: Float get() = (top + bottom) / 2f
}

data class ResolutionPreset(
  val name: String,
  val baseStandard: Int, // e.g. 720, 1080, 1440, 2160, 4096, 4320
  val isVerticalBase: Boolean = false // whether baseStandard is width or height
)

data class ComputedDimensions(
  val width: Int,
  val height: Int,
  val rawWidth: Float,
  val rawHeight: Float,
  val isMod2Safe: Boolean,
  val isMod16Safe: Boolean,
  val totalPixels: Long,
  val megapixels: Float,
  val estBitrateMbps: Float,
  val codecRecommendation: String
)
