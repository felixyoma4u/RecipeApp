package gaur.himanshu.searchrecipeapp.di

import com.example.search.ui.navigation.SearchFeatureApi
import com.example.search.ui.navigation.SearchFeatureApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.searchrecipeapp.navigation.NavigationSubGraphs

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideNavigationSubGraphs(searchFeatureApi: SearchFeatureApi): NavigationSubGraphs{
        return NavigationSubGraphs(searchFeatureApi)
    }
}