package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.model.Brand
import com.example.model.Drama
import com.example.model.Product
import com.example.model.ShortVideo
import com.example.ui.components.BadgePill
import com.example.ui.components.JorezEmblem
import com.example.ui.components.JorezFullEditorialLogo
import com.example.ui.components.JorezHeaderLogo
import com.example.ui.components.OfflineModeStatusBar
import com.example.ui.components.ProductCard
import com.example.ui.components.formatUgx
import com.example.ui.theme.AirtelRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.BrandPurpleDark
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.BrightOrangeBg
import com.example.ui.theme.BrightYellow
import com.example.ui.theme.BrightYellowBg
import com.example.ui.theme.CoralRed
import com.example.ui.theme.CoralRedBg
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueBg
import com.example.ui.theme.ElectricBlueDark
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceSubtle
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.LimeGreenBg
import com.example.ui.theme.LimeGreenBright
import com.example.ui.theme.MtnTextDark
import com.example.ui.theme.MtnYellow
import com.example.ui.theme.NeonLime
import com.example.ui.theme.OffWhite
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    repository: JorezRepository,
    onNavigateToDiscover: () -> Unit,
    onNavigateToShorts: (String?) -> Unit,
    onNavigateToShop: () -> Unit,
    onNavigateToDrama: (String?) -> Unit,
    onNavigateToBrand: (String) -> Unit,
    onNavigateToProduct: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSell: () -> Unit,
    onNavigateToAdvertise: () -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val products by repository.products.collectAsState()
    val videos by repository.videos.collectAsState()
    val dramas by repository.dramas.collectAsState()
    val brands by repository.brands.collectAsState()
    val cart by repository.cart.collectAsState()
    val favorites by repository.favorites.collectAsState()
    val isOffline by repository.isOfflineMode.collectAsState()
    val ads by repository.activeAds.collectAsState()

    val featuredProducts = products.filter { it.isFeatured }
    val newArrivals = products.filter { it.isNewArrival }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("home_screen_feed")
    ) {
        // 1. HEADER (Logo, Search Bar, Notifications, Favorites, Cart)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .border(0.5.dp, BorderLight)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    JorezHeaderLogo(compact = false)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onNavigateToSearch,
                            modifier = Modifier.testTag("header_search_btn")
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = TextDark)
                        }

                        IconButton(
                            onClick = onNavigateToFavorites,
                            modifier = Modifier.testTag("header_favorites_btn")
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = "Favorites", tint = CoralRed)
                        }

                        BadgedBox(
                            badge = {
                                if (cart.isNotEmpty()) {
                                    Badge(
                                        containerColor = LimeGreen,
                                        contentColor = PureWhite
                                    ) {
                                        Text("${cart.sumOf { it.quantity }}", fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                        ) {
                            IconButton(
                                onClick = onNavigateToCart,
                                modifier = Modifier.testTag("header_cart_btn")
                            ) {
                                Icon(Icons.Default.ShoppingBag, contentDescription = "Cart", tint = ElectricBlue)
                            }
                        }

                        IconButton(
                            onClick = onNavigateToNotifications,
                            modifier = Modifier.testTag("header_notifications_btn")
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = TextDark)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar Trigger
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightSurfaceSubtle)
                        .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                        .clickable { onNavigateToSearch() }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Search Ugandan brands, streetwear, drama...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMutedDark, fontSize = 13.sp)
                    )
                }
            }
        }

        // Offline Mode Banner
        item {
            OfflineModeStatusBar(
                isOffline = isOffline,
                onToggle = { repository.toggleOfflineMode() }
            )
        }

        // 2. HERO CAMPAIGN SECTION (Bright, Vibrant, Engaging)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFFF5F3FF), // BrandPurpleBg
                                Color(0xFFEFF6FF), // ElectricBlueBg
                                Color(0xFFFEF9C3)  // BrightYellowBg
                            )
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.linearGradient(listOf(BrandPurple, ElectricBlue, LimeGreen)),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                // Background visual emblem watermark
                JorezEmblem(
                    modifier = Modifier
                        .size(170.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp),
                    size = 170.dp,
                    showGlow = false
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Badge: OFFLINE MODE / CONNECT
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(PureWhite, RoundedCornerShape(20.dp))
                            .border(1.dp, BrandPurple.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(LimeGreen, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isOffline) "OFFLINE MODE ENABLED" else "UGANDA'S YOUTH CULTURE HUB",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = BrandPurpleDark,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp,
                                letterSpacing = 0.8.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "JOREZ MEDIA",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            fontSize = 32.sp
                        ),
                        color = TextDark
                    )

                    Text(
                        text = "DISCONNECT TO CONNECT",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = BrandPurple,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "BRANDS • DRAMA • CULTURE • COMMERCE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = ElectricBlueDark,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "NO SIGNAL. PURE STYLE.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondaryDark,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onNavigateToDiscover,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_explore_now_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandPurple,
                                contentColor = PureWhite
                            )
                        ) {
                            Text("EXPLORE NOW", fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, fontSize = 12.sp)
                        }

                        Button(
                            onClick = onNavigateToSell,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_start_selling_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LimeGreen,
                                contentColor = PureWhite
                            )
                        ) {
                            Text("START SELLING", fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 2B. TODAY'S DEALS SECTION (Bright Promo Banner & Exclusive Offers)
        item {
            val dealProducts = products.filter { it.discountPriceUgx != null }
            if (dealProducts.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    // Deal Banner Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(BrightYellow, BrightOrange)
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⚡", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "TODAY'S DEALS",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp
                                        ),
                                        color = TextDark
                                    )
                                }
                                Text(
                                    text = "Limited drops • Up to 40% OFF today",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextDark.copy(alpha = 0.85f),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(CoralRed, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "LIVE DEALS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = PureWhite,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(dealProducts) { deal ->
                            ProductCard(
                                product = deal,
                                isFavorite = favorites.contains(deal.id),
                                onProductClick = { onNavigateToProduct(it.id) },
                                onFavoriteToggle = { repository.toggleFavorite(it.id) },
                                onAddToCart = { repository.addToCart(it, it.variants.firstOrNull() ?: com.example.model.ProductVariant(color = "Default", size = "Standard", stock = 1)) },
                                modifier = Modifier.width(190.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. TRENDING SHORTS (TikTok-Style Vertical Videos Carousel)
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TRENDING VIDEOS",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(CoralRed, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("HOT 🔥", color = PureWhite, fontWeight = FontWeight.Black, fontSize = 9.sp)
                            }
                        }
                        Text(
                            text = "Vertical streetwear drops & creator promos",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                        )
                    }
                    Text(
                        text = "WATCH ALL",
                        style = MaterialTheme.typography.labelMedium.copy(color = ElectricBlue, fontWeight = FontWeight.Black),
                        modifier = Modifier.clickable { onNavigateToShorts(null) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(videos) { video ->
                        HomeShortCard(
                            video = video,
                            onClick = { onNavigateToShorts(video.id) }
                        )
                    }
                }
            }
        }

        // 4. FEATURED BRANDS
        item {
            Column(modifier = Modifier.padding(bottom = 20.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "FEATURED BRANDS",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = TextDark
                        )
                        Text(
                            text = "Verified Ugandan designers & streetwear labels",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(brands) { brand ->
                        HomeBrandCard(
                            brand = brand,
                            onBrandClick = { onNavigateToBrand(brand.id) },
                            onFollowToggle = { repository.toggleFollowBrand(brand.id) }
                        )
                    }
                }
            }
        }

        // 5. SHOP TRENDING PRODUCTS
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "POPULAR PRODUCTS",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = TextDark
                        )
                        Text(
                            text = "High-demand Ugandan streetwear & tech",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                        )
                    }
                    Text(
                        text = "VIEW SHOP",
                        style = MaterialTheme.typography.labelMedium.copy(color = BrandPurple, fontWeight = FontWeight.Black),
                        modifier = Modifier.clickable { onNavigateToShop() }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Products Grid (2 columns)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                featuredProducts.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { product ->
                            ProductCard(
                                product = product,
                                isFavorite = favorites.contains(product.id),
                                onProductClick = { onNavigateToProduct(it.id) },
                                onFavoriteToggle = { repository.toggleFavorite(it.id) },
                                onAddToCart = { repository.addToCart(it, it.variants.firstOrNull() ?: com.example.model.ProductVariant(color = "Default", size = "Standard", stock = 1)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // 6. DRAMA SPOTLIGHT (JOREZ DRAMA)
        item {
            val spotlightDrama = dramas.firstOrNull()
            if (spotlightDrama != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "JOREZ DRAMA",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                                    color = TextDark
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .background(BrandPurple, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                    ) {
                                    Text("ORIGINALS", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Black, color = PureWhite))
                                }
                            }
                            Text(
                                text = "Ugandan cinema, series & trailers",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                            )
                        }
                        Text(
                            text = "ALL DRAMAS",
                            style = MaterialTheme.typography.labelMedium.copy(color = ElectricBlue, fontWeight = FontWeight.Black),
                            modifier = Modifier.clickable { onNavigateToDrama(null) }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Spotlight Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFFAF5FF), PureWhite)
                                )
                            )
                            .border(1.5.dp, BrandPurpleLight, RoundedCornerShape(16.dp))
                            .clickable { onNavigateToDrama(spotlightDrama.id) }
                            .padding(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BadgePill("SPOTLIGHT FILM", BrandPurple, PureWhite)
                                Text(
                                    text = "${spotlightDrama.year} • ${spotlightDrama.genre}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondaryDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = spotlightDrama.title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${spotlightDrama.episodes.size} Episodes available to stream offline",
                                style = MaterialTheme.typography.bodySmall.copy(color = BrandPurpleDark, fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = spotlightDrama.synopsis,
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { onNavigateToDrama(spotlightDrama.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp), tint = PureWhite)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("WATCH TRAILER", fontWeight = FontWeight.Black)
                                }
                                OutlinedButton(
                                    onClick = { repository.toggleFollowDrama(spotlightDrama.id) },
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurple),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(if (spotlightDrama.isFollowed) "FOLLOWING ✓" else "+ FOLLOW", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7. NEARBY BUSINESSES & LOCATIONS IN UGANDA
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "NEARBY BUSINESSES & HUBS",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                    color = TextDark
                )
                Text(
                    text = "Discover drops, creatives and storefronts across Uganda",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val hubs = listOf(
                        "📍 Kampala Central" to ElectricBlueBg,
                        "📍 Kololo & Nakasero" to BrandPurpleBg,
                        "📍 Ntinda" to LimeGreenBg,
                        "📍 Jinja Main" to BrightOrangeBg,
                        "📍 Entebbe" to BrightYellowBg,
                        "📍 Mbarara" to ElectricBlueBg,
                        "📍 Gulu" to BrandPurpleBg,
                        "📍 Mbale" to LimeGreenBg
                    )
                    hubs.forEach { (hub, bgColor) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(bgColor)
                                .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                                .clickable { onNavigateToShop() }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = hub,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // 8. FEATURED ADVERTISEMENT BANNER
        item {
            val featuredAd = ads.firstOrNull()
            if (featuredAd != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(BrightOrangeBg, BrightYellowBg)
                            )
                        )
                        .border(1.5.dp, BrightOrange, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BadgePill("FEATURED AD", BrightOrange, PureWhite)
                            Text(
                                text = featuredAd.sellerBrand,
                                style = MaterialTheme.typography.labelSmall.copy(color = BrightOrange, fontWeight = FontWeight.Black)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = featuredAd.headline,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = featuredAd.description,
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onNavigateToShop,
                            colors = ButtonDefaults.buttonColors(containerColor = BrightOrange, contentColor = PureWhite),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(featuredAd.callToAction, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 9. “BECOME A SELLER” CTA
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PureWhite)
                    .border(1.5.dp, LimeGreen, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalMall, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SELL ON JOREZ MEDIA",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = TextDark
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Turn your Ugandan products into a brand people can discover.",
                        style = MaterialTheme.typography.bodyLarge.copy(color = TextDark)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Manage inventory by variant, receive MTN MoMo & Airtel Money payouts directly.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onNavigateToSell,
                        colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Text("START SELLING", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    }
                }
            }
        }

        // 10. “ADVERTISE WITH JOREZ MEDIA” CTA
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PureWhite)
                    .border(1.5.dp, BrandPurple, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Campaign, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "MAKE YOUR BRAND SEEN.",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = TextDark
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Reach Ugandan youth through products, short videos, drama promotions and premium placements.",
                        style = MaterialTheme.typography.bodyLarge.copy(color = TextDark)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onNavigateToAdvertise,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple, contentColor = PureWhite),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Text("CREATE AD CAMPAIGN", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    }
                }
            }
        }

        // 11. FOOTER WITH BRAND ESSENCE
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .border(0.5.dp, BorderLight)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                JorezFullEditorialLogo(modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SUPPORTED PAYMENTS:",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(MtnYellow, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("MTN MoMo", color = MtnTextDark, fontWeight = FontWeight.Black, fontSize = 9.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(AirtelRed, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("Airtel Money", color = PureWhite, fontWeight = FontWeight.Black, fontSize = 9.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "© 2026 JOREZ MEDIA. All rights reserved. Kampala, Uganda.",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMutedDark, fontSize = 10.sp)
                )
                Spacer(modifier = Modifier.height(60.dp)) // padding for bottom bar
            }
        }
    }
}

