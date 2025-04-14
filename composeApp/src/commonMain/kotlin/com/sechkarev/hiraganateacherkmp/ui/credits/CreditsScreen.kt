package com.sechkarev.hiraganateacherkmp.ui.credits

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.sechkarev.hiraganateacherkmp.ui.components.TopBarWithBackIcon
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.author_photo
import kmphiraganateacher.composeapp.generated.resources.credits_app_icon
import kmphiraganateacher.composeapp.generated.resources.credits_app_icon_wikimedia
import kmphiraganateacher.composeapp.generated.resources.credits_developer
import kmphiraganateacher.composeapp.generated.resources.credits_developer_image_description
import kmphiraganateacher.composeapp.generated.resources.credits_developer_name
import kmphiraganateacher.composeapp.generated.resources.credits_drawing_instructions
import kmphiraganateacher.composeapp.generated.resources.credits_drawing_instructions_wikimedia
import kmphiraganateacher.composeapp.generated.resources.credits_inspiration
import kmphiraganateacher.composeapp.generated.resources.credits_inspiration_book
import kmphiraganateacher.composeapp.generated.resources.credits_screen_title
import kmphiraganateacher.composeapp.generated.resources.credits_text_recognition
import kmphiraganateacher.composeapp.generated.resources.credits_text_recognition_google_ml_kit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBarWithBackIcon(
                title = stringResource(Res.string.credits_screen_title),
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Spacer(Modifier.height(24.dp))
            Image(
                painter = painterResource(Res.drawable.author_photo),
                contentDescription = stringResource(Res.string.credits_developer_image_description),
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(192.dp)
                        .clip(RoundedCornerShape(16.dp)),
            )
            Spacer(Modifier.height(16.dp))
            TextWithLink(
                commonText = stringResource(Res.string.credits_developer),
                textWithLink = stringResource(Res.string.credits_developer_name),
                link = "https://github.com/sechkarev",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(8.dp))
            TextWithLink(
                commonText = stringResource(Res.string.credits_text_recognition),
                textWithLink = stringResource(Res.string.credits_text_recognition_google_ml_kit),
                link = "https://developers.google.com/ml-kit/vision/digital-ink-recognition",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(8.dp))
            TextWithLink(
                commonText = stringResource(Res.string.credits_inspiration),
                textWithLink = stringResource(Res.string.credits_inspiration_book),
                link = "https://archive.org/details/japanese-hiragana-katakana-for-beginners-first-steps-to-mastering-2",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(8.dp))
            TextWithLink(
                commonText = stringResource(Res.string.credits_drawing_instructions),
                textWithLink = stringResource(Res.string.credits_drawing_instructions_wikimedia),
                link = "https://commons.wikimedia.org/wiki/Category:Hiragana_stroke_order_(animated_image_set)",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(8.dp))
            TextWithLink(
                commonText = stringResource(Res.string.credits_app_icon),
                textWithLink = stringResource(Res.string.credits_app_icon_wikimedia),
                link = "https://commons.wikimedia.org/wiki/File:Hiragana_letter_Nu.svg",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun TextWithLink(
    commonText: String,
    textWithLink: String,
    link: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text =
            buildAnnotatedString {
                append(commonText)
                append(" ")
                withLink(
                    LinkAnnotation.Url(
                        url = link,
                        styles = TextLinkStyles(style = SpanStyle(color = Color.Blue)),
                    ),
                ) {
                    append(textWithLink)
                }
            },
        modifier = modifier,
    )
}
