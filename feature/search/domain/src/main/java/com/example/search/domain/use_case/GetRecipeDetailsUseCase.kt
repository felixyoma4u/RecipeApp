package com.example.search.domain.use_case

import com.example.common.utils.NetworkResult
import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.repository.SearchRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRecipeDetailsUseCase @Inject constructor(
    private val searchRecipeRepository: SearchRecipeRepository
) {

    operator fun invoke(id:String) = flow<NetworkResult<RecipeDetails>> {
        emit(NetworkResult.Loading())
        val response = searchRecipeRepository.getRecipeDetails(id)
        if (response.isSuccess){
            emit(NetworkResult.Success(response.getOrThrow()))
        }else{
            emit(NetworkResult.Error(response.exceptionOrNull()?.localizedMessage))
        }
    }.catch {
        emit(NetworkResult.Error(it.message))

    }.flowOn(Dispatchers.IO)
}