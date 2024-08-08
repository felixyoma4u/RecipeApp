package gaur.himanshu.searchrecipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.searchrecipeapp.navigation.NavigationSubGraphs
import gaur.himanshu.searchrecipeapp.navigation.RecipeNavigation
import gaur.himanshu.searchrecipeapp.ui.theme.SearchRecipeAppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationSubGraphs: NavigationSubGraphs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            SearchRecipeAppTheme {
                Surface(modifier = Modifier.safeContentPadding()) {
                    RecipeNavigation(navigationSubGraphs = navigationSubGraphs)
                }
             val apiKey =  BuildConfig.apiKeySafe
                println("API Key: $apiKey")
            }
        }
    }
}

