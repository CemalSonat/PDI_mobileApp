import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.plantdiseaseidentifier.R

@Composable
fun WelcomeScreen(onTimeout: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val alphaAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val scaleAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Background Image with blur effect
        Image(
            painter = painterResource(id = R.drawable.plant7),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.4f)
        )

        // Centered content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spacer to add padding around the text
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                // Animated Text
                Text(
                    text = "Plant Disease Identifier",
                    fontSize = 30.sp,
                    color = Color.Black,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .alpha(alphaAnimation)
                        .graphicsLayer(
                            scaleX = scaleAnimation,
                            scaleY = scaleAnimation
                        )
                )
            }
        }
    }
}
