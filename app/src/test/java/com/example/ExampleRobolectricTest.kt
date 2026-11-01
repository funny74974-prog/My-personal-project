package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AspectRatioRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AspectLab", appName)
  }

  @Test
  fun `verify catalog contains more than 100 aspect ratios`() {
    val totalCount = AspectRatioRepository.allAspectRatios.size
    assertTrue("Catalog must provide 100+ aspect ratios, found: $totalCount", totalCount >= 100)
  }

  @Test
  fun `verify GCD simplification logic`() {
    val (x1, y1) = AspectRatioRepository.simplifyRatio(1920, 1080)
    assertEquals(16, x1)
    assertEquals(9, y1)

    val (x2, y2) = AspectRatioRepository.simplifyRatio(64, 27)
    assertEquals(64, x2)
    assertEquals(27, y2)
  }

  @Test
  fun `verify video codec macroblock mod2 alignment`() {
    val dims = AspectRatioRepository.calculateVideoDimensions(
      ratioX = 239,
      ratioY = 100,
      baseStandard = 1080,
      isHeightBase = true
    )
    assertTrue("Width must be even for YUV 4:2:0 codec", dims.isMod2Safe)
    assertEquals(0, dims.width % 2)
    assertEquals(0, dims.height % 2)
  }

  @Test
  fun `verify CV subject detection spans from prompt`() {
    val spans = AspectRatioRepository.cvDetectedSubjectSpans
    assertEquals(2, spans.size)
    assertEquals(0.0f, spans[0].start, 0.01f)
    assertEquals(12.4f, spans[0].end, 0.01f)
    assertEquals(15.2f, spans[1].start, 0.01f)
    assertEquals(45.8f, spans[1].end, 0.01f)
  }
}
