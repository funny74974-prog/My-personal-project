package com.example.model

import kotlin.math.roundToInt

object AspectRatioRepository {

  // Video timeline subject detection from computer vision analysis (from user prompt)
  val cvDetectedSubjectSpans: List<SubjectDetectionSpan> = listOf(
    SubjectDetectionSpan(
      start = 0.0f,
      end = 12.4f,
      label = "Person Detected (Scene 1)",
      confidence = 0.96f,
      boundingBox = RectFNorm(0.33f, 0.16f, 0.67f, 0.86f)
    ),
    SubjectDetectionSpan(
      start = 15.2f,
      end = 45.8f,
      label = "Person Detected (Scene 2)",
      confidence = 0.99f,
      boundingBox = RectFNorm(0.30f, 0.14f, 0.70f, 0.88f)
    )
  )

  // Timeline effect markers (from user prompt)
  val timelineEffectMarkers: List<VideoEffectMarker> = listOf(
    VideoEffectMarker(
      timestamp = 0.0f,
      action = "Animation_In",
      effectName = "Fade_In",
      colorHex = 0xFF00E676
    ),
    VideoEffectMarker(
      timestamp = 12.4f,
      action = "Cut_Transition",
      effectName = "Flash_White",
      colorHex = 0xFFFFB300
    ),
    VideoEffectMarker(
      timestamp = 30.5f,
      action = "Frame_Effect",
      effectName = "Cinematic_Glow",
      colorHex = 0xFF00E5FF
    )
  )

  // Primary Curated Catalog (Over 100+ aspect ratios organized into clear categories)
  val socialPresets: List<AspectRatioItem> = listOf(
    AspectRatioItem("soc_tiktok", 9, 16, "TikTok / Reels / Shorts", RatioCategory.SOCIAL, "Full-screen vertical standard for viral short videos", badge = "🔥 Top Trend", safeZoneNotice = "Keep subject inside 80% box to avoid buttons/captions"),
    AspectRatioItem("soc_ig_feed", 4, 5, "Instagram Feed Portrait", RatioCategory.SOCIAL, "Maximizes screen estate on mobile Instagram feeds", badge = "Popular", safeZoneNotice = "1080x1350 px optimal"),
    AspectRatioItem("soc_square", 1, 1, "Instagram Square", RatioCategory.SOCIAL, "Universal square format for feed posts & album artwork", badge = "Classic"),
    AspectRatioItem("soc_yt_landscape", 16, 9, "YouTube / Video Standard", RatioCategory.SOCIAL, "Standard widescreen video for desktop & television", badge = "Global Std"),
    AspectRatioItem("soc_x_landscape", 16, 9, "X (Twitter) Video", RatioCategory.SOCIAL, "Recommended horizontal video for news feeds"),
    AspectRatioItem("soc_x_portrait", 3, 4, "X (Twitter) Portrait", RatioCategory.SOCIAL, "Tall video for timeline feed engagement"),
    AspectRatioItem("soc_x_landscape_link", 191, 100, "X (Twitter) Card 1.91:1", RatioCategory.SOCIAL, "Optimal ratio for link preview banners"),
    AspectRatioItem("soc_pinterest_opt", 2, 3, "Pinterest Standard Pin", RatioCategory.SOCIAL, "Recommended vertical image ratio for Pinterest (1000x1500)"),
    AspectRatioItem("soc_pinterest_long", 1, 2, "Pinterest Tall Pin", RatioCategory.SOCIAL, "Super tall infographic & step-by-step video pin"),
    AspectRatioItem("soc_fb_feed", 4, 5, "Facebook Feed Video", RatioCategory.SOCIAL, "High engagement mobile video post"),
    AspectRatioItem("soc_fb_cover", 820, 312, "Facebook Page Cover", RatioCategory.SOCIAL, "Desktop banner ratio (2.63:1)"),
    AspectRatioItem("soc_fb_story", 9, 16, "Facebook Stories", RatioCategory.SOCIAL, "Vertical immersive story format"),
    AspectRatioItem("soc_linkedin_sq", 1, 1, "LinkedIn Square Post", RatioCategory.SOCIAL, "Clean corporate post for B2B feeds"),
    AspectRatioItem("soc_linkedin_port", 4, 5, "LinkedIn Vertical Document", RatioCategory.SOCIAL, "Multi-slide PDF & vertical carousel"),
    AspectRatioItem("soc_linkedin_vid", 16, 9, "LinkedIn Landscape Video", RatioCategory.SOCIAL, "Conference talk & product demo video"),
    AspectRatioItem("soc_snapchat", 9, 16, "Snapchat Spotlight", RatioCategory.SOCIAL, "Full-bleed mobile vertical with sound overlay"),
    AspectRatioItem("soc_threads", 4, 5, "Instagram Threads Portrait", RatioCategory.SOCIAL, "Modern photo and short clip format"),
    AspectRatioItem("soc_threads_sq", 1, 1, "Instagram Threads Square", RatioCategory.SOCIAL, "Standard square visual post"),
    AspectRatioItem("soc_twitch_stream", 16, 9, "Twitch Livestream", RatioCategory.SOCIAL, "Broadcast 1080p60 gameplay standard"),
    AspectRatioItem("soc_twitch_banner", 5, 2, "Twitch Profile Banner", RatioCategory.SOCIAL, "Header banner 1200x480 px (2.5:1)"),
    AspectRatioItem("soc_yt_banner", 16, 9, "YouTube Channel Header (TV)", RatioCategory.SOCIAL, "2560x1440 canvas with 1546x423 safe area"),
    AspectRatioItem("soc_yt_header_desk", 62, 10, "YouTube Channel Safe Banner", RatioCategory.SOCIAL, "Desktop safe viewing crop (6.2:1)"),
    AspectRatioItem("soc_discord_banner", 5, 2, "Discord Server Banner", RatioCategory.SOCIAL, "Header ratio for server welcome screens"),
    AspectRatioItem("soc_whatsapp_status", 9, 16, "WhatsApp Status", RatioCategory.SOCIAL, "Full screen status video and photo"),
    AspectRatioItem("soc_telegram_story", 9, 16, "Telegram Story", RatioCategory.SOCIAL, "Vertical telegram channel story with stickers"),
    AspectRatioItem("soc_reddit_video", 16, 9, "Reddit Landscape Post", RatioCategory.SOCIAL, "Community video upload format"),
    AspectRatioItem("soc_reddit_portrait", 4, 5, "Reddit Portrait Feed", RatioCategory.SOCIAL, "Tall scroll-stopping post"),
    AspectRatioItem("soc_vimeo", 16, 9, "Vimeo HD Showcase", RatioCategory.SOCIAL, "High bitrate creative portfolio standard")
  )

