package com.example.search.ui.screens.recipe_list

import com.example.common.utils.NetworkResult
import com.example.common.utils.UiText
import com.example.search.domain.model.Recipe
import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.use_case.GetAllRecipeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class RecipeListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun before() {

    }

    @After
    fun after() {

    }

    private val getAllRecipeUseCase: GetAllRecipeUseCase = mock()

    @Test
    fun `test success case`() = runTest {
        `when`(getAllRecipeUseCase.invoke("chicken"))
            .thenReturn(flowOf(NetworkResult.Success(data = getRecipeResponse())))

        val viewModel = RecipeListViewModel(getAllRecipeUseCase)

        viewModel.onEvent(RecipeList.Event.SearchRecipe("chicken"))

        assertEquals(getRecipeResponse(), viewModel.uiState.value.data)
    }

    @Test
    fun `test failure case`() = runTest {
        `when`(getAllRecipeUseCase.invoke("chicken"))
            .thenReturn(flowOf(NetworkResult.Error(message = "error")))

        val viewModel = RecipeListViewModel(getAllRecipeUseCase)

        viewModel.onEvent(RecipeList.Event.SearchRecipe("chicken"))

        assertEquals(UiText.RemoteString("error"), viewModel.uiState.value.error)
    }

    @Test
    fun `test navigation to recipe details`() = runTest {
        val viewModel = RecipeListViewModel(getAllRecipeUseCase)
        viewModel.onEvent(RecipeList.Event.GotoRecipeDetails("id"))

        val list = mutableListOf<RecipeList.Navigation>()

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.navigation.collectLatest {
                list.add(it)
            }
        }
        assert(list.first() is RecipeList.Navigation.GotoRecipeDetails)
    }

    @Test
    fun `test navigation to favorite screen`() = runTest {
        val viewModel = RecipeListViewModel(getAllRecipeUseCase)
        viewModel.onEvent(RecipeList.Event.FavoriteScreen)
        val list = mutableListOf<RecipeList.Navigation>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.navigation.collectLatest {
                list.add(it)
            }
        }
        assert(list.first() is RecipeList.Navigation.GotoFavoriteScreen)
    }
}


class MainDispatcherRule(private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher() {
    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
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