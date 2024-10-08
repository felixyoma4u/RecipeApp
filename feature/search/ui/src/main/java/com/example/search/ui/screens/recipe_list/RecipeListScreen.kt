package com.example.search.ui.screens.recipe_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.common.navigation.NavigationRoutes
import com.example.common.utils.UiText
import com.example.search.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecipeListScreen(
    navigation: Flow<RecipeList.Navigation>,
    uiState: RecipeList.UiState,
    navHostController: NavHostController,
    onEvent: (RecipeList.Event) -> Unit
) {
    val query = rememberSaveable {
        mutableStateOf("")
    }
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = navigation) {
        navigation.flowWithLifecycle(lifeCycleOwner.lifecycle)
            .collectLatest {
                when (it) {
                    is RecipeList.Navigation.GotoRecipeDetails -> {
                        navHostController.navigate(NavigationRoutes.RecipeDetails.sendId(it.id))
                    }

                    RecipeList.Navigation.GotoFavoriteScreen -> {
                        navHostController.navigate(NavigationRoutes.Favorite.route)
                    }
                }
            }
    }
    Scaffold(
        floatingActionButton =
        {
            FloatingActionButton(onClick = {
                onEvent(RecipeList.Event.FavoriteScreen)
            }) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null)
            }
        },
        topBar = {
            TextField(
                placeholder = {
                    Text(
                        text = "Search Recipe...",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                value = query.value, onValueChange = {
                    query.value = it
                    onEvent(RecipeList.Event.SearchRecipe(query.value))

                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.error !is UiText.Idle) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.error.getString())
            }
        }

        uiState.data?.let { list ->
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                items(list) { recipe ->
                    RecipeListItem(recipe = recipe) {
                        onEvent(RecipeList.Event.GotoRecipeDetails(recipe.idMeal))
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
 fun RecipeListItem(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    showDeleteIcon: Boolean = false,
    onDeleteIconClick: () -> Unit = {},
    onRecipeClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable {
                onRecipeClick.invoke()
            },
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            AsyncImage(
                model = recipe.strMealThumb,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            if (showDeleteIcon){
                IconButton(
                    onClick = {
                        onDeleteIconClick.invoke()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(text = recipe.strMeal, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = recipe.strInstructions,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (recipe.strTags.isNotEmpty()) {
                FlowRow {
                    recipe.strTags.split(",")
                        .forEach {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .wrapContentSize()
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .clip(shape = RoundedCornerShape(24.dp))
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(24.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    )
                                )
                            }
                        }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

}