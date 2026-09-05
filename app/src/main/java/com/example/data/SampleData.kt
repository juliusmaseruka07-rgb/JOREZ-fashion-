package com.example.data

import com.example.model.AdPackage
import com.example.model.AdPlacement
import com.example.model.AdType
import com.example.model.Advertisement
import com.example.model.Brand
import com.example.model.BrandPost
import com.example.model.ClothingType
import com.example.model.Drama
import com.example.model.DramaEpisode
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.ProductCondition
import com.example.model.ProductVariant
import com.example.model.ShortVideo

object SampleData {

    val brands = listOf(
        Brand(
            id = "brand-offline",
            name = "OFFLINE MODE ENABLED",
            handle = "@offlinemodeenabled",
            isVerified = true,
            description = "High-concept Ugandan streetwear. Disconnect to Connect. No signal, pure style.",
            category = "Streetwear & Fashion",
            location = "Kampala Central, Uganda",
            phone = "+256 772 890123",
            whatsAppNumber = "+256 772 890123",
            logoUrl = "jorez_brand_1",
            coverGradient = listOf(0xFF0F0F12, 0xFF1E1035, 0xFF0A0A0C),
            followersCount = 14200,
            productsCount = 12,
            videosCount = 18
        ),
        Brand(
            id = "brand-kampala-drip",
            name = "KAMPALA DRIP CO.",
            handle = "@kampaladrip",
            isVerified = true,
            description = "Urban heavy cotton hoodies, tracksuits & techwear crafted in downtown Kampala.",
            category = "Clothing & Accessories",
            location = "Kikuubo / Nakasero, Kampala",
            phone = "+256 701 456789",
            whatsAppNumber = "+256 701 456789",
            logoUrl = "jorez_brand_2",
            coverGradient = listOf(0xFF141416, 0xFF132A13, 0xFF0D0D0D),
            followersCount = 9800,
            productsCount = 9,
            videosCount = 14
        ),
        Brand(
            id = "brand-nile-threads",
            name = "NILE APPAREL UG",
            handle = "@nileapparel",
            isVerified = true,
            description = "Handcrafted graphic apparel celebrating contemporary East African street art.",
            category = "Fashion",
            location = "Jinja Road, Kampala",
            phone = "+256 788 332211",
            whatsAppNumber = "+256 788 332211",
            logoUrl = "jorez_brand_3",
            coverGradient = listOf(0xFF1A1A24, 0xFF0B3B4B, 0xFF0E0E12),
            followersCount = 7600,
            productsCount = 7,
            videosCount = 8
        ),
        Brand(
            id = "brand-cinemaug",
            name = "UG DIGITAL CINEMA",
            handle = "@ugdigitalcinema",
            isVerified = true,
            description = "Promoting new-age Ugandan dramas, short films, and high-octane indie cinematic stories.",
            category = "Drama & Entertainment",
            location = "Entebbe, Uganda",
            phone = "+256 752 998877",
            whatsAppNumber = "+256 752 998877",
            logoUrl = "jorez_brand_4",
            coverGradient = listOf(0xFF280B1E, 0xFF140810, 0xFF090909),
            followersCount = 22400,
            productsCount = 3,
            videosCount = 35
        )
    )

