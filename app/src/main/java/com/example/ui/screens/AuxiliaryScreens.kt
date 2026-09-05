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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.ui.components.BadgePill
import com.example.ui.components.JorezEmblem
import com.example.ui.components.ProductCard
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.BrightYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceSubtle
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun FavoritesScreen(
    repository: JorezRepository,
    onBack: () -> Unit,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val products by repository.products.collectAsState()
    val favorites by repository.favorites.collectAsState()

    val favProducts = products.filter { favorites.contains(it.id) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("favorites_screen")
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("SAVED DROPS (${favProducts.size})", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = TextDark)
                    Text("Your wishlist & favorite Ugandan brands", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                }
            }
        }

        if (favProducts.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = TextMutedDark, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("No saved drops yet", color = TextDark, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text("Tap the heart icon on any streetwear item to save it here.", color = TextSecondaryDark)
                }
            }
        } else {
            items(favProducts) { prod ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PureWhite)
                        .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                        .clickable { onProductClick(prod.id) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(LightSurfaceSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        JorezEmblem(size = 28.dp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(prod.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark), maxLines = 1)
                        Text("UGX ${prod.priceUgx} • ${prod.brandName}", color = LimeGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(onClick = { repository.toggleFavorite(prod.id) }) {
                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = CoralRed)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun NotificationsScreen(
    repository: JorezRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notifications by repository.notifications.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("notifications_screen")
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("NOTIFICATIONS", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = TextDark)
                    Text("Order progress, new drops & alerts", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                }
            }
        }

        items(notifications) { notif ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PureWhite)
                    .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            when (notif.type) {
                                "ORDER" -> LimeGreen
                                "BRAND" -> BrandPurple
                                else -> ElectricBlue
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = PureWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(notif.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                        Text(notif.timeAgo, color = TextSecondaryDark, fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(notif.message, color = TextSecondaryDark, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SearchOverlayScreen(
    repository: JorezRepository,
    onBack: () -> Unit,
    onProductClick: (String) -> Unit,
    onBrandClick: (String) -> Unit,
    onDramaClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val products by repository.products.collectAsState()
    val brands by repository.brands.collectAsState()
    val dramas by repository.dramas.collectAsState()

    var query by remember { mutableStateOf("") }

    val matchedProducts = if (query.isBlank()) emptyList() else products.filter {
        it.name.contains(query, ignoreCase = true) || it.brandName.contains(query, ignoreCase = true)
    }
    val matchedBrands = if (query.isBlank()) emptyList() else brands.filter {
        it.name.contains(query, ignoreCase = true) || it.handle.contains(query, ignoreCase = true)
    }
    val matchedDramas = if (query.isBlank()) emptyList() else dramas.filter {
        it.title.contains(query, ignoreCase = true) || it.genre.contains(query, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("search_overlay_screen")
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                    }

                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search JOREZ catalog...", color = TextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondaryDark)
                                }
                            }
                        }
                    )
                }
            }
        }

        if (query.isBlank()) {
            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("POPULAR SEARCHES IN UGANDA", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                    Spacer(modifier = Modifier.height(10.dp))
                    val suggestions = listOf("Offline Mode Tee", "Kampala Shadows", "Tactical Vest", "Cargo Hoodie", "Entebbe Streetwear", "Jinja Nile Apparel")
                    suggestions.forEach { sug ->
                        Text(
                            text = "🔍 $sug",
                            color = TextDark,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { query = sug }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        } else {
            // Matched Products
            if (matchedProducts.isNotEmpty()) {
                item {
                    Text("PRODUCTS (${matchedProducts.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = LimeGreen), modifier = Modifier.padding(16.dp))
                }
                items(matchedProducts) { prod ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProductClick(prod.id) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(36.dp).background(LightSurfaceSubtle, RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                            JorezEmblem(size = 20.dp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(prod.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                            Text("UGX ${prod.priceUgx} • ${prod.brandName}", color = TextSecondaryDark, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Matched Brands
            if (matchedBrands.isNotEmpty()) {
                item {
                    Text("BRANDS (${matchedBrands.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = BrandPurple), modifier = Modifier.padding(16.dp))
                }
                items(matchedBrands) { brand ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBrandClick(brand.id) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(36.dp).background(BrandPurple, CircleShape), contentAlignment = Alignment.Center) {
                            JorezEmblem(size = 20.dp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(brand.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                            Text(brand.handle, color = TextSecondaryDark, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Matched Dramas
            if (matchedDramas.isNotEmpty()) {
                item {
                    Text("DRAMAS & SERIES (${matchedDramas.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = BrightOrange), modifier = Modifier.padding(16.dp))
                }
                items(matchedDramas) { drama ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDramaClick(drama.id) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(drama.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("(${drama.genre})", color = TextSecondaryDark, fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
