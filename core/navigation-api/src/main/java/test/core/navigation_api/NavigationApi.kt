package test.core.navigation_api

interface NavigationApi<DIRECTION> {
    fun navigate(direction: DIRECTION)
}