package com.griga.composition.domain.usecases

import com.griga.composition.domain.entities.GameSettings
import com.griga.composition.domain.entities.Level
import com.griga.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}