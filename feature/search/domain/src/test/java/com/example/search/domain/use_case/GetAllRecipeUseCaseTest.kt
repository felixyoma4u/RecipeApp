package com.example.search.domain.use_case

import com.example.search.domain.model.Recipe
import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.repository.SearchRecipeRepository
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetAllRecipeUseCaseTest {

    private val searchRecipeRepository: SearchRecipeRepository = mock()

    @Test
    fun `Test success case`() = runTest {
        `when`(searchRecipeRepository.getRecipes("chicken"))
            .thenReturn(
                Result.success(getRecipeResponse())
            )
        val response = GetAllRecipeUseCase(searchRecipeRepository).invoke("chicken")

        assertEquals(getRecipeResponse(), response.last().data)
    }

    @Test
    fun `Test failure case`() = runTest {
        `when`(searchRecipeRepository.getRecipes("chicken"))
            .thenReturn(
                Result.failure(RuntimeException("Error"))
            )

        val response = GetAllRecipeUseCase(searchRecipeRepository).invoke("chicken")

        assertEquals("Error", response.last().message)
    }

    @Test
    fun `Test exception case`() = runTest {
        `when`(searchRecipeRepository.getRecipes("chicken"))
            .thenThrow(RuntimeException("Error"))

        val response = GetAllRecipeUseCase(searchRecipeRepository).invoke("chicken")

        assertEquals("Error", response.last().message)
    }


}


private fun getRecipeResponse(): List<Recipe> {
    return listOf(
        Recipe(
            idMeal = "idMeal",
            strArea = "India",
            strCategory = "category",
            strYoutube = "strYoutube",
            strTags = "tag1,tag2",
            strMeal = "Chicken",
            strMealThumb = "strMealThumb",
            strInstructions = "12",
        ),
        Recipe(
            idMeal = "idMeal",
            strArea = "India",
            strCategory = "category",
            strYoutube = "strYoutube",
            strTags = "tag1,tag2",
            strMeal = "Chicken",
            strMealThumb = "strMealThumb",
            strInstructions = "123",
        )
    )

}

private fun getRecipeDetails(): RecipeDetails {
    return RecipeDetails(
        idMeal = "idMeal",
        strArea = "India",
        strCategory = "category",
        strYoutube = "strYoutube",
        strTags = "tag1,tag2",
        strMeal = "Chicken",
        strMealThumb = "strMealThumb",
        strInstructions = "strInstructions",
        ingredientsPairs = listOf(Pair("Ingredients", "Measure"))
    )
}