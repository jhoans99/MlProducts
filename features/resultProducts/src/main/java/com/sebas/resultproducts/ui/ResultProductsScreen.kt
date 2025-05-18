package com.sebas.resultproducts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.sebas.core.ui.components.DialogComponent
import com.sebas.core.ui.components.Loader
import com.sebas.core.ui.components.TopBarComponent
import com.sebas.core.ui.theme.Black
import com.sebas.core.ui.theme.Blue
import com.sebas.core.ui.theme.GrayLight
import com.sebas.core.ui.theme.Green
import com.sebas.domain.model.Product
import com.sebas.resultproducts.state.ResultProductUiState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ResultProductRoute(
    viewModel: ResultProductViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit,
    onNavigationBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.onLoadInitData()
        viewModel.fetchProducts()
    }
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading && uiState.listProduct.isEmpty() -> Loader()
        uiState.showModalError -> {
            DialogComponent(
                stringResource(
                    com.sebas.core.R.string.title_modal_error
                ),
                stringResource(
                    com.sebas.resultproducts.R.string.message_error_get_products
                ),
                isCancelAction = true,
                textDismissButton = stringResource(
                    com.sebas.core.R.string.text_button_exit
                ),
                onConfirmAction = {
                    viewModel.onUpdateShowModalError(false)
                },
                onDismissAction = {
                    viewModel.onUpdateShowModalError(false)
                    onNavigationBack()
                }
            )
        }
    }


    ResultProductScreen(
        uiState,
        onNavigateToDetails = {
            onNavigateToDetails(it)
        },
        onNavigationBack = {
            onNavigationBack()
        }
    )
}

@Composable
fun ResultProductScreen(
    uiState: ResultProductUiState,
    onNavigateToDetails: (String) -> Unit,
    onNavigationBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                title = stringResource(com.sebas.resultproducts.R.string.title_result_products)
            ) {
                onNavigationBack()
            }
        }
    ) { paddingValues ->
        Column(
           modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            ResultProductBody(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 15.dp),
                uiState
            ) {
                onNavigateToDetails(it)
            }
        }
    }
}

@Composable
fun ResultProductBody(
    modifier: Modifier,
    uiState: ResultProductUiState,
    viewModel: ResultProductViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit
) {
    val listState = rememberLazyListState()
    Column(
        modifier.fillMaxWidth()
    ) {
        Text(
            text = uiState.query,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Black
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.listProduct, key = { it.id }) { products ->
                ProductsItem(products, modifier) {
                    onNavigateToDetails(it)
                }
            }

            if (uiState.isLoading && uiState.listProduct.isNotEmpty()) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .distinctUntilChanged()
                .collect { lastVisibleIndex ->
                    val totalItems = listState.layoutInfo.totalItemsCount
                    if (lastVisibleIndex == totalItems - 3) {
                        viewModel.fetchProducts()
                    }
                }
        }
    }
}

@Composable
fun ProductsItem(
    products: Product,
    modifier: Modifier,
    onProductSelected: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onProductSelected(products.id.toString())
            },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = products.images[0],
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = products.title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 5.dp)
                )

                Text(
                    text = products.brand,
                    style = MaterialTheme.typography.labelMedium,
                    color = GrayLight,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 5.dp)
                )

                Text(
                    text = "$${products.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green,
                    modifier = Modifier.padding(top = 5.dp)
                )


                RatingBar(
                    modifier = Modifier.padding(top = 10.dp),
                    size = 20.dp,
                    value = products.rating,
                    style = RatingBarStyle.Stroke(
                        activeColor = Blue
                    ),
                    onValueChange = {
                    },
                    onRatingChanged = {
                    }
                )
            }
        }
    }
}