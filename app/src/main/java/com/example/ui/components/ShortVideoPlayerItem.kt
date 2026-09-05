package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ShortVideo
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPink
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.NeonLime
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted

@Composable
fun ShortVideoPlayerItem(
    video: ShortVideo,
    onLikeToggle: (ShortVideo) -> Unit,
    onSaveToggle: (ShortVideo) -> Unit,
    onCommentClick: (ShortVideo) -> Unit,
    onShareClick: (ShortVideo) -> Unit,
    onBrandClick: (String) -> Unit,
    onShopProduct: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }

    val bgGradient = if (video.videoGradientColors.size >= 2) {
        video.videoGradientColors.map { Color(it) }
    } else {
        listOf(DeepBlack, Color(0xFF1E1035), DeepBlack)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(bgGradient))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPlaying = !isPlaying
            }
            .testTag("video_item_${video.id}")
    ) {
        // Subtle animated tech circuit aesthetic overlay
        CanvasCircuitBackdrop(modifier = Modifier.fillMaxSize())

        // Play/pause overlay indicator
        AnimatedVisibility(
            visible = !isPlaying,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(DeepBlack.copy(alpha = 0.6f), CircleShape)
                    .border(1.dp, PureWhite.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Paused",
                    tint = PureWhite,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        // Top Header: "JOREZ SHORTS" + Views + Offline indicator
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                JorezEmblem(size = 26.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "JOREZ SHORTS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = PureWhite
                )
            }
            Box(
                modifier = Modifier
                    .background(DeepBlack.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "👁 ${video.views} VIEWS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonLime
                    )
                )
            }
        }

        // Right Action Bar (Like, Comment, Share, Save, Brand Avatar)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Brand Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(DeepBlack)
                    .border(2.dp, ElectricPurple, CircleShape)
                    .clickable { onBrandClick(video.brandId) },
                contentAlignment = Alignment.Center
            ) {
                JorezEmblem(size = 24.dp)
            }

            // Like Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { onLikeToggle(video) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(DeepBlack.copy(alpha = 0.5f), CircleShape)
                        .testTag("video_like_${video.id}")
                ) {
                    Icon(
                        imageVector = if (video.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (video.isLiked) CyberPink else PureWhite,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = formatCount(video.likesCount),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = PureWhite
                    )
                )
            }

            // Comment Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { onCommentClick(video) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(DeepBlack.copy(alpha = 0.5f), CircleShape)
                        .testTag("video_comment_${video.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = PureWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "${video.commentsCount}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = PureWhite
                    )
                )
            }

            // Save Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { onSaveToggle(video) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(DeepBlack.copy(alpha = 0.5f), CircleShape)
                        .testTag("video_save_${video.id}")
                ) {
                    Icon(
                        imageVector = if (video.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (video.isSaved) ElectricBlue else PureWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "${video.savesCount}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = PureWhite
                    )
                )
            }

            // Share Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { onShareClick(video) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(DeepBlack.copy(alpha = 0.5f), CircleShape)
                        .testTag("video_share_${video.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = PureWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Share",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = PureWhite
                    )
                )
            }
        }

        // Bottom Info Area
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 24.dp)
        ) {
            // Brand & Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onBrandClick(video.brandId) }
            ) {
                Text(
                    text = video.brandName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = PureWhite
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = NeonLime,
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "📍 ${video.location}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = ElectricPurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Caption
            Text(
                text = video.caption,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = PureWhite,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Audio track info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = video.musicTrack,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Attached Product / "SHOP THIS PRODUCT" Card
            if (video.attachedProductId != null && video.attachedProductName != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepBlack.copy(alpha = 0.85f))
                        .border(1.dp, NeonLime.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .clickable { onShopProduct(video.attachedProductId) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(CardDark, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = NeonLime,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = video.attachedProductName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = PureWhite
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (video.attachedProductPriceUgx != null) {
                                Text(
                                    text = "UGX ${formatUgx(video.attachedProductPriceUgx)}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NeonLime,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(NeonLime, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "SHOP NOW",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepBlack,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CanvasCircuitBackdrop(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val traceColor = Color(0x15FFFFFF)

        // Diagonal tech guide lines
        drawLine(traceColor, androidx.compose.ui.geometry.Offset(0f, h * 0.3f), androidx.compose.ui.geometry.Offset(w * 0.4f, h * 0.3f), 1.5f)
        drawLine(traceColor, androidx.compose.ui.geometry.Offset(w * 0.4f, h * 0.3f), androidx.compose.ui.geometry.Offset(w * 0.6f, h * 0.38f), 1.5f)
        drawLine(traceColor, androidx.compose.ui.geometry.Offset(w * 0.6f, h * 0.38f), androidx.compose.ui.geometry.Offset(w, h * 0.38f), 1.5f)

        drawLine(traceColor, androidx.compose.ui.geometry.Offset(0f, h * 0.65f), androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.65f), 1.5f)
        drawLine(traceColor, androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.65f), androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.72f), 1.5f)
        drawLine(traceColor, androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.72f), androidx.compose.ui.geometry.Offset(w, h * 0.72f), 1.5f)
    }
}

fun formatCount(count: Int): String {
    return if (count >= 1000) {
        String.format(java.util.Locale.US, "%.1fK", count / 1000.0)
    } else {
        count.toString()
    }
}
