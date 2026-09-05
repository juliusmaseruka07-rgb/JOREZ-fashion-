package com.example.model

import java.util.UUID

enum class ProductCategory(val label: String) {
    CLOTHING("Clothing"),
    SHOES("Shoes"),
    ACCESSORIES("Accessories"),
    BAGS("Bags"),
    PHONES("Phones"),
    TECH("Tech"),
    BEAUTY("Beauty"),
    MUSIC("Music"),
    FOOD("Food"),
    SERVICES("Services"),
    OTHER("Other")
}

enum class ClothingType(val label: String) {
    TSHIRT("T-shirt"),
    SHIRT("Shirt"),
    HOODIE("Hoodie"),
    JACKET("Jacket"),
    TROUSERS("Trousers"),
    SHORTS("Shorts"),
    DRESS("Dress"),
    OTHER("Other")
}

enum class ProductCondition(val label: String) {
    NEW("Brand New"),
    USED("Pre-owned / Vintage")
}

data class ProductVariant(
    val id: String = UUID.randomUUID().toString(),
    val color: String,
    val size: String,
    var stock: Int
)

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val brandId: String,
    val brandName: String,
    val brandVerified: Boolean = true,
    val description: String,
    val category: ProductCategory,
    val clothingType: ClothingType? = null,
    val condition: ProductCondition = ProductCondition.NEW,
    val priceUgx: Long,
    val discountPriceUgx: Long? = null,
    val mainImage: String,
    val images: List<String> = emptyList(),
    val videoUrl: String? = null,
    val variants: List<ProductVariant> = emptyList(),
    val availableColors: List<String> = emptyList(),
    val availableSizes: List<String> = emptyList(),
    val district: String = "Kampala",
    val cityTown: String = "Kampala Central",
    val country: String = "Uganda",
    val rating: Float = 4.8f,
    val reviewsCount: Int = 24,
    val isFeatured: Boolean = false,
    val isNewArrival: Boolean = false,
    val isApproved: Boolean = true
) {
    val totalStock: Int get() = if (variants.isNotEmpty()) variants.sumOf { it.stock } else 10
    val stockStatus: String get() = when {
        totalStock == 0 -> "OUT OF STOCK"
        totalStock <= 5 -> "LOW STOCK"
        else -> "IN STOCK"
    }
}

data class ShortVideo(
    val id: String = UUID.randomUUID().toString(),
    val brandId: String,
    val brandName: String,
    val brandHandle: String,
    val brandAvatar: String,
    val caption: String,
    val videoGradientColors: List<Long>,
    val musicTrack: String = "Original Sound - JOREZ Beats",
    var likesCount: Int = 1240,
    var isLiked: Boolean = false,
    var commentsCount: Int = 89,
    var savesCount: Int = 340,
    var isSaved: Boolean = false,
    val attachedProductId: String? = null,
    val attachedProductName: String? = null,
    val attachedProductPriceUgx: Long? = null,
    val location: String = "Kampala, Uganda",
    val tags: List<String> = listOf("Streetwear", "Kampala", "Fashion"),
    val views: String = "24.5K"
)

data class Comment(
    val id: String = UUID.randomUUID().toString(),
    val userName: String,
    val userHandle: String,
    val text: String,
    val timeAgo: String = "2h ago",
    var likes: Int = 12
)

data class DramaEpisode(
    val number: Int,
    val title: String,
    val duration: String,
    val description: String
)

data class Drama(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val genre: String,
    val year: Int = 2026,
    val rating: String = "16+",
    val posterUrl: String,
    val backdropGradient: List<Long>,
    val synopsis: String,
    val director: String,
    val cast: List<String>,
    val productionCompany: String,
    val episodes: List<DramaEpisode>,
    var isFollowed: Boolean = false,
    val trailerDuration: String = "2:15"
)

data class Brand(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val handle: String,
    val isVerified: Boolean = true,
    val description: String,
    val category: String,
    val location: String = "Kampala, Uganda",
    val phone: String = "+256 770 123456",
    val whatsAppNumber: String = "+256 770 123456",
    val logoUrl: String,
    val coverGradient: List<Long>,
    var followersCount: Int = 8400,
    var isFollowed: Boolean = false,
    val productsCount: Int = 18,
    val videosCount: Int = 12
)

