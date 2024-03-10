package com.danielp4.productapp.presentation.productlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.danielp4.productapp.data.models.ProductListEntry
import com.danielp4.productapp.util.Routes
import com.danielp4.productapp.util.UiEvent

@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductListViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Navigate -> {
                    navController.navigate(uiEvent.route)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            ProductList()
        }
    }
}

@Composable
fun ProductList(
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val productList by remember {
        viewModel.productList
    }
    val endReached by remember {
        viewModel.endReached
    }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(productList.size) {
            if (it >= productList.size - 1 && !endReached && !isLoading) {
                LaunchedEffect(key1 = true) {
                    viewModel.loadProductPaginated()
                }
            }
            ProductEntry(entry = productList[it])
            Spacer(modifier = Modifier.height(5.dp))
        }
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.error
            )
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadProductPaginated()
            }
        }
    }

}

@Composable
fun ProductEntry(
    entry: ProductListEntry,
    viewModel: ProductListViewModel = hiltViewModel()
) {

    val constrains = ConstraintSet {
        val imageProduct = createRefFor("imageProduct")
        val titleProduct = createRefFor("titleProduct")
        val descProduct = createRefFor("descProduct")
        val brand = createRefFor("brand")
        val price = createRefFor("price")

        constrain(imageProduct) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
        constrain(titleProduct) {
            top.linkTo(imageProduct.top)
            start.linkTo(imageProduct.end, 20.dp)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
        constrain(price) {
            top.linkTo(titleProduct.bottom)
            start.linkTo(imageProduct.end, 20.dp)
            end.linkTo(parent.end)
        }
        constrain(brand) {
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
        constrain(descProduct) {
            top.linkTo(imageProduct.bottom, 7.dp)
            start.linkTo(imageProduct.start)
            end.linkTo(parent.end)
            bottom.linkTo(brand.top, 3.dp)
            width = Dimension.fillToConstraints
        }
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable {
                viewModel.onEvent(ProductListEvent.OnProductClick(Routes.PRODUCT_INFO + "/${entry.id}"))
            }
    ) {
        ConstraintLayout(
            constraintSet = constrains,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = entry.description,
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.scale(0.5F)
                    )
                },
                success = { success ->
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .width(200.dp)
                    .layoutId("imageProduct"),
                contentScale = ContentScale.Crop
            )
            Text(
                text = entry.title,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                softWrap = true,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .layoutId("titleProduct")
            )
            Text(
                text = entry.description,
                fontSize = 13.sp,
                maxLines = 4,
                lineHeight = 20.sp,
                softWrap = true,
                modifier = Modifier
                    .layoutId("descProduct")
            )
            Text(
                text = entry.brand,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .layoutId("brand")
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
            )
            Text(
                text = entry.price.toString()+"$",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                maxLines = 4,
                softWrap = true,
                modifier = Modifier
                    .layoutId("price")
            )
        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(
            text = error,
            color = Color.Red,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(
                text = "Перезагрузить",
                color = Color.White
            )
        }
    }

}