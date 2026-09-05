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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JorezRepository
import com.example.data.SampleData
import com.example.model.AdPlacement
import com.example.model.AdType
import com.example.model.Advertisement
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdvertisingScreen(
    repository: JorezRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeAds by repository.activeAds.collectAsState()
    val packages by repository.adPackages.collectAsState()

    var activeTab by remember { mutableStateOf("CREATE CAMPAIGN") }
    val tabs = listOf("CREATE CAMPAIGN", "PACKAGES & RATES", "LIVE CAMPAIGNS")

    // Campaign Builder State
    var selectedAdType by remember { mutableStateOf(AdType.PRODUCT) }
    var selectedPlacement by remember { mutableStateOf(AdPlacement.HOME) }
    var selectedPackageId by remember { mutableStateOf(packages.firstOrNull()?.id ?: "pkg-standard") }
    var selectedTargetDistrict by remember { mutableStateOf("Kampala") }
    var headline by remember { mutableStateOf("OFFLINE MODE 2026 DROP") }
    var description by remember { mutableStateOf("High-contrast Ugandan streetwear tailored for the culture.") }
    var callToAction by remember { mutableStateOf("SHOP NOW") }
    var campaignSuccess by remember { mutableStateOf(false) }

    val activePackage = packages.find { it.id == selectedPackageId } ?: packages.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("advertising_screen")
    ) {
        // App Bar
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
                            Text("JOREZ ADS NETWORK", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = TextDark)
                            Text("Reach Ugandan consumers, creators & shoppers", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                        }
                    }

                    BadgePill("MTN / AIRTEL READY", LimeGreen, PureWhite)
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
            "CREATE CAMPAIGN" -> {
                item {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (campaignSuccess) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(LimeGreen, RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓ CAMPAIGN LAUNCHED VIA MTN/AIRTEL MOMO", color = PureWhite, fontWeight = FontWeight.Black)
                            }
                        }

                        Text("1. SELECT AD TYPE", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = TextDark)

                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            AdType.values().forEach { adType ->
                                val isSelected = adType == selectedAdType
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) BrandPurple else LightSurfaceSubtle)
                                        .border(1.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(8.dp))
                                        .clickable { selectedAdType = adType }
                                        .padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                    Text(adType.label, color = if (isSelected) PureWhite else TextDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }

                        Text("2. SELECT PLACEMENT SURFACE", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = TextDark)

                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            AdPlacement.values().forEach { placement ->
                                val isSelected = placement == selectedPlacement
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) LimeGreen else LightSurfaceSubtle)
                                        .border(1.dp, if (isSelected) LimeGreen else BorderLight, RoundedCornerShape(8.dp))
                                        .clickable { selectedPlacement = placement }
                                        .padding(horizontal = 12.dp, vertical = 7.dp)
                                ) {
                                    Text(placement.label, color = if (isSelected) PureWhite else TextDark, fontWeight = FontWeight.Black, fontSize = 11.sp)
                                }
                            }
                        }

                        Text("3. AD COPY & CALL TO ACTION", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = TextDark)

                        OutlinedTextField(
                            value = headline,
                            onValueChange = { headline = it },
                            label = { Text("Campaign Headline") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Short Description") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors()
                        )

                        OutlinedTextField(
                            value = callToAction,
                            onValueChange = { callToAction = it },
                            label = { Text("Call to Action (e.g. SHOP NOW, ORDER ON WHATSAPP)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors()
                        )

                        Text("4. CHOOSE BUDGET & DURATION", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = TextDark)

                        packages.forEach { pkg ->
                            val isSelected = pkg.id == selectedPackageId
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PureWhite)
                                    .border(if (isSelected) 2.dp else 1.dp, if (isSelected) BrandPurple else BorderLight, RoundedCornerShape(12.dp))
                                    .clickable { selectedPackageId = pkg.id }
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(pkg.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = TextDark)
                                        if (pkg.badge != null) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            BadgePill(pkg.badge, BrightOrange, PureWhite)
                                        }
                                    }
                                    Text("${pkg.durationDays} Days • ${pkg.estimatedViews}", color = TextSecondaryDark, fontSize = 11.sp)
                                }
                                Text("UGX ${formatUgx(pkg.priceUgx)}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = LimeGreen)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                val ad = Advertisement(
                                    id = "ad-" + System.currentTimeMillis(),
                                    sellerBrand = "OFFLINE MODE ENABLED",
                                    adType = selectedAdType,
                                    headline = headline,
                                    description = description,
                                    placement = selectedPlacement,
                                    durationDays = activePackage.durationDays,
                                    totalCostUgx = activePackage.priceUgx,
                                    callToAction = callToAction,
                                    targetDistrict = selectedTargetDistrict,
                                    isApproved = true,
                                    status = "ACTIVE",
                                    impressions = 120,
                                    clicks = 18
                                )
                                repository.createAdvertisement(ad)
                                campaignSuccess = true
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite)
                        ) {
                            Text("PAY UGX ${formatUgx(activePackage.priceUgx)} & LAUNCH CAMPAIGN", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            "PACKAGES & RATES" -> {
                items(packages) { pkg ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(pkg.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = TextDark)
                            Text("UGX ${formatUgx(pkg.priceUgx)}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = LimeGreen)
                        }
                        Text("Duration: ${pkg.durationDays} Days | Estimated Reach: ${pkg.estimatedViews}", color = BrandPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                        pkg.features.forEach { feat ->
                            Text("✓ $feat", color = TextSecondaryDark, fontSize = 12.sp)
                        }
                    }
                }
            }

            "LIVE CAMPAIGNS" -> {
                items(activeAds) { ad ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(ad.headline, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = TextDark)
                            BadgePill(ad.status, LimeGreen, PureWhite)
                        }
                        Text(ad.description, color = TextSecondaryDark, fontSize = 12.sp)
                        Text("Placement: ${ad.placement.label} • CTA: [${ad.callToAction}]", color = TextSecondaryDark, fontSize = 11.sp)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Impressions: ${formatUgx(ad.impressions.toLong())}", color = BrandPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Clicks: ${formatUgx(ad.clicks.toLong())}", color = LimeGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            val ctr = if (ad.impressions > 0) String.format(java.util.Locale.US, "%.1f%%", (ad.clicks.toFloat() / ad.impressions) * 100) else "0%"
                            Text("CTR: $ctr", color = TextDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
