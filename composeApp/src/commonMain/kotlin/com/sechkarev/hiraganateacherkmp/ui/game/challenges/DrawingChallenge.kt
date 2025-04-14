package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sechkarev.hiraganateacherkmp.ui.components.DrawingCanvas
import com.sechkarev.hiraganateacherkmp.ui.components.StaticCanvas
import com.sechkarev.hiraganateacherkmp.ui.components.drawingCanvasSize
import com.sechkarev.hiraganateacherkmp.ui.game.ChallengeCompletionError
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.answer_appearance_hint_description
import kmphiraganateacher.composeapp.generated.resources.challenge_text_recognition_mistake
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DrawingChallenge(
    challengeState: ChallengeUiState,
    drawingLineThickness: Float,
    hintImageRes: DrawableResource?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .then(modifier),
    ) {
        when (challengeState) {
            is ChallengeUiState.Completed -> {
                StaticCanvas(
                    paths = challengeState.solution,
                    drawingLineThickness = drawingLineThickness,
                    modifier =
                        Modifier
                            .drawingCanvasSize()
                            .clipToBounds()
                            .background(Color.White)
                            .align(Alignment.Center)
                            .background(color = Color.Green.copy(alpha = 0.1f))
                            .border(width = 1.dp, color = Color.Black),
                )
            }
            is ChallengeUiState.Current -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .drawingCanvasSize()
                                .background(Color.White)
                                .align(Alignment.CenterHorizontally)
                                .border(width = 1.dp, color = Color.Black),
                    ) {
                        if (hintImageRes != null) {
                            Image(
                                painter = painterResource(hintImageRes),
                                modifier = Modifier.fillMaxSize(),
                                alpha = 0.1f,
                                contentScale = ContentScale.FillWidth,
                                contentDescription =
                                    stringResource(
                                        Res.string.answer_appearance_hint_description,
                                        challengeState.challenge.answer.answerText,
                                    ),
                            )
                        }
                        DrawingCanvas(
                            paths = challengeState.drawnStrokes,
                            currentPath = challengeState.currentStroke,
                            drawingLineThickness = drawingLineThickness,
                            onAction = challengeState.onAction,
                            modifier =
                                Modifier
                                    .background(
                                        color =
                                            if (challengeState.completionError == null) {
                                                Color.Transparent
                                            } else {
                                                Color.Red.copy(alpha = 0.1f)
                                            },
                                    ).fillMaxSize(),
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    // This is a hack I introduced so the canvas doesn't move around during drawing:
                    // The error message is always drawn, it's just invisible when there is no error.
                    // todo: rethink in the future?
                    val erroneouslyRecognizedText = (challengeState.completionError as? ChallengeCompletionError.WrongText)?.text
                    Text(
                        text =
                            stringResource(
                                Res.string.challenge_text_recognition_mistake,
                                erroneouslyRecognizedText.toString(),
                            ),
                        color =
                            if (erroneouslyRecognizedText == null) {
                                Color.Transparent
                            } else {
                                Color.Black
                            },
                    )
                }
            }
        }
    }
}
