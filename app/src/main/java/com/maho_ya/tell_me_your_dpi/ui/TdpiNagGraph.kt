package com.maho_ya.tell_me_your_dpi.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maho_ya.tell_me_your_dpi.ui.aboutapp.AboutAppRoute
import com.maho_ya.tell_me_your_dpi.ui.home.HomeRoute
import com.maho_ya.tell_me_your_dpi.ui.releasenotes.ReleaseNoteRoute
import com.maho_ya.tell_me_your_dpi.ui.releasenotes.ReleaseNotesViewModel

@Composable
fun TdpiNagGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeRoute(
                scaffoldState = scaffoldState
            )
        }
        composable(Screen.ReleaseNote.route) {
            val releaseNotesViewModel = hiltViewModel<ReleaseNotesViewModel>()
            ReleaseNoteRoute(
                releaseNotesViewModel = releaseNotesViewModel
            )
        }
        composable(Screen.AboutApp.route) {
            AboutAppRoute()
        }
    }
}
