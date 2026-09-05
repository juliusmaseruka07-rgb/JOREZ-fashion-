package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.BrandPurpleDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark

/**
 * The official JOREZ / Offline Mode Enabled graphic emblem.
 * Bright, crisp emblem with purple-blue tech traces and glowing node.
 */
@Composable
fun JorezEmblem(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    showGlow: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .size(size)
            .background(PureWhite, RoundedCornerShape(10.dp))
            .border(
                1.5.dp,
                Brush.linearGradient(listOf(BrandPurple, ElectricBlue)),
                RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(size * 0.12f)) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h / 2f

            // Circuit board traces (purple/blue subtle tone)
            val traceColor = Color(0xFF8B5CF6).copy(alpha = 0.4f)
            val strokeWidth = 1.8f

            // Horizontal circuit trace
            drawLine(traceColor, Offset(0f, cy * 0.4f), Offset(cx * 0.7f, cy * 0.4f), strokeWidth)
            drawLine(traceColor, Offset(cx * 0.7f, cy * 0.4f), Offset(cx * 0.7f, 0f), strokeWidth)
            drawCircle(Color(0xFF7C3AED), radius = 2.5f, center = Offset(cx * 0.7f, 0f))

            drawLine(traceColor, Offset(w, cy * 1.6f), Offset(cx * 1.3f, cy * 1.6f), strokeWidth)
            drawLine(traceColor, Offset(cx * 1.3f, cy * 1.6f), Offset(cx * 1.3f, h), strokeWidth)
            drawCircle(Color(0xFF2563EB), radius = 2.5f, center = Offset(cx * 1.3f, h))

            // Cross dimension
            val crossThickness = w * 0.30f
            val armLength = w * 0.88f

            // Vertical arm
            drawRect(
                color = BrandPurple,
                topLeft = Offset(cx - crossThickness / 2f, (h - armLength) / 2f),
                size = androidx.compose.ui.geometry.Size(crossThickness, armLength)
            )

            // Horizontal arm
            drawRect(
                color = BrandPurple,
                topLeft = Offset((w - armLength) / 2f, cy - crossThickness / 2f),
                size = androidx.compose.ui.geometry.Size(armLength, crossThickness)
            )

            // Center cutout / circuit junction
            drawRect(
                color = PureWhite,
                topLeft = Offset(cx - crossThickness * 0.35f, cy - crossThickness * 0.35f),
                size = androidx.compose.ui.geometry.Size(crossThickness * 0.7f, crossThickness * 0.7f)
            )

            // Glowing center node
            drawCircle(
                color = if (showGlow) ElectricBlue.copy(alpha = pulseAlpha) else ElectricBlue,
                radius = crossThickness * 0.22f,
                center = Offset(cx, cy)
            )
        }
    }
}

/**
 * Compact Header Logo with Brand typography and Tagline
 */
@Composable
fun JorezHeaderLogo(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        JorezEmblem(size = if (compact) 32.dp else 40.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "JOREZ",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = TextDark
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "MEDIA",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = BrandPurple
                    )
                )
            }
            if (!compact) {
                Text(
                    text = "DISCONNECT TO CONNECT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.5.sp,
                        color = TextMutedDark
                    )
                )
            }
        }
    }
}

/**
 * Full Hero & Editorial Logo Component
 */
@Composable
fun JorezFullEditorialLogo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(PureWhite, RoundedCornerShape(20.dp))
            .border(1.5.dp, BorderLight, RoundedCornerShape(20.dp))
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(BrandPurpleBg, RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(LimeGreen, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "BRANDS • DRAMA • CULTURE • COMMERCE",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.2.sp,
                    color = BrandPurpleDark,
                    fontWeight = FontWeight.Black,
                    fontSize = 9.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        JorezEmblem(size = 64.dp)
        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "JOREZ MEDIA",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            ),
            color = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "DISCONNECT TO CONNECT",
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurple
            )
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "NO SIGNAL. PURE STYLE.",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.8.sp,
                color = TextSecondaryDark,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
