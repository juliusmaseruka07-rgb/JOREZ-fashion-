package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.model.Product
import com.example.model.ProductVariant
import com.example.ui.components.BadgePill
import com.example.ui.components.JorezEmblem
import com.example.ui.components.ProductCard
import com.example.ui.components.formatUgx
import com.example.ui.theme.AirtelRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.BrightYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.CoralRedBg
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
fun ProductDetailScreen(
    productId: String,
    repository: JorezRepository,
    onBack: () -> Unit,
    onBrandClick: (String) -> Unit,
    onRelatedProductClick: (String) -> Unit,
    onNavigateToCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val products by repository.products.collectAsState()
    val favorites by repository.favorites.collectAsState()
    val product = products.find { it.id == productId } ?: products.firstOrNull()

    if (product == null) {
        Box(modifier = modifier.fillMaxSize().background(LightBackground), contentAlignment = Alignment.Center) {
            Text("Product not found", color = TextDark)
        }
        return
    }

    val isFavorite = favorites.contains(product.id)

    // Variant selection state
    var selectedColor by remember(product) {
        mutableStateOf(product.availableColors.firstOrNull() ?: "Standard")
    }
    var selectedSize by remember(product) {
        mutableStateOf(product.availableSizes.firstOrNull() ?: "M")
    }
    var selectedQuantity by remember(product) { mutableStateOf(1) }
    var selectedImageIndex by remember(product) { mutableStateOf(0) }
    var showAddedFeedback by remember { mutableStateOf(false) }

    // Find the matching variant to determine precise stock
    val activeVariant = product.variants.find {
        it.color.equals(selectedColor, ignoreCase = true) &&
                (product.availableSizes.isEmpty() || it.size.equals(selectedSize, ignoreCase = true))
    } ?: product.variants.firstOrNull()

    val currentVariantStock = activeVariant?.stock ?: product.totalStock
    val isVariantOutOfStock = currentVariantStock <= 0

    val galleryImages = if (product.images.isNotEmpty()) product.images else listOf(product.mainImage)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("product_detail_screen")
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { repository.toggleFavorite(product.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) CoralRed else TextDark
                        )
                    }
                    IconButton(onClick = { /* Phone native share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = TextDark)
                    }
                }
            }
        }

        // 1. Image Gallery (Main showcase + Thumbnails)
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(BrandPurpleBg, ElectricBlueBg, Color(0xFFF0FDF4))
                            )
                        )
                        .border(1.dp, BorderLight, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    JorezEmblem(size = 130.dp)

                    // Stock status badge on top
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        when {
                            isVariantOutOfStock -> BadgePill("OUT OF STOCK", CoralRed, PureWhite)
                            currentVariantStock <= 5 -> BadgePill("LOW STOCK ($currentVariantStock LEFT)", BrightOrange, PureWhite)
                            else -> BadgePill("IN STOCK ($currentVariantStock AVAILABLE)", LimeGreen, PureWhite)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Thumbnails
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(galleryImages.indices.toList()) { idx ->
                        val isSelected = idx == selectedImageIndex
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PureWhite)
                                .border(2.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(8.dp))
                                .clickable { selectedImageIndex = idx },
                            contentAlignment = Alignment.Center
                        ) {
                            JorezEmblem(size = 28.dp)
                        }
                    }
                }
            }
        }

        // 2. Product Brand & Title
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onBrandClick(product.brandId) }
                ) {
                    Text(
                        text = product.brandName.uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = BrandPurple
                        )
                    )
                    if (product.brandVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = LimeGreen, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp
                    ),
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Rating & Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = BrightYellow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.rating} (${product.reviewsCount} reviews)",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${product.cityTown}, ${product.district}, Uganda",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Price in UGX
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val price = product.discountPriceUgx ?: product.priceUgx
                    Text(
                        text = "UGX ${formatUgx(price)}",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = LimeGreen,
                            fontSize = 28.sp
                        )
                    )
                    if (product.discountPriceUgx != null) {
                        Text(
                            text = "UGX ${formatUgx(product.priceUgx)}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                textDecoration = TextDecoration.LineThrough,
                                color = TextMutedDark
                            )
                        )
                    }
                }
            }
        }

        // 3. Variant Selector (Colors & Sizes)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PureWhite)
                    .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                // Colors
                if (product.availableColors.isNotEmpty()) {
                    Text(
                        text = "AVAILABLE COLORS: ${selectedColor.uppercase()}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = TextDark
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        product.availableColors.forEach { color ->
                            val isSelected = color.equals(selectedColor, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) BrandPurple else LightSurfaceSubtle)
                                    .border(1.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(8.dp))
                                    .clickable { selectedColor = color }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = color,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) PureWhite else TextDark
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Sizes
                if (product.availableSizes.isNotEmpty()) {
                    Text(
                        text = "SELECT SIZE: $selectedSize",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = TextDark
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        product.availableSizes.forEach { size ->
                            val isSelected = size.equals(selectedSize, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) BrandPurple else LightSurfaceSubtle)
                                    .border(1.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(8.dp))
                                    .clickable { selectedSize = size }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = size,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (isSelected) PureWhite else TextDark
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Precise Stock per Variant indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "STOCK FOR $selectedColor / $selectedSize:",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isVariantOutOfStock) "0 PIECES (OUT OF STOCK)" else "$currentVariantStock PIECES AVAILABLE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = if (isVariantOutOfStock) CoralRed else LimeGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quantity Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "QUANTITY",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, color = TextDark)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(LightSurfaceSubtle, RoundedCornerShape(8.dp))
                            .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
                    ) {
                        IconButton(
                            onClick = { if (selectedQuantity > 1) selectedQuantity-- },
                            enabled = selectedQuantity > 1,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = TextDark, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "$selectedQuantity",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark),
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                        IconButton(
                            onClick = { if (selectedQuantity < currentVariantStock) selectedQuantity++ },
                            enabled = selectedQuantity < currentVariantStock,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = TextDark, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // 4. Primary CTA Action Buttons (ADD TO CART, BUY NOW)
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                if (showAddedFeedback) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LimeGreen, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓ ADDED TO YOUR CART", color = PureWhite, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Add to Cart
                    OutlinedButton(
                        onClick = {
                            if (activeVariant != null && !isVariantOutOfStock) {
                                repository.addToCart(product, activeVariant, selectedQuantity)
                                showAddedFeedback = true
                            }
                        },
                        enabled = !isVariantOutOfStock,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = BrandPurple
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, BrandPurple),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("detail_add_to_cart_btn")
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp), tint = BrandPurple)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isVariantOutOfStock) "OUT OF STOCK" else "ADD TO CART", fontWeight = FontWeight.Black)
                    }

                    // Buy Now
                    Button(
                        onClick = {
                            if (activeVariant != null && !isVariantOutOfStock) {
                                repository.addToCart(product, activeVariant, selectedQuantity)
                                onNavigateToCheckout()
                            }
                        },
                        enabled = !isVariantOutOfStock,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LimeGreen,
                            contentColor = PureWhite
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("detail_buy_now_btn")
                    ) {
                        Text(if (isVariantOutOfStock) "SOLD OUT" else "BUY NOW", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    }
                }
            }
        }

        // 5. Description & Delivery Information (Uganda Mobile Money)
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DESCRIPTION",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyLarge.copy(color = TextSecondaryDark, lineHeight = 22.sp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Uganda Delivery & Payment Guarantee Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PureWhite)
                        .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Fast Delivery Across Uganda", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextDark)
                        }
                        Text(
                            text = "Same-day rider delivery in Kampala & Entebbe (UGX 5,000). Upcountry bus parcel delivery to Jinja, Mbarara, Gulu, Mbale in 24-48 hours (UGX 12,000).",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Uganda Mobile Money Protected", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextDark)
                        }
                        Text(
                            text = "Pay seamlessly with MTN MoMo or Airtel Money. Verified merchant escrow protects your funds until delivery confirmation.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                        )
                    }
                }
            }
        }

        // 6. Related Products
        item {
            val related = products.filter { it.id != product.id }.take(3)
            if (related.isNotEmpty()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "RELATED STREETWEAR DROPS",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(related) { rel ->
                            Box(modifier = Modifier.width(180.dp)) {
                                ProductCard(
                                    product = rel,
                                    isFavorite = favorites.contains(rel.id),
                                    onProductClick = { onRelatedProductClick(it.id) },
                                    onFavoriteToggle = { repository.toggleFavorite(it.id) },
                                    onAddToCart = {
                                        val variant = it.variants.firstOrNull() ?: com.example.model.ProductVariant(color = "Default", size = "Standard", stock = 1)
                                        repository.addToCart(it, variant)
                                    }
                                )
                            }
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
