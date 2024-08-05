package com.example.common.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoutes(val route: String){
    data object RecipeList: NavigationRoutes(route = "/recipe_list")
    data object RecipeDetails: NavigationRoutes(route = "/recipe_details/{id}"){
        fun sendId(id: String) = "/recipe_details/${id}"
    }
    data object Favorite: NavigationRoutes(route = "/favorite")

    data object MediaPlayer: NavigationRoutes(route = "/player/{video_id}"){
        fun sendUrl(videoId: String) = "/player/$videoId"
    }
}

sealed class NavigationSubGraphRoute(val route: String){
    data object Search: NavigationSubGraphRoute(route = "/search")
    data object MediaPlayer: NavigationSubGraphRoute(route = "/media_player")
}

//sealed class SubGraphDest{
//
//    @Serializable
//    data object Search: SubGraphDest()
//
//    @Serializable
//    data object MediaPlayer: SubGraphDest()
//
//}
//
//sealed class DestinationRoute{
//
//    @Serializable
//    data object RecipeListScreen: DestinationRoute()
//
//    @Serializable
//    data class RecipeDetailsScreen(val id: String?): DestinationRoute()
//
//    @Serializable
//    data object FavoriteScreen: DestinationRoute()
//
//    @Serializable
//    data class MediaPlayerScreen(val videoId: String?): DestinationRoute()
//}
