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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.data.SampleData
import com.example.model.Comment
import com.example.model.ShortVideo
import com.example.ui.components.ShortVideoPlayerItem
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightSurfaceSubtle
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextSecondaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortsScreen(
    repository: JorezRepository,
    initialVideoId: String? = null,
    onBrandClick: (String) -> Unit,
    onShopProduct: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val videos by repository.videos.collectAsState()
    val initialIndex = remember(initialVideoId, videos) {
        val idx = videos.indexOfFirst { it.id == initialVideoId }
        if (idx >= 0) idx else 0
    }

    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { videos.size }
    )

    var activeCommentVideo by remember { mutableStateOf<ShortVideo?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBlack)
            .testTag("shorts_screen_pager")
    ) {
        if (videos.isNotEmpty()) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val video = videos[page]
                ShortVideoPlayerItem(
                    video = video,
                    onLikeToggle = { repository.toggleVideoLike(video.id) },
                    onSaveToggle = { repository.toggleVideoSave(video.id) },
                    onCommentClick = { activeCommentVideo = video },
                    onShareClick = { /* phone share trigger */ },
                    onBrandClick = onBrandClick,
                    onShopProduct = onShopProduct,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No videos available", color = PureWhite)
            }
        }

        // Comments Bottom Sheet
        if (activeCommentVideo != null) {
            ModalBottomSheet(
                onDismissRequest = { activeCommentVideo = null },
                sheetState = sheetState,
                containerColor = PureWhite
            ) {
                CommentsSheetContent(
                    video = activeCommentVideo!!,
                    onClose = { activeCommentVideo = null }
                )
            }
        }
    }
}

@Composable
fun CommentsSheetContent(
    video: ShortVideo,
    onClose: () -> Unit
) {
    var comments by remember { mutableStateOf(SampleData.sampleComments) }
    var newCommentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(480.dp)
            .background(PureWhite)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Comments (${comments.size})",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = TextDark
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextDark)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(comments) { comment ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(BrandPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = comment.userName.take(1),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = comment.userName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = comment.timeAgo,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondaryDark,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = comment.text,
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextDark)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = TextSecondaryDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${comment.likes}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                color = TextSecondaryDark
                            )
                        )
                    }
                }
            }
        }

        // Add Comment Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newCommentText,
                onValueChange = { newCommentText = it },
                placeholder = { Text("Add a comment...", color = TextSecondaryDark) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPurple,
                    unfocusedBorderColor = BorderLight,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark,
                    focusedContainerColor = LightSurfaceSubtle,
                    unfocusedContainerColor = LightSurfaceSubtle
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (newCommentText.isNotBlank()) {
                        comments = comments + Comment(
                            userName = "You",
                            userHandle = "@current_user",
                            text = newCommentText.trim(),
                            timeAgo = "Just now"
                        )
                        newCommentText = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .background(LimeGreen, CircleShape)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = PureWhite, modifier = Modifier.size(20.dp))
            }
        }
    }
}
