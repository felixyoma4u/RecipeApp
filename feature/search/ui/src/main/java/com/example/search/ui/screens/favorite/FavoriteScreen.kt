package com.example.search.ui.screens.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import com.example.common.navigation.NavigationRoutes
import com.example.common.utils.UiText
import com.example.search.ui.screens.recipe_list.RecipeListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navHostController: NavHostController,
    uiState: FavoriteScreen.UiState,
    navigation: Flow<FavoriteScreen.Navigation>,
    onEvent: (FavoriteScreen.Event) -> Unit
) {

    val showDropDown = rememberSaveable {
        mutableStateOf(false)
    }

    val selectedIndex = rememberSaveable {
        mutableIntStateOf(-1)
    }


    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = navigation) {
        navigation.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { navigation ->
                when (navigation) {
                    is FavoriteScreen.Navigation.GotoRecipeDetailsScreen -> {
                        navHostController.navigate(NavigationRoutes.RecipeDetails.sendId(navigation.id))
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorite Recipe",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = {
                        showDropDown.value = !showDropDown.value
                    }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                    if (showDropDown.value) {
                        DropdownMenu(
                            expanded = showDropDown.value,
                            onDismissRequest = { showDropDown.value = false }
                        ) {
                            val menuItems = listOf(
                                "Alphabetical" to FavoriteScreen.Event.AlphabeticalSort,
                                "Less Ingredient" to FavoriteScreen.Event.LessIngredientSort,
                                "Reset" to FavoriteScreen.Event.ResetSort
                            )

                            menuItems.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = { Text(text = item.first) },
                                    onClick = {
                                        selectedIndex.intValue = index
                                        showDropDown.value = false
                                        onEvent(item.second)
                                    },
                                    leadingIcon = {
                                        RadioButton(
                                            selected = selectedIndex.intValue == index,
                                            onClick = {
                                                selectedIndex.intValue = index
                                                showDropDown.value = false
                                                onEvent(item.second)
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }

                })
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
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.error.getString())
            }
        }

        uiState.data?.let { list ->
            if (list.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Nothing Found")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    items(list) { recipe ->
                        RecipeListItem(
                            recipe = recipe,
                            showDeleteIcon = true,
                            onDeleteIconClick = { onEvent(FavoriteScreen.Event.DeleteRecipe(recipe)) },
                            onRecipeClick = { onEvent(FavoriteScreen.Event.GotoDetails(recipe.idMeal)) }
                        )
                    }
                }
            }
        }

    }
}