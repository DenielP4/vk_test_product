package com.danielp4.productapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danielp4.productapp.presentation.productinfo.ProductInfoScreen
import com.danielp4.productapp.presentation.productlist.ProductListScreen
import com.danielp4.productapp.ui.theme.ProductAppTheme
import com.danielp4.productapp.util.Routes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.PRODUCT_LIST
                ) {
                    composable(Routes.PRODUCT_LIST) {
                        ProductListScreen(navController = navController)
                    }
                    composable(
                        Routes.PRODUCT_INFO + "/{idProduct}",
                        arguments = listOf(
                            navArgument("idProduct") {
                                type = NavType.IntType
                            }
                        )
                    ) {
                        ProductInfoScreen()
                    }
                }
            }
        }
    }
}




