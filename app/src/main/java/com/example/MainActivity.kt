package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.JorezRepository
import com.example.ui.viewmodel.CartViewModel
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AdvertisingScreen
import com.example.ui.screens.BrandProfileScreen
import com.example.ui.screens.CartAndCheckoutScreen
import com.example.ui.screens.DiscoverFeedScreen
import com.example.ui.screens.DramaScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.SearchOverlayScreen
import com.example.ui.screens.SellerDashboardScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.ShortsScreen
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.LightBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMutedDark

sealed class Screen {
    object Home : Screen()
    object Discover : Screen()
    data class Shorts(val videoId: String? = null) : Screen()
    object Shop : Screen()
    data class ProductDetail(val productId: String) : Screen()
    data class Drama(val dramaId: String? = null) : Screen()
    data class BrandProfile(val brandId: String) : Screen()
    object Cart : Screen()
    object SellerDashboard : Screen()
    object Advertising : Screen()
    object AdminPanel : Screen()
    object Favorites : Screen()
    object Notifications : Screen()
    object Search : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = JorezRepository.instance

        setContent {
            MyApplicationTheme {
                JorezApp(repository = repository)
            }
        }
    }
}

@Composable
fun JorezApp(repository: JorezRepository) {
    val cartViewModel: CartViewModel = viewModel { CartViewModel(repository) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var screenHistory by remember { mutableStateOf(listOf<Screen>(Screen.Home)) }

    val cart by repository.cart.collectAsState()
    val totalCartItems = cart.sumOf { it.quantity }

    fun navigateTo(screen: Screen) {
        if (currentScreen != screen) {
            screenHistory = screenHistory + currentScreen
            currentScreen = screen
        }
    }

    fun navigateBack() {
        if (screenHistory.isNotEmpty()) {
            val previous = screenHistory.last()
            screenHistory = screenHistory.dropLast(1)
            currentScreen = previous
        } else {
            currentScreen = Screen.Home
        }
    }

    BackHandler(enabled = currentScreen != Screen.Home) {
        navigateBack()
    }

    val isTopLevelScreen = currentScreen is Screen.Home ||
            currentScreen is Screen.Discover ||
            currentScreen is Screen.Shorts ||
            currentScreen is Screen.Shop ||
            currentScreen is Screen.Drama ||
            currentScreen is Screen.SellerDashboard

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            if (isTopLevelScreen) {
                JorezBottomNav(
                    currentScreen = currentScreen,
                    onSelect = { dest -> navigateTo(dest) }
                )
            }
        },
        floatingActionButton = {
            // Quick Admin Toggle Floating Button if on Home or Shop
            if (currentScreen is Screen.Home) {
                FloatingActionButton(
                    onClick = { navigateTo(Screen.AdminPanel) },
                    containerColor = PureWhite,
                    contentColor = BrandPurple,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(46.dp)
                        .border(1.5.dp, BrandPurple.copy(alpha = 0.4f), CircleShape)
                        .testTag("admin_fab_toggle")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Control",
                        tint = BrandPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBackground)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(
                        repository = repository,
                        onNavigateToDiscover = { navigateTo(Screen.Discover) },
                        onNavigateToShorts = { vidId -> navigateTo(Screen.Shorts(vidId)) },
                        onNavigateToShop = { navigateTo(Screen.Shop) },
                        onNavigateToDrama = { dramaId -> navigateTo(Screen.Drama(dramaId)) },
                        onNavigateToBrand = { brandId -> navigateTo(Screen.BrandProfile(brandId)) },
                        onNavigateToProduct = { prodId -> navigateTo(Screen.ProductDetail(prodId)) },
                        onNavigateToCart = { navigateTo(Screen.Cart) },
                        onNavigateToFavorites = { navigateTo(Screen.Favorites) },
                        onNavigateToNotifications = { navigateTo(Screen.Notifications) },
                        onNavigateToSell = { navigateTo(Screen.SellerDashboard) },
                        onNavigateToAdvertise = { navigateTo(Screen.Advertising) },
                        onNavigateToSearch = { navigateTo(Screen.Search) }
                    )
                }

                is Screen.Discover -> {
                    DiscoverFeedScreen(
                        repository = repository,
                        onBrandClick = { brandId -> navigateTo(Screen.BrandProfile(brandId)) },
                        onProductClick = { prodId -> navigateTo(Screen.ProductDetail(prodId)) },
                        onShortsClick = { vidId -> navigateTo(Screen.Shorts(vidId)) }
                    )
                }

                is Screen.Shorts -> {
                    ShortsScreen(
                        repository = repository,
                        initialVideoId = screen.videoId,
                        onBrandClick = { brandId -> navigateTo(Screen.BrandProfile(brandId)) },
                        onShopProduct = { prodId -> navigateTo(Screen.ProductDetail(prodId)) }
                    )
                }

                is Screen.Shop -> {
                    ShopScreen(
                        repository = repository,
                        onProductClick = { prodId -> navigateTo(Screen.ProductDetail(prodId)) }
                    )
                }

                is Screen.ProductDetail -> {
                    ProductDetailScreen(
                        productId = screen.productId,
                        repository = repository,
                        onBack = { navigateBack() },
                        onBrandClick = { brandId -> navigateTo(Screen.BrandProfile(brandId)) },
                        onRelatedProductClick = { prodId -> navigateTo(Screen.ProductDetail(prodId)) },
                        onNavigateToCheckout = { navigateTo(Screen.Cart) }
                    )
                }

                is Screen.Drama -> {
                    DramaScreen(
                        repository = repository,
                        initialDramaId = screen.dramaId
                    )
                }

                is Screen.BrandProfile -> {
                    BrandProfileScreen(
                        brandId = screen.brandId,
                        repository = repository,
                        onBack = { navigateBack() },
                        onProductClick = { prodId -> navigateTo(Screen.ProductDetail(prodId)) },
                        onVideoClick = { vidId -> navigateTo(Screen.Shorts(vidId)) }
                    )
                }

                is Screen.Cart -> {
                    CartAndCheckoutScreen(
                        viewModel = cartViewModel,
                        onContinueShopping = { navigateTo(Screen.Shop) }
                    )
                }

                is Screen.SellerDashboard -> {
                    SellerDashboardScreen(
                        repository = repository,
                        onBack = { navigateBack() }
                    )
                }

                is Screen.Advertising -> {
                    AdvertisingScreen(
                        repository = repository,
                        onBack = { navigateBack() }
                    )
                }

                is Screen.AdminPanel -> {
                    AdminPanelScreen(
                        repository = repository,
                        onBack = { navigateBack() }
                    )
                }

                is Screen.Favorites -> {
                    FavoritesScreen(
                        repository = repository,
                        onBack = { navigateBack() },
                        onProductClick = { prodId -> navigateTo(Screen.ProductDetail(prodId)) }
                    )
                }

                is Screen.Notifications -> {
                    NotificationsScreen(
                        repository = repository,
                        onBack = { navigateBack() }
                    )
                }

                is Screen.Search -> {
                    SearchOverlayScreen(
                        repository = repository,
                        onBack = { navigateBack() },
                        onProductClick = { prodId -> navigateTo(Screen.ProductDetail(prodId)) },
                        onBrandClick = { brandId -> navigateTo(Screen.BrandProfile(brandId)) },
                        onDramaClick = { dramaId -> navigateTo(Screen.Drama(dramaId)) }
                    )
                }
            }
        }
    }
}

