package com.example.search.ui.screens.recipe_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.common.utils.UiText
import com.example.search.domain.model.Recipe
import com.example.search.domain.model.RecipeDetails
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailsViewModel,
    navHostController: NavHostController,
    onNavigationClicked: () -> Unit,
    onDeleteClicked: (RecipeDetails) -> Unit,
    onFavoriteClicked: (RecipeDetails) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {navigation->
                when(navigation){
                    Details.Navigation.GotoRecipeListScreen -> navHostController.navigateUp()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.value.data?.strMeal.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable {onNavigationClicked.invoke()}
                    )
                },
                actions = {
                    IconButton(onClick = {
                        uiState.value.data?.let {
                            onFavoriteClicked(it)
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null)
                    }

                    IconButton(onClick = {
                        uiState.value.data?.let {
                            onDeleteClicked(it)
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
            )
        }
    ) {
        if (uiState.value.isLoading) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.value.error !is UiText.Idle) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.value.error.getString())
            }
        }

        uiState.value.data?.let { recipeDetails ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {

                AsyncImage(
                    model = recipeDetails.strMealThumb,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = recipeDetails.strInstructions,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    recipeDetails.ingredientsPairs.forEach { ingredient ->
                        if (ingredient.first.isNotEmpty() && ingredient.second.isNotEmpty()) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                AsyncImage(
                                    model = getIngredientImageUrl(ingredient.first),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(color = Color.White, shape = CircleShape)
                                        .clip(shape = CircleShape)
                                )
                                Text(
                                    text = ingredient.second,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Watch Youtube Video", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }

    }
}

fun getIngredientImageUrl(name: String) = "https://www.themealdb.com/images/ingredients/${name}.png"