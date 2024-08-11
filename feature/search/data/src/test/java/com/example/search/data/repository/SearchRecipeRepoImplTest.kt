package com.example.search.data.repository

import com.example.search.data.local.RecipeDao
import com.example.search.data.mappers.toDomainRecipe
import com.example.search.data.model.RecipeDTO
import com.example.search.data.model.RecipeResponse
import com.example.search.data.remote.SearchApiService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class SearchRecipeRepoImplTest {

    private val searchApiService: SearchApiService = mock()
    private val recipeDao: RecipeDao = mock()

    @Test
    fun `test success for getRecipe api call`() = runTest {
        `when`(searchApiService.getRecipe("chicken"))
            .thenReturn(Response.success(200, getRecipeResponse()))

        val repo = SearchRecipeRepoImpl(searchApiService, recipeDao)
        val response = repo.getRecipes("chicken")

        assertEquals(getRecipeResponse().meals?.toDomainRecipe(), response.getOrThrow())
    }

    @Test
    fun `test null meal from backend getRecipe api call`() = runTest {
        `when`(searchApiService.getRecipe("chicken"))
            .thenReturn(Response.success(200, RecipeResponse()))

        val repo = SearchRecipeRepoImpl(searchApiService, recipeDao)

        val response = repo.getRecipes("chicken")
        val message = "Something went wrong"
        assertEquals(message, response.exceptionOrNull()?.message)
    }

    @Test
    fun `test failure from getRecipe api call`() = runTest {
        `when`(searchApiService.getRecipe("chicken"))
            .thenReturn(Response.error(404, ResponseBody.create(null, "")))

        val repo = SearchRecipeRepoImpl(searchApiService, recipeDao)
        val response = repo.getRecipes("chicken")
        val message = "Something went wrong"
        assertEquals(message, response.exceptionOrNull()?.message)
    }

    @Test
    fun `test getrecipe throw exception`() = runTest {
        `when`(searchApiService.getRecipe("chicken"))
            .thenThrow(RuntimeException("error"))

        val repo = SearchRecipeRepoImpl(searchApiService, recipeDao)
        val response = repo.getRecipes("chicken")
        val message = "error"
        assertEquals(message, response.exceptionOrNull()?.message)
    }

}


private fun getRecipeResponse(): RecipeResponse {
    return RecipeResponse(
        meals = listOf(
            RecipeDTO(
                dateModified = null,
                idMeal = "idMeal",
                strArea = "India",
                strCategory = "category",
                strYoutube = "strYoutube",
                strTags = "tag1,tag2",
                strMeal = "Chicken",
                strSource = "strSource",
                strMealThumb = "strMealThumb",
                strInstructions = "strInstructions",
                strCreativeCommonsConfirmed = null,
                strIngredient1 = null,
                strIngredient2 = null,
                strIngredient3 = null,
                strIngredient4 = null,
                strIngredient5 = null,
                strIngredient6 = null,
                strIngredient7 = null,
                strIngredient8 = null,
                strIngredient9 = null,
                strIngredient10 = null,
                strIngredient11 = null,
                strIngredient12 = null,
                strIngredient13 = null,
                strIngredient14 = null,
                strIngredient15 = null,
                strIngredient16 = null,
                strIngredient17 = null,
                strIngredient18 = null,
                strIngredient19 = null,
                strIngredient20 = null,
                strMeasure1 = null,
                strMeasure2 = null,
                strMeasure3 = null,
                strMeasure4 = null,
                strMeasure5 = null,
                strMeasure6 = null,
                strMeasure7 = null,
                strMeasure8 = null,
                strMeasure9 = null,
                strMeasure10 = null,
                strMeasure11 = null,
                strMeasure12 = null,
                strMeasure13 = null,
                strMeasure14 = null,
                strMeasure15 = null,
                strMeasure16 = null,
                strMeasure17 = null,
                strMeasure18 = null,
                strMeasure19 = null,
                strMeasure20 = null,
                strDrinkAlternate = null,
                strImageSource = "empty"
            )
        )
    )
}