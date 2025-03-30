package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    //    private lateinit var myObserver: MyLifecycleObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        myObserver = MyLifecycleObserver()
//        lifecycle.addObserver(myObserver)
        setContent {
            MyApplicationTheme {
                HomeList()
            }
        }

//        lifecycle.addObserver(LifecycleEventObserver { _, event ->
//            when (event) {
//                Lifecycle.Event.ON_CREATE -> Log.d("lifecycle", "onCreate")
//                Lifecycle.Event.ON_START -> Log.d("lifecycle", "onStart")
//                Lifecycle.Event.ON_RESUME -> Log.d("lifecycle", "onResume")
//                Lifecycle.Event.ON_PAUSE -> Log.d("lifecycle", "onPause")
//                Lifecycle.Event.ON_STOP -> Log.d("lifecycle", "onStop")
//                Lifecycle.Event.ON_DESTROY -> Log.d("lifecycle", "onDestroy")
//                else -> Log.d("lifecycle", "else")
//            }
//        })
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        HomeList()
    }
}