@Composable
fun JorezBottomNav(
    currentScreen: Screen,
    onSelect: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = PureWhite,
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderLight)
            .testTag("bottom_nav_bar")
    ) {
        val items = listOf(
            Triple(Screen.Home, "HOME", Icons.Default.Home),
            Triple(Screen.Discover, "DISCOVER", Icons.Default.Explore),
            Triple(Screen.Shorts(), "SHORTS", Icons.Default.OndemandVideo),
            Triple(Screen.Shop, "SHOP", Icons.Default.ShoppingBag),
            Triple(Screen.Drama(), "DRAMA", Icons.Default.Movie),
            Triple(Screen.SellerDashboard, "STUDIO", Icons.Default.Storefront)
        )

        items.forEach { (screenDest, label, icon) ->
            val isSelected = when (screenDest) {
                is Screen.Home -> currentScreen is Screen.Home
                is Screen.Discover -> currentScreen is Screen.Discover
                is Screen.Shorts -> currentScreen is Screen.Shorts
                is Screen.Shop -> currentScreen is Screen.Shop
                is Screen.Drama -> currentScreen is Screen.Drama
                is Screen.SellerDashboard -> currentScreen is Screen.SellerDashboard
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelect(screenDest) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandPurple,
                    selectedTextColor = BrandPurple,
                    unselectedIconColor = TextMutedDark,
                    unselectedTextColor = TextMutedDark,
                    indicatorColor = BrandPurpleBg
                )
            )
        }
    }
}
