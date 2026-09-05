package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.model.ProductVariant
import com.example.ui.components.JorezEmblem
import com.example.ui.components.ProductCard
import com.example.ui.screens.HomeShortCard
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.BrightYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueBg
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceSubtle
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun BrandProfileScreen(
    brandId: String,
    repository: JorezRepository,
    onBack: () -> Unit,
    onProductClick: (String) -> Unit,
    onVideoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val brands by repository.brands.collectAsState()
    val products by repository.products.collectAsState()
    val videos by repository.videos.collectAsState()
    val posts by repository.brandPosts.collectAsState()
    val favorites by repository.favorites.collectAsState()

    val brand = brands.find { it.id == brandId } ?: brands.firstOrNull()

    if (brand == null) {
        Box(modifier = modifier.fillMaxSize().background(LightBackground), contentAlignment = Alignment.Center) {
            Text("Brand not found", color = TextDark)
        }
        return
    }

    val brandProducts = products.filter { it.brandId == brand.id }
    val brandVideos = videos.filter { it.brandId == brand.id }
    val brandFeedPosts = posts.filter { it.brandId == brand.id }

    var selectedTab by remember { mutableStateOf("PRODUCTS") }
    val tabs = listOf("PRODUCTS", "SHORTS", "POSTS", "ABOUT")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("brand_profile_screen")
    ) {
        // App Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                }
                Text(
                    text = brand.handle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = TextDark
                )
                IconButton(onClick = { /* Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = TextDark)
                }
            }
        }

        // Cover & Avatar Header
        item {
            Column(modifier = Modifier.fillMaxWidth().background(PureWhite)) {
                // Cover
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            Brush.verticalGradient(
                                if (brand.coverGradient.size >= 2) {
                                    brand.coverGradient.map { Color(it) }
                                } else listOf(BrandPurple, ElectricBlue)
                            )
                        )
                )

                // Avatar and Follow row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(PureWhite)
                            .border(3.dp, BrandPurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        JorezEmblem(size = 48.dp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { repository.toggleFollowBrand(brand.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (brand.isFollowed) LightSurfaceSubtle else BrandPurple,
                                contentColor = if (brand.isFollowed) TextDark else PureWhite
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Text(if (brand.isFollowed) "FOLLOWING ✓" else "+ FOLLOW", fontWeight = FontWeight.Black)
                        }

                        // WhatsApp Action
                        Button(
                            onClick = { /* Launch WhatsApp Intent */ },
                            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = "WhatsApp", modifier = Modifier.size(16.dp), tint = PureWhite)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WHATSAPP", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                // Brand details
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = brand.name,
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                            color = TextDark
                        )
                        if (brand.isVerified) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = LimeGreen, modifier = Modifier.size(18.dp))
                        }
                    }

                    Text(
                        text = "${brand.category} • 📍 ${brand.location}",
                        style = MaterialTheme.typography.labelSmall.copy(color = BrandPurple, fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = brand.description,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column {
                            Text("${brand.followersCount}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                            Text("Followers", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                        }
                        Column {
                            Text("${brandProducts.size}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                            Text("Products", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                        }
                        Column {
                            Text("${brandVideos.size}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                            Text("Shorts", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                        }
                    }
                }
            }
        }

        // Profile Tabs (PRODUCTS, SHORTS, POSTS, ABOUT)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .border(1.dp, BorderLight),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == selectedTab
                    Box(
                        modifier = Modifier
                            .clickable { selectedTab = tab }
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                    ) {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = if (isSelected) BrandPurple else TextSecondaryDark
                            )
                        )
                    }
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            "PRODUCTS" -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        brandProducts.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { prod ->
                                    ProductCard(
                                        product = prod,
                                        isFavorite = favorites.contains(prod.id),
                                        onProductClick = { onProductClick(it.id) },
                                        onFavoriteToggle = { repository.toggleFavorite(it.id) },
                                        onAddToCart = {
                                            val v = it.variants.firstOrNull() ?: ProductVariant(color = "Default", size = "Standard", stock = 1)
                                            repository.addToCart(it, v)
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            "SHORTS" -> {
                item {
                    LazyRow(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(brandVideos) { video ->
                            HomeShortCard(video = video, onClick = { onVideoClick(video.id) })
                        }
                    }
                }
            }

            "POSTS" -> {
                items(brandFeedPosts) { post ->
                    Spacer(modifier = Modifier.height(10.dp))
                    DiscoverPostCard(
                        post = post,
                        onLike = { repository.togglePostLike(post.id) },
                        onBrandClick = {},
                        onBuyNow = { if (post.linkedProductId != null) onProductClick(post.linkedProductId) }
                    )
                }
            }

            "ABOUT" -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("ABOUT ${brand.name}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = TextDark)
                        Text(brand.description, style = MaterialTheme.typography.bodyLarge.copy(color = TextSecondaryDark))
                        Text("📞 Phone: ${brand.phone}", color = TextDark, fontWeight = FontWeight.Medium)
                        Text("💬 WhatsApp: ${brand.whatsAppNumber}", color = LimeGreen, fontWeight = FontWeight.Bold)
                        Text("📍 Studio / Workshop: ${brand.location}", color = TextDark, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
