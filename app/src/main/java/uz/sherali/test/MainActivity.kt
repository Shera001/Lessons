package uz.sherali.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import test.core.navigation_api.NavigationApi
import test.feature.lessons.navigation.LessonsDirections

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationApi<LessonsDirections> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        installSplashScreen()
        setContentView(R.layout.activity_main)
    }

    override fun navigate(direction: LessonsDirections) {
        if (direction is LessonsDirections.ToVideoPlayerFragment) {
            findNavController(R.id.main_activity_nav_host).navigate(
                R.id.action_lessonsFragment_to_videoPlayerFragment,
                bundleOf("id" to direction.id)
            )
        }
    }
}