package com.shimmita.full_gospel

import LoginScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shimmita.full_gospel.routing.ScreenRoutes
import com.shimmita.full_gospel.screens.AllMembersView
import com.shimmita.full_gospel.screens.AnnouncePost
import com.shimmita.full_gospel.screens.DailyPrayerPost
import com.shimmita.full_gospel.screens.DrawerScreen
import com.shimmita.full_gospel.screens.EventMissionPost
import com.shimmita.full_gospel.screens.EventsMissionView
import com.shimmita.full_gospel.screens.LastSundayPost
import com.shimmita.full_gospel.screens.LiveStreamView
import com.shimmita.full_gospel.screens.NatureTalentPost
import com.shimmita.full_gospel.screens.NavItems
import com.shimmita.full_gospel.screens.NotificationView
import com.shimmita.full_gospel.screens.RegistrationScreen
import com.shimmita.full_gospel.screens.StarterScreen
import com.shimmita.full_gospel.screens.StarterTopBar
import com.shimmita.full_gospel.screens.TestimonyPost
import com.shimmita.full_gospel.screens.WeeklyVersePost
import com.shimmita.full_gospel.ui.theme.Full_GospelTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Full_GospelTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                val snackBarHostState = remember { SnackbarHostState() }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var currentItemIndex by rememberSaveable() { mutableIntStateOf(0) }


                val itemsBottomNav = listOf(
                    NavItems(
                        title = "Home",
                        selectedIcon = R.drawable.baseline_home_24,
                        unselectedIcon = R.drawable.outline_home_24,
                        routePath = ScreenRoutes.Home
                    ),

                    NavItems(
                        title = "Members",
                        selectedIcon = R.drawable.baseline_group_24,
                        unselectedIcon = R.drawable.outline_group_24,
                        routePath = ScreenRoutes.Members
                    ),


                    NavItems(
                        title = "Events",
                        selectedIcon = R.drawable.baseline_event_available_24,
                        unselectedIcon = R.drawable.outline_event_available_24,
                        routePath = ScreenRoutes.EventMissionView
                    ),

                    NavItems(
                        title = "Our TV",
                        selectedIcon = R.drawable.baseline_live_tv_24,
                        unselectedIcon = R.drawable.baseline_live_tv_24,
                        routePath = ScreenRoutes.LiveTV
                    ),
                )


                ModalNavigationDrawer(
                    drawerContent = {
                        DrawerScreen(drawerState, navController, handleAlterNavIndex = {
                            currentItemIndex = 5
                        })
                    },
                    drawerState = drawerState,
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                        StarterTopBar(bellIconClicked = {
                            navController.navigate(route = ScreenRoutes.Notification)
                            //alter the index of bottom nav to highest value
                            currentItemIndex = 5
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

                                    //navigate to screens clicked. consider protecting all except home
                                    navController.navigate(route = navItem.routePath)


                                    //if the user not logged in display snack for them to login
//                                    coroutineScope.launch {
//                                        val result = snackBarHostState.showSnackbar(
//                                            message = getString(R.string.login_or_register_to_access),
//                                            actionLabel = getString(R.string.login),
//                                            duration = SnackbarDuration.Short
//                                        )
//
//                                        when (result) {
//                                            SnackbarResult.Dismissed -> {
//
//                                            }
//
//                                            SnackbarResult.ActionPerformed -> {
//                                                navController.navigate(route = ScreenRoutes.Login)
//                                            }
//                                        }
//                                    }


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

                            composable(route = ScreenRoutes.Announce) {
                                AnnouncePost(handleNavigateAnnouncement = {
                                    navController.navigate(route = ScreenRoutes.Notification)
                                })
                            }

                            composable(route = ScreenRoutes.DailyPrayer) {
                                DailyPrayerPost(handleNavigateHome = {
                                    navController.navigate(route = ScreenRoutes.Home)
                                })
                            }

                            composable(route = ScreenRoutes.EventMission) {
                                EventMissionPost(handleNavigateHome = {
                                    navController.navigate(route = ScreenRoutes.Home)
                                })
                            }

                            composable(route = ScreenRoutes.NatureTalent) {
                                NatureTalentPost(handleNavigateHome = {
                                    navController.navigate(route = ScreenRoutes.Home)
                                })
                            }

                            composable(route = ScreenRoutes.ShareTestimony) {
                                TestimonyPost(handleNavigateHome = {
                                    navController.navigate(route = ScreenRoutes.Home)
                                })
                            }

                            composable(route = ScreenRoutes.WeeklyVerse) {
                                WeeklyVersePost(handleNavigateHome = {
                                    navController.navigate(route = ScreenRoutes.Home)
                                })
                            }

                            composable(route = ScreenRoutes.LastSunday) {
                                LastSundayPost(handleNavigateHome = {
                                    navController.navigate(route = ScreenRoutes.Home)

                                })
                            }

                            composable(route = ScreenRoutes.Notification) {
                                NotificationView()
                            }

                            composable(route = ScreenRoutes.Members) {
                                AllMembersView()
                            }

                            composable(route = ScreenRoutes.EventMissionView) {
                                EventsMissionView()
                            }

                            composable(route = ScreenRoutes.LiveTV) {
                                LiveStreamView()
                            }



                            composable(route = ScreenRoutes.Register) {
                                RegistrationScreen(
                                    handleNavigateLogin = {
                                        navController.navigate(route = ScreenRoutes.Login)
                                    },
                                    handleRegistrationSuccess = {
                                        //navigate login screen
                                        navController.navigate(route = ScreenRoutes.Login)
                                        navController.popBackStack()
                                    },
                                    handleRegistrationFailed = {
                                        Toast.makeText(
                                            context,
                                            "Registration Failed",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                                )
                            }

                            composable(route = ScreenRoutes.Login) {
                                LoginScreen(handleNavigateRegistration = {
                                    navController.navigate(route = ScreenRoutes.Register)
                                }, handleNavigateHomeLoginSuccess = {
                                    navController.navigate(route = ScreenRoutes.Home)
                                    navController.popBackStack()
                                }, handleLoginFailed = {
                                    Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG)
                                        .show()
                                })

                            }
                        }
                    }
                }
            }
        }
    }

}



