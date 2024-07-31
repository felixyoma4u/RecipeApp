package com.example.search.domain.use_case

import com.example.common.utils.NetworkResult
import com.example.search.domain.model.Recipe
import com.example.search.domain.repository.SearchRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllRecipeUseCase @Inject constructor(
    private val searchRecipeRepository: SearchRecipeRepository
){
    operator fun invoke(s: String) = flow<NetworkResult<List<Recipe>>> {
        emit(NetworkResult.Loading())
        val response = searchRecipeRepository.getRecipes(s)
        if (response.isSuccess){
            emit(NetworkResult.Success(response.getOrThrow()))
        }else{
            emit(NetworkResult.Error(response.exceptionOrNull()?.localizedMessage))
        }
    }.catch {
        emit(NetworkResult.Error(it.message))
    }.flowOn(Dispatchers.IO)

}