    val products = listOf(
        Product(
            id = "prod-1",
            name = "JOREZ OFFLINE MODE TEE",
            brandId = "brand-offline",
            brandName = "OFFLINE MODE ENABLED",
            brandVerified = true,
            description = "Heavyweight 280GSM pure combed cotton tee. High-density screenprint featuring circuit board cross motif and 'DISCONNECT TO CONNECT' typography across the back.",
            category = ProductCategory.CLOTHING,
            clothingType = ClothingType.TSHIRT,
            condition = ProductCondition.NEW,
            priceUgx = 45000,
            discountPriceUgx = 35000,
            mainImage = "tee_black",
            images = listOf("tee_black", "tee_white", "tee_detail", "tee_model"),
            variants = listOf(
                ProductVariant(color = "Black", size = "S", stock = 3),
                ProductVariant(color = "Black", size = "M", stock = 5),
                ProductVariant(color = "Black", size = "L", stock = 4),
                ProductVariant(color = "Black", size = "XL", stock = 2),
                ProductVariant(color = "White", size = "M", stock = 4),
                ProductVariant(color = "White", size = "L", stock = 3)
            ),
            availableColors = listOf("Black", "White"),
            availableSizes = listOf("S", "M", "L", "XL"),
            district = "Kampala",
            cityTown = "Kampala Central",
            rating = 4.9f,
            reviewsCount = 48,
            isFeatured = true,
            isNewArrival = true
        ),
        Product(
            id = "prod-2",
            name = "KAMPALA CIRCUIT CARGO HOODIE",
            brandId = "brand-offline",
            brandName = "OFFLINE MODE ENABLED",
            brandVerified = true,
            description = "Drop-shoulder relaxed streetwear hoodie with dual tactical sleeve pouches and reflective neon cyber piping. Designed for chilly Kampala nights.",
            category = ProductCategory.CLOTHING,
            clothingType = ClothingType.HOODIE,
            condition = ProductCondition.NEW,
            priceUgx = 95000,
            discountPriceUgx = 85000,
            mainImage = "hoodie_black",
            images = listOf("hoodie_black", "hoodie_neon", "hoodie_back"),
            variants = listOf(
                ProductVariant(color = "Black", size = "M", stock = 4),
                ProductVariant(color = "Black", size = "L", stock = 6),
                ProductVariant(color = "Purple", size = "M", stock = 2),
                ProductVariant(color = "Purple", size = "L", stock = 3)
            ),
            availableColors = listOf("Black", "Purple"),
            availableSizes = listOf("M", "L", "XL"),
            district = "Kampala",
            cityTown = "Ntinda, Kampala",
            rating = 5.0f,
            reviewsCount = 31,
            isFeatured = true,
            isNewArrival = true
        ),
        Product(
            id = "prod-3",
            name = "TACTICAL CYBER STREET VEST",
            brandId = "brand-kampala-drip",
            brandName = "KAMPALA DRIP CO.",
            brandVerified = true,
            description = "Multi-pocket ballistic nylon streetwear utility vest with detachable chest rig and matte black D-rings. Water-resistant fabric.",
            category = ProductCategory.CLOTHING,
            clothingType = ClothingType.JACKET,
            condition = ProductCondition.NEW,
            priceUgx = 110000,
            discountPriceUgx = 95000,
            mainImage = "vest_tactical",
            images = listOf("vest_tactical", "vest_detail"),
            variants = listOf(
                ProductVariant(color = "Matte Black", size = "M", stock = 2),
                ProductVariant(color = "Matte Black", size = "L", stock = 3),
                ProductVariant(color = "Olive Green", size = "L", stock = 2)
            ),
            availableColors = listOf("Matte Black", "Olive Green"),
            availableSizes = listOf("M", "L"),
            district = "Kampala",
            cityTown = "Kikuubo, Kampala",
            rating = 4.7f,
            reviewsCount = 19,
            isFeatured = true,
            isNewArrival = false
        ),
        Product(
            id = "prod-4",
            name = "PEARL HIGH-TOP STREET SNEAKERS",
            brandId = "brand-kampala-drip",
            brandName = "KAMPALA DRIP CO.",
            brandVerified = true,
            description = "Hand-assembled vulcanized rubber high-tops with contrast zig-zag stitching and reinforced heel cup. Durable for urban walking.",
            category = ProductCategory.SHOES,
            condition = ProductCondition.NEW,
            priceUgx = 145000,
            discountPriceUgx = 130000,
            mainImage = "sneakers_street",
            images = listOf("sneakers_street", "sneakers_side"),
            variants = listOf(
                ProductVariant(color = "Black & White", size = "41", stock = 2),
                ProductVariant(color = "Black & White", size = "42", stock = 4),
                ProductVariant(color = "Black & White", size = "43", stock = 1),
                ProductVariant(color = "Black & White", size = "44", stock = 2)
            ),
            availableColors = listOf("Black & White"),
            availableSizes = listOf("41", "42", "43", "44"),
            district = "Wakiso",
            cityTown = "Entebbe Town",
            rating = 4.8f,
            reviewsCount = 22,
            isFeatured = true,
            isNewArrival = true
        ),
        Product(
            id = "prod-5",
            name = "JOREZ MATRIX CROSSBODY BAG",
            brandId = "brand-offline",
            brandName = "OFFLINE MODE ENABLED",
            brandVerified = true,
            description = "Compact Cordura nylon sling pack with RFID-shielded phone compartment, YKK waterproof zippers, and laser-etched metal hardware.",
            category = ProductCategory.BAGS,
            condition = ProductCondition.NEW,
            priceUgx = 55000,
            discountPriceUgx = 48000,
            mainImage = "bag_crossbody",
            images = listOf("bag_crossbody", "bag_inside"),
            variants = listOf(
                ProductVariant(color = "Stealth Black", size = "One Size", stock = 7),
                ProductVariant(color = "Cyber Lime", size = "One Size", stock = 3)
            ),
            availableColors = listOf("Stealth Black", "Cyber Lime"),
            availableSizes = listOf("One Size"),
            district = "Kampala",
            cityTown = "Kololo, Kampala",
            rating = 4.9f,
            reviewsCount = 17,
            isFeatured = false,
            isNewArrival = true
        ),
        Product(
            id = "prod-6",
            name = "NILE BREEZE OVERSIZED GRAPHIC SHIRT",
            brandId = "brand-nile-threads",
            brandName = "NILE APPAREL UG",
            brandVerified = true,
            description = "Silky rayon camp-collar button-down showcasing stylized Kampala architectural geometry and source-of-the-Nile wave motifs.",
            category = ProductCategory.CLOTHING,
            clothingType = ClothingType.SHIRT,
            condition = ProductCondition.NEW,
            priceUgx = 65000,
            discountPriceUgx = 55000,
            mainImage = "shirt_graphic",
            images = listOf("shirt_graphic", "shirt_pattern"),
            variants = listOf(
                ProductVariant(color = "Monochrome", size = "M", stock = 3),
                ProductVariant(color = "Monochrome", size = "L", stock = 5),
                ProductVariant(color = "Monochrome", size = "XL", stock = 2)
            ),
            availableColors = listOf("Monochrome"),
            availableSizes = listOf("M", "L", "XL"),
            district = "Jinja",
            cityTown = "Main Street, Jinja",
            rating = 4.6f,
            reviewsCount = 14,
            isFeatured = false,
            isNewArrival = false
        )
    )

