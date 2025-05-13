package com.sechkarev.hiraganateacherkmp.data.mapping

import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.blue_house
import kmphiraganateacher.composeapp.generated.resources.challenge10_text
import kmphiraganateacher.composeapp.generated.resources.challenge11_text
import kmphiraganateacher.composeapp.generated.resources.challenge12_task
import kmphiraganateacher.composeapp.generated.resources.challenge12_text
import kmphiraganateacher.composeapp.generated.resources.challenge13_task
import kmphiraganateacher.composeapp.generated.resources.challenge14_task
import kmphiraganateacher.composeapp.generated.resources.challenge15_task
import kmphiraganateacher.composeapp.generated.resources.challenge16_task
import kmphiraganateacher.composeapp.generated.resources.challenge16_text
import kmphiraganateacher.composeapp.generated.resources.challenge17_task
import kmphiraganateacher.composeapp.generated.resources.challenge18_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge18_task
import kmphiraganateacher.composeapp.generated.resources.challenge19_task
import kmphiraganateacher.composeapp.generated.resources.challenge1_task
import kmphiraganateacher.composeapp.generated.resources.challenge1_text
import kmphiraganateacher.composeapp.generated.resources.challenge1_welcome_message
import kmphiraganateacher.composeapp.generated.resources.challenge2_task
import kmphiraganateacher.composeapp.generated.resources.challenge3_task
import kmphiraganateacher.composeapp.generated.resources.challenge3_text
import kmphiraganateacher.composeapp.generated.resources.challenge4_task
import kmphiraganateacher.composeapp.generated.resources.challenge7_task
import kmphiraganateacher.composeapp.generated.resources.challenge8_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge8_task
import kmphiraganateacher.composeapp.generated.resources.challenge9_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge9_task
import kmphiraganateacher.composeapp.generated.resources.challenge_aoi_task
import kmphiraganateacher.composeapp.generated.resources.challenge_blue_house_remainder
import kmphiraganateacher.composeapp.generated.resources.challenge_blue_house_task
import kmphiraganateacher.composeapp.generated.resources.challenge_blue_house_text
import kmphiraganateacher.composeapp.generated.resources.challenge_hae_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge_hae_task
import kmphiraganateacher.composeapp.generated.resources.challenge_i_no_hint_task
import kmphiraganateacher.composeapp.generated.resources.challenge_ii_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge_ii_task
import kmphiraganateacher.composeapp.generated.resources.challenge_o_introduction_task
import kmphiraganateacher.composeapp.generated.resources.challenge_o_introduction_text
import kmphiraganateacher.composeapp.generated.resources.challenge_o_repetition_task
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ai
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ao_blue
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ao_green
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_aoi_blue
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_aoi_green
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_hae
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_hai
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ie
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ii
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_iie
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_a
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_e
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_ha
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_i
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_o
import kmphiraganateacher.composeapp.generated.resources.picture_description_blue_house
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

class ConfigMapper {
    fun mapHeadlineConfigIdToStringResource(headlineId: String): StringResource =
        when (headlineId) {
            "i_beginning_introduction" -> Res.string.challenge1_welcome_message
            else -> throw IllegalArgumentException("Can't find a string corresponding with the headline id $headlineId")
        }

    fun mapAnimationConfigIdToFileName(animationId: String): String =
        when (animationId) {
            "i_introduction_animation" -> "hiragana_animated_i"
            "e_introduction_animation" -> "hiragana_animated_e"
            "ha_introduction_animation" -> "hiragana_animated_ha"
            "a_introduction_animation" -> "hiragana_animated_a"
            "o_introduction_animation" -> "hiragana_animated_o"
            else -> throw IllegalArgumentException("Can't find a string corresponding with the animation id $animationId")
        }

