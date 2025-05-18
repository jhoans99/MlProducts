package com.sebas.detailproduct.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.sebas.core.ui.components.DialogComponent
import com.sebas.core.ui.components.Loader
import com.sebas.core.ui.components.PrimaryButton
import com.sebas.core.ui.components.SecondaryButton
import com.sebas.core.ui.components.TopBarComponent
import com.sebas.core.ui.theme.Black
import com.sebas.core.ui.theme.Blue
import com.sebas.core.ui.theme.GrayLight
import com.sebas.core.ui.theme.Green
import com.sebas.detailproduct.state.DetailsProductUiState
import com.sebas.domain.model.Product

@Composable
fun DetailProductRoute(
    viewModel: DetailProductViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit,
    onNavigateSearchProduct: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.saveRecentViewedProduct()
        viewModel.detailProductById()
    }

    when {
        uiState.isLoading -> Loader()

        uiState.isShowModalError -> {
            DialogComponent(
                stringResource(
                    com.sebas.core.R.string.title_modal_error
                ),
                stringResource(
                    com.sebas.detailproduct.R.string.message_error_get_detail_product
                ),
                isCancelAction = false,
                onDismissAction = {
                    viewModel.onUpdateValueModalError(false)
                },
                onConfirmAction = {
                    viewModel.onUpdateValueModalError(false)
                    onNavigationBack()
                }
            )
        }
    }

    DetailProductScreen(
        uiState,
        onNavigateSearchProduct = {
            onNavigateSearchProduct()
        },
        onNavBack = {
            onNavigationBack()
        }
    )
}

@Composable
fun DetailProductScreen(
    uiState: DetailsProductUiState,
    onNavBack: () -> Unit,
    onNavigateSearchProduct: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                title = stringResource(com.sebas.detailproduct.R.string.title_detail_product)
            ) {
                onNavBack()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DetailsProductBody(
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 20.dp),
                uiState.detailProduct
            ) {
                onNavigateSearchProduct()
            }
        }
    }
}

@Composable
fun DetailsProductBody(
    modifier: Modifier,
    detailProduct: Product,
    onNavigateSearchProduct: () -> Unit
) {
    ConstraintLayout(
        modifier.fillMaxSize()
    ) {
        val (textTitleProduct, pagerImages, indicatorLayout) = createRefs()
        val (textPrice, layoutRating, textState) = createRefs()
        val (textLabelDescription, textDescription, layoutRequestProduct) = createRefs()

        val pagerState = rememberPagerState { detailProduct.images.size }

        Text(
            stringResource(com.sebas.detailproduct.R.string.product_stock_info,detailProduct.stock),
            modifier = Modifier.constrainAs(
                textState
            ) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            style = MaterialTheme.typography.labelMedium,
            color = GrayLight
        )

        RatingBar(
            modifier = Modifier.constrainAs(
                layoutRating
            ) {
                top.linkTo(parent.top,5.dp)
                end.linkTo(parent.end,5.dp)
            },
            size = 15.dp,
            value = detailProduct.rating,
            style = RatingBarStyle.Stroke(
                activeColor = Blue
            ),
            onValueChange = {
            },
            onRatingChanged = {
            }
        )

        Text(
            text = detailProduct.title,
            modifier = Modifier.constrainAs(textTitleProduct) {
                top.linkTo(textState.bottom, 10.dp)
                start.linkTo(parent.start)
            },
            textAlign = TextAlign.Start,
            maxLines = 2,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.size(250.dp).constrainAs(pagerImages) {
                top.linkTo(textTitleProduct.bottom,20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) { page ->
            AsyncImage(
                model = detailProduct.images[page],
                contentDescription = null,
                modifier = Modifier,
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .constrainAs(indicatorLayout) {
                top.linkTo(pagerImages.bottom,5.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            repeat(detailProduct.images.size) {
                val color = if(pagerState.currentPage == it) Blue else GrayLight
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(8.dp)
                        .background(color)
                )
            }
        }

        Text(
            text = "$ ${detailProduct.price}",
            modifier = Modifier.constrainAs(textPrice) {
                top.linkTo(indicatorLayout.bottom,40.dp)
                start.linkTo(parent.start)
            },
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = Green
        )

        Text(
            stringResource(com.sebas.detailproduct.R.string.label_product_description),
            modifier = Modifier.constrainAs(textLabelDescription) {
                top.linkTo(textPrice.bottom,40.dp)
                start.linkTo(parent.start)
            },
            style = MaterialTheme.typography.titleMedium,
            color = Black
        )

        Text(
            detailProduct.description,
            modifier = Modifier.constrainAs(
                textDescription
            ) {
                top.linkTo(textLabelDescription.bottom,10.dp)
                start.linkTo(parent.start)
            },
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.labelLarge,
            color = GrayLight
        )

        LayoutRequestProduct(
            Modifier.constrainAs(layoutRequestProduct) {
                top.linkTo(textDescription.bottom, 10.dp)
                bottom.linkTo(parent.bottom, 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            onNavigateSearchProduct()
        }
    }
}

@Composable
fun LayoutRequestProduct(
    modifier: Modifier,
    onNavigateSearchProduct: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
    ) {
        PrimaryButton(
             textButton =  stringResource(com.sebas.detailproduct.R.string.text_button_buy_now),
             isEnable = true,
        ) {
            Toast.makeText(context, "Compraste el producto", Toast.LENGTH_SHORT).show()
            onNavigateSearchProduct()
        }

        Spacer(Modifier.height(8.dp))

        SecondaryButton(
            textButton = stringResource(com.sebas.detailproduct.R.string.text_button_add_card),
            isEnable = true
        ) {
            Toast.makeText(context, "Se agrego al carrito", Toast.LENGTH_SHORT).show()
            onNavigateSearchProduct()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsProductPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DetailsProductBody(
                Modifier,
                Product(
                    title = "Iphone",
                    description = "Iphone en en venta",
                    price = 12.9f,
                    rating = 4.4f,
                    images = listOf(
                        "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/1.webp"
                    )
                )
            ) {}
        }
    }
}