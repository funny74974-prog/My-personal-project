package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogicExplainerSheet(
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val scrollState = rememberScrollState()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = DarkSurface,
    contentColor = TextPrimary,
    modifier = modifier.testTag("logic_explainer_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.92f)
        .padding(horizontal = 16.dp)
        .verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "📐 100+ Aspect Ratio UI & Math Logic",
            color = CyberCyan,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "एस्पेक्ट रेशियो सेलेक्टर स्क्रीन डिज़ाइन व आर्किटेक्चर गाइड",
            color = NeonAmber,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        IconButton(onClick = onDismiss) {
          Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextSecondary)
        }
      }

      // Section 1: 100+ Ratios Logic & Architecture
      ExplainerSection(
        stepNumber = "1",
        title = "100+ एस्पेक्ट रेशियो का सेलेक्टर लॉजिक (Ratio Logic)",
        hindiDesc = "ऐप में 1:1, 16:9, 9:16 के अतिरिक्त 100 से अधिक रेशियो सपोर्ट करने के लिए 3-स्तरीय आर्किटेक्चर उपयोग किया जाता है:",
        points = listOf(
          "Category Hierarchy: 100+ रेशियो को 5 स्पष्ट श्रेणियों (Social/Reels, Cinema/Anamorphic, Displays/Ultrawide, Photography, Mathematical) में बाँटा जाता है ताकि यूज़र भ्रमित न हो।",
          "Dual-Axis X:Y Stepper (1 to 100): यूज़र किसी भी दो पूर्णांक (X और Y) को 1 से 100 तक स्लाइडर/बटन से चुन सकता है, जिससे 10,000+ संभावित अनुपात बन सकते हैं।",
          "GCD Simplification Algorithm: जैसे ही यूज़र 1920:1080 या 64:27 इनपुट करता है, Euclidean GCD फंक्शन उसे तुरंत न्यूनतम भिन्न (e.g. 16:9) में रिड्यूस कर देता है।",
          "Decimal-to-Fraction Converter: 2.39 या 1.85 जैसे दशमलव इनपुट को तुरंत निकटतम पूर्णांक अनुपात (239:100) में बदला जाता है।"
        )
      )

      // Code snippet card
      CodeSnippetBox(
        title = "Euclidean GCD & Simplification Algorithm",
        code = """// Lowest terms fraction calculator
tailrec fun gcd(a: Int, b: Int): Int {
    return if (b == 0) abs(a) else gcd(b, a % b)
}

fun simplify(x: Int, y: Int): Pair<Int, Int> {
    val d = gcd(x, y)
    return Pair(x / d, y / d)
}"""
      )

      // Section 2: UI Design Principles for Video Editors
      ExplainerSection(
        stepNumber = "2",
        title = "UI डिज़ाइन का सर्वोत्तम लॉजिक (UI Design Principles)",
        hindiDesc = "वीडियो एडिटिंग ऐप्स (जैसे Premiere Pro, DaVinci Resolve, CapCut) के लिए UI कैसे तैयार करें:",
        points = listOf(
          "Dark Workstation Theme: बैकग्राउंड को डार्क (#0A0E14) रखा जाता है ताकि वीडियो का कलर कंट्रास्ट और लेटरबॉक्स स्पष्ट दिखे।",
          "Live Geometric Framing Preview: यूज़र जब भी कोई रेशियो बदले, कैनवास पर तुरंत वास्तविक समय में लेटरबॉक्स (Top/Bottom बार) या पिलरबॉक्स (Left/Right बार) रेंडर होता है।",
          "Miniature Visual Thumbnails: हर रेशियो कार्ड में एक छोटा ज्यामितीय बॉक्स दिखाया जाता है, जिससे यूज़र को बिना पढ़े ही ओरिएंटेशन (Landscape, Portrait, Square) समझ आ जाए।",
          "Orientation Filter Chips: 100+ लिस्ट में तुरंत केवल 'Portrait' या केवल 'Landscape' फ़िल्टर करने की सुविधा।"
        )
      )

      // Section 3: AI Cutout & Subject Tracking Auto-Reframe
      ExplainerSection(
        stepNumber = "3",
        title = "AI Cutout व सब्जेक्ट ट्रैकिंग ऑटो-रीफ्रेम लॉजिक",
        hindiDesc = "जब वीडियो 16:9 से 9:16 (TikTok/Reels) में बदला जाता है, तब मुख्य व्यक्ति फ्रेम से बाहर न हो, इसके लिए AI ट्रैकिंग लॉजिक:",
        points = listOf(
          "Timestamp Detection Spans: कंप्यूटर विज़न से व्यक्ति के स्क्रीन पर रहने के समय [0.0s - 12.4s] व [15.2s - 45.8s] को डिटेक्ट किया जाता है।",
          "Bounding Box Normalization: व्यक्ति का निर्देशांक [left: 0.30, top: 0.15, right: 0.70, bottom: 0.88] निकाला जाता है।",
          "Smart Auto-Reframe (Pan & Scan): जब 9:16 क्रॉप लगाया जाता है, तो क्रॉप विंडो को वीडियो के सेंटर में रखने की बजाय डिटेक्ट किए गए व्यक्ति के bounding box सेंटर पर लॉक कर दिया जाता है।",
          "Safe Zone Overlays: टिकटॉक/रील्स के लाइक बटन और कैप्शन बार (नीचे और दाईं ओर) के ऊपर सेफ ज़ोन गाइड्स दिखाई जाती हैं ताकि व्यक्ति का चेहरा छिप न जाए।"
        )
      )

      // Section 4: Hardware Codec Alignment (Mod-2 / Mod-16)
      ExplainerSection(
        stepNumber = "4",
        title = "हार्डवेयर कोडेक एलाइनमेंट (Macroblock Safety Rule)",
        hindiDesc = "अक्सर नौसिखिया डेवलपर्स 1080p में कस्टम रेशियो लगाते समय विषम पिक्सल (Odd Pixels) बना देते हैं, जिससे वीडियो एक्सपोर्ट क्रैश हो जाता है:",
        points = listOf(
          "Mod-2 Rule: H.264 और HEVC में YUV 4:2:0 क्रोमा सब-सैंपलिंग के कारण चौड़ाई और ऊँचाई हमेशा सम संख्या (Even Number, mod 2 == 0) होनी चाहिए।",
          "Mod-16 Rule: हार्डवेयर एनकोडर्स 16x16 पिक्सल के मैक्रोब्लॉक प्रोसेस करते हैं। यदि रिज़ॉल्यूशन 16 से विभाज्य हो तो ज़ीरो पैडिंग और अधिकतम स्पीड मिलती है।",
          "Resolution Formula: Width = ((BaseHeight * (X / Y)) + 1) / 2 * 2."
        )
      )

      // Close Button
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp)
      ) {
        Text("Got it (समझ आ गया)", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun ExplainerSection(
  stepNumber: String,
  title: String,
  hindiDesc: String,
  points: List<String>
) {
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
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(CyberCyan),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = stepNumber,
            color = Color(0xFF0F172A),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Text(
          text = title,
          color = TextPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Text(
        text = hindiDesc,
        color = TextSecondary,
        fontSize = 12.sp,
        lineHeight = 18.sp
      )

      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        points.forEach { pt ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text("•", color = CyberCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(
              text = pt,
              color = TextMuted,
              fontSize = 12.sp,
              lineHeight = 17.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CodeSnippetBox(
  title: String,
  code: String
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFF0B1017))
      .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
      .padding(10.dp)
  ) {
    Column {
      Text(
        text = title,
        color = NeonAmber,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 4.dp)
      )
      Text(
        text = code,
        color = Color(0xFF7DD3FC),
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        lineHeight = 16.sp
      )
    }
  }
}
