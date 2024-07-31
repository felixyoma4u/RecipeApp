package com.example.search.data.repository

import com.example.search.data.mappers.toDomainRecipe
import com.example.search.data.mappers.toDomainRecipeDetails
import com.example.search.data.remote.SearchApiService
import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.model.Recipe
import com.example.search.domain.repository.SearchRecipeRepository

class SearchRecipeRepoImpl(
    private val searchApiService: SearchApiService
) : SearchRecipeRepository {
    override suspend fun getRecipes(s: String): Result<List<Recipe>> {
        return  try {
            val response = searchApiService.getRecipe(s)
             if (response.isSuccessful) {
                response.body()?.meals?.let {
                    Result.success(it.toDomainRecipe())
                } ?: run {
                    Result.failure(Exception("Something went wrong"))
                }
            } else {
                Result.failure(Exception("Something went wrong"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getRecipeDetails(id: String): Result<RecipeDetails> {
        return  try {
            val response = searchApiService.getRecipeDetail(id)
             if (response.isSuccessful) {
                response.body()?.meals?.let {
                    Result.success(it.first().toDomainRecipeDetails())
                } ?: run {
                    Result.failure(Exception("Something went wrong"))
                }
            } else {
                Result.failure(Exception("Something went wrong"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}