  val cinemaPresets: List<AspectRatioItem> = listOf(
    AspectRatioItem("cin_anamorphic", 239, 100, "Anamorphic Scope (2.39:1)", RatioCategory.CINEMA, "The modern theatrical widescreen cinema standard", badge = "Hollywood Std"),
    AspectRatioItem("cin_classic_scope", 235, 100, "Classic Scope (2.35:1)", RatioCategory.CINEMA, "Vintage theatrical Panavision letterbox standard"),
    AspectRatioItem("cin_cinemascope", 255, 100, "CinemaScope (2.55:1)", RatioCategory.CINEMA, "Early 1950s 4-track magnetic stereo format"),
    AspectRatioItem("cin_dci_flat", 185, 100, "DCI Flat (1.85:1)", RatioCategory.CINEMA, "Standard theatrical digital cinema flat container (1998x1080 / 3996x2160)", badge = "DCI Std"),
    AspectRatioItem("cin_dci_scope", 239, 100, "DCI Scope (2.39:1)", RatioCategory.CINEMA, "DCI 4K Scope theatrical distribution (4096x1716)"),
    AspectRatioItem("cin_euro_flat", 166, 100, "European Widescreen (1.66:1 / 5:3)", RatioCategory.CINEMA, "Classic European art cinema format (Kubrick, Italian Neorealism)"),
    AspectRatioItem("cin_academy", 1375, 1000, "Academy Ratio (1.375:1 / 11:8)", RatioCategory.CINEMA, "Golden Age of Hollywood 35mm optical sound standard (1932-1953)"),
    AspectRatioItem("cin_silent", 4, 3, "Silent Era / Classic TV (1.33:1)", RatioCategory.CINEMA, "Thomas Edison / Early cinema & 20th century CRT TV standard"),
    AspectRatioItem("cin_imax_digital", 190, 100, "IMAX Digital (1.90:1)", RatioCategory.CINEMA, "Commercial multiplex IMAX Xenon & Laser standard", badge = "IMAX"),
    AspectRatioItem("cin_imax_70mm", 143, 100, "IMAX 70mm Film (1.43:1)", RatioCategory.CINEMA, "Full-height 15/70mm dome & flagship IMAX screens (Nolan standard)", badge = "IMAX 70mm"),
    AspectRatioItem("cin_univisium", 2, 1, "Univisium (2:1 / 18:9)", RatioCategory.CINEMA, "Storaro standard bridge between cinema & TV (Netflix / Stranger Things)", badge = "Streaming Hit"),
    AspectRatioItem("cin_cinerama", 259, 100, "Cinerama (2.59:1)", RatioCategory.CINEMA, "Three-strip synchronized curved screen roadshow format"),
    AspectRatioItem("cin_ultra_panavision", 276, 100, "Ultra Panavision 70 (2.76:1)", RatioCategory.CINEMA, "Ultra-wide 65mm format (Ben-Hur, The Hateful Eight)"),
    AspectRatioItem("cin_vistavision", 196, 100, "VistaVision (1.96:1)", RatioCategory.CINEMA, "Horizontal 35mm Paramount high-fidelity format (Hitchcock / Vertigo)"),
    AspectRatioItem("cin_super35", 16, 9, "Super 35mm (1.78:1)", RatioCategory.CINEMA, "Full gate camera aperture for television & theatrical extraction"),
    AspectRatioItem("cin_techniscope", 233, 100, "Techniscope (2.33:1 / 2-perf)", RatioCategory.CINEMA, "Spaghetti Western format (Sergio Leone / Dollars trilogy)"),
    AspectRatioItem("cin_polyvision", 4, 1, "Polyvision (4:1)", RatioCategory.CINEMA, "Abel Gance 1927 triptych three-projector spectacle"),
    AspectRatioItem("cin_todd_ao", 220, 100, "Todd-AO 70mm (2.20:1)", RatioCategory.CINEMA, "65mm capture for 70mm roadshow release (Sound of Music, 2001)"),
    AspectRatioItem("cin_super16", 166, 100, "Super 16mm (1.66:1)", RatioCategory.CINEMA, "Indie feature film aesthetic format"),
    AspectRatioItem("cin_vintage_8mm", 136, 100, "Standard 8mm / Super 8 (1.36:1)", RatioCategory.CINEMA, "Nostalgic home movie film gate"),
    AspectRatioItem("cin_mgm_camera65", 276, 100, "MGM Camera 65 (2.76:1)", RatioCategory.CINEMA, "Super-wide 1.25x anamorphic squeeze on 65mm film"),
    AspectRatioItem("cin_super_technirama", 225, 100, "Super Technirama (2.25:1)", RatioCategory.CINEMA, "Large area 35mm 8-perf horizontal negative format"),
    AspectRatioItem("cin_panavision_anamorphic", 240, 100, "Modern Panavision (2.40:1)", RatioCategory.CINEMA, "SMPTE 195-2000 revision widescreen aperture"),
    AspectRatioItem("cin_widescreen_tv", 16, 9, "ATSC / DVB HDTV (1.78:1)", RatioCategory.CINEMA, "Worldwide digital television standard"),
    AspectRatioItem("cin_dci_full_container", 190, 100, "DCI Native Container (256:135)", RatioCategory.CINEMA, "4096x2160 true DCI container ratio (1.896:1)"),
    AspectRatioItem("cin_anamorphic_vertical", 100, 239, "Vertical Cinema (1:2.39)", RatioCategory.CINEMA, "Ultra-narrow vertical art video & fashion film")
  )

