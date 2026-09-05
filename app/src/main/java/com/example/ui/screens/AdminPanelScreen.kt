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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.components.BadgePill
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

@Composable
fun AdminPanelScreen(
    repository: JorezRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val products by repository.products.collectAsState()
    val orders by repository.orders.collectAsState()
    val reports by repository.reports.collectAsState()
    val activeAds by repository.activeAds.collectAsState()
    val brands by repository.brands.collectAsState()

    var activeTab by remember { mutableStateOf("OVERVIEW") }
    val tabs = listOf("OVERVIEW", "MODERATION", "MERCHANTS", "ADS APPROVAL", "ORDERS LOG")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("admin_panel_screen")
    ) {
        // Top Header
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("JOREZ ROOT ADMIN", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = TextDark)
                            }
                            Text("Super Control & Platform Governance", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                        }
                    }

                    BadgePill("ADMIN ACCESS", CoralRed, PureWhite)
                }

                Spacer(modifier = Modifier.height(14.dp))

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

        when (activeTab) {
            "OVERVIEW" -> {
                item {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("PLATFORM SYSTEM METRICS", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = TextDark))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MetricBox("GROSS MERCHANDISE", "UGX 84.5M", LimeGreen, Modifier.weight(1f))
                            MetricBox("AD REVENUE", "UGX 12.8M", BrandPurple, Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MetricBox("ACTIVE BRANDS", "${brands.size}", TextDark, Modifier.weight(1f))
                            MetricBox("REPORTED ITEMS", "${reports.size}", CoralRed, Modifier.weight(1f))
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
                                Text("PLATFORM HEALTH", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                                Text("✓ MTN MoMo API Gateway: Operational", color = LimeGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("✓ Airtel Money Gateway: Operational", color = LimeGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("✓ Video CDN (JOREZ Shorts): 99.98% uptime", color = TextDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text("✓ Offline Mode PWA Cache: Sync Ready", color = BrandPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            "MODERATION" -> {
                item {
                    Text(
                        text = "FLAGGED CONTENT & USER REPORTS (${reports.size})",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black, color = TextSecondaryDark),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(reports) { rep ->
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
                            Text("${rep.targetType}: ${rep.targetName}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                            BadgePill(rep.status, BrightOrange, PureWhite)
                        }
                        Text("Reason: ${rep.reason}", color = CoralRed, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("Reported by ${rep.reportedBy} on ${rep.date}", color = TextSecondaryDark, fontSize = 11.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { /* Dismiss */ },
                                colors = ButtonDefaults.buttonColors(containerColor = LightSurfaceSubtle, contentColor = TextDark),
                                modifier = Modifier.height(32.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("DISMISS", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { /* Ban */ },
                                colors = ButtonDefaults.buttonColors(containerColor = CoralRed, contentColor = PureWhite),
                                modifier = Modifier.height(32.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("BAN / REMOVE", fontSize = 10.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            "MERCHANTS" -> {
                items(brands) { brand ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(brand.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                            Text(brand.location, color = TextSecondaryDark, fontSize = 11.sp)
                            Text("${brand.followersCount} followers • ${brand.productsCount} items", color = BrandPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { repository.toggleFollowBrand(brand.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (brand.isVerified) LimeGreen else LightSurfaceSubtle,
                                contentColor = if (brand.isVerified) PureWhite else TextDark
                            ),
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (brand.isVerified) "VERIFIED ✓" else "VERIFY", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            "ADS APPROVAL" -> {
                items(activeAds) { ad ->
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
                            Text(ad.headline, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                            BadgePill(ad.status, LimeGreen, PureWhite)
                        }
                        Text("Sponsor: ${ad.sellerBrand} • Budget: UGX ${formatUgx(ad.totalCostUgx)}", color = BrandPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(ad.description, color = TextSecondaryDark, fontSize = 12.sp)
                    }
                }
            }

            "ORDERS LOG" -> {
                items(orders) { order ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${order.id} • ${order.customerName}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                            Text("UGX ${formatUgx(order.totalUgx)} via ${order.paymentMethod.label}", color = LimeGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${order.district} • ${order.date}", color = TextSecondaryDark, fontSize = 11.sp)
                        }

                        BadgePill(order.deliveryStatus.name, ElectricBlue, PureWhite)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
