package ir.sharif.simplenote.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import ir.sharif.simplenote.app.navigation.AppNavHost
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleNoteTheme {
                val nav = rememberNavController()
                MaterialTheme {
                    AppNavHost(nav)
                }
            }
        }
    }
}
