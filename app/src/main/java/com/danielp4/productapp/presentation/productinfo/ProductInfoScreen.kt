package com.danielp4.productapp.presentation.productinfo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.danielp4.productapp.R
import com.danielp4.productapp.data.remote.responses.Product
import com.danielp4.productapp.presentation.productlist.RetrySection
import com.danielp4.productapp.ui.theme.Orange

@Composable
fun ProductInfoScreen(
    viewModel: ProductInfoViewModel = hiltViewModel()
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (viewModel.isLoading.value) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.error
            )
        }
        if (viewModel.loadError.value.isNotEmpty()) {
            RetrySection(error = viewModel.loadError.value) {
                viewModel.loadProductInfo()
            }
        }
    }
    viewModel.product?.let {
        ProductInfo(product = it)
    }
}

@Composable
fun ProductInfo(
    product: Product
) {
    val constrains = ConstraintSet {
        val imageProduct = createRefFor("imageProduct")
        val titleProduct = createRefFor("titleProduct")
        val descProduct = createRefFor("descProduct")
        val ratingProduct = createRefFor("ratingProduct")
        val ratingText = createRefFor("ratingText")
        val price = createRefFor("price")
        val brand = createRefFor("brand")
        val category = createRefFor("category")
        val stock = createRefFor("stock")
        val discountPercentage = createRefFor("discountPercentage")

        constrain(imageProduct) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(titleProduct) {
            top.linkTo(imageProduct.bottom, 10.dp)
            start.linkTo(imageProduct.start, 10.dp)
            end.linkTo(price.start, 5.dp)
            width = Dimension.fillToConstraints
        }
        constrain(price) {
            top.linkTo(discountPercentage.top)
            end.linkTo(discountPercentage.start, 5.dp)
        }
        constrain(discountPercentage) {
            top.linkTo(titleProduct.top)
            end.linkTo(parent.end, 10.dp)
        }
        constrain(ratingProduct) {
            top.linkTo(titleProduct.bottom, 7.dp)
            start.linkTo(titleProduct.start)
        }
        constrain(ratingText) {
            top.linkTo(ratingProduct.top)
            start.linkTo(ratingProduct.end, 5.dp)
        }
        constrain(descProduct) {
            top.linkTo(ratingText.bottom, 7.dp)
            start.linkTo(ratingProduct.start)
            end.linkTo(parent.end, 10.dp)
            width = Dimension.fillToConstraints
        }
        constrain(category) {
            bottom.linkTo(parent.bottom, 10.dp)
            end.linkTo(parent.end, 10.dp)
            width = Dimension.fillToConstraints
        }
        constrain(brand) {
            bottom.linkTo(category.top, 5.dp)
            end.linkTo(category.end)
            width = Dimension.fillToConstraints
        }
        constrain(stock) {
            start.linkTo(titleProduct.start)
            bottom.linkTo(parent.bottom, 10.dp)
            width = Dimension.fillToConstraints
        }
    }

    ConstraintLayout(
        constraintSet = constrains,
        modifier = Modifier
            .padding(10.dp)
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White).fillMaxSize()
    ) {
        Carousel(
            images = product.images,
            pageCount = product.images.size,
            modifier = Modifier.layoutId("imageProduct")
        )
        Text(
            text = product.title,
            fontSize = 25.sp,
            maxLines = 2,
            softWrap = true,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .layoutId("titleProduct")
        )
        Text(
            text = product.description,
            fontSize = 20.sp,
            maxLines = 4,
            lineHeight = 20.sp,
            softWrap = true,
            modifier = Modifier
                .layoutId("descProduct")
        )
        Text(
            text = product.rating.toString(),
            fontSize = 17.sp,
            lineHeight = 20.sp,
            modifier = Modifier
                .layoutId("ratingText")
        )
        Text(
            text = product.price.toString()+"$",
            fontSize = 25.sp,
            modifier = Modifier
                .layoutId("price")
        )
        Text(
            text = " / -"+product.discountPercentage.toString()+"%",
            fontSize = 25.sp,
            color = Color.Red,
            modifier = Modifier
                .layoutId("discountPercentage")
        )
        RatingBar(
            modifier = Modifier.layoutId("ratingProduct"),
            rating = product.rating
        )
        Text(
            text = product.brand,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .layoutId("brand")
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
        )
        Text(
            text = product.category,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .layoutId("category")
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
        )
        Text(
            text = "Осталось: "+product.stock.toString(),
            fontSize = 17.sp,
            softWrap = true,
            modifier = Modifier
                .layoutId("stock")
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    images: List<String>?,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()

    Column(
        modifier = modifier
    ) {
        HorizontalPager(
            pageCount = pageCount,
            state = pagerState,
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier
        ) { page ->
            Column {
                Image(
                    painter = rememberAsyncImagePainter(model = images?.get(page)),
                    contentDescription = "Карусель",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(325.dp)
                        .clip(RoundedCornerShape(7.dp))
                )
            }
        }
        Row(
            Modifier
                .height(25.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double,
    stars: Int = 5,
    starsColor: Color = Orange,
) {
    val filledStars = kotlin.math.floor(rating).toInt()
    val unfilledStars = (stars - kotlin.math.ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
        if (halfStar) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_half),
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}