@Composable
fun HomeShortCard(
    video: ShortVideo,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(135.dp)
            .height(215.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    if (video.videoGradientColors.size >= 2) {
                        video.videoGradientColors.map { Color(it) }
                    } else {
                        listOf(BrandPurple, ElectricBlue)
                    }
                )
            )
            .border(1.5.dp, BorderLight, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        // Play Icon center
        Box(
            modifier = Modifier
                .size(38.dp)
                .align(Alignment.Center)
                .background(PureWhite.copy(alpha = 0.25f), CircleShape)
                .border(1.dp, PureWhite.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = PureWhite, modifier = Modifier.size(22.dp))
        }

        // Views badge top
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(PureWhite.copy(alpha = 0.92f), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Text(
                text = "▶ ${video.views}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = BrandPurpleDark
                )
            )
        }

        // Info bottom
        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = video.brandName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = PureWhite
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = video.caption,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = PureWhite.copy(alpha = 0.9f)
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HomeBrandCard(
    brand: Brand,
    onBrandClick: () -> Unit,
    onFollowToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(PureWhite)
            .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
            .clickable { onBrandClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(BrandPurpleBg)
                .border(2.dp, BrandPurple, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            JorezEmblem(size = 34.dp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = brand.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                ),
                color = TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (brand.isVerified) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = LimeGreen, modifier = Modifier.size(13.dp))
            }
        }

        Text(
            text = "${brand.followersCount} followers",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, color = TextSecondaryDark)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onFollowToggle,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (brand.isFollowed) LimeGreenBg else BrandPurple,
                contentColor = if (brand.isFollowed) LimeGreen else PureWhite
            ),
            modifier = Modifier.fillMaxWidth().height(32.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = if (brand.isFollowed) "FOLLOWING ✓" else "+ FOLLOW",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, fontSize = 11.sp)
            )
        }
    }
}
