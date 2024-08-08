package com.example.search.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                val navigation = viewModel.navigation
                RecipeListScreen(
                    navigation = navigation,
                    uiState = uiState.value,
                    navHostController = navHostController,
                    onEvent = { event ->
                        when (event) {
                            RecipeList.Event.FavoriteScreen -> viewModel.onEvent(RecipeList.Event.FavoriteScreen)
                            is RecipeList.Event.GotoRecipeDetails -> viewModel.onEvent(
                                RecipeList.Event.GotoRecipeDetails(
                                    event.id
                                )
                            )

                            is RecipeList.Event.SearchRecipe -> viewModel.onEvent(
                                RecipeList.Event.SearchRecipe(
                                    event.q
                                )
                            )
                        }
                    }
                )
            }

            composable(route = NavigationRoutes.RecipeDetails.route) {
                val viewModel = hiltViewModel<RecipeDetailsViewModel>()
                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                val navigation = viewModel.navigation
                val mealId = it.arguments?.getString("id")
                LaunchedEffect(key1 = mealId) {
                    mealId?.let { id ->
                        viewModel.onEvent(Details.Event.FetchDetails(id))
                    }
                }
                RecipeDetailScreen(
                    navHostController = navHostController,
                    uiState = uiState.value,
                    navigation = navigation,
                    onEvent = { event ->
                        when (event) {
                            is Details.Event.DeleteRecipe -> viewModel.onEvent(
                                Details.Event.DeleteRecipe(
                                    event.recipeDetails
                                )
                            )

                            is Details.Event.FetchDetails -> viewModel.onEvent(
                                Details.Event.FetchDetails(
                                    event.id
                                )
                            )

                            is Details.Event.GotoMediaPlayer -> viewModel.onEvent(
                                Details.Event.GotoMediaPlayer(
                                    event.youtubeUrl
                                )
                            )

                            Details.Event.GotoRecipeListScreen -> viewModel.onEvent(Details.Event.GotoRecipeListScreen)
                            is Details.Event.InsertRecipe -> viewModel.onEvent(
                                Details.Event.InsertRecipe(
                                    event.recipeDetails
                                )
                            )
                        }
                    }
                )
            }

            composable(route = NavigationRoutes.Favorite.route) {
                val viewModel = hiltViewModel<FavoriteViewModel>()
                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                val navigation = viewModel.navigation
                FavoriteScreen(
                    navHostController = navHostController,
                    uiState = uiState.value,
                    navigation = navigation,
                    onEvent = { event ->
                        when (event) {
                            FavoriteScreen.Event.AlphabeticalSort -> viewModel.onEvent(
                                FavoriteScreen.Event.AlphabeticalSort
                            )

                            is FavoriteScreen.Event.DeleteRecipe -> viewModel.onEvent(
                                FavoriteScreen.Event.DeleteRecipe(
                                    event.recipe
                                )
                            )

                            is FavoriteScreen.Event.GotoDetails -> viewModel.onEvent(
                                FavoriteScreen.Event.GotoDetails(
                                    event.id
                                )
                            )

                            FavoriteScreen.Event.LessIngredientSort -> viewModel.onEvent(
                                FavoriteScreen.Event.LessIngredientSort
                            )

                            FavoriteScreen.Event.ResetSort -> viewModel.onEvent(FavoriteScreen.Event.ResetSort)
                            is FavoriteScreen.Event.ShowDetails -> viewModel.onEvent(
                                FavoriteScreen.Event.ShowDetails(
                                    event.id
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}