    val shortVideos = listOf(
        ShortVideo(
            id = "video-1",
            brandId = "brand-offline",
            brandName = "OFFLINE MODE ENABLED",
            brandHandle = "@offlinemodeenabled",
            brandAvatar = "jorez_brand_1",
            caption = "NO SIGNAL. PURE STYLE. The 2026 Black Tee drop is officially live in Kampala. Tap below to cop yours before it sells out! 🔥⚡ #JOREZ #StreetwearUG",
            videoGradientColors = listOf(0xFF0F0A1C, 0xFF3B125C, 0xFF050505),
            musicTrack = "Afrobeats Drills - Kampala Night Rider (Remix)",
            likesCount = 3840,
            isLiked = false,
            commentsCount = 214,
            savesCount = 680,
            attachedProductId = "prod-1",
            attachedProductName = "JOREZ OFFLINE MODE TEE",
            attachedProductPriceUgx = 35000,
            location = "Kampala Central, Uganda",
            tags = listOf("Streetwear", "KampalaFashion", "NoSignal"),
            views = "45.2K"
        ),
        ShortVideo(
            id = "video-2",
            brandId = "brand-offline",
            brandName = "OFFLINE MODE ENABLED",
            brandHandle = "@offlinemodeenabled",
            brandAvatar = "jorez_brand_1",
            caption = "Heavyweight cargo hoodie fitting check. Heavy 450GSM french terry fabric with water-repellent finish. 📍 Available now in Ntinda & Central.",
            videoGradientColors = listOf(0xFF14141E, 0xFF1D283A, 0xFF0D0D12),
            musicTrack = "UG HipHop Beat - 808 Pulse",
            likesCount = 2920,
            isLiked = false,
            commentsCount = 140,
            savesCount = 490,
            attachedProductId = "prod-2",
            attachedProductName = "KAMPALA CIRCUIT CARGO HOODIE",
            attachedProductPriceUgx = 85000,
            location = "Ntinda, Kampala",
            tags = listOf("Hoodie", "TechwearUG", "KampalaDrip"),
            views = "32.8K"
        ),
        ShortVideo(
            id = "video-3",
            brandId = "brand-cinemaug",
            brandName = "UG DIGITAL CINEMA",
            brandHandle = "@ugdigitalcinema",
            brandAvatar = "jorez_brand_4",
            caption = "🎬 KAMPALA SHADOWS | Episode 4 Teaser! When the signal drops, the real game begins. Streaming on JOREZ Drama this Friday!",
            videoGradientColors = listOf(0xFF2E091B, 0xFF581530, 0xFF0A0508),
            musicTrack = "Kampala Shadows - Official Theme Orchestration",
            likesCount = 5120,
            isLiked = true,
            commentsCount = 398,
            savesCount = 1120,
            attachedProductId = null,
            attachedProductName = null,
            attachedProductPriceUgx = null,
            location = "Kololo Heights, Kampala",
            tags = listOf("UgandanDrama", "CinemaUG", "KampalaShadows"),
            views = "89.4K"
        ),
        ShortVideo(
            id = "video-4",
            brandId = "brand-kampala-drip",
            brandName = "KAMPALA DRIP CO.",
            brandHandle = "@kampaladrip",
            brandAvatar = "jorez_brand_2",
            caption = "Tactical rig unboxing. Built for urban creators, videographers & street style pioneers. Order with MTN or Airtel Money! 📦",
            videoGradientColors = listOf(0xFF0F1A12, 0xFF1B3D23, 0xFF090D0A),
            musicTrack = "Kampala Underground Bass",
            likesCount = 1890,
            isLiked = false,
            commentsCount = 82,
            savesCount = 310,
            attachedProductId = "prod-3",
            attachedProductName = "TACTICAL CYBER STREET VEST",
            attachedProductPriceUgx = 95000,
            location = "Kikuubo, Kampala",
            tags = listOf("UtilityVest", "Streetwear", "Kampala"),
            views = "19.1K"
        )
    )

