package com.endava.parking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.endava.parking.ui.navigation.NavigationCallback
import com.endava.parking.ui.welcomescreen.WelcomeFragment

class MainActivity : AppCompatActivity(), NavigationCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        navigate(WelcomeFragment())
    }

    override fun navigate(fragment: Fragment, stackName: String?) {
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.nav_host, fragment)
            if (stackName != null) addToBackStack(stackName)
            commit()
        }
    }

    override fun popBackStack() {
        supportFragmentManager.popBackStackImmediate()
    }
}
