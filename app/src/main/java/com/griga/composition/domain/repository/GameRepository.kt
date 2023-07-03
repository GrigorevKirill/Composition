package com.griga.composition.domain.repository

import com.griga.composition.domain.entities.GameSettings
import com.griga.composition.domain.entities.Level
import com.griga.composition.domain.entities.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}