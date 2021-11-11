package com.example.pracainzynierska.navigation

sealed class Screen(val route: String) {
    object LoginScreen: Screen("login_screen")
    object MainScreen: Screen("main_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