  val displayPresets: List<AspectRatioItem> = listOf(
    AspectRatioItem("disp_ultrawide_21_9", 21, 9, "Ultrawide Monitor (21:9)", RatioCategory.DISPLAYS, "Gaming & multi-window productivity display (64:27 / 2.37:1)", badge = "Gaming"),
    AspectRatioItem("disp_super_ultrawide_32_9", 32, 9, "Super Ultrawide (32:9)", RatioCategory.DISPLAYS, "Dual 16:9 side-by-side simulator display (5120x1440 / 3.56:1)", badge = "Sim Rig"),
    AspectRatioItem("disp_dual_qhd", 32, 10, "Dual QHD (32:10)", RatioCategory.DISPLAYS, "Dual 16:10 workspace curved monitor"),
    AspectRatioItem("disp_laptop_16_10", 16, 10, "MacBook & Laptop (16:10 / 8:5)", RatioCategory.DISPLAYS, "Modern laptop productivity display with extra vertical space"),
    AspectRatioItem("disp_phone_19_5_9", 39, 18, "Modern Smartphone (19.5:9)", RatioCategory.DISPLAYS, "Current flagship smartphone screen (iPhone, Galaxy S)"),
    AspectRatioItem("disp_phone_20_9", 20, 9, "Android Flagship (20:9)", RatioCategory.DISPLAYS, "Tall aspect smartphone display (2400x1080)"),
    AspectRatioItem("disp_phone_21_9", 21, 9, "Cinema Phone (21:9)", RatioCategory.DISPLAYS, "Sony Xperia cinema-ratio smartphone display"),
    AspectRatioItem("disp_phone_18_9", 18, 9, "Univisium Mobile (18:9 / 2:1)", RatioCategory.DISPLAYS, "Modern edge-to-edge balanced mobile screen"),
    AspectRatioItem("disp_phone_20_5_9", 41, 18, "Ultra-Tall Mobile (20.5:9)", RatioCategory.DISPLAYS, "Periscope & extra-tall gaming phone screen"),
    AspectRatioItem("disp_foldable_inner", 6, 5, "Foldable Inner Display (6:5 / 1.2:1)", RatioCategory.DISPLAYS, "Square-ish tablet mode for folding devices (Galaxy Z Fold)"),
    AspectRatioItem("disp_foldable_outer", 23, 9, "Foldable Outer Cover (23:9)", RatioCategory.DISPLAYS, "Narrow tall cover display on folded phones"),
    AspectRatioItem("disp_foldable_square", 4, 3, "Foldable Tablet Mode (4:3)", RatioCategory.DISPLAYS, "Pixel Fold interior unfolded canvas"),
    AspectRatioItem("disp_ipad_4_3", 4, 3, "iPad Classic (4:3)", RatioCategory.DISPLAYS, "Standard Apple iPad 9.7\" - 10.2\" screen"),
    AspectRatioItem("disp_ipad_pro_11", 139, 100, "iPad Pro 11\" (1.39:1 / ~7:5)", RatioCategory.DISPLAYS, "Liquid Retina modern tablet canvas"),
    AspectRatioItem("disp_ipad_mini", 3, 2, "Compact Tablet (3:2)", RatioCategory.DISPLAYS, "Surface Pro / iPad Mini reading canvas"),
    AspectRatioItem("disp_smartwatch_sq", 1, 1, "Smartwatch Square (1:1)", RatioCategory.DISPLAYS, "Wear OS & smartwatch widget display"),
    AspectRatioItem("disp_apple_watch", 4, 5, "Apple Watch Display (4:5)", RatioCategory.DISPLAYS, "Curved rectangle smartwatch face"),
    AspectRatioItem("disp_smart_display", 16, 10, "Smart Hub / Nest Hub (16:10)", RatioCategory.DISPLAYS, "Kitchen ambient display format"),
    AspectRatioItem("disp_digital_signage_horiz", 3, 1, "Horizontal Billboard (3:1)", RatioCategory.DISPLAYS, "LED ribbon ticker & airport terminal banner"),
    AspectRatioItem("disp_digital_signage_vert", 1, 3, "Vertical Totem Kiosk (1:3)", RatioCategory.DISPLAYS, "Shopping mall interactive directory totem"),
    AspectRatioItem("disp_stadium_ribbon", 10, 1, "Stadium LED Ribbon (10:1)", RatioCategory.DISPLAYS, "Arena perimeter ribbon banner video"),
    AspectRatioItem("disp_vertical_billboard", 9, 21, "Vertical Highway Sign (9:21)", RatioCategory.DISPLAYS, "Digital roadside vertical advertising display"),
    AspectRatioItem("disp_crt_retro", 4, 3, "Retro CRT Monitor (4:3)", RatioCategory.DISPLAYS, "Arcade cabinet & vintage gaming display"),
    AspectRatioItem("disp_gameboy", 10, 9, "Game Boy Classic (10:9 / 1.11:1)", RatioCategory.DISPLAYS, "160x144 retro handheld console display")
  )

