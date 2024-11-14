package com.example.rekomendasifilm

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Tablet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rekomendasifilm.ui.screen.about.AboutScreen
import com.example.rekomendasifilm.ui.navigation.NavigationItem
import com.example.rekomendasifilm.ui.navigation.Screen
import com.example.rekomendasifilm.ui.screen.detail.DetailScreen
import com.example.rekomendasifilm.ui.screen.favorite.FavoriteScreen
import com.example.rekomendasifilm.ui.screen.home.HomeScreen
import com.example.rekomendasifilm.ui.screen.lazyrow.MovieScreen

@Preview
@Composable
fun MovieCompose(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute != Screen.DetailMovie.route) {
                TopBar(title = stringResource(id = R.string.app_name))
            }
            if (currentRoute == Screen.MovieList.route) {
                TopBar(title = stringResource(id = R.string.menu_movie))
            }
            if (currentRoute == Screen.Favorite.route) {
                TopBar(title = stringResource(id = R.string.menu_favorite))
            }
            if (currentRoute == Screen.Profile.route) {
                TopBar(title = stringResource(id = R.string.menu_profile))
            }
            if (currentRoute == Screen.DetailMovie.route) {
                DetailTopBar(
                    title = stringResource(id = R.string.detail),
                    onBackClick = { navController.navigateUp() }
                )
            }
        },
        bottomBar = {
            if (currentRoute != Screen.DetailMovie.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { movieId ->
                        navController.navigate(Screen.DetailMovie.createRoute(movieId))
                    }
                )
            }
            composable(Screen.MovieList.route) {
                MovieScreen(
                    navigateToDetail = { movieId ->
                        navController.navigate(Screen.DetailMovie.createRoute(movieId))
                    }
                )
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    navigateToDetail = { movieId ->
                        navController.navigate(Screen.DetailMovie.createRoute(movieId))
                    }
                )
            }
            composable(Screen.Profile.route) {
                AboutScreen()
            }
            composable(
                route = Screen.DetailMovie.route,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType }
                )
            ) {
                val id = it.arguments?.getInt("movieId") ?: -1
                DetailScreen(
                    movieId = id,
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
fun TopBar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
    )
}

@Composable
fun DetailTopBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
    )
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_movie),
                icon = Icons.Rounded.Tablet,
                screen = Screen.MovieList
            ),
            NavigationItem(
                title = stringResource(R.string.menu_favorite),
                icon = Icons.Rounded.FavoriteBorder,
                screen = Screen.Favorite
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            ),
        )
        BottomNavigation {
            navigationItems.map { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
