package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.JorezRepository
import com.example.data.SampleData
import com.example.model.CartItem
import com.example.model.Order
import com.example.model.PaymentMethod
import com.example.ui.components.JorezEmblem
import com.example.ui.components.formatUgx
import com.example.ui.theme.AirtelRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleBg
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.BrightOrangeBg
import com.example.ui.theme.BrightYellow
import com.example.ui.theme.BrightYellowBg
import com.example.ui.theme.CoralRed
import com.example.ui.theme.CoralRedBg
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueBg
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceSubtle
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.LimeGreenBg
import com.example.ui.theme.MtnYellow
import com.example.ui.theme.MtnTextDark
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.CartItemUiModel
import com.example.ui.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAndCheckoutScreen(
    repository: JorezRepository = JorezRepository.instance,
    viewModel: CartViewModel = viewModel { CartViewModel(repository) },
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var districtExpanded by remember { mutableStateOf(false) }

    // 1. If Order was successfully placed, display Order Confirmation Receipt
    if (uiState.placedOrder != null) {
        val order = uiState.placedOrder!!
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(LightBackground)
                .padding(16.dp)
                .testTag("order_confirmation_view"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(LimeGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Order Confirmed",
                        tint = PureWhite,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ORDER CONFIRMED!",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                    color = TextDark
                )

                Text(
                    text = "Your Ugandan streetwear order is now being dispatched.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Receipt Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(PureWhite)
                        .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("ORDER NUMBER:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark, fontWeight = FontWeight.Bold))
                            Text(order.id, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = BrandPurple))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("DATE:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(order.date, style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, fontWeight = FontWeight.SemiBold))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("PAYMENT METHOD:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(order.paymentMethod.label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("PAYMENT STATUS:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(order.paymentStatus.name, style = MaterialTheme.typography.bodyMedium.copy(color = LimeGreen, fontWeight = FontWeight.Black))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("DELIVERY STATUS:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(order.deliveryStatus.name, style = MaterialTheme.typography.bodyMedium.copy(color = BrandPurple, fontWeight = FontWeight.Black))
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderLight))

                        // Items purchased in this order
                        order.items.forEach { itm ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(itm.product.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark))
                                    Text("${itm.selectedVariant.color} / ${itm.selectedVariant.size} • Qty ${itm.quantity}", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                                }
                                Text("UGX ${formatUgx(itm.totalPriceUgx)}", style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, fontWeight = FontWeight.Bold))
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderLight))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark))
                            Text("UGX ${formatUgx(order.subtotalUgx)}", style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, fontWeight = FontWeight.SemiBold))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Rider Delivery (${order.district})", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark))
                            Text("UGX ${formatUgx(order.deliveryFeeUgx)}", style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, fontWeight = FontWeight.SemiBold))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("TOTAL PAID", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                            Text("UGX ${formatUgx(order.totalUgx)}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = LimeGreen))
                        }
                    }
                }

                // If items still remain in cart
                if (uiState.items.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ElectricBlueBg)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "You have ${uiState.items.size} item(s) remaining in your cart for later purchase.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextDark, fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.dismissOrderReceipt()
                        onContinueShopping()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("continue_shopping_after_order_btn"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple, contentColor = PureWhite)
                ) {
                    Text("CONTINUE SHOPPING", fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
        return
    }

    // 2. Main Cart & Checkout Stream
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("cart_screen")
    ) {
        // Top Header
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (uiState.isCheckingOut) {
                            IconButton(
                                onClick = { viewModel.cancelCheckout() },
                                modifier = Modifier.testTag("checkout_back_btn")
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back to selection", tint = TextDark)
                            }
                        }
                        Column {
                            Text(
                                text = if (uiState.isCheckingOut) "CHECKOUT" else "SHOPPING CART",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp
                                ),
                                color = TextDark
                            )
                            if (!uiState.isCheckingOut && uiState.hasItems) {
                                Text(
                                    text = "${uiState.selectedItemsCount} of ${uiState.items.size} items selected (${uiState.selectedUnitsCount} units)",
                                    style = MaterialTheme.typography.labelSmall.copy(color = LimeGreen, fontWeight = FontWeight.Black)
                                )
                            }
                        }
                    }

                    if (!uiState.isCheckingOut && uiState.hasItems) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            if (uiState.selectedItemsCount > 0) {
                                Text(
                                    text = "REMOVE SELECTED (${uiState.selectedItemsCount})",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CoralRed,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 11.sp
                                    ),
                                    modifier = Modifier
                                        .clickable { viewModel.removeSelectedItems() }
                                        .testTag("remove_selected_btn")
                                        .padding(4.dp)
                                )
                            }
                            Text(
                                text = "CLEAR ALL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondaryDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier
                                    .clickable { viewModel.clearCart() }
                                    .testTag("clear_cart_btn")
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Empty Cart State
        if (!uiState.hasItems && !uiState.isCheckingOut) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp, start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(BrandPurpleBg)
                            .border(1.5.dp, BrandPurpleLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(44.dp))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Your cart is empty",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Select items from JOREZ drops to build your Ugandan streetwear look.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onContinueShopping,
                        colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("explore_shop_empty_btn")
                    ) {
                        Text("EXPLORE SHOP", fontWeight = FontWeight.Black)
                    }
                }
            }
            return@LazyColumn
        }

        // Cart Selection View (Not checking out)
        if (!uiState.isCheckingOut) {
            // Top Selection Bar (Select All Checkbox + Counter)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PureWhite)
                        .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("selection_control_bar")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { viewModel.selectAll(!uiState.isAllSelected) }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = uiState.isAllSelected,
                                onCheckedChange = { viewModel.selectAll(it) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = LimeGreen,
                                    checkmarkColor = PureWhite,
                                    uncheckedColor = TextSecondaryDark
                                ),
                                modifier = Modifier.size(32.dp).testTag("select_all_checkbox")
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (uiState.isAllSelected) "Deselect All" else "Select All Items (${uiState.totalCartUnitsCount})",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                            )
                        }

                        // Selected count pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (uiState.hasSelectedItems) LimeGreenBg else LightSurfaceSubtle,
                            border = BorderStroke(1.dp, if (uiState.hasSelectedItems) LimeGreen else BorderLight)
                        ) {
                            Text(
                                text = "${uiState.selectedItemsCount} Selected",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (uiState.hasSelectedItems) LimeGreen else TextSecondaryDark,
                                    fontWeight = FontWeight.Black
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Unselected notice if some items are left out
            if (uiState.unselectedItemsCount > 0 && uiState.hasSelectedItems) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ElectricBlueBg)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${uiState.unselectedItemsCount} unselected item(s) will stay in your cart for next time.",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextDark, fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }
            }

            // Validation message (e.g. attempting to checkout with 0 selected items)
            if (uiState.validationMessage != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CoralRedBg)
                            .border(1.dp, CoralRed, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                            .testTag("cart_validation_banner")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = CoralRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.validationMessage ?: "",
                                style = MaterialTheme.typography.bodySmall.copy(color = CoralRed, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // List of cart items with selection checkbox & controls
            items(uiState.items, key = { it.id }) { itemUi ->
                CartItemRow(
                    itemUi = itemUi,
                    onToggleSelect = { viewModel.toggleItemSelection(itemUi.id) },
                    onIncrease = { viewModel.increaseQuantity(itemUi.id) },
                    onDecrease = { viewModel.decreaseQuantity(itemUi.id) },
                    onRemove = { viewModel.removeItem(itemUi.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Subtotal and Proceed CTA
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                            .testTag("selection_summary_card")
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "SELECTION SUMMARY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = BrandPurple,
                                    letterSpacing = 1.sp
                                )
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Selected Products", color = TextSecondaryDark)
                                Text("${uiState.selectedItemsCount} items (${uiState.selectedUnitsCount} units)", color = TextDark, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Selected Subtotal", color = TextSecondaryDark)
                                Text("UGX ${formatUgx(uiState.subtotalUgx)}", color = TextDark, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Estimated Delivery", color = TextSecondaryDark)
                                Text(
                                    if (uiState.hasSelectedItems) "UGX ${formatUgx(uiState.deliveryFeeUgx)}" else "UGX 0",
                                    color = TextDark
                                )
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderLight))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Payable Total", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                                Text(
                                    "UGX ${formatUgx(uiState.grandTotalUgx)}",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = LimeGreen)
                                )
                            }

                            if (!uiState.hasSelectedItems) {
                                Text(
                                    text = "Select at least 1 item using checkboxes to checkout.",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMutedDark)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Checkout Button for Selected Items
                    Button(
                        onClick = { viewModel.proceedToCheckout() },
                        enabled = uiState.hasSelectedItems,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("proceed_to_checkout_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LimeGreen,
                            contentColor = PureWhite,
                            disabledContainerColor = BorderLight,
                            disabledContentColor = TextMutedDark
                        )
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp), tint = PureWhite)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.hasSelectedItems) "CHECKOUT (${uiState.selectedItemsCount} SELECTED)" else "SELECT ITEMS TO CHECKOUT",
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onContinueShopping,
                        modifier = Modifier.fillMaxWidth().height(44.dp).testTag("continue_shopping_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurple),
                        border = BorderStroke(1.dp, BrandPurple)
                    ) {
                        Text("CONTINUE SHOPPING", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        } else {
            // CHECKOUT FORM (Only for selected items!)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Preview of Selected Items Being Checked Out
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(1.5.dp, LimeGreen, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                            .testTag("checkout_selected_items_preview")
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SELECTED FOR CHECKOUT (${uiState.selectedItemsCount} ITEMS)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, color = LimeGreen)
                                )
                                Text(
                                    text = "Edit Selection",
                                    style = MaterialTheme.typography.labelSmall.copy(color = BrandPurple, fontWeight = FontWeight.Bold),
                                    modifier = Modifier.clickable { viewModel.cancelCheckout() }
                                )
                            }

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(uiState.selectedCartItems) { selItem ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(LightSurfaceSubtle)
                                            .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(
                                                selItem.product.name,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = TextDark),
                                                maxLines = 1
                                            )
                                            Text(
                                                "${selItem.selectedVariant.color}/${selItem.selectedVariant.size} • Qty ${selItem.quantity}",
                                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                                            )
                                            Text(
                                                "UGX ${formatUgx(selItem.totalPriceUgx)}",
                                                style = MaterialTheme.typography.labelSmall.copy(color = LimeGreen, fontWeight = FontWeight.Black)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Validation Message in Checkout
                    if (uiState.validationMessage != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                            .background(CoralRedBg)
                            .border(1.dp, CoralRed, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = CoralRed, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiState.validationMessage ?: "",
                                    style = MaterialTheme.typography.bodySmall.copy(color = CoralRed, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    Text("1. DELIVERY DETAILS (UGANDA)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))

                    // Full Name
                    OutlinedTextField(
                        value = uiState.fullName,
                        onValueChange = { viewModel.updateFullName(it) },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth().testTag("checkout_name_field"),
                        colors = outlinedFieldColors()
                    )

                    // Phone Numbers
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = uiState.phone,
                            onValueChange = { viewModel.updatePhone(it) },
                            label = { Text("Primary Phone (+256...)") },
                            modifier = Modifier.weight(1f).testTag("checkout_phone_field"),
                            colors = outlinedFieldColors()
                        )
                        OutlinedTextField(
                            value = uiState.altPhone,
                            onValueChange = { viewModel.updateAltPhone(it) },
                            label = { Text("Alt. Phone") },
                            modifier = Modifier.weight(1f).testTag("checkout_alt_phone_field"),
                            colors = outlinedFieldColors()
                        )
                    }

                    // District Dropdown
                    ExposedDropdownMenuBox(
                        expanded = districtExpanded,
                        onExpandedChange = { districtExpanded = !districtExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.district,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("District") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth().testTag("checkout_district_dropdown"),
                            colors = outlinedFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = districtExpanded,
                            onDismissRequest = { districtExpanded = false },
                            modifier = Modifier.background(PureWhite)
                        ) {
                            SampleData.ugandaDistricts.forEach { dist ->
                                DropdownMenuItem(
                                    text = { Text(dist, color = TextDark) },
                                    onClick = {
                                        viewModel.updateDistrict(dist)
                                        districtExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // City / Town & Address
                    OutlinedTextField(
                        value = uiState.cityTown,
                        onValueChange = { viewModel.updateCityTown(it) },
                        label = { Text("City / Town / Suburb (e.g. Ntinda, Kikuubo, Bugolobi)") },
                        modifier = Modifier.fillMaxWidth().testTag("checkout_city_field"),
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = uiState.address,
                        onValueChange = { viewModel.updateAddress(it) },
                        label = { Text("Street / Plot / Landmark") },
                        modifier = Modifier.fillMaxWidth().testTag("checkout_address_field"),
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = uiState.deliveryInstructions,
                        onValueChange = { viewModel.updateDeliveryInstructions(it) },
                        label = { Text("Delivery Instructions for Rider") },
                        modifier = Modifier.fillMaxWidth().testTag("checkout_instructions_field"),
                        colors = outlinedFieldColors()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("2. SELECT PAYMENT METHOD", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))

                    // Payment Method Radio Cards
                    PaymentMethod.values().forEach { method ->
                        val isSelected = uiState.selectedPaymentMethod == method
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(PureWhite)
                                .border(1.5.dp, if (isSelected) LimeGreen else BorderLight, RoundedCornerShape(10.dp))
                                .clickable { viewModel.selectPaymentMethod(method) }
                                .padding(14.dp)
                                .testTag("payment_method_${method.name}"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.selectPaymentMethod(method) },
                                colors = RadioButtonDefaults.colors(selectedColor = LimeGreen, unselectedColor = TextSecondaryDark)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = method.label,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                                )
                                when (method) {
                                    PaymentMethod.MTN_MOMO -> Text("Instant push prompt on +256 MTN line", color = Color(0xFFB45309), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    PaymentMethod.AIRTEL_MONEY -> Text("Instant push prompt on +256 Airtel line", color = AirtelRed, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    PaymentMethod.CASH_ON_DELIVERY -> Text("Available for Kampala Central & Entebbe only", color = TextSecondaryDark, fontSize = 11.sp)
                                    else -> Text("Secure digital checkout", color = TextSecondaryDark, fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Order Summary Breakdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Selected Items Subtotal", color = TextSecondaryDark)
                                Text("UGX ${formatUgx(uiState.subtotalUgx)}", color = TextDark, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Delivery Fee (${uiState.district})", color = TextSecondaryDark)
                                Text("UGX ${formatUgx(uiState.deliveryFeeUgx)}", color = TextDark)
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderLight))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("TOTAL TO PAY", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = TextDark))
                                Text("UGX ${formatUgx(uiState.grandTotalUgx)}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = LimeGreen))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pay & Complete Order CTA
                    Button(
                        onClick = { viewModel.placeOrder() },
                        enabled = !uiState.isProcessingPayment && uiState.hasSelectedItems,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("pay_and_complete_order_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LimeGreen, contentColor = PureWhite)
                    ) {
                        if (uiState.isProcessingPayment) {
                            CircularProgressIndicator(color = PureWhite, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("PROCESSING...", fontWeight = FontWeight.Black)
                        } else {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp), tint = PureWhite)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PAY UGX ${formatUgx(uiState.grandTotalUgx)} VIA ${uiState.selectedPaymentMethod.label.uppercase()}",
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    itemUi: CartItemUiModel,
    onToggleSelect: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    val item = itemUi.cartItem
    val isSelected = itemUi.isSelected

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(PureWhite)
            .border(
                1.5.dp,
                if (isSelected) BrandPurple else BorderLight,
                RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
            .testTag("cart_item_${itemUi.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox for selection before checkout
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggleSelect() },
            colors = CheckboxDefaults.colors(
                checkedColor = BrandPurple,
                checkmarkColor = PureWhite,
                uncheckedColor = TextSecondaryDark
            ),
            modifier = Modifier
                .size(40.dp)
                .testTag("checkbox_item_${itemUi.id}")
        )

        Spacer(modifier = Modifier.width(4.dp))

        // Thumbnail Emblem
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BrandPurpleBg)
                .border(1.dp, BrandPurpleLight, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            JorezEmblem(size = 28.dp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) TextDark else TextSecondaryDark,
                maxLines = 1
            )
            Text(
                text = "${item.selectedVariant.color} / ${item.selectedVariant.size} • Stock: ${item.selectedVariant.stock}",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "UGX ${formatUgx(item.totalPriceUgx)}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) LimeGreen else TextSecondaryDark
                )
            )
        }

        // Stepper
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(LightSurfaceSubtle, RoundedCornerShape(6.dp))
                .border(1.dp, BorderLight, RoundedCornerShape(6.dp))
        ) {
            IconButton(
                onClick = onDecrease,
                modifier = Modifier.size(28.dp).testTag("decrease_item_${itemUi.id}")
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity", tint = TextDark, modifier = Modifier.size(12.dp))
            }
            Text(
                text = "${item.quantity}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextDark),
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            IconButton(
                onClick = onIncrease,
                enabled = !itemUi.isMaxStockReached,
                modifier = Modifier.size(28.dp).testTag("increase_item_${itemUi.id}")
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase Quantity",
                    tint = if (itemUi.isMaxStockReached) TextMutedDark else TextDark,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        // Remove button
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .padding(start = 2.dp)
                .testTag("remove_item_${itemUi.id}")
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Remove item", tint = CoralRed, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = BrandPurple,
    unfocusedBorderColor = BorderLight,
    focusedTextColor = TextDark,
    unfocusedTextColor = TextDark,
    focusedLabelColor = BrandPurple,
    unfocusedLabelColor = TextSecondaryDark,
    focusedContainerColor = PureWhite,
    unfocusedContainerColor = PureWhite
)
