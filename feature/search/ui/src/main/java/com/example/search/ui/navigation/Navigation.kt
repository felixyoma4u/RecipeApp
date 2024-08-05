package com.example.search.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.common.navigation.FeatureApi
import com.example.common.navigation.NavigationRoutes
import com.example.common.navigation.NavigationSubGraphRoute
import com.example.search.ui.screens.favorite.FavoriteScreen
import com.example.search.ui.screens.favorite.FavoriteViewModel
import com.example.search.ui.screens.recipe_details.Details
import com.example.search.ui.screens.recipe_details.RecipeDetailScreen
import com.example.search.ui.screens.recipe_details.RecipeDetailsViewModel
import com.example.search.ui.screens.recipe_list.RecipeList
import com.example.search.ui.screens.recipe_list.RecipeListScreen
import com.example.search.ui.screens.recipe_list.RecipeListViewModel


interface SearchFeatureApi : FeatureApi

class SearchFeatureApiImpl : SearchFeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation(
            route = NavigationSubGraphRoute.Search.route,
            startDestination = NavigationRoutes.RecipeList.route
        ) {

            composable(route = NavigationRoutes.RecipeList.route) {
                val viewModel = hiltViewModel<RecipeListViewModel>()
                RecipeListScreen(
                    viewModel = viewModel,
                    navHostController = navHostController
                ) { mealId ->
                    viewModel.onEvent(RecipeList.Event.GotoRecipeDetails(mealId))
                }
            }

            composable(route = NavigationRoutes.RecipeDetails.route) {
                val viewModel = hiltViewModel<RecipeDetailsViewModel>()
                val mealId = it.arguments?.getString("id")
                LaunchedEffect(key1 = mealId) {
                    mealId?.let { id ->
                        viewModel.onEvent(Details.Event.FetchDetails(id))
                    }
                }
                RecipeDetailScreen(
                    viewModel = viewModel,
                    onNavigationClicked = {
                        viewModel.onEvent(Details.Event.GotoRecipeListScreen)
                    },
                    onDeleteClicked = {
                        viewModel.onEvent(Details.Event.DeleteRecipe(it))
                    },
                    onFavoriteClicked = {
                        viewModel.onEvent(Details.Event.InsertRecipe(it))
                    },
                    navHostController = navHostController
                )
            }

            composable(route = NavigationRoutes.Favorite.route){
                val viewModel = hiltViewModel<FavoriteViewModel>()
                FavoriteScreen(
                    navHostController = navHostController,
                    viewModel = viewModel,
                    onClick = { mealId ->
                        viewModel.onEvent(FavoriteScreen.Event.GotoDetails(mealId))
                    }
                )
            }
        }
    }
}