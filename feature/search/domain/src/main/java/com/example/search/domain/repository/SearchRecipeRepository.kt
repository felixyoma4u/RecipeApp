package com.example.search.domain.repository

import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface SearchRecipeRepository {

    suspend fun getRecipes(s: String): Result<List<Recipe>>

    suspend fun getRecipeDetails(id: String): Result<RecipeDetails>

    suspend fun insertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

     fun getAllRecipe(): Flow<List<Recipe>>
}