    val dramas = listOf(
        Drama(
            id = "drama-1",
            title = "KAMPALA SHADOWS",
            genre = "Crime / Cyber Thriller",
            year = 2026,
            rating = "16+",
            posterUrl = "drama_shadows",
            backdropGradient = listOf(0xFF1E0E2E, 0xFF35124A, 0xFF0A060F),
            synopsis = "In a near-future Kampala where high-frequency digital trading meets subterranean street politics, an underground coder and a streetwear designer uncover a nationwide telecommunications blackout conspiracy.",
            director = "Kato Ronald & Sarah Namubiru",
            cast = listOf("Julius Maseruka", "Brenda Nalubega", "Denis Okello", "Amina Mugerwa"),
            productionCompany = "Pearl Cyber Studios & JOREZ Media",
            episodes = listOf(
                DramaEpisode(1, "Zero Signal", "44 min", "A sudden power outage paralyzes the capital's communication towers."),
                DramaEpisode(2, "Subterranean Threads", "48 min", "A courier discovers encrypted data hidden inside streetwear QR tags."),
                DramaEpisode(3, "Boda Boda Midnight", "42 min", "High-speed chase across the Northern Bypass under rain."),
                DramaEpisode(4, "The Disconnect Protocol", "51 min", "The mastermind behind the blackout makes their demand.")
            ),
            isFollowed = true,
            trailerDuration = "2:34"
        ),
        Drama(
            id = "drama-2",
            title = "NILE REIGN: THE DYNASTY",
            genre = "Urban Drama / Fashion Empire",
            year = 2026,
            rating = "13+",
            posterUrl = "drama_nile",
            backdropGradient = listOf(0xFF0A1E24, 0xFF123E4A, 0xFF060D10),
            synopsis = "Two rival Ugandan fashion houses in Jinja and Kampala battle for dominance over East Africa's runway and the lucrative street-commerce export market.",
            director = "Patricia Kigozi",
            cast = listOf("Grace Aketch", "Moses Mukasa", "Eunice Namaganda"),
            productionCompany = "Victoria Film Guild",
            episodes = listOf(
                DramaEpisode(1, "First Stitch", "40 min", "The Jinja warehouse fire threatens the spring showcase."),
                DramaEpisode(2, "Catwalk & Concrete", "45 min", "Rival influencers clash backstage at Kampala Fashion Week.")
            ),
            isFollowed = false,
            trailerDuration = "1:58"
        ),
        Drama(
            id = "drama-3",
            title = "MATATU SOUNDS",
            genre = "Music / Youth Culture",
            year = 2025,
            rating = "All Ages",
            posterUrl = "drama_matatu",
            backdropGradient = listOf(0xFF26190A, 0xFF4A2B0E, 0xFF0E0A06),
            synopsis = "The journey of three teenage sound producers turning everyday taxi horn melodies and street slang into the biggest afrobeats movement in East Africa.",
            director = "Brian Ssebagala",
            cast = listOf("Timothy Ochieng", "Lillian Nakato", "Joel Kafeero"),
            productionCompany = "Urban Wave Media",
            episodes = listOf(
                DramaEpisode(1, "Downtown Beats", "36 min", "Recording vocals inside a parked coaster on Old Taxi Park."),
                DramaEpisode(2, "Viral Wave", "39 min", "A TikTok trend catapults the track onto national radio.")
            ),
            isFollowed = false,
            trailerDuration = "2:10"
        )
    )

