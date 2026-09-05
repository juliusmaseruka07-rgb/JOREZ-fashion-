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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.model.ClothingType
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.ProductCondition
import com.example.model.ProductVariant
import com.example.model.ShortVideo
import com.example.ui.components.BadgePill
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SellerDashboardScreen(
    repository: JorezRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val products by repository.products.collectAsState()
    val orders by repository.orders.collectAsState()
    val videos by repository.videos.collectAsState()

    var activeTab by remember { mutableStateOf("PRODUCTS") }
    val tabs = listOf("PRODUCTS", "ORDERS", "ANALYTICS", "+ ADD PRODUCT", "+ UPLOAD VIDEO")

    // Add Product Form State
    var newProductName by remember { mutableStateOf("") }
    var newProductDesc by remember { mutableStateOf("") }
    var newProductPrice by remember { mutableStateOf("") }
    var newProductDiscount by remember { mutableStateOf("") }
    var newProductColor by remember { mutableStateOf("Black") }
    var newProductSize by remember { mutableStateOf("M") }
    var newProductStock by remember { mutableStateOf("10") }
    var selectedCondition by remember { mutableStateOf(ProductCondition.NEW) }
    var selectedCategory by remember { mutableStateOf(ProductCategory.CLOTHING) }
    var addProductSuccess by remember { mutableStateOf(false) }

    // Upload Video Form State
    var videoCaption by remember { mutableStateOf("") }
    var videoMusic by remember { mutableStateOf("Kampala Night Rider Drill") }
    var videoLocation by remember { mutableStateOf("Kampala Central, Uganda") }
    var uploadVideoSuccess by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("seller_dashboard_screen")
    ) {
        // Dashboard Header
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                        }
                        Column {
                            Text("SELLER STUDIO", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = TextDark)
                            Text("OFFLINE MODE ENABLED • Kampala Central", style = MaterialTheme.typography.labelSmall.copy(color = LimeGreen, fontWeight = FontWeight.Bold))
                        }
                    }

                    BadgePill("MERCHANT VERIFIED", BrandPurple, PureWhite)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Navigation Tabs
                androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tabs) { tab ->
                        val isSelected = tab == activeTab
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) BrandPurple else LightSurfaceSubtle)
                                .border(1.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(8.dp))
                                .clickable { activeTab = tab }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = tab,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) PureWhite else TextDark
                                )
                            )
                        }
                    }
                }
            }
        }

        // Tab Content
        when (activeTab) {
            "PRODUCTS" -> {
                item {
                    Text(
                        text = "MANAGE INVENTORY & VARIANTS (${products.size})",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black, color = TextSecondaryDark),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(products) { prod ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(prod.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                                Text("UGX ${formatUgx(prod.priceUgx)} • Total Stock: ${prod.totalStock}", color = LimeGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            BadgePill(
                                text = if (prod.totalStock == 0) "OUT OF STOCK" else if (prod.totalStock <= 5) "LOW STOCK" else "IN STOCK",
                                bgColor = if (prod.totalStock == 0) CoralRed else if (prod.totalStock <= 5) BrightOrange else LimeGreen,
                                textColor = PureWhite
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Variant Stock Controllers
                        Text("Variants stock tracker:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                        Spacer(modifier = Modifier.height(4.dp))
                        prod.variants.forEach { variant ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${variant.color} / ${variant.size}", color = TextDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { repository.updateVariantStock(prod.id, variant.id, variant.stock - 1) },
                                        modifier = Modifier.size(28.dp).background(LightSurfaceSubtle, CircleShape)
                                    ) {
                                        Text("-", color = TextDark, fontWeight = FontWeight.Bold)
                                    }
                                    Text("${variant.stock}", color = TextDark, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp))
                                    IconButton(
                                        onClick = { repository.updateVariantStock(prod.id, variant.id, variant.stock + 1) },
                                        modifier = Modifier.size(28.dp).background(LightSurfaceSubtle, CircleShape)
                                    ) {
                                        Text("+", color = TextDark, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "ORDERS" -> {
                item {
                    Text(
                        text = "CUSTOMER ORDERS & DELIVERY (${orders.size})",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black, color = TextSecondaryDark),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(orders) { order ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(order.id, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = BrandPurple))
                            Text(order.date, color = TextSecondaryDark, fontSize = 11.sp)
                        }
                        Text("Customer: ${order.customerName} (${order.customerPhone})", color = TextDark, fontWeight = FontWeight.Bold)
                        Text("Delivery: ${order.deliveryAddress}, ${order.cityTown}, ${order.district}", color = TextSecondaryDark, fontSize = 12.sp)
                        Text("Total: UGX ${formatUgx(order.totalUgx)} via ${order.paymentMethod.label}", color = TextDark, fontWeight = FontWeight.Bold)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Status: ${order.deliveryStatus.name}", color = ElectricBlue, fontWeight = FontWeight.Bold)

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { repository.updateOrderStatus(order.id, OrderStatus.READY_FOR_PICKUP) },
                                    colors = ButtonDefaults.buttonColors(containerColor = LightSurfaceSubtle, contentColor = TextDark),
                                    modifier = Modifier.height(30.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("READY", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { repository.updateOrderStatus(order.id, OrderStatus.DELIVERED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite),
                                    modifier = Modifier.height(30.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("DELIVERED ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            "ANALYTICS" -> {
                item {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("STORE ANALYTICS OVERVIEW", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = TextDark))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MetricBox("TOTAL REVENUE", "UGX 4,850,000", LimeGreen, Modifier.weight(1f))
                            MetricBox("TOTAL ORDERS", "${orders.size + 34}", TextDark, Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MetricBox("SHORTS VIEWS", "142.5K", BrandPurple, Modifier.weight(1f))
                            MetricBox("CONVERSION RATE", "4.2%", ElectricBlue, Modifier.weight(1f))
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(PureWhite)
                                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("TOP SELLING STREETWEAR", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                                Text("1. JOREZ OFFLINE MODE TEE (48 units sold)", color = TextSecondaryDark)
                                Text("2. KAMPALA CIRCUIT CARGO HOODIE (31 units sold)", color = TextSecondaryDark)
                                Text("3. PEARL HIGH-TOP SNEAKERS (22 units sold)", color = TextSecondaryDark)
                            }
                        }
                    }
                }
            }

            "+ ADD PRODUCT" -> {
                item {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("LIST NEW STREETWEAR PRODUCT", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = TextDark))

                        if (addProductSuccess) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(LimeGreen, RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓ PRODUCT LISTED SUCCESSFULLY IN SHOP", color = PureWhite, fontWeight = FontWeight.Black)
                            }
                        }

                        OutlinedTextField(
                            value = newProductName,
                            onValueChange = { newProductName = it },
                            label = { Text("Product Name (e.g. CYBER KAMPALA JOGGERS)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = newProductDesc,
                            onValueChange = { newProductDesc = it },
                            label = { Text("Detailed Description & Fabric Material") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors()
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newProductPrice,
                                onValueChange = { newProductPrice = it },
                                label = { Text("Price in UGX (e.g. 75000)") },
                                modifier = Modifier.weight(1f),
                                colors = outlinedFieldColors()
                            )
                            OutlinedTextField(
                                value = newProductDiscount,
                                onValueChange = { newProductDiscount = it },
                                label = { Text("Discount Price UGX (Optional)") },
                                modifier = Modifier.weight(1f),
                                colors = outlinedFieldColors()
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newProductColor,
                                onValueChange = { newProductColor = it },
                                label = { Text("Colorway (e.g. Black)") },
                                modifier = Modifier.weight(1f),
                                colors = outlinedFieldColors()
                            )
                            OutlinedTextField(
                                value = newProductSize,
                                onValueChange = { newProductSize = it },
                                label = { Text("Size (e.g. M)") },
                                modifier = Modifier.weight(1f),
                                colors = outlinedFieldColors()
                            )
                            OutlinedTextField(
                                value = newProductStock,
                                onValueChange = { newProductStock = it },
                                label = { Text("Stock Qty") },
                                modifier = Modifier.weight(1f),
                                colors = outlinedFieldColors()
                            )
                        }

                        Button(
                            onClick = {
                                val price = newProductPrice.toLongOrNull() ?: 50000L
                                val discount = newProductDiscount.toLongOrNull()
                                val stock = newProductStock.toIntOrNull() ?: 5
                                val newProd = Product(
                                    id = "prod-" + System.currentTimeMillis(),
                                    name = if (newProductName.isNotBlank()) newProductName else "JOREZ STREETWEAR DROP",
                                    brandId = "brand-offline",
                                    brandName = "OFFLINE MODE ENABLED",
                                    brandVerified = true,
                                    description = if (newProductDesc.isNotBlank()) newProductDesc else "Ugandan streetwear collection drop.",
                                    category = selectedCategory,
                                    clothingType = ClothingType.OTHER,
                                    condition = selectedCondition,
                                    priceUgx = price,
                                    discountPriceUgx = discount,
                                    mainImage = "tee_black",
                                    images = listOf("tee_black"),
                                    variants = listOf(
                                        ProductVariant(color = newProductColor, size = newProductSize, stock = stock)
                                    ),
                                    availableColors = listOf(newProductColor),
                                    availableSizes = listOf(newProductSize),
                                    district = "Kampala",
                                    cityTown = "Kampala Central",
                                    isNewArrival = true
                                )
                                repository.addProduct(newProd)
                                addProductSuccess = true
                                newProductName = ""
                                newProductDesc = ""
                                newProductPrice = ""
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite)
                        ) {
                            Text("PUBLISH TO SHOP", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            "+ UPLOAD VIDEO" -> {
                item {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("UPLOAD VERTICAL SHORT VIDEO (9:16)", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = TextDark))

                        if (uploadVideoSuccess) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BrandPurple, RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓ SHORT VIDEO STREAMING ON JOREZ SHORTS", color = PureWhite, fontWeight = FontWeight.Black)
                            }
                        }

                        OutlinedTextField(
                            value = videoCaption,
                            onValueChange = { videoCaption = it },
                            label = { Text("Video Caption & Streetwear Tags (#JOREZ #Kampala)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = videoMusic,
                            onValueChange = { videoMusic = it },
                            label = { Text("Music / Audio Track") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = videoLocation,
                            onValueChange = { videoLocation = it },
                            label = { Text("Location (e.g. Ntinda, Kampala)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors()
                        )

                        Button(
                            onClick = {
                                val newVid = ShortVideo(
                                    id = "vid-" + System.currentTimeMillis(),
                                    brandId = "brand-offline",
                                    brandName = "OFFLINE MODE ENABLED",
                                    brandHandle = "@offlinemodeenabled",
                                    brandAvatar = "jorez_brand_1",
                                    caption = if (videoCaption.isNotBlank()) videoCaption else "Kampala new drop! Pure style, no signal.",
                                    videoGradientColors = listOf(0xFF1B0B2E, 0xFF4A156B, 0xFF0A0512),
                                    musicTrack = videoMusic,
                                    attachedProductId = products.firstOrNull()?.id,
                                    attachedProductName = products.firstOrNull()?.name,
                                    attachedProductPriceUgx = products.firstOrNull()?.priceUgx,
                                    location = videoLocation,
                                    views = "1.2K"
                                )
                                repository.uploadVideo(newVid)
                                uploadVideoSuccess = true
                                videoCaption = ""
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple, contentColor = PureWhite)
                        ) {
                            Text("PUBLISH TO JOREZ SHORTS", fontWeight = FontWeight.Black)
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

@Composable
fun MetricBox(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(PureWhite)
            .border(1.dp, BorderLight, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark, fontSize = 10.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = valueColor))
        }
    }
}
