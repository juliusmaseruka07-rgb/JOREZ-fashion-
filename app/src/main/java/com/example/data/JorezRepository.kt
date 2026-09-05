package com.example.data

import com.example.model.AdPackage
import com.example.model.Advertisement
import com.example.model.Brand
import com.example.model.BrandPost
import com.example.model.CartItem
import com.example.model.Comment
import com.example.model.Drama
import com.example.model.NotificationItem
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.PaymentMethod
import com.example.model.Product
import com.example.model.ProductVariant
import com.example.model.ReportItem
import com.example.model.ShortVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JorezRepository(
    initialProducts: List<Product> = SampleData.products,
    initialVideos: List<ShortVideo> = SampleData.shortVideos,
    initialDramas: List<Drama> = SampleData.dramas,
    initialCart: List<CartItem> = listOf(
        CartItem(
            product = SampleData.products[0],
            selectedVariant = SampleData.products[0].variants[0],
            quantity = 1
        )
    )
) {

    private val _products = MutableStateFlow(initialProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _videos = MutableStateFlow(initialVideos)
    val videos: StateFlow<List<ShortVideo>> = _videos.asStateFlow()

    private val _dramas = MutableStateFlow(initialDramas)
    val dramas: StateFlow<List<Drama>> = _dramas.asStateFlow()

    private val _brands = MutableStateFlow(SampleData.brands)
    val brands: StateFlow<List<Brand>> = _brands.asStateFlow()

    private val _brandPosts = MutableStateFlow(SampleData.brandPosts)
    val brandPosts: StateFlow<List<BrandPost>> = _brandPosts.asStateFlow()

    private val _cart = MutableStateFlow(initialCart)
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(listOf(
        Order(
            id = "JM-88219",
            date = "04 Sep 2026",
            items = listOf(
                CartItem(
                    product = SampleData.products[0],
                    selectedVariant = SampleData.products[0].variants[0],
                    quantity = 1
                )
            ),
            subtotalUgx = 35000,
            deliveryFeeUgx = 5000,
            totalUgx = 40000,
            customerName = "Julius Maseruka",
            customerPhone = "+256 772 123456",
            district = "Kampala",
            cityTown = "Ntinda",
            deliveryAddress = "Plot 14 Kimera Road, Ntinda",
            paymentMethod = PaymentMethod.MTN_MOMO,
            paymentStatus = OrderStatus.PAID,
            deliveryStatus = OrderStatus.SHIPPED
        )
    ))
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(setOf("prod-1", "drama-1"))
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _activeAds = MutableStateFlow(SampleData.activeAds)
    val activeAds: StateFlow<List<Advertisement>> = _activeAds.asStateFlow()

    private val _adPackages = MutableStateFlow(SampleData.adPackages)
    val adPackages: StateFlow<List<AdPackage>> = _adPackages.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(listOf(
        NotificationItem(title = "Welcome to JOREZ MEDIA", message = "DISCONNECT TO CONNECT. Discover Ugandan streetwear, drama, and culture.", timeAgo = "10m ago", type = "SYSTEM"),
        NotificationItem(title = "Order Shipped! 🚀", message = "Order #JM-88219 is out for delivery with our Kampala rider.", timeAgo = "2h ago", type = "ORDER"),
        NotificationItem(title = "New Drop Alert ⚡", message = "OFFLINE MODE ENABLED released the 2026 Circuit Hoodie.", timeAgo = "1d ago", type = "BRAND")
    ))
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _reports = MutableStateFlow<List<ReportItem>>(listOf(
        ReportItem(targetType = "Product", targetName = "Counterfeit Tee Listing", reason = "Copyright / Fake product", reportedBy = "@kampalastyle", date = "03 Sep 2026", status = "PENDING")
    ))
    val reports: StateFlow<List<ReportItem>> = _reports.asStateFlow()

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    fun toggleOfflineMode() {
        _isOfflineMode.update { !it }
    }

    fun toggleFavorite(productId: String) {
        _favorites.update { current ->
            if (current.contains(productId)) current - productId else current + productId
        }
    }

    fun toggleVideoLike(videoId: String) {
        _videos.update { list ->
            list.map { v ->
                if (v.id == videoId) {
                    val newLiked = !v.isLiked
                    v.copy(
                        isLiked = newLiked,
                        likesCount = if (newLiked) v.likesCount + 1 else v.likesCount - 1
                    )
                } else v
            }
        }
    }

    fun toggleVideoSave(videoId: String) {
        _videos.update { list ->
            list.map { v ->
                if (v.id == videoId) {
                    val newSaved = !v.isSaved
                    v.copy(
                        isSaved = newSaved,
                        savesCount = if (newSaved) v.savesCount + 1 else v.savesCount - 1
                    )
                } else v
            }
        }
    }

    fun toggleFollowBrand(brandId: String) {
        _brands.update { list ->
            list.map { b ->
                if (b.id == brandId) {
                    val newFollow = !b.isFollowed
                    b.copy(
                        isFollowed = newFollow,
                        followersCount = if (newFollow) b.followersCount + 1 else b.followersCount - 1
                    )
                } else b
            }
        }
    }

    fun toggleFollowDrama(dramaId: String) {
        _dramas.update { list ->
            list.map { d ->
                if (d.id == dramaId) d.copy(isFollowed = !d.isFollowed) else d
            }
        }
    }

    fun togglePostLike(postId: String) {
        _brandPosts.update { list ->
            list.map { p ->
                if (p.id == postId) {
                    val newLiked = !p.isLiked
                    p.copy(
                        isLiked = newLiked,
                        likesCount = if (newLiked) p.likesCount + 1 else p.likesCount - 1
                    )
                } else p
            }
        }
    }

    fun addToCart(product: Product, variant: ProductVariant, quantity: Int = 1): Boolean {
        if (variant.stock <= 0) return false
        _cart.update { current ->
            val existingIndex = current.indexOfFirst {
                it.product.id == product.id && it.selectedVariant.id == variant.id
            }
            if (existingIndex >= 0) {
                val existingItem = current[existingIndex]
                val newQty = (existingItem.quantity + quantity).coerceAtMost(variant.stock)
                current.toMutableList().apply {
                    this[existingIndex] = existingItem.copy(quantity = newQty)
                }
            } else {
                current + CartItem(product, variant, quantity.coerceAtMost(variant.stock))
            }
        }
        return true
    }

    fun updateCartQuantity(index: Int, newQuantity: Int) {
        _cart.update { current ->
            if (index in current.indices) {
                if (newQuantity <= 0) {
                    current.toMutableList().apply { removeAt(index) }
                } else {
                    val item = current[index]
                    val bounded = newQuantity.coerceAtMost(item.selectedVariant.stock)
                    current.toMutableList().apply {
                        this[index] = item.copy(quantity = bounded)
                    }
                }
            } else current
        }
    }

    fun updateCartQuantityById(itemId: String, newQuantity: Int) {
        _cart.update { current ->
            val index = current.indexOfFirst { it.id == itemId }
            if (index >= 0) {
                if (newQuantity <= 0) {
                    current.filterNot { it.id == itemId }
                } else {
                    val item = current[index]
                    val bounded = newQuantity.coerceAtMost(item.selectedVariant.stock)
                    current.toMutableList().apply {
                        this[index] = item.copy(quantity = bounded)
                    }
                }
            } else current
        }
    }

    fun removeFromCart(index: Int) {
        _cart.update { current ->
            if (index in current.indices) {
                current.toMutableList().apply { removeAt(index) }
            } else current
        }
    }

    fun removeFromCartById(itemId: String) {
        _cart.update { current -> current.filterNot { it.id == itemId } }
    }

    fun removeItemsFromCart(itemIds: Set<String>) {
        _cart.update { current -> current.filterNot { it.id in itemIds } }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun placeOrder(
        customerName: String,
        customerPhone: String,
        district: String,
        cityTown: String,
        deliveryAddress: String,
        paymentMethod: PaymentMethod,
        itemsToCheckout: List<CartItem> = _cart.value
    ): Order {
        val items = if (itemsToCheckout.isNotEmpty()) itemsToCheckout else _cart.value
        val subtotal = items.sumOf { it.totalPriceUgx }
        val deliveryFee = if (district.equals("Kampala", ignoreCase = true)) 5000L else 12000L
        val total = subtotal + deliveryFee

        val newOrder = Order(
            id = "JM-" + (10000..99999).random(),
            date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
            items = items,
            subtotalUgx = subtotal,
            deliveryFeeUgx = deliveryFee,
            totalUgx = total,
            customerName = customerName,
            customerPhone = customerPhone,
            district = district,
            cityTown = cityTown,
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod,
            paymentStatus = OrderStatus.PAID,
            deliveryStatus = OrderStatus.PROCESSING
        )

        // Decrease stock per variant for the checked out items
        _products.update { currentProducts ->
            currentProducts.map { product ->
                val purchasedForProduct = items.filter { it.product.id == product.id }
                if (purchasedForProduct.isNotEmpty()) {
                    val updatedVariants = product.variants.map { variant ->
                        val purchasedItem = purchasedForProduct.find { it.selectedVariant.id == variant.id }
                        if (purchasedItem != null) {
                            variant.copy(stock = (variant.stock - purchasedItem.quantity).coerceAtLeast(0))
                        } else variant
                    }
                    product.copy(variants = updatedVariants)
                } else product
            }
        }

        _orders.update { listOf(newOrder) + it }
        // Remove only the checked out items from the cart; unselected items stay in cart
        val purchasedIds = items.map { it.id }.toSet()
        _cart.update { current -> current.filterNot { it.id in purchasedIds } }

        _notifications.update {
            listOf(
                NotificationItem(
                    title = "Payment Successful ✓",
                    message = "Order ${newOrder.id} paid via ${paymentMethod.label}. Total UGX ${"%,d".format(total)}.",
                    timeAgo = "Just now",
                    type = "ORDER"
                )
            ) + it
        }

        return newOrder
    }

    fun addProduct(product: Product) {
        _products.update { listOf(product) + it }
        _notifications.update {
            listOf(
                NotificationItem(
                    title = "Product Listed",
                    message = "${product.name} is now live in the JOREZ Shop.",
                    timeAgo = "Just now",
                    type = "SELLER"
                )
            ) + it
        }
    }

    fun updateVariantStock(productId: String, variantId: String, newStock: Int) {
        _products.update { list ->
            list.map { p ->
                if (p.id == productId) {
                    val updated = p.variants.map { v ->
                        if (v.id == variantId) v.copy(stock = newStock.coerceAtLeast(0)) else v
                    }
                    p.copy(variants = updated)
                } else p
            }
        }
    }

    fun createAdvertisement(ad: Advertisement) {
        _activeAds.update { listOf(ad) + it }
        _notifications.update {
            listOf(
                NotificationItem(
                    title = "Ad Campaign Created",
                    message = "Campaign '${ad.headline}' submitted for ${ad.placement.label}.",
                    timeAgo = "Just now",
                    type = "ADS"
                )
            ) + it
        }
    }

    fun uploadVideo(video: ShortVideo) {
        _videos.update { listOf(video) + it }
        _notifications.update {
            listOf(
                NotificationItem(
                    title = "Short Video Published",
                    message = "Your vertical video is now streaming on JOREZ Shorts.",
                    timeAgo = "Just now",
                    type = "VIDEO"
                )
            ) + it
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.update { list ->
            list.map { if (it.id == orderId) it.copy(deliveryStatus = newStatus) else it }
        }
    }

    fun approveProduct(productId: String) {
        _products.update { list ->
            list.map { if (it.id == productId) it.copy(isApproved = true) else it }
        }
    }

    fun rejectProduct(productId: String) {
        _products.update { list ->
            list.filterNot { it.id == productId }
        }
    }

    fun submitReport(targetType: String, targetName: String, reason: String) {
        val report = ReportItem(
            targetType = targetType,
            targetName = targetName,
            reason = reason,
            reportedBy = "@current_user",
            date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        )
        _reports.update { listOf(report) + it }
    }

    companion object {
        val instance by lazy { JorezRepository() }
    }
}
