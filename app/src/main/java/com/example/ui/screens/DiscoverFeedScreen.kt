package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
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
import com.example.data.SampleData
import com.example.model.BrandPost
import com.example.ui.components.JorezEmblem
import com.example.ui.components.formatUgx
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiscoverFeedScreen(
    repository: JorezRepository,
    onBrandClick: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onShortsClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val posts by repository.brandPosts.collectAsState()
    var selectedDistrict by remember { mutableStateOf("All Uganda") }
    var selectedCategory by remember { mutableStateOf("All") }

    val filterDistricts = listOf("All Uganda", "Kampala", "Wakiso", "Entebbe", "Jinja", "Mbarara", "Gulu", "Mbale")
    val filterCategories = listOf("All", "Fashion & Streetwear", "Drama & Cinema", "Tech & Gadgets", "Music & Culture")

    val filteredPosts = posts.filter { post ->
        val matchesDistrict = selectedDistrict == "All Uganda" || post.location.contains(selectedDistrict, ignoreCase = true)
        matchesDistrict
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("discover_feed_screen")
    ) {
        // Feed Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "DISCOVER FEED",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = TextDark
                )
                Text(
                    text = "Authentic drops, culture, cinema & streetwear across Uganda",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // District location filter pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterDistricts) { district ->
                        val isSelected = district == selectedDistrict
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) BrandPurple else LightSurfaceSubtle)
                                .border(1.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(20.dp))
                                .clickable { selectedDistrict = district }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = district,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) PureWhite else TextDark
                                )
                            )
                        }
                    }
                }
            }
        }

        // Post Stream
        items(filteredPosts) { post ->
            Spacer(modifier = Modifier.height(10.dp))
            DiscoverPostCard(
                post = post,
                onLike = { repository.togglePostLike(post.id) },
                onBrandClick = { onBrandClick(post.brandId) },
                onBuyNow = {
                    if (post.linkedProductId != null) {
                        onProductClick(post.linkedProductId)
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiscoverPostCard(
    post: BrandPost,
    onLike: () -> Unit,
    onBrandClick: () -> Unit,
    onBuyNow: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PureWhite)
            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
    ) {
        // Brand Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onBrandClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BrandPurpleBg)
                        .border(1.5.dp, BrandPurpleLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    JorezEmblem(size = 22.dp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.brandName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = TextDark
                        )
                        if (post.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = LimeGreen, modifier = Modifier.size(14.dp))
                        }
                    }
                    Text(
                        text = "📍 ${post.location} • ${post.timeAgo}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark, fontSize = 11.sp)
                    )
                }
            }

            OutlinedButton(
                onClick = onBrandClick,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurple),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text("VISIT BRAND", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Black))
            }
        }

        // Post Visual Media Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .background(
                    Brush.verticalGradient(
                        if (post.gradientColors.size >= 2) {
                            post.gradientColors.map { Color(it) }
                        } else {
                            listOf(BrandPurpleBg, ElectricBlueBg)
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            JorezEmblem(size = 110.dp)

            // Price overlay if linked product
            if (post.linkedPriceUgx != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PureWhite)
                        .border(1.dp, LimeGreen, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "UGX ${formatUgx(post.linkedPriceUgx)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = LimeGreen,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        // Caption & Tags
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                post.tags.forEach { tag ->
                    Text(
                        text = "#$tag",
                        style = MaterialTheme.typography.labelSmall.copy(color = ElectricBlue, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row: Like, Comment, Share, Save, Buy Now
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Like
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onLike() }
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) CoralRed else TextDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${post.likesCount}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (post.isLiked) CoralRed else TextDark,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Comment
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comments", tint = TextDark, modifier = Modifier.size(19.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${post.commentsCount}", style = MaterialTheme.typography.labelSmall.copy(color = TextDark))
                    }

                    // Share
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = TextDark, modifier = Modifier.size(19.dp))
                }

                // Buy Now CTA if linked
                if (post.linkedProductId != null) {
                    Button(
                        onClick = onBuyNow,
                        colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(14.dp), tint = PureWhite)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("BUY NOW", fontWeight = FontWeight.Black, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
