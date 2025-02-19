package com.example.mychat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.mychat.ui.theme.MyChatTheme
import java.security.KeyStore.TrustedCertificateEntry

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FragmentContainerView doğru şekilde kullanılarak navController başlatılmalı
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            if (navController.currentDestination?.id == R.id.HomeFragment) {
                moveTaskToBack(true)
            } else {
                super.onBackPressed()
            }
        }
    }
}