    val brandPosts = listOf(
        BrandPost(
            id = "post-1",
            brandId = "brand-offline",
            brandName = "OFFLINE MODE ENABLED",
            brandHandle = "@offlinemodeenabled",
            location = "Kampala Central, Uganda",
            caption = "DROP 01: 'NO SIGNAL. PURE STYLE.' Engineered for urban rebellion and pure aesthetics. Available in Black and White sizes S to XL. Stock is limited.",
            gradientColors = listOf(0xFF140D24, 0xFF351659, 0xFF0A0712),
            tags = listOf("Streetwear", "KampalaFashion", "NoSignal", "CyberUganda"),
            timeAgo = "1h ago",
            linkedProductId = "prod-1",
            linkedPriceUgx = 35000,
            likesCount = 890,
            commentsCount = 64
        ),
        BrandPost(
            id = "post-2",
            brandId = "brand-kampala-drip",
            brandName = "KAMPALA DRIP CO.",
            brandHandle = "@kampaladrip",
            location = "Kikuubo / Nakasero, Kampala",
            caption = "Tactical utility vests just restocked at our downtown hub. Fits laptops up to 13\", phones, passports, and notebooks. Delivery available countrywide via MTN MoMo!",
            gradientColors = listOf(0xFF0C1910, 0xFF193D22, 0xFF070E09),
            tags = listOf("UtilityWear", "KampalaTech", "UgandaDrops"),
            timeAgo = "3h ago",
            linkedProductId = "prod-3",
            linkedPriceUgx = 95000,
            likesCount = 540,
            commentsCount = 38
        ),
        BrandPost(
            id = "post-3",
            brandId = "brand-cinemaug",
            brandName = "UG DIGITAL CINEMA",
            brandHandle = "@ugdigitalcinema",
            location = "Entebbe, Uganda",
            caption = "Official behind-the-scenes still from Episode 4 of Kampala Shadows. Huge shoutout to the JOREZ styling team for the wardrobe!",
            gradientColors = listOf(0xFF260D1E, 0xFF4E163B, 0xFF0E060B),
            tags = listOf("KampalaShadows", "CinemaUG", "BehindTheScenes"),
            timeAgo = "5h ago",
            linkedProductId = null,
            linkedPriceUgx = null,
            likesCount = 1420,
            commentsCount = 112
        )
    )

