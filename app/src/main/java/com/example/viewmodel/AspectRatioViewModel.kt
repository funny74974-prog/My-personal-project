package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AspectRatioItem
import com.example.model.AspectRatioRepository
import com.example.model.ComputedDimensions
import com.example.model.FramingMode
import com.example.model.RatioCategory
import com.example.model.RatioOrientation
import com.example.model.SubjectDetectionSpan
import com.example.model.VideoEffectMarker
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class AspectStudioUiState(
  val selectedRatio: AspectRatioItem = AspectRatioRepository.socialPresets[0], // 9:16 TikTok/Reels
  val framingMode: FramingMode = FramingMode.AI_AUTO_REFRAME,
  val selectedCategory: RatioCategory = RatioCategory.SOCIAL,
  val searchQuery: String = "",
  val orientationFilter: RatioOrientation? = null,
  val customX: Int = 9,
  val customY: Int = 16,
  val customDecimalInput: String = "0.56",
  val timelineCurrentTime: Float = 6.2f, // Default inside the first subject span 0.0 - 12.4s
  val isPlaying: Boolean = false,
  val showAiCutoutMask: Boolean = true,
  val showGuidesGrid: Boolean = true,
  val showSafeZoneOverlay: Boolean = true,
  val baseResolutionStandard: Int = 1080,
  val isHeightBase: Boolean = true,
  val customSavedRatios: List<AspectRatioItem> = emptyList(),
  val showExplainerSheet: Boolean = false,
  val showExportSheet: Boolean = false,
  val matteOpacity: Float = 0.85f,
  val subjectDetectionSpans: List<SubjectDetectionSpan> = AspectRatioRepository.cvDetectedSubjectSpans,
  val effectMarkers: List<VideoEffectMarker> = AspectRatioRepository.timelineEffectMarkers
) {
  val computedDimensions: ComputedDimensions
    get() = AspectRatioRepository.calculateVideoDimensions(
      ratioX = selectedRatio.x,
      ratioY = selectedRatio.y,
      baseStandard = baseResolutionStandard,
      isHeightBase = isHeightBase
    )

  // Current active subject detection at timelineCurrentTime
  val activeSubjectSpan: SubjectDetectionSpan?
    get() = subjectDetectionSpans.firstOrNull { timelineCurrentTime in it.start..it.end }

  // Current active effect at timelineCurrentTime
  val activeEffectMarker: VideoEffectMarker?
    get() = effectMarkers.firstOrNull { kotlin.math.abs(timelineCurrentTime - it.timestamp) < 1.0f }
}

class AspectRatioViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(AspectStudioUiState())
  val uiState: StateFlow<AspectStudioUiState> = _uiState.asStateFlow()

  private var playbackJob: Job? = null

  fun selectRatio(item: AspectRatioItem) {
    _uiState.update { current ->
      current.copy(
        selectedRatio = item,
        customX = item.x,
        customY = item.y,
        customDecimalInput = String.format("%.3f", item.decimal)
      )
    }
  }

  fun setFramingMode(mode: FramingMode) {
    _uiState.update { it.copy(framingMode = mode) }
  }

  fun selectCategory(category: RatioCategory) {
    _uiState.update { it.copy(selectedCategory = category) }
  }

  fun setSearchQuery(query: String) {
    _uiState.update { it.copy(searchQuery = query) }
  }

  fun setOrientationFilter(filter: RatioOrientation?) {
    _uiState.update { it.copy(orientationFilter = filter) }
  }

  fun updateCustomX(newX: Int) {
    val clamped = newX.coerceIn(1, 100)
    val curY = _uiState.value.customY
    val (simpleX, simpleY) = AspectRatioRepository.simplifyRatio(clamped, curY)
    val dec = clamped.toFloat() / curY.toFloat()
    val customItem = AspectRatioItem(
      id = "custom_${clamped}_${curY}",
      x = clamped,
      y = curY,
      name = "Custom $clamped:$curY ($simpleX:$simpleY)",
      category = RatioCategory.CUSTOM,
      description = "User custom mathematical ratio",
      badge = "Custom X:Y",
      isCustomUserCreated = true
    )
    _uiState.update {
      it.copy(
        customX = clamped,
        selectedRatio = customItem,
        customDecimalInput = String.format("%.3f", dec)
      )
    }
  }

  fun updateCustomY(newY: Int) {
    val clamped = newY.coerceIn(1, 100)
    val curX = _uiState.value.customX
    val (simpleX, simpleY) = AspectRatioRepository.simplifyRatio(curX, clamped)
    val dec = curX.toFloat() / clamped.toFloat()
    val customItem = AspectRatioItem(
      id = "custom_${curX}_${clamped}",
      x = curX,
      y = clamped,
      name = "Custom $curX:$clamped ($simpleX:$simpleY)",
      category = RatioCategory.CUSTOM,
      description = "User custom mathematical ratio",
      badge = "Custom X:Y",
      isCustomUserCreated = true
    )
    _uiState.update {
      it.copy(
        customY = clamped,
        selectedRatio = customItem,
        customDecimalInput = String.format("%.3f", dec)
      )
    }
  }

  fun updateCustomDecimal(decimalStr: String) {
    _uiState.update { it.copy(customDecimalInput = decimalStr) }
    val dec = decimalStr.toFloatOrNull() ?: return
    if (dec <= 0f) return

    // Convert decimal to closest integer ratio out of 100
    val approxY = 100
    val approxX = (dec * 100).roundToInt().coerceIn(1, 300)
    val (sX, sY) = AspectRatioRepository.simplifyRatio(approxX, approxY)

    val item = AspectRatioItem(
      id = "custom_dec_${(dec * 100).toInt()}",
      x = sX,
      y = sY,
      name = "Custom ${String.format("%.2f:1", dec)} ($sX:$sY)",
      category = RatioCategory.CUSTOM,
      description = "Decimal custom ratio generated from user input",
      badge = "Decimal",
      isCustomUserCreated = true
    )
    _uiState.update {
      it.copy(
        customX = sX,
        customY = sY,
        selectedRatio = item
      )
    }
  }

  fun invertRatio() {
    val current = _uiState.value.selectedRatio
    val inverted = AspectRatioItem(
      id = "inverted_${current.y}_${current.x}",
      x = current.y,
      y = current.x,
      name = "${current.y}:${current.x} (Inverted)",
      category = current.category,
      description = "Inverted orientation of ${current.name}",
      badge = "Inverted",
      isCustomUserCreated = true
    )
    _uiState.update {
      it.copy(
        selectedRatio = inverted,
        customX = current.y,
        customY = current.x,
        customDecimalInput = String.format("%.3f", inverted.decimal)
      )
    }
  }

  fun saveCurrentCustomPreset(name: String) {
    val current = _uiState.value.selectedRatio
    val savedItem = current.copy(
      id = "saved_${System.currentTimeMillis()}",
      name = name.ifBlank { "Preset ${current.x}:${current.y}" },
      badge = "★ Saved",
      isCustomUserCreated = true
    )
    _uiState.update {
      it.copy(
        customSavedRatios = it.customSavedRatios + savedItem
      )
    }
  }

  fun setTimelinePosition(timeSeconds: Float) {
    val clamped = timeSeconds.coerceIn(0.0f, 50.0f)
    _uiState.update { it.copy(timelineCurrentTime = clamped) }
  }

  fun togglePlayback() {
    val willPlay = !_uiState.value.isPlaying
    _uiState.update { it.copy(isPlaying = willPlay) }

    if (willPlay) {
      playbackJob?.cancel()
      playbackJob = viewModelScope.launch {
        while (true) {
          delay(100)
          _uiState.update { state ->
            val next = (state.timelineCurrentTime + 0.1f)
            if (next > 50.0f) {
              state.copy(timelineCurrentTime = 0.0f)
            } else {
              state.copy(timelineCurrentTime = next)
            }
          }
        }
      }
    } else {
      playbackJob?.cancel()
    }
  }

  fun toggleAiCutoutMask() {
    _uiState.update { it.copy(showAiCutoutMask = !it.showAiCutoutMask) }
  }

  fun toggleGuidesGrid() {
    _uiState.update { it.copy(showGuidesGrid = !it.showGuidesGrid) }
  }

  fun toggleSafeZones() {
    _uiState.update { it.copy(showSafeZoneOverlay = !it.showSafeZoneOverlay) }
  }

  fun setBaseResolution(res: Int) {
    _uiState.update { it.copy(baseResolutionStandard = res) }
  }

  fun setMatteOpacity(opacity: Float) {
    _uiState.update { it.copy(matteOpacity = opacity.coerceIn(0.2f, 1.0f)) }
  }

  fun openExplainerSheet(open: Boolean) {
    _uiState.update { it.copy(showExplainerSheet = open) }
  }

  fun openExportSheet(open: Boolean) {
    _uiState.update { it.copy(showExportSheet = open) }
  }

  override fun onCleared() {
    super.onCleared()
    playbackJob?.cancel()
  }
}
