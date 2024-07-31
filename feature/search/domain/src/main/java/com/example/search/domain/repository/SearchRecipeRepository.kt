package com.example.search.domain.repository

import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.model.Recipe

interface SearchRecipeRepository {

    suspend fun getRecipes(s: String): Result<List<Recipe>>

    suspend fun getRecipeDetails(id: String): Result<RecipeDetails>
}