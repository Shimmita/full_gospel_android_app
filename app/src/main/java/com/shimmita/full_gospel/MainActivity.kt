package com.shimmita.full_gospel

import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shimmita.full_gospel.routing.ScreenRoutes
import com.shimmita.full_gospel.screens.DrawerScreen
import com.shimmita.full_gospel.screens.NavItems
import com.shimmita.full_gospel.screens.RegistrationScreen
import com.shimmita.full_gospel.screens.StarterScreen
import com.shimmita.full_gospel.screens.StarterTopBar
import com.shimmita.full_gospel.ui.theme.Full_GospelTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Full_GospelTheme {

                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                val snackBarHostState = remember { SnackbarHostState() }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var currentItemIndex by rememberSaveable() { mutableIntStateOf(0) }


                val itemsBottomNav = listOf(
                    NavItems(
                        title = "Home",
                        selectedIcon = R.drawable.baseline_home_24,
                        unselectedIcon = R.drawable.outline_home_24
                    ),
                    NavItems(
                        title = "Members",
                        selectedIcon = R.drawable.baseline_group_24,
                        unselectedIcon = R.drawable.outline_group_24
                    ),


                    NavItems(
                        title = "Events",
                        selectedIcon = R.drawable.baseline_event_available_24,
                        unselectedIcon = R.drawable.outline_event_available_24
                    ),

                    NavItems(
                        title = "Our TV",
                        selectedIcon = R.drawable.baseline_live_tv_24,
                        unselectedIcon = R.drawable.baseline_live_tv_24
                    ),
                )

                ModalNavigationDrawer(
                    drawerContent = {
                        DrawerScreen()

                    },
                    drawerState = drawerState,
                    scrimColor = MaterialTheme.colorScheme.surfaceDim
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                        StarterTopBar(bellIconClicked = {
                            coroutineScope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = getString(R.string.login_or_register_to_access),
                                    actionLabel = getString(R.string.login),
                                    duration = SnackbarDuration.Short
                                )

                                when (result) {
                                    SnackbarResult.Dismissed -> {

                                    }

                                    SnackbarResult.ActionPerformed -> {
                                        navController.navigate(route = ScreenRoutes.Login)

                                    }
                                }
                            }
                        }, registerAccountClicked = {
                            //alter the state of an index to the highest value for nav no focused
                            currentItemIndex = 4
                            navController.navigate(route = ScreenRoutes.Login)
                        }, shareApp = {

                        }, handleToggleDrawer = {
                            coroutineScope.launch {
                                if (drawerState.isOpen) drawerState.close() else drawerState.open()

                            }
                        })
                    }, bottomBar = {
                        NavigationBar {
                            itemsBottomNav.forEachIndexed { index, navItem ->
                                NavigationBarItem(onClick = {
                                    //update the index
                                    currentItemIndex = index

                                    //home screen is by default can be accessed logged in or not
                                    if (navItem.title == ScreenRoutes.Home) {
                                        navController.navigate(route = ScreenRoutes.Home)
                                    } else {

                                        //if the user not logged in display snack for them to login
                                        coroutineScope.launch {
                                            val result = snackBarHostState.showSnackbar(
                                                message = getString(R.string.login_or_register_to_access),
                                                actionLabel = getString(R.string.login),
                                                duration = SnackbarDuration.Short
                                            )

                                            when (result) {
                                                SnackbarResult.Dismissed -> {

                                                }

                                                SnackbarResult.ActionPerformed -> {
                                                    navController.navigate(route = ScreenRoutes.Login)
                                                }
                                            }
                                        }
                                    }

                                }, icon = {
                                    Icon(
                                        painter = if (currentItemIndex == index) {
                                            painterResource(navItem.selectedIcon)
                                        } else painterResource(navItem.unselectedIcon),
                                        contentDescription = navItem.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }, label = {
                                    Text(text = navItem.title, fontWeight = FontWeight.Bold)
                                }, selected = currentItemIndex == index

                                )
                            }
                        }
                    }, snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }) { innerPadding ->

                        NavHost(
                            navController = navController,
                            startDestination = ScreenRoutes.Home,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = ScreenRoutes.Home) {
                                StarterScreen()
                            }

                            composable(route = ScreenRoutes.Register) {
                                RegistrationScreen(handleNavigateLogin = {
                                    navController.navigate(route = ScreenRoutes.Login)
                                })
                            }

                            composable(route = ScreenRoutes.Login) {
                                LoginScreen(handleNavigateRegistration = {
                                    navController.navigate(route = ScreenRoutes.Register)
                                })
                            }
                        }
                    }
                }
            }
        }
    }

}



