package com.sibedge.yokodzun.common.utils

import com.sibedge.yokodzun.common.exception.ApiException
import ru.hnau.jutils.TimeValue


object Validators {

    private val BASE_SUPPORTED_SYMBOLS = (('а'..'я') + ('А'..'Я') + ('a'..'z') + ('A'..'Z') + ('0'..'9') + "!@#$%^&*()_+=-;:,.<>{}[]/?№".toList()).toHashSet()
    private val BASE_SUPPORTED_SYMBOLS_WITH_SPACE = BASE_SUPPORTED_SYMBOLS + ' '

    const val MIN_PASSWORD_LENGTH = 3
    const val MAX_PASSWORD_LENGTH = 256
    val SUPPORTED_PASSWORD_SYMBOLS = BASE_SUPPORTED_SYMBOLS

    fun validatePasswordOrThrow(password: String) =
            validateString(password, "пароля", MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH, SUPPORTED_PASSWORD_SYMBOLS)


    const val CREATE_BATTLES_RATERS_MIN_COUNT = 1
    const val CREATE_BATTLES_RATERS_MAX_COUNT = 100

    fun validateCreateBattlesRatersCount(count: Int) {
        if (count < CREATE_BATTLES_RATERS_MIN_COUNT) {
            throw ApiException.raw("Минимальное количество голосующих для создания: $CREATE_BATTLES_RATERS_MIN_COUNT")
        }
        if (count > CREATE_BATTLES_RATERS_MAX_COUNT) {
            throw ApiException.raw("Максимальное количество голосующих для создания: $CREATE_BATTLES_RATERS_MAX_COUNT")
        }
    }


    private fun validateString(
            string: String,
            name: String,
            minLength: Int,
            maxLength: Int,
            supportedSymbols: Set<Char>
    ) {
        if (string.length < minLength) {
            throw ApiException.raw("Минимальная длина $name - $minLength")
        }

        if (string.length > maxLength) {
            throw ApiException.raw("Максимальная длина $name - $maxLength")
        }

        string.forEach {
            if (it !in supportedSymbols) {
                throw ApiException.raw("Недопустимый символ в строке $name - '$it'")
            }
        }


    }

}