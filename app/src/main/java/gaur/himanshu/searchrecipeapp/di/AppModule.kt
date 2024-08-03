package gaur.himanshu.searchrecipeapp.di

import android.content.Context
import androidx.room.Room
import com.example.search.ui.navigation.SearchFeatureApi
import com.example.search.ui.navigation.SearchFeatureApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.searchrecipeapp.local.AppDatabase
import gaur.himanshu.searchrecipeapp.navigation.NavigationSubGraphs
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideNavigationSubGraphs(searchFeatureApi: SearchFeatureApi): NavigationSubGraphs {
        return NavigationSubGraphs(searchFeatureApi)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun provideRecipeDao(appDatabase: AppDatabase) = appDatabase.getRecipeDao()
}