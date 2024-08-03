package com.example.search.domain.use_case

import com.example.search.domain.repository.SearchRecipeRepository
import javax.inject.Inject

class GetAllRecipesFromLocalDbUseCase @Inject constructor(
    private val searchRecipeRepository: SearchRecipeRepository
) {
    operator fun invoke() = searchRecipeRepository.getAllRecipe()
}