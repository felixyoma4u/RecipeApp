package com.example.search.ui.screens.recipe_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.NetworkResult
import com.example.common.utils.UiText
import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.use_case.GetRecipeDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(Details.UiState())
    val uiState: StateFlow<Details.UiState> get() = _uiState.asStateFlow()

    fun onEvent(event: Details.Event){
        when(event){
            is Details.Event.FetchDetails -> {recipeDetails(event.id)}
        }
    }

    private fun recipeDetails(id: String) = getRecipeDetailsUseCase.invoke(id)
        .onEach { result ->
            when(result){
                is NetworkResult.Loading -> {
                    _uiState.update { Details.UiState(isLoading = true) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { Details.UiState(error = UiText.RemoteString(result.message.toString())) }
                }
                is NetworkResult.Success -> {
                    _uiState.update { Details.UiState(data = result.data ) }
                }
            }
        }.launchIn(viewModelScope)

}

object Details{

    data class UiState(
        val isLoading: Boolean = false,
        val error: UiText = UiText.Idle,
        val data: RecipeDetails? = null,
    )

    sealed interface Navigation{

    }

    sealed interface Event{
        data class FetchDetails(val id: String): Event
    }
}