  val photoPresets: List<AspectRatioItem> = listOf(
    AspectRatioItem("pho_35mm_full_frame", 3, 2, "35mm Full Frame Film (3:2)", RatioCategory.PHOTOGRAPHY, "Standard 24x36mm camera sensor (Sony, Canon, Nikon)", badge = "Photo Std"),
    AspectRatioItem("pho_35mm_portrait", 2, 3, "35mm Portrait (2:3)", RatioCategory.PHOTOGRAPHY, "Vertical camera orientation standard"),
    AspectRatioItem("pho_medium_645", 4, 3, "Medium Format 645 (4:3 / 1.33:1)", RatioCategory.PHOTOGRAPHY, "Hasselblad & Fujifilm GFX medium format sensor"),
    AspectRatioItem("pho_medium_6x6", 1, 1, "Medium Format 6x6 (1:1)", RatioCategory.PHOTOGRAPHY, "Square Hasselblad 500C & Rolleiflex TLR film"),
    AspectRatioItem("pho_medium_6x7", 7, 6, "Medium Format 6x7 (7:6 / 1.17:1)", RatioCategory.PHOTOGRAPHY, "Mamiya RB67 / Pentax 67 ideal format"),
    AspectRatioItem("pho_large_format_4x5", 5, 4, "Large Format 4x5 Sheet (5:4)", RatioCategory.PHOTOGRAPHY, "Traditional large format view camera sheet film"),
    AspectRatioItem("pho_large_format_8x10", 5, 4, "Large Format 8x10 (5:4)", RatioCategory.PHOTOGRAPHY, "Fine-art gallery contact print standard"),
    AspectRatioItem("pho_print_8x10", 4, 5, "8x10 Portrait Print (4:5)", RatioCategory.PHOTOGRAPHY, "Standard framed wall art & headshot print"),
    AspectRatioItem("pho_print_5x7", 7, 5, "5x7 Greeting Card (7:5)", RatioCategory.PHOTOGRAPHY, "Standard photo card & framed desk portrait"),
    AspectRatioItem("pho_print_4x6", 3, 2, "4x6 Postcard Print (3:2)", RatioCategory.PHOTOGRAPHY, "Consumer drugstore lab photo print standard"),
    AspectRatioItem("pho_iso_a4", 707, 500, "ISO A-Series Paper (1:√2 ≈ 1.414:1)", RatioCategory.PHOTOGRAPHY, "Lichtenberg ratio (A4, A3, A2) paper standard", badge = "ISO 216"),
    AspectRatioItem("pho_iso_a4_vert", 500, 707, "ISO A4 Vertical (1:1.414)", RatioCategory.PHOTOGRAPHY, "Vertical document, magazine, & poster print"),
    AspectRatioItem("pho_golden_ratio", 1618, 1000, "Golden Ratio Phi (Φ:1 / 1.618:1)", RatioCategory.PHOTOGRAPHY, "Divine proportion found in classical architecture & nature", badge = "Golden Ratio"),
    AspectRatioItem("pho_golden_ratio_vert", 1000, 1618, "Golden Ratio Vertical (1:Φ)", RatioCategory.PHOTOGRAPHY, "Vertical classical aesthetic framing"),
    AspectRatioItem("pho_silver_ratio", 2414, 1000, "Silver Ratio (2.414:1)", RatioCategory.PHOTOGRAPHY, "Mathematical plastic ratio for geometric composition"),
    AspectRatioItem("pho_pano_2_1", 2, 1, "Panoramic 2:1", RatioCategory.PHOTOGRAPHY, "Hasselblad XPan landscape film format"),
    AspectRatioItem("pho_pano_3_1", 3, 1, "Super Panoramic (3:1)", RatioCategory.PHOTOGRAPHY, "Triple-frame panoramic skyline composition"),
    AspectRatioItem("pho_pano_4_1", 4, 1, "Ultra Panoramic (4:1)", RatioCategory.PHOTOGRAPHY, "360-degree stitched architectural panorama"),
    AspectRatioItem("pho_polaroid", 107, 100, "Polaroid Square (1.07:1)", RatioCategory.PHOTOGRAPHY, "Classic white-frame instant film photo"),
    AspectRatioItem("pho_instax_mini", 46, 62, "Fujifilm Instax Mini (46:62 / 1:1.35)", RatioCategory.PHOTOGRAPHY, "Credit card size instant photo print"),
    AspectRatioItem("pho_instax_wide", 99, 62, "Fujifilm Instax Wide (99:62 / 1.60:1)", RatioCategory.PHOTOGRAPHY, "Wide instant snapshot format"),
    AspectRatioItem("pho_wallet_photo", 5, 7, "Wallet Size Photo (5:7)", RatioCategory.PHOTOGRAPHY, "Traditional 2.5x3.5 inch wallet photograph")
  )

