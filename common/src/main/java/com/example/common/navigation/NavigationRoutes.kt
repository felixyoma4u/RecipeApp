package com.example.common.navigation

sealed class NavigationRoutes(val route: String){
    data object RecipeList: NavigationRoutes(route = "/recipe_list")
    data object RecipeDetails: NavigationRoutes(route = "/recipe_details/{id}"){
        fun sendId(id: String) = "/recipe_details/${id}"
    }
}

sealed class NavigationSubGraphRoute(val route: String){
    data object Search: NavigationSubGraphRoute(route = "/search")
}