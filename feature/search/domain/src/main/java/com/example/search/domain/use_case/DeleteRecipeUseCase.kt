package com.example.search.domain.use_case

import com.example.search.domain.model.Recipe
import com.example.search.domain.repository.SearchRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteRecipeUseCase @Inject constructor(
    private val searchRecipeRepository: SearchRecipeRepository
) {

    operator fun invoke(recipe: Recipe) = flow<Unit> {
        searchRecipeRepository.deleteRecipe(recipe)
    }.flowOn(Dispatchers.IO)
}