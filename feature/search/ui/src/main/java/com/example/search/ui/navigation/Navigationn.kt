package com.example.search.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.common.navigation.FeatureApi
import com.example.common.navigation.NavigationRoutes
import com.example.common.navigation.NavigationSubGraphRoute
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
            route =  NavigationSubGraphRoute.Search.route,
            startDestination = NavigationRoutes.RecipeList.route
        ){
            composable(route = NavigationRoutes.RecipeList.route){
                val viewModel = hiltViewModel<RecipeListViewModel>()
                RecipeListScreen(viewModel = viewModel, navHostController = navHostController){mealId->
                    viewModel.onEvent(RecipeList.Event.GotoRecipeDetails(mealId))
                }
            }

            composable(route = NavigationRoutes.RecipeDetails.route){

            }
        }
    }
}