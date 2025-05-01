package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sechkarev.hiraganateacherkmp.ui.components.drawingCanvasSize

// todo: add more?
enum class CanvasDecoration {
    HEARTS,
}

@Composable
fun CanvasDecorationHearts(modifier: Modifier = Modifier) {
    Box(
        modifier =
            Modifier
                .drawingCanvasSize()
                .clipToBounds()
                .then(modifier),
    ) {
        Text(
            text = "❤\uFE0F",
            fontSize = 24.sp,
            modifier =
                Modifier
                    .rotate(74f)
                    .align(Alignment.TopStart)
                    .padding(top = 19.dp, start = 31.dp)
                    .alpha(0.2f),
        )
        Text(
            text = "❤\uFE0F",
            fontSize = 32.sp,
            modifier =
                Modifier
                    .rotate(199f)
                    .align(Alignment.CenterStart)
                    .padding(bottom = 33.dp, start = 90.dp)
                    .alpha(0.2f),
        )
        Text(
            text = "❤\uFE0F",
            fontSize = 48.sp,
            modifier =
                Modifier
                    .rotate(111f)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 48.dp, end = 100.dp)
                    .alpha(0.2f),
        )
        Text(
            text = "❤\uFE0F",
            fontSize = 40.sp,
            modifier =
                Modifier
                    .rotate(12f)
                    .align(Alignment.TopCenter)
                    .padding(top = 30.dp, start = 50.dp)
                    .alpha(0.2f),
        )
        Text(
            text = "❤\uFE0F",
            fontSize = 60.sp,
            modifier =
                Modifier
                    .rotate(30f)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 29.dp)
                    .alpha(0.2f),
        )
        Text(
            text = "❤\uFE0F",
            fontSize = 18.sp,
            modifier =
                Modifier
                    .rotate(30f)
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 41.dp)
                    .alpha(0.2f),
        )
    }
}
