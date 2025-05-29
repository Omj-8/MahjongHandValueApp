// MainActivity.kt
package com.example.mahjonghandvalueapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.mahjonghandvalueapp.ui.theme.MahjongHandValueAppTheme
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MahjongHandValueAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "opening") {
                    composable("opening") { OpeningScreen(navController) }
                    composable("question") { QuestionScreen(navController) }
                    composable(
                        "result/{score}",
                        arguments = listOf(navArgument("score") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val score = backStackEntry.arguments?.getInt("score") ?: 0
                        ResultScreen(score, navController)
                    }
                }
            }
        }
    }
}


@Composable
fun OpeningScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Green){
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("麻雀配牌価値観アプリ", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { navController.navigate("problem") }) {
                Text("スタート")
            }
        }
    }
}

@Composable
fun QuestionScreen(navController: NavController) {
    var sliderValue by remember { mutableStateOf(50f) }

    val tileImages = listOf(
        R.drawable.p1,
        R.drawable.p2,
        R.drawable.p3,
        R.drawable.m1,
        R.drawable.m2,
        R.drawable.m3,
        R.drawable.s1,
        R.drawable.s2,
        R.drawable.s3,
        R.drawable.s1,
        R.drawable.s1,
        R.drawable.p1,
        R.drawable.p1,
        R.drawable.p1
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "これ何点？",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(tileImages) { imageRes ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${sliderValue.toInt()} 点", fontSize = 20.sp)

            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 1f..100f,
                steps = 98,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Button(onClick = {
            navController.navigate("result/${sliderValue.toInt()}")
        }) {
            Text("投票する")
        }
    }
}

@Composable
fun ResultScreen(score: Int, navController: NavController) {
    println("scoreの型: ${score::class}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "あなたのスコアは", fontSize = 20.sp)
        Text(text = "$score 点", fontSize = 48.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            navController.navigate("question") {
                popUpTo("question") { inclusive = true }
            }
        }) {
            Text("みんなの回答を見る")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewOpeningScreen() {
    MahjongHandValueAppTheme {
        OpeningScreen(navController = rememberNavController())
    }
}