  // Computed / Common Mathematical X:Y Variations (over 30 additional mathematical combinations)
  val mathematicalVariations: List<AspectRatioItem> by lazy {
    generateMathematicalRatios()
  }

  // Combined master list of 100+ aspect ratios
  val allAspectRatios: List<AspectRatioItem> by lazy {
    socialPresets + cinemaPresets + displayPresets + photoPresets + mathematicalVariations
  }

  // Common export resolutions for video editors
  val resolutionPresets = listOf(
    ResolutionPreset("720p HD", 720),
    ResolutionPreset("1080p Full HD", 1080),
    ResolutionPreset("1440p 2K QHD", 1440),
    ResolutionPreset("2160p 4K UHD", 2160),
    ResolutionPreset("4096p DCI 4K", 2160),
    ResolutionPreset("4320p 8K UHD", 4320)
  )

  // Greatest Common Divisor
  tailrec fun gcd(a: Int, b: Int): Int {
    return if (b == 0) kotlin.math.abs(a) else gcd(b, a % b)
  }

  // Simplifies any X and Y into lowest terms
  fun simplifyRatio(x: Int, y: Int): Pair<Int, Int> {
    if (x <= 0 || y <= 0) return 1 to 1
    val divisor = gcd(x, y)
    return (x / divisor) to (y / divisor)
  }

