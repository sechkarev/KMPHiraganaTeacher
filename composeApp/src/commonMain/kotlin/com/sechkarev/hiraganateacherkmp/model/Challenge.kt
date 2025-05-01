package com.sechkarev.hiraganateacherkmp.model

import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_hai
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ie
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_iie
import org.jetbrains.compose.resources.StringResource

data class SolvedChallenge(
    val challenge: Challenge,
    val solution: List<Stroke>,
)

enum class Answer(
    val answerText: String,
    val requiredStrokes: Int,
) {
    I(answerText = "い", requiredStrokes = 2),
    E(answerText = "え", requiredStrokes = 2),
    IE(answerText = "いえ", requiredStrokes = 4),
    IIE(answerText = "いいえ", requiredStrokes = 6),
    HA(answerText = "は", requiredStrokes = 3),
    HAI(answerText = "はい", requiredStrokes = 5),
    A(answerText = "あ", requiredStrokes = 3),
}

enum class DictionaryItem(
    val original: String,
    val translation: StringResource,
) {
    IE(original = "いえ", translation = Res.string.dictionary_word_ie),
    IIE(original = "いいえ", translation = Res.string.dictionary_word_iie),
    HAI(original = "はい", translation = Res.string.dictionary_word_hai),
}

sealed interface HiraganaCharacterOnGrid {
    data class Filled(
        val character: HiraganaCharacter,
    ) : HiraganaCharacterOnGrid

    data object Empty : HiraganaCharacterOnGrid

    data object Null : HiraganaCharacterOnGrid
}

enum class HiraganaCharacter(
    val spelling: Char,
    val pronunciation: String,
    val gridCell: Int,
) {
    A(spelling = 'あ', pronunciation = "a", gridCell = 0),
    I(spelling = 'い', pronunciation = "i", gridCell = 1),
    U(spelling = 'う', pronunciation = "u", gridCell = 2),
    E(spelling = 'え', pronunciation = "e", gridCell = 3),
    O(spelling = 'お', pronunciation = "o", gridCell = 4),
    KA(spelling = 'か', pronunciation = "ka", gridCell = 5),
    KI(spelling = 'き', pronunciation = "ki", gridCell = 6),
    KU(spelling = 'く', pronunciation = "ku", gridCell = 7),
    KE(spelling = 'け', pronunciation = "ke", gridCell = 8),
    KO(spelling = 'こ', pronunciation = "ko", gridCell = 9),
    SA(spelling = 'さ', pronunciation = "sa", gridCell = 10),
    SHI(spelling = 'し', pronunciation = "shi", gridCell = 11),
    SU(spelling = 'す', pronunciation = "su", gridCell = 12),
    SE(spelling = 'せ', pronunciation = "se", gridCell = 13),
    SO(spelling = 'そ', pronunciation = "so", gridCell = 14),
    TA(spelling = 'た', pronunciation = "ta", gridCell = 15),
    CHI(spelling = 'ち', pronunciation = "chi", gridCell = 16),
    TSU(spelling = 'つ', pronunciation = "tsu", gridCell = 17),
    TE(spelling = 'て', pronunciation = "te", gridCell = 18),
    TO(spelling = 'と', pronunciation = "to", gridCell = 19),
    NA(spelling = 'な', pronunciation = "na", gridCell = 20),
    NI(spelling = 'に', pronunciation = "ni", gridCell = 21),
    NU(spelling = 'ぬ', pronunciation = "nu", gridCell = 22),
    NE(spelling = 'ね', pronunciation = "ne", gridCell = 23),
    NO(spelling = 'の', pronunciation = "no", gridCell = 24),
    HA(spelling = 'は', pronunciation = "ha", gridCell = 25),
    HI(spelling = 'ひ', pronunciation = "hi", gridCell = 26),
    FU(spelling = 'ふ', pronunciation = "fu", gridCell = 27),
    HE(spelling = 'へ', pronunciation = "he", gridCell = 28),
    HO(spelling = 'ほ', pronunciation = "ho", gridCell = 29),
    MA(spelling = 'ま', pronunciation = "ma", gridCell = 30),
    MI(spelling = 'み', pronunciation = "mi", gridCell = 31),
    MU(spelling = 'む', pronunciation = "mu", gridCell = 32),
    ME(spelling = 'め', pronunciation = "me", gridCell = 33),
    MO(spelling = 'も', pronunciation = "mo", gridCell = 34),
    YA(spelling = 'や', pronunciation = "ya", gridCell = 35),
    YU(spelling = 'ゆ', pronunciation = "yu", gridCell = 37),
    YO(spelling = 'よ', pronunciation = "yo", gridCell = 39),
    RA(spelling = 'ら', pronunciation = "ra", gridCell = 40),
    RI(spelling = 'り', pronunciation = "ri", gridCell = 41),
    RU(spelling = 'る', pronunciation = "ru", gridCell = 42),
    RE(spelling = 'れ', pronunciation = "re", gridCell = 43),
    RO(spelling = 'ろ', pronunciation = "ro", gridCell = 44),
    WA(spelling = 'わ', pronunciation = "wa", gridCell = 45),
    WO(spelling = 'を', pronunciation = "o", gridCell = 47),
    N(spelling = 'ん', pronunciation = "n", gridCell = 49),
}

enum class Challenge(
    val answer: Answer,
    val dictionaryItem: DictionaryItem? = null,
    val newCharacter: HiraganaCharacter? = null,
    val secondsToComplete: Int? = null,
) {
    Challenge1(
        answer = Answer.I,
        newCharacter = HiraganaCharacter.I,
    ),
    Challenge2(
        answer = Answer.I,
    ),
    Challenge3(
        answer = Answer.E,
        newCharacter = HiraganaCharacter.E,
    ),
    Challenge4(
        answer = Answer.E,
    ),
    Challenge5(
        answer = Answer.E,
    ),
    Challenge6(
        answer = Answer.I,
    ),
    Challenge7(
        answer = Answer.IE,
        dictionaryItem = DictionaryItem.IE,
    ),
    Challenge8(
        answer = Answer.IIE,
        dictionaryItem = DictionaryItem.IIE,
    ),
    Challenge9(
        answer = Answer.IIE,
    ),
    Challenge10(
        answer = Answer.IIE,
        secondsToComplete = 10,
    ),
    Challenge11(
        answer = Answer.IIE,
        secondsToComplete = 5,
    ),
    Challenge12(
        answer = Answer.HA,
        newCharacter = HiraganaCharacter.HA,
    ),
    Challenge13(
        answer = Answer.HA,
    ),
    Challenge14(
        answer = Answer.HAI,
        dictionaryItem = DictionaryItem.HAI,
    ),
    Challenge15(
        answer = Answer.HAI,
        secondsToComplete = 5,
    ),
    Challenge16(
        answer = Answer.A,
        newCharacter = HiraganaCharacter.A,
    ),
    Challenge17(
        answer = Answer.A,
    ),
}
