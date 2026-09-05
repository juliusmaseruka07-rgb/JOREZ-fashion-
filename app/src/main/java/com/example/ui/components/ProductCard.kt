package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.BrightYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueBg
import com.example.ui.theme.LightSurfaceSubtle
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductCard(
    product: Product,
    isFavorite: Boolean,
    onProductClick: (Product) -> Unit,
    onFavoriteToggle: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val isOutOfStock = product.totalStock == 0
    val isLowStock = product.totalStock in 1..5
    val hasDiscount = product.discountPriceUgx != null && product.discountPriceUgx < product.priceUgx

    Column(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = BrandPurple.copy(alpha = 0.12f))
            .clip(RoundedCornerShape(16.dp))
            .background(PureWhite)
            .border(1.5.dp, BorderLight, RoundedCornerShape(16.dp))
            .clickable { onProductClick(product) }
            .testTag("product_card_${product.id}")
    ) {
        // Top Image Preview with clean background and accent badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.05f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            LightSurfaceSubtle,
                            BrandPurpleBg.copy(alpha = 0.35f)
                        )
                    )
                )
        ) {
            // Visual decorative emblem
            JorezEmblem(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center),
                size = 60.dp,
                showGlow = true
            )

            // Stock status badges (Bright green for IN STOCK, yellow/orange for LOW STOCK, red/coral for SOLD OUT)
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                when {
                    isOutOfStock -> {
                        BadgePill("SOLD OUT", CoralRed, PureWhite)
                    }
                    isLowStock -> {
                        BadgePill("LOW STOCK (${product.totalStock})", BrightOrange, PureWhite)
                    }
                    else -> {
                        BadgePill("IN STOCK", LimeGreen, PureWhite)
                    }
                }

                if (hasDiscount) {
                    val discountPercent = ((product.priceUgx - product.discountPriceUgx!!) * 100 / product.priceUgx).toInt()
                    BadgePill("-$discountPercent%", CoralRed, PureWhite)
                } else if (product.isNewArrival) {
                    BadgePill("NEW", ElectricBlue, PureWhite)
                }
            }

            // Favorite button
            IconButton(
                onClick = { onFavoriteToggle(product) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(34.dp)
                    .background(PureWhite.copy(alpha = 0.9f), CircleShape)
                    .border(1.dp, BorderLight, CircleShape)
                    .testTag("favorite_button_${product.id}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) CoralRed else TextMutedDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Pieces remaining pill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(PureWhite.copy(alpha = 0.92f), RoundedCornerShape(6.dp))
                    .border(0.5.dp, BorderLight, RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${product.totalStock} pcs remaining",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isLowStock) BrightOrange else TextSecondaryDark
                    )
                )
            }
        }

        // Product Details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Brand Name & Verified
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = product.brandName.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrandPurple
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (product.brandVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified Brand",
                        tint = LimeGreen,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Product Name
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Price in UGX
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val effectivePrice = product.discountPriceUgx ?: product.priceUgx
                Text(
                    text = "UGX ${formatUgx(effectivePrice)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TextDark,
                        fontSize = 14.5.sp
                    )
                )
                if (hasDiscount) {
                    Text(
                        text = "UGX ${formatUgx(product.priceUgx)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = TextMutedDark,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Available Colors & Sizes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Colors
                if (product.availableColors.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        product.availableColors.take(4).forEach { colorName ->
                            val dotColor = when (colorName.lowercase()) {
                                "black", "matte black", "stealth black" -> Color(0xFF1F1F1F)
                                "white" -> PureWhite
                                "purple" -> BrandPurple
                                "lime", "cyber lime" -> LimeGreen
                                "red" -> CoralRed
                                "blue" -> ElectricBlue
                                "yellow" -> BrightYellow
                                "orange" -> BrightOrange
                                else -> Color(0xFF64748B)
                            }
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(dotColor, CircleShape)
                                    .border(1.dp, BorderLight, CircleShape)
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // Sizes
                if (product.availableSizes.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        product.availableSizes.take(3).forEach { size ->
                            Box(
                                modifier = Modifier
                                    .background(LightSurfaceSubtle, RoundedCornerShape(4.dp))
                                    .border(0.5.dp, BorderLight, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = size,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondaryDark
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = ElectricBlue,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "${product.cityTown}, ${product.district}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondaryDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bright Action Button: "SHOP NOW" or "SOLD OUT"
            Button(
                onClick = { onAddToCart(product) },
                enabled = !isOutOfStock,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .testTag("add_to_cart_btn_${product.id}"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOutOfStock) LightSurfaceSubtle else LimeGreen,
                    contentColor = if (isOutOfStock) TextMutedDark else PureWhite
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isOutOfStock) "SOLD OUT" else "SHOP NOW",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}

@Composable
fun BadgePill(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 9.sp,
                letterSpacing = 0.5.sp
            ),
            color = textColor
        )
    }
}

fun formatUgx(amount: Long): String {
    return NumberFormat.getNumberInstance(Locale.US).format(amount)
}
