package com.sechkarev.hiraganateacherkmp.domain

import com.sechkarev.hiraganateacherkmp.data.challenges.AnimationUiComponentDto
import com.sechkarev.hiraganateacherkmp.data.challenges.ChallengeDto
import com.sechkarev.hiraganateacherkmp.data.challenges.ChallengesDataSource
import com.sechkarev.hiraganateacherkmp.data.challenges.ConditionalTextUiComponentDto
import com.sechkarev.hiraganateacherkmp.data.challenges.DrawingChallengeUiComponentDto
import com.sechkarev.hiraganateacherkmp.data.challenges.HeadlineUiComponentDto
import com.sechkarev.hiraganateacherkmp.data.challenges.ImageUiComponentDto
import com.sechkarev.hiraganateacherkmp.data.challenges.NewWordUiComponentDto
import com.sechkarev.hiraganateacherkmp.data.challenges.TextUiComponentDto
import com.sechkarev.hiraganateacherkmp.data.database.ChallengeSolutionDao
import com.sechkarev.hiraganateacherkmp.data.database.ChallengeSolutionEntity
import com.sechkarev.hiraganateacherkmp.data.database.UserEventDao
import com.sechkarev.hiraganateacherkmp.data.database.UserEventEntity
import com.sechkarev.hiraganateacherkmp.data.mapping.ConfigMapper
import com.sechkarev.hiraganateacherkmp.model.CanvasDecoration
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.ChallengeAnswer
import com.sechkarev.hiraganateacherkmp.model.DictionaryItem
import com.sechkarev.hiraganateacherkmp.model.GameProgress
import com.sechkarev.hiraganateacherkmp.model.HiraganaCharacter
import com.sechkarev.hiraganateacherkmp.model.SolvedChallenge
import com.sechkarev.hiraganateacherkmp.model.Stroke
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Animation
import com.sechkarev.hiraganateacherkmp.model.UiComponent.DrawingChallenge
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Headline
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Image
import com.sechkarev.hiraganateacherkmp.model.UiComponent.NewWord
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Text
import com.sechkarev.hiraganateacherkmp.model.UserEvent
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class GameRepository(
    private val challengeSolutionDao: ChallengeSolutionDao,
    private val userEventDao: UserEventDao,
    private val challengesDataSource: ChallengesDataSource,
    private val configMapper: ConfigMapper,
) {
    private var initialised = false

    private lateinit var challenges: List<ChallengeDto>
    private lateinit var hiraganaCharacters: List<HiraganaCharacter>
    private lateinit var challengeAnswers: List<ChallengeAnswer>
    private lateinit var dictionaryItems: List<DictionaryItem>

    suspend fun init() {
        if (initialised) {
            return
        }

        hiraganaCharacters =
            challengesDataSource
                .parseHiraganaCharacters()
                .map {
                    HiraganaCharacter(
                        name = it.name,
                        spelling = it.spelling.first(), // should only be one character
                        pronunciation = it.pronunciation,
                        gridCell = it.gridCell,
                        requiredStrokes = it.requiredStrokes,
                    )
                }

        challengeAnswers =
            challengesDataSource
                .parseChallengeAnswers()
                .map {
                    ChallengeAnswer(
                        name = it.name,
                        answerText = it.answerText,
                    )
                }

        dictionaryItems =
            challengesDataSource
                .parseDictionaryItems()
                .map {
                    DictionaryItem(
                        name = it.name,
                        original = it.original,
                        translation = configMapper.mapDictionaryItemIdToTranslation(it.name),
                        onlineDictionaryLink = configMapper.mapDictionaryItemIdToOnlineDictionaryLink(it.name),
                    )
                }

        challenges =
            challengesDataSource
                .parseChallenges()

        initialised = true
    }

    fun getRequiredStrokesNumber(challengeAnswer: ChallengeAnswer) =
        challengeAnswer
            .answerText
            .map { answerCharacter ->
                hiraganaCharacters.first { it.spelling == answerCharacter }.requiredStrokes
            }.sum()

    suspend fun retrieveGameProgress(): GameProgress {
        val solutions = challengeSolutionDao.retrieveAllSolutions()
        return GameProgress(
            solvedChallenges =
                solutions.map { challengeSolution ->
                    SolvedChallenge(
                        challenge = mapChallengeDtoToChallenge(challenges.first { it.name == challengeSolution.challengeId }),
                        solution = Json.decodeFromString(challengeSolution.solution),
                    )
                },
            currentChallenge =
                if (solutions.size < challenges.size) {
                    mapChallengeDtoToChallenge(challenges[solutions.size])
                } else {
                    null
                },
            gameCompleted = solutions.size >= challenges.size,
        )
    }

    suspend fun getUnlockedDictionaryItems(): List<DictionaryItem> {
        val challengeSolutions = challengeSolutionDao.retrieveAllSolutions()
        return challenges
            .filter { challenge ->
                challengeSolutions.any { it.challengeId == challenge.name }
            }.mapNotNull { challenge ->
                dictionaryItems.firstOrNull { challenge.dictionaryItem == it.name }
            }
    }

    suspend fun getUnlockedCharacters(): List<HiraganaCharacter> {
        val challengeSolutions = challengeSolutionDao.retrieveAllSolutions()
        return challenges
            .filter { challenge ->
                challengeSolutions.any { it.challengeId == challenge.name }
            }.mapNotNull { challenge ->
                hiraganaCharacters.firstOrNull { challenge.newCharacter == it.name }
            }
    }

    suspend fun insertSolution(
        challengeId: String,
        solution: List<Stroke>,
    ) {
        challengeSolutionDao.insertSolution(
            ChallengeSolutionEntity(
                challengeId = challengeId,
                solution = Json.encodeToString(solution),
            ),
        )
    }

    suspend fun retrieveNextChallenge(currentChallengeId: String): Challenge? {
        val indexOfCurrentChallenge = challenges.indexOfFirst { it.name == currentChallengeId }
        return if (indexOfCurrentChallenge == challenges.lastIndex) {
            null
        } else {
            mapChallengeDtoToChallenge(challenges[indexOfCurrentChallenge + 1])
        }
    }

    suspend fun deleteAllSolutions() {
        challengeSolutionDao.deleteAllSolutions()
    }

    suspend fun logUserEvent(userEvent: UserEvent) {
        userEventDao.insertUserEvent(
            UserEventEntity(
                eventName = userEvent.name,
                timestamp = Clock.System.now().epochSeconds,
            ),
        )
    }

    // We're mapping a DTO to the actual model only when it is required to take into account the data for conditionals.
    private suspend fun mapChallengeDtoToChallenge(challengeDto: ChallengeDto) =
        Challenge(
            name = challengeDto.name,
            challengeAnswer = challengeAnswers.first { challengeDto.answer == it.name },
            dictionaryItem =
                if (challengeDto.dictionaryItem == null) {
                    null
                } else {
                    dictionaryItems.first { challengeDto.dictionaryItem == it.name }
                },
            newCharacter =
                if (challengeDto.newCharacter == null) {
                    null
                } else {
                    hiraganaCharacters.first { challengeDto.newCharacter == it.name }
                },
            secondsToComplete = challengeDto.secondsToComplete,
            uiComponents =
                challengeDto.uiComponents.map { uiComponent ->
                    when (uiComponent) {
                        is HeadlineUiComponentDto -> {
                            Headline(
                                textResource = configMapper.mapHeadlineConfigIdToStringResource(uiComponent.properties.textId),
                            )
                        }
                        is AnimationUiComponentDto -> {
                            Animation(
                                animationId = configMapper.mapAnimationConfigIdToFileName(uiComponent.properties.animationId),
                            )
                        }
                        is DrawingChallengeUiComponentDto -> {
                            DrawingChallenge(
                                hintResource =
                                    uiComponent.properties.hint?.let {
                                        configMapper.mapHintConfigIdToDrawableResource(it)
                                    },
                                decoration =
                                    if (uiComponent.properties.canvasDecoration == null) {
                                        null
                                    } else {
                                        CanvasDecoration.entries.first {
                                            it.configKey == uiComponent.properties.canvasDecoration
                                        }
                                    },
                            )
                        }
                        is NewWordUiComponentDto -> {
                            NewWord(
                                word = dictionaryItems.first { it.name == uiComponent.properties.word },
                            )
                        }
                        is TextUiComponentDto -> {
                            Text(
                                textResource = configMapper.mapTextConfigIdToStringResource(uiComponent.properties.textId),
                            )
                        }
                        is ImageUiComponentDto -> {
                            Image(
                                link = uiComponent.properties.link,
                                courtesy = uiComponent.properties.courtesy,
                                imageResource = configMapper.mapImageIdToDrawableResource(uiComponent.properties.imageId),
                                descriptionResource =
                                    configMapper.mapImageIdToDescriptionResource(
                                        uiComponent.properties.imageId,
                                    ),
                            )
                        }
                        is ConditionalTextUiComponentDto -> {
                            Text(
                                textResource =
                                    configMapper.mapConditionalStringIdToResource(
                                        if (mapChallengeIdToCondition(challengeDto.name).invoke()) {
                                            uiComponent.properties.trueTextId
                                        } else {
                                            uiComponent.properties.falseTextId
                                        },
                                    ),
                            )
                        }
                    }
                },
        )

    private fun mapChallengeIdToCondition(challengeId: String): suspend () -> Boolean =
        when (challengeId) {
            "aoi_repetition" -> ::didUserVisitDictionaryWhileCompletingBlueHouseChallenge
            else -> throw IllegalArgumentException("Can't find the lambda corresponding with the challenge id $challengeId")
        }

    private suspend fun didUserVisitDictionaryWhileCompletingBlueHouseChallenge(): Boolean {
        val openDictionaryEvent = userEventDao.retrieveLastEventByName("open_dictionary") ?: return false
        val completeNewWordAoiChallengeEvent =
            userEventDao.retrieveLastEventByName("beat_challenge_new_word_aoi")
                ?: throw IllegalStateException("New word: aoi challenge must be completed already")
        val completeBlueHouseChallengeEvent =
            userEventDao.retrieveLastEventByName("beat_challenge_blue_house")
                ?: throw IllegalStateException("Blue house challenge must be completed already")
        return (openDictionaryEvent.timestamp > completeNewWordAoiChallengeEvent.timestamp)
            .and(openDictionaryEvent.timestamp < completeBlueHouseChallengeEvent.timestamp)
    }
}