    val adPackages = listOf(
        AdPackage(
            id = "pkg-basic",
            name = "BASIC REACH",
            durationDays = 3,
            priceUgx = 50000,
            estimatedViews = "10K - 25K Impressions",
            features = listOf(
                "Discover Stream Placement",
                "Product & Brand Tagging",
                "Basic Performance Analytics",
                "Kampala & Surrounding Districts"
            ),
            badge = null
        ),
        AdPackage(
            id = "pkg-standard",
            name = "STANDARD BOOST",
            durationDays = 7,
            priceUgx = 120000,
            estimatedViews = "40K - 90K Impressions",
            features = listOf(
                "Home Page Banner + Discover Stream",
                "JOREZ Shorts In-Feed Video Ad",
                "Location Target (All UG Regions)",
                "Click-to-WhatsApp Direct Link"
            ),
            badge = "POPULAR"
        ),
        AdPackage(
            id = "pkg-featured",
            name = "FEATURED MEDIA TAKEOVER",
            durationDays = 14,
            priceUgx = 250000,
            estimatedViews = "120K - 250K Impressions",
            features = listOf(
                "Hero Campaign Banner Rotation",
                "Top Shorts Recommendation",
                "Featured Badge on Brand Profile",
                "Dedicated Push Notification to Followers",
                "Detailed Conversion & Sales Analytics"
            ),
            badge = "RECOMMENDED"
        ),
        AdPackage(
            id = "pkg-premium",
            name = "PREMIUM ENTERPRISE / DRAMA SPOTLIGHT",
            durationDays = 30,
            priceUgx = 550000,
            estimatedViews = "350K+ Impressions",
            features = listOf(
                "All Surface Placements (Home, Shorts, Drama, Shop)",
                "Exclusive Drama Section Sponsorship",
                "Priority Review & Verification",
                "Custom Video Ad Production Support",
                "Dedicated Account Manager"
            ),
            badge = "ENTERPRISE"
        )
    )

    val activeAds = listOf(
        Advertisement(
            id = "ad-1",
            sellerBrand = "OFFLINE MODE ENABLED",
            adType = AdType.PRODUCT,
            headline = "DISCONNECT TO CONNECT • THE 2026 DROP",
            description = "Experience pure Ugandan streetwear minimalism. Shop the Offline Mode tee today.",
            placement = AdPlacement.HOME,
            durationDays = 14,
            totalCostUgx = 250000,
            callToAction = "SHOP NOW",
            targetDistrict = "Kampala",
            isApproved = true,
            status = "ACTIVE",
            impressions = 48500,
            clicks = 3420
        ),
        Advertisement(
            id = "ad-2",
            sellerBrand = "UG DIGITAL CINEMA",
            adType = AdType.DRAMA,
            headline = "WATCH KAMPALA SHADOWS EPISODE 4",
            description = "The cyber thriller gripping Uganda. Watch the trailer now on JOREZ Drama.",
            placement = AdPlacement.DRAMA,
            durationDays = 7,
            totalCostUgx = 120000,
            callToAction = "WATCH TRAILER",
            targetDistrict = "All Uganda",
            isApproved = true,
            status = "ACTIVE",
            impressions = 62100,
            clicks = 5890
        )
    )

    val sampleComments = listOf(
        com.example.model.Comment(userName = "Denis K.", userHandle = "@deniskampala", text = "The fabric quality on this tee is insane! Bought mine yesterday via MTN MoMo, arrived in Ntinda in 2 hours.", timeAgo = "45m ago", likes = 18),
        com.example.model.Comment(userName = "Joanita N.", userHandle = "@joanitan", text = "Disconnect to connect! Proudly Ugandan streetwear taking over 🔥", timeAgo = "2h ago", likes = 29),
        com.example.model.Comment(userName = "Mugisha Alex", userHandle = "@alexmugisha", text = "Is the purple colorway still in stock in size Large?", timeAgo = "3h ago", likes = 8)
    )

    val ugandaDistricts = listOf(
        "Kampala", "Wakiso", "Entebbe", "Mukono", "Jinja", 
        "Mbarara", "Gulu", "Mbale", "Masaka", "Fort Portal", "Arua", "Lira", "Kabale"
    )
}