  // Macroblock calculation & video dimension safety
  fun calculateVideoDimensions(
    ratioX: Int,
    ratioY: Int,
    baseStandard: Int = 1080,
    isHeightBase: Boolean = true
  ): ComputedDimensions {
    val x = if (ratioX > 0) ratioX else 1
    val y = if (ratioY > 0) ratioY else 1
    val aspect = x.toFloat() / y.toFloat()

    val rawW: Float
    val rawH: Float
    if (isHeightBase) {
      rawH = baseStandard.toFloat()
      rawW = baseStandard.toFloat() * aspect
    } else {
      rawW = baseStandard.toFloat()
      rawH = baseStandard.toFloat() / aspect
    }

    // Video encoders REQUIRE even dimensions (mod 2) for YUV 4:2:0 subsampling,
    // and ideally mod 16 for H.264 macroblock efficiency.
    var finalW = (rawW.roundToInt() + 1) / 2 * 2
    var finalH = (rawH.roundToInt() + 1) / 2 * 2
    if (finalW < 2) finalW = 2
    if (finalH < 2) finalH = 2

    val isMod2 = (finalW % 2 == 0) && (finalH % 2 == 0)
    val isMod16 = (finalW % 16 == 0) && (finalH % 16 == 0)
    val totalPix = finalW.toLong() * finalH.toLong()
    val mp = totalPix.toFloat() / 1_000_000f

    // Standard h.264 / HEVC video bitrate approximation for 30fps high-quality
    val estBitrate = (mp * 3.5f).coerceIn(2.0f, 85.0f)

    val codecRec = when {
      isMod16 -> "Hardware Accelerated (Zero Padding / Peak Encoder Efficiency)"
      isMod2 -> "Safe for H.264/HEVC (Subsampled YUV 4:2:0 OK)"
      else -> "Warning: Odd pixel dimension will fail in hardware encoders"
    }

    return ComputedDimensions(
      width = finalW,
      height = finalH,
      rawWidth = rawW,
      rawHeight = rawH,
      isMod2Safe = isMod2,
      isMod16Safe = isMod16,
      totalPixels = totalPix,
      megapixels = mp,
      estBitrateMbps = estBitrate,
      codecRecommendation = codecRec
    )
  }