data class BrandPost(
    val id: String = UUID.randomUUID().toString(),
    val brandId: String,
    val brandName: String,
    val brandHandle: String,
    val isVerified: Boolean = true,
    val location: String,
    val caption: String,
    val gradientColors: List<Long>,
    val tags: List<String>,
    val timeAgo: String = "1h ago",
    val linkedProductId: String? = null,
    val linkedPriceUgx: Long? = null,
    var likesCount: Int = 420,
    var isLiked: Boolean = false,
    var isSaved: Boolean = false,
    var commentsCount: Int = 38
)

enum class AdPlacement(val label: String, val dailyRateUgx: Long) {
    HOME("Home Page Hero & Feed", 35_000),
    DISCOVER("Discover Stream", 25_000),
    SHORTS("JOREZ Shorts In-Stream", 30_000),
    SHOP("Shop Catalog Sponsored", 20_000),
    DRAMA("Drama Section Spotlight", 25_000)
}

enum class AdType(val label: String) {
    PRODUCT("Product Ad"),
    BRAND("Brand Awareness"),
    DRAMA("Drama/Film Promotion"),
    EVENT("Street Event / Concert"),
    BUSINESS("Local Business"),
    VIDEO("Video Campaign"),
    SALE("Flash Sale")
}

data class Advertisement(
    val id: String = UUID.randomUUID().toString(),
    val sellerBrand: String,
    val adType: AdType,
    val headline: String,
    val description: String,
    val placement: AdPlacement,
    val durationDays: Int,
    val totalCostUgx: Long,
    val callToAction: String = "SHOP NOW",
    val targetDistrict: String = "Kampala",
    val isApproved: Boolean = true,
    val status: String = "ACTIVE",
    val impressions: Int = 14200,
    val clicks: Int = 890
)

data class AdPackage(
    val id: String,
    val name: String,
    val durationDays: Int,
    val priceUgx: Long,
    val estimatedViews: String,
    val features: List<String>,
    val badge: String? = null
)

data class CartItem(
    val product: Product,
    val selectedVariant: ProductVariant,
    var quantity: Int,
    val id: String = "${product.id}_${selectedVariant.id}"
) {
    val totalPriceUgx: Long get() = (product.discountPriceUgx ?: product.priceUgx) * quantity
}

enum class PaymentMethod(val label: String, val providerPrefix: String) {
    MTN_MOMO("MTN Mobile Money", "MTN MoMo"),
    AIRTEL_MONEY("Airtel Money", "Airtel Money"),
    CASH_ON_DELIVERY("Cash on Delivery", "COD"),
    BANK_TRANSFER("Bank Transfer", "Bank"),
    CARD("Visa / Mastercard", "Card")
}

enum class OrderStatus(val label: String) {
    PAYMENT_PENDING("PAYMENT PENDING"),
    PAID("PAID"),
    PROCESSING("PROCESSING"),
    READY("READY FOR PICKUP/DISPATCH"),
    READY_FOR_PICKUP("READY FOR PICKUP"),
    SHIPPED("SHIPPED"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED")
}

data class Order(
    val id: String = "JM-" + (10000..99999).random(),
    val date: String,
    val items: List<CartItem>,
    val subtotalUgx: Long,
    val deliveryFeeUgx: Long,
    val totalUgx: Long,
    val customerName: String,
    val customerPhone: String,
    val district: String,
    val cityTown: String,
    val deliveryAddress: String,
    val paymentMethod: PaymentMethod,
    var paymentStatus: OrderStatus = OrderStatus.PAID,
    var deliveryStatus: OrderStatus = OrderStatus.PROCESSING
)

data class NotificationItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: String,
    val isRead: Boolean = false
)

data class ReportItem(
    val id: String = UUID.randomUUID().toString(),
    val targetType: String,
    val targetName: String,
    val reason: String,
    val reportedBy: String,
    val date: String,
    var status: String = "PENDING"
)
