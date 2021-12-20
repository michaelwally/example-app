package com.breathwork.challenge.presentation

import android.accounts.NetworkErrorException
import android.util.Log
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

// Would be preferred to get these from strings resources so easier for multi-language support,
// done this way in interest of time.
fun Exception.resolveFailure() =
    when (this) {
        is ConnectException -> State.FailureState("Please check your connection and try again.")
        is NetworkErrorException -> State.FailureState("Please check your connection and try again.")
        is SocketTimeoutException -> State.FailureState("Connection error. Please try again.")
        is HttpException -> State.FailureState(getErrorMessage())
        else -> {
            Log.d("Extensions", "resolveFailure - unhandled exception $this")
            State.FailureState("An unknown error occurred. Please try again.")
        }
    }

private fun HttpException.getErrorMessage() =
    when (this.code()) {
        400 -> "Server error. Please try again later."
        401 -> "Authentication error."
        else -> {
            Log.d("Extensions", "getErrorMessage - unhandled code ${this.code()}")
            "An unknown error occurred. Please try again."
        }
    }