    fun mapTextConfigIdToStringResource(textId: String): StringResource =
        when (textId) {
            "i_beginning_explanation" -> Res.string.challenge1_text // todo: remove numbers from ids
            "i_beginning_task" -> Res.string.challenge1_task
            "i_repetition_task" -> Res.string.challenge2_task
            "i_no_hint_task" -> Res.string.challenge_i_no_hint_task
            "new_word_ii_task" -> Res.string.challenge_ii_task
            "new_word_ii_congratulations" -> Res.string.challenge_ii_congratulations
            "e_introduction_text" -> Res.string.challenge3_text
            "e_introduction_task" -> Res.string.challenge3_task
            "e_repetition_task" -> Res.string.challenge4_task
            "new_word_ie_task" -> Res.string.challenge7_task
            "new_word_ie_congratulations" -> Res.string.challenge8_congratulations
            "new_word_iie_task" -> Res.string.challenge8_task
            "new_word_iie_congratulations" -> Res.string.challenge9_congratulations
            "are_you_afraid_task" -> Res.string.challenge9_task
            "iie_timed_10_sec_task" -> Res.string.challenge10_text
            "iie_timed_5_sec_task" -> Res.string.challenge11_text
            "ha_introduction_text" -> Res.string.challenge12_text
            "ha_introduction_task" -> Res.string.challenge12_task
            "ha_repetition_task" -> Res.string.challenge13_task
            "new_word_hai_task" -> Res.string.challenge14_task
            "hai_timed_5_sec_task" -> Res.string.challenge15_task
            "new_word_hae_task" -> Res.string.challenge_hae_task
            "new_word_hae_congratulations" -> Res.string.challenge_hae_congratulations
            "a_introduction_text" -> Res.string.challenge16_text
            "a_introduction_task" -> Res.string.challenge16_task
            "a_repetition_task" -> Res.string.challenge17_task
            "new_word_ai_congratulations" -> Res.string.challenge18_congratulations
            "new_word_ai_task" -> Res.string.challenge18_task
            "ai_decorated_canvas_task" -> Res.string.challenge19_task
            "o_introduction_text" -> Res.string.challenge_o_introduction_text
            "o_introduction_task" -> Res.string.challenge_o_introduction_task
            "o_repetition_task" -> Res.string.challenge_o_repetition_task
            "new_word_aoi_task" -> Res.string.challenge_aoi_task
            "blue_house_text" -> Res.string.challenge_blue_house_text
            "blue_house_task" -> Res.string.challenge_blue_house_task
            "blue_house_remainder" -> Res.string.challenge_blue_house_remainder
            else -> throw IllegalArgumentException("Can't find a string corresponding with the id $textId")
        }

    fun mapHintConfigIdToDrawableResource(hintId: String): DrawableResource =
        when (hintId) {
            "a_introduction_image" -> Res.drawable.hiragana_static_a
            "o_introduction_image" -> Res.drawable.hiragana_static_o
            "i_introduction_image" -> Res.drawable.hiragana_static_i
            "e_introduction_image" -> Res.drawable.hiragana_static_e
            "ha_introduction_image" -> Res.drawable.hiragana_static_ha
            else -> throw IllegalArgumentException("Can't find a drawable corresponding with the hint id $hintId")
        }

    fun mapImageIdToDrawableResource(imageId: String): DrawableResource =
        when (imageId) {
            "blue_house_image" -> Res.drawable.blue_house
            else -> throw IllegalArgumentException("Can't find a drawable corresponding with the image id $imageId")
        }

    fun mapImageIdToDescriptionResource(imageId: String): StringResource =
        when (imageId) {
            "blue_house_image" -> Res.string.picture_description_blue_house
            else -> throw IllegalArgumentException("Can't find a drawable corresponding with the image id $imageId")
        }

    fun mapDictionaryItemIdToTranslation(dictionaryItemId: String): StringResource =
        when (dictionaryItemId) {
            "II" -> Res.string.dictionary_word_ii
            "IE" -> Res.string.dictionary_word_ie
            "IIE" -> Res.string.dictionary_word_iie
            "HAI" -> Res.string.dictionary_word_hai
            "HAE" -> Res.string.dictionary_word_hae
            "AI" -> Res.string.dictionary_word_ai
            "AO_green" -> Res.string.dictionary_word_ao_green
            "AO_blue" -> Res.string.dictionary_word_ao_blue
            "AOI_green" -> Res.string.dictionary_word_aoi_green
            "AOI_blue" -> Res.string.dictionary_word_aoi_blue
            else -> throw IllegalArgumentException("Can't find a dictionary item corresponding with the name $dictionaryItemId")
        }
}