  // Generates 30+ clean mathematical fractional ratios (e.g. 5:4, 7:5, 8:5, 11:8, 12:5, 14:9, 15:9, 17:9, 21:10, 24:10, etc.)
  private fun generateMathematicalRatios(): List<AspectRatioItem> {
    val mathRatios = listOf(
      Pair(5, 3) to "5:3 (1.67:1) Japanese Vista",
      Pair(7, 4) to "7:4 (1.75:1) Early Widescreen",
      Pair(11, 8) to "11:8 (1.375:1) 35mm Gate",
      Pair(13, 9) to "13:9 (1.44:1) Broadcaster Transition",
      Pair(14, 9) to "14:9 (1.56:1) BBC Compromise Ratio",
      Pair(15, 9) to "15:9 (1.67:1) Arcade Wide Screen",
      Pair(17, 9) to "17:9 (1.89:1) Digital Cinema Native 4K",
      Pair(19, 10) to "19:10 (1.90:1) Modern Wide Format",
      Pair(21, 10) to "21:10 (2.10:1) Wide Monitor Aspect",
      Pair(24, 10) to "24:10 (2.40:1) Precision Cinema",
      Pair(25, 16) to "25:16 (1.56:1) Square Root Ratio",
      Pair(32, 15) to "32:15 (2.13:1) Video Game HUD Ratio",
      Pair(3, 1) to "3:1 (3.00:1) Panoramic Ribbon",
      Pair(4, 1) to "4:1 (4.00:1) Quad Canvas Triptych",
      Pair(5, 1) to "5:1 (5.00:1) Ultra Skyline",
      Pair(1, 3) to "1:3 (0.33:1) Tall Kiosk Ribbon",
      Pair(1, 4) to "1:4 (0.25:1) Vertical Skyscraper Ad",
      Pair(3, 5) to "3:5 (0.60:1) Tall Phone Feed",
      Pair(4, 7) to "4:7 (0.57:1) Mobile Video Scroll",
      Pair(5, 7) to "5:7 (0.71:1) Portrait Compact",
      Pair(5, 8) to "5:8 (0.625:1) Golden Ratio Inverted",
      Pair(7, 9) to "7:9 (0.78:1) Mid-Portrait",
      Pair(8, 9) to "8:9 (0.89:1) Near Square Feed",
      Pair(9, 8) to "9:8 (1.125:1) Near Square Landscape",
      Pair(10, 7) to "10:7 (1.43:1) Tablet Balanced",
      Pair(11, 9) to "11:9 (1.22:1) Vintage Television",
      Pair(13, 10) to "13:10 (1.30:1) Medium Frame",
      Pair(14, 10) to "14:10 (1.40:1) Compact Widescreen",
      Pair(16, 7) to "16:7 (2.29:1) Ultra Scope",
      Pair(18, 7) to "18:7 (2.57:1) Panorama Strip",
      Pair(20, 7) to "20:7 (2.86:1) Super Cinema Strip",
      Pair(25, 9) to "25:9 (2.78:1) Multi-Display Array"
    )

    return mathRatios.mapIndexed { index, (pair, desc) ->
      AspectRatioItem(
        id = "math_${pair.first}_${pair.second}",
        x = pair.first,
        y = pair.second,
        name = "${pair.first}:${pair.second} Aspect",
        category = RatioCategory.CUSTOM,
        description = desc,
        badge = "Math X:Y"
      )
    }
  }
}
