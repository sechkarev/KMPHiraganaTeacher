package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sechkarev.hiraganateacherkmp.model.DictionaryItem
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_new
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewWord(
    dictionaryItem: DictionaryItem,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = Color.Yellow.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp))
                .then(modifier),
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.dictionary_word_new),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(8.dp))
                Row {
                    Text(
                        text = dictionaryItem.original,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(dictionaryItem.translation),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                }
            }
        },
    )
}
