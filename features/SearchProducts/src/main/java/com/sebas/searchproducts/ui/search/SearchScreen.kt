package com.sebas.searchproducts.ui.search

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sebas.core.ui.components.SearchInputText
import com.sebas.core.ui.components.TopBarComponent
import com.sebas.domain.model.Product
import com.sebas.searchproducts.state.SearchProductUiState

@Composable
fun SearchProductRoute(
    viewModel: SearchProductViewModel = hiltViewModel(),
    onNavigateResult: (String) -> Unit,
    onNavigateDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getIdRecentProduct()
    }

    when {
        uiState.isShowToastEmptyQuery -> {
            val messageError = stringResource(com.sebas.searchproducts.R.string.message_empty_query)
            Toast.makeText(context, messageError, Toast.LENGTH_SHORT).show()
            viewModel.onUpdateValueShowToast(false)
        }
    }

    SearchProductScreen(
        uiState,
        onNavigateResult = {
            onNavigateResult(uiState.query)
        },
        onNavigateDetail = {
            onNavigateDetail(it)
        }
    )
}

@Composable
fun SearchProductScreen(
    uiState: SearchProductUiState,
    onNavigateResult: () -> Unit,
    onNavigateDetail: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBarComponent(
                title = stringResource(com.sebas.searchproducts.R.string.title_search_product),
                isNavigation = false
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            SearchProductBody(
               modifier =  Modifier.fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 80.dp),
                uiState,
                onNavigateResult = {
                    onNavigateResult()
                },
                onNavigateDetail = {
                    onNavigateDetail(it)
                }
            )
        }
    }
}

@Composable
fun SearchProductBody(
    modifier: Modifier,
    uiState: SearchProductUiState,
    viewModel: SearchProductViewModel = hiltViewModel(),
    onNavigateResult: () -> Unit,
    onNavigateDetail: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(com.sebas.searchproducts.R.string.label_search_product),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        SearchInputText(
            uiState.query,
            onQueryChange = {
                viewModel.onUpdateQueryValue(it)
            },
            onSearch = {
                viewModel.onValidateSearchText {
                    onNavigateResult()
                }
            }
        )

        AnimatedVisibility(
            visible = uiState.detailProduct != null,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            uiState.detailProduct?.let {
                RecentViewProduct(Modifier,it) { id ->
                    onNavigateDetail(id)
                }
            }
        }
    }
}


@Composable
fun RecentViewProduct(
    modifier: Modifier,
    product: Product,
    navigateDetailsProduct: (String) -> Unit
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .clickable {
                navigateDetailsProduct(product.id.toString())
            },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = "Visto recientemente",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
            )

            AsyncImage(
                model = product.images[0],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(top = 8.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                product.title,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                "$${product.price}",
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchPreview() {
    SearchProductRoute(
        onNavigateDetail = {},
        onNavigateResult = {}
    )
}