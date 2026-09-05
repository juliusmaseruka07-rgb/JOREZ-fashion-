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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.model.ProductCategory
import com.example.ui.components.ProductCard
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrightYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueBg
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceSubtle
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.LimeGreenBg
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShopScreen(
    repository: JorezRepository,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val products by repository.products.collectAsState()
    val favorites by repository.favorites.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }
    var selectedDistrict by remember { mutableStateOf("All Uganda") }
    var showOnlyInStock by remember { mutableStateOf(false) }

    val categories = ProductCategory.values()
    val districts = listOf("All Uganda", "Kampala", "Wakiso", "Entebbe", "Jinja", "Mbarara", "Gulu", "Mbale")

    val filteredProducts = products.filter { product ->
        val matchesCategory = selectedCategory == null || product.category == selectedCategory
        val matchesDistrict = selectedDistrict == "All Uganda" || product.district.contains(selectedDistrict, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() || product.name.contains(searchQuery, ignoreCase = true) ||
                product.brandName.contains(searchQuery, ignoreCase = true) ||
                product.description.contains(searchQuery, ignoreCase = true)
        val matchesStock = !showOnlyInStock || product.totalStock > 0

        matchesCategory && matchesDistrict && matchesSearch && matchesStock
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("shop_catalog_screen")
    ) {
        // Shop Title and Search Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .border(0.5.dp, BorderLight)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "JOREZ MARKETPLACE",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                fontSize = 26.sp
                            ),
                            color = TextDark
                        )
                        Text(
                            text = "Authentic Ugandan Streetwear & Local Brands",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondaryDark,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name, brand, hoodie, tee...", color = TextMutedDark, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ElectricBlue) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondaryDark)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("shop_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = LightSurfaceSubtle,
                        focusedBorderColor = BrandPurple,
                        unfocusedBorderColor = BorderLight,
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Categories Horizontal Filter
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        val isAllSelected = selectedCategory == null
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isAllSelected) BrandPurple else PureWhite)
                                .border(1.dp, if (isAllSelected) BrandPurple else BorderLight, RoundedCornerShape(8.dp))
                                .clickable { selectedCategory = null }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "ALL CATEGORIES",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    color = if (isAllSelected) PureWhite else TextDark
                                )
                            )
                        }
                    }

                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) BrandPurple else PureWhite)
                                .border(1.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(8.dp))
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = category.label.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    color = if (isSelected) PureWhite else TextDark
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // District location filter
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(districts) { district ->
                        val isSelected = district == selectedDistrict
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) ElectricBlue else ElectricBlueBg)
                                .border(1.dp, if (isSelected) ElectricBlue else BorderSubtle, RoundedCornerShape(16.dp))
                                .clickable { selectedDistrict = district }
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = district,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) PureWhite else TextDark
                                )
                            )
                        }
                    }
                }
            }
        }

        // Product Count & Filter Status
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredProducts.size} PRODUCTS AVAILABLE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Black,
                        color = TextSecondaryDark
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showOnlyInStock = !showOnlyInStock }
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .border(1.5.dp, if (showOnlyInStock) LimeGreen else BorderLight, RoundedCornerShape(4.dp))
                            .background(if (showOnlyInStock) LimeGreen else PureWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        if (showOnlyInStock) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = PureWhite,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "In Stock Only",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (showOnlyInStock) LimeGreen else TextSecondaryDark,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // 2-Column Product Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                filteredProducts.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { product ->
                            ProductCard(
                                product = product,
                                isFavorite = favorites.contains(product.id),
                                onProductClick = { onProductClick(it.id) },
                                onFavoriteToggle = { repository.toggleFavorite(it.id) },
                                onAddToCart = {
                                    val variant = it.variants.firstOrNull() ?: com.example.model.ProductVariant(color = "Default", size = "Standard", stock = 1)
                                    repository.addToCart(it, variant)
                                },
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

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
