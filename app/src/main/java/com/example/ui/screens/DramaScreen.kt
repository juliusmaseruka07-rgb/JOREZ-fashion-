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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.model.Drama
import com.example.model.DramaEpisode
import com.example.ui.components.BadgePill
import com.example.ui.components.JorezEmblem
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
fun DramaScreen(
    repository: JorezRepository,
    initialDramaId: String? = null,
    modifier: Modifier = Modifier
) {
    val dramas by repository.dramas.collectAsState()
    var selectedDramaId by remember(initialDramaId) {
        mutableStateOf(initialDramaId ?: dramas.firstOrNull()?.id)
    }
    var selectedGenreTab by remember { mutableStateOf("ALL") }
    var isTrailerPlaying by remember { mutableStateOf(false) }

    val genreTabs = listOf("ALL", "TRENDING", "CRIME / THRILLER", "URBAN / FASHION", "MUSIC CULTURE")

    val activeDrama = dramas.find { it.id == selectedDramaId } ?: dramas.firstOrNull()

    val filteredDramas = dramas.filter { drama ->
        if (selectedGenreTab == "ALL" || selectedGenreTab == "TRENDING") true
        else drama.genre.contains(selectedGenreTab.split("/").first().trim(), ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("drama_screen")
    ) {
        // Section Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Movie, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "JOREZ DRAMA",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                ),
                                color = TextDark
                            )
                        }
                        Text(
                            text = "Ugandan Cinema, Short Films & Indie Series",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(BrandPurple, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text("UG CINEMA", fontWeight = FontWeight.Black, fontSize = 10.sp, color = PureWhite)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Genre Tabs
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(genreTabs) { tab ->
                        val isSelected = tab == selectedGenreTab
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) BrandPurple else LightSurfaceSubtle)
                                .border(1.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(8.dp))
                                .clickable { selectedGenreTab = tab }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = tab,
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

        // Active Spotlight / Selected Drama Trailer Player
        if (activeDrama != null) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Trailer Video Player Frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.verticalGradient(
                                    if (activeDrama.backdropGradient.size >= 2) {
                                        activeDrama.backdropGradient.map { Color(it) }
                                    } else {
                                        listOf(BrandPurpleBg, ElectricBlueBg)
                                    }
                                )
                            )
                            .border(1.5.dp, BrandPurple, RoundedCornerShape(14.dp))
                            .clickable { isTrailerPlaying = !isTrailerPlaying },
                        contentAlignment = Alignment.Center
                    ) {
                        JorezEmblem(size = 72.dp)

                        // Play/Pause button
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(PureWhite.copy(alpha = 0.9f), CircleShape)
                                .border(1.5.dp, if (isTrailerPlaying) LimeGreen else BrandPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Trailer",
                                tint = if (isTrailerPlaying) LimeGreen else BrandPurple,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        // Trailer duration & Rating
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(PureWhite.copy(alpha = 0.9f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text("TRAILER • ${activeDrama.trailerDuration}", color = TextDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            BadgePill(activeDrama.rating, BrandPurple, PureWhite)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Title & Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeDrama.title,
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                                color = TextDark
                            )
                            Text(
                                text = "${activeDrama.genre} • ${activeDrama.year}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = BrandPurple, fontWeight = FontWeight.Bold)
                            )
                        }

                        Button(
                            onClick = { repository.toggleFollowDrama(activeDrama.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (activeDrama.isFollowed) LightSurfaceSubtle else BrandPurple,
                                contentColor = if (activeDrama.isFollowed) TextDark else PureWhite
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (activeDrama.isFollowed) "FOLLOWING ✓" else "+ FOLLOW", fontWeight = FontWeight.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Synopsis
                    Text(
                        text = activeDrama.synopsis,
                        style = MaterialTheme.typography.bodyLarge.copy(color = TextSecondaryDark, lineHeight = 22.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cast & Crew
                    Text(
                        text = "DIRECTED BY: ${activeDrama.director}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "STARRING: ${activeDrama.cast.joinToString(", ")}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                    )
                    Text(
                        text = "STUDIO: ${activeDrama.productionCompany}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Episode List
                    Text(
                        text = "EPISODES (${activeDrama.episodes.size})",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        activeDrama.episodes.forEach { ep ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PureWhite)
                                    .border(1.dp, BorderLight, RoundedCornerShape(10.dp))
                                    .clickable { /* Stream episode */ }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(BrandPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = PureWhite, modifier = Modifier.size(22.dp))
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "EP ${ep.number}: ${ep.title}",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = TextDark
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "• ${ep.duration}",
                                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                                        )
                                    }
                                    Text(
                                        text = ep.description,
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Browse All Dramas Carousel / List
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MORE DRAMAS & RELEASES",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(filteredDramas) { drama ->
                        val isSelected = drama.id == selectedDramaId
                        Column(
                            modifier = Modifier
                                .width(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PureWhite)
                                .border(1.5.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(12.dp))
                                .clickable { selectedDramaId = drama.id }
                                .padding(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            if (drama.backdropGradient.size >= 2) {
                                                drama.backdropGradient.map { Color(it) }
                                            } else listOf(BrandPurpleBg, ElectricBlueBg)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                JorezEmblem(size = 40.dp)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = drama.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, fontSize = 13.sp),
                                color = TextDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = drama.genre,
                                style = MaterialTheme.typography.labelSmall.copy(color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${drama.episodes.size} Episodes • ${drama.rating}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark, fontSize = 10.sp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
