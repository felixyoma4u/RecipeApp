package com.example.search.data.di

import com.example.search.data.remote.SearchApiService
import com.example.search.data.repository.SearchRecipeRepoImpl
import com.example.search.domain.repository.SearchRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://api.github.com/"

@InstallIn(SingletonComponent::class)
@Module
object SearchDataModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }

    @Provides
    fun provideSearchRecipeRepository(searchApiService: SearchApiService): SearchRecipeRepository {
        return SearchRecipeRepoImpl(searchApiService)
    }

}