package com.example.ui.viewmodel

import com.example.data.JorezRepository
import com.example.data.SampleData
import com.example.model.PaymentMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: JorezRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = JorezRepository(initialCart = emptyList())
        viewModel = CartViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testAddItemAndAutoSelection() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        val product1 = SampleData.products[0]
        val variant1 = product1.variants[0]

        val added = viewModel.addItem(product1, variant1, quantity = 2)
        assertTrue(added)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertTrue(state.hasSelectedItems)
        assertEquals(1, state.selectedItemsCount)
        assertEquals(2, state.selectedUnitsCount)
        assertTrue(state.items[0].isSelected)
        val expectedSubtotal = (product1.discountPriceUgx ?: product1.priceUgx) * 2
        assertEquals(expectedSubtotal, state.subtotalUgx)
    }

    @Test
    fun testToggleSelectionUpdatesCheckoutSubtotal() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        val product1 = SampleData.products[0]
        val variant1 = product1.variants[0]
        val product2 = SampleData.products[1]
        val variant2 = product2.variants[0]

        viewModel.addItem(product1, variant1, quantity = 1)
        viewModel.addItem(product2, variant2, quantity = 1)
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertEquals(2, state.items.size)
        assertEquals(2, state.selectedItemsCount)

        val item1Id = "${product1.id}_${variant1.id}"
        viewModel.toggleItemSelection(item1Id)
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(1, state.selectedItemsCount)
        assertEquals(1, state.unselectedItemsCount)
        val item2ExpectedPrice = (product2.discountPriceUgx ?: product2.priceUgx) * 1
        assertEquals(item2ExpectedPrice, state.subtotalUgx)
    }

    @Test
    fun testRemoveItem() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        val product = SampleData.products[0]
        val variant = product.variants[0]

        viewModel.addItem(product, variant, quantity = 1)
        advanceUntilIdle()
        val itemId = "${product.id}_${variant.id}"

        viewModel.removeItem(itemId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0, state.items.size)
        assertEquals(0, state.selectedItemsCount)
        assertEquals(0L, state.subtotalUgx)
    }

    @Test
    fun testRemoveSelectedItems() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        val product1 = SampleData.products[0]
        val variant1 = product1.variants[0]
        val product2 = SampleData.products[1]
        val variant2 = product2.variants[0]

        viewModel.addItem(product1, variant1, quantity = 1)
        viewModel.addItem(product2, variant2, quantity = 1)
        advanceUntilIdle()

        val item1Id = "${product1.id}_${variant1.id}"
        val item2Id = "${product2.id}_${variant2.id}"
        viewModel.toggleItemSelection(item2Id)
        advanceUntilIdle()

        viewModel.removeSelectedItems()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals(item2Id, state.items[0].id)
    }

    @Test
    fun testProceedToCheckoutRequiresSelection() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        val product = SampleData.products[0]
        val variant = product.variants[0]
        viewModel.addItem(product, variant, quantity = 1)
        advanceUntilIdle()

        val itemId = "${product.id}_${variant.id}"
        viewModel.toggleItemSelection(itemId)
        advanceUntilIdle()

        val proceedSuccess = viewModel.proceedToCheckout()
        assertFalse(proceedSuccess)
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.validationMessage)
        assertFalse(viewModel.uiState.value.isCheckingOut)

        viewModel.toggleItemSelection(itemId)
        advanceUntilIdle()

        val retrySuccess = viewModel.proceedToCheckout()
        assertTrue(retrySuccess)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isCheckingOut)
    }

    @Test
    fun testPlaceOrderOnlyOrdersSelectedItems() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        val product1 = SampleData.products[0]
        val variant1 = product1.variants[0]
        val product2 = SampleData.products[1]
        val variant2 = product2.variants[0]

        viewModel.addItem(product1, variant1, quantity = 1)
        viewModel.addItem(product2, variant2, quantity = 1)
        advanceUntilIdle()

        val item1Id = "${product1.id}_${variant1.id}"
        val item2Id = "${product2.id}_${variant2.id}"

        viewModel.toggleItemSelection(item2Id)
        advanceUntilIdle()

        viewModel.proceedToCheckout()
        viewModel.updateFullName("Julius Maseruka")
        viewModel.updatePhone("+256 772 123456")
        viewModel.updateAddress("Plot 10 Kampala Road")
        viewModel.selectPaymentMethod(PaymentMethod.MTN_MOMO)
        advanceUntilIdle()

        viewModel.placeOrder()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.placedOrder)
        assertEquals(1, state.placedOrder?.items?.size)
        assertEquals(product1.id, state.placedOrder?.items?.get(0)?.product?.id)

        assertEquals(1, state.items.size)
        assertEquals(item2Id, state.items[0].id)
    }
}
