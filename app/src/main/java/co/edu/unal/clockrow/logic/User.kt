package co.edu.unal.clockrow.logic

import co.edu.unal.clockrow.ProviderType

data class User (
        val email: String,
        val username: String,
        val provider: ProviderType,
        val isPremium: Boolean
        )