package com.sechkarev.hiraganateacherkmp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.sechkarev.hiraganateacherkmp.ui.game.DrawingAction
import kotlin.math.abs

@Composable
fun DrawingCanvas(
    paths: List<com.sechkarev.hiraganateacherkmp.model.Stroke>,
    currentPath: com.sechkarev.hiraganateacherkmp.model.Stroke?,
    drawingLineThickness: Float,
    onAction: (DrawingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier =
            modifier
                .clipToBounds()
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = {
                            onAction(DrawingAction.OnNewPathStart)
                        },
                        onDragEnd = {
                            onAction(DrawingAction.OnPathEnd)
                        },
                        onDrag = { change, _ ->
                            onAction(DrawingAction.OnDraw(change.position))
                        },
                        onDragCancel = {
                            onAction(DrawingAction.OnPathEnd)
                        },
                    )
                },
    ) {
        paths.fastForEach { pathData ->
            drawPath(
                path = pathData.path.map { Offset(it.x, it.y) },
                thickness = drawingLineThickness,
            )
        }
        currentPath?.let { path ->
            drawPath(
                path = path.path.map { Offset(it.x, it.y) },
                thickness = drawingLineThickness,
            )
        }
    }
}

@Composable
fun StaticCanvas(
    paths: List<com.sechkarev.hiraganateacherkmp.model.Stroke>,
    drawingLineThickness: Float,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
    ) {
        paths.fastForEach { path ->
            drawPath(
                path = path.path.map { Offset(it.x, it.y) },
                thickness = drawingLineThickness,
            )
        }
    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color = Color.Black,
    thickness: Float,
) {
    val smoothedPath =
        Path().apply {
            if (path.isNotEmpty()) {
                moveTo(path.first().x, path.first().y)

                val smoothness = 5
                for (i in 1..path.lastIndex) {
                    val from = path[i - 1]
                    val to = path[i]
                    val dx = abs(from.x - to.x)
                    val dy = abs(from.y - to.y)
                    if (dx >= smoothness || dy >= smoothness) {
                        quadraticTo(
                            x1 = (from.x + to.x) / 2f,
                            y1 = (from.y + to.y) / 2f,
                            x2 = to.x,
                            y2 = to.y,
                        )
                    }
                }
            }
        }
    drawPath(
        path = smoothedPath,
        color = color,
        style =
            Stroke(
                width = thickness,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
    )
}

// todo: in the future I probably want to make this size modifier device specs-aware and keep the 4:3 aspect ratio
fun Modifier.drawingCanvasSize() = this.size(width = 320.dp, height = 240.dp)
