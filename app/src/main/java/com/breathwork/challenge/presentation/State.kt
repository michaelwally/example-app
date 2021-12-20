package com.breathwork.challenge.presentation

sealed class State<out ResultType> {
    object LoadingState : State<Nothing>()
    data class SuccessState<ResultType>(var result: ResultType) : State<ResultType>()
    data class FailureState(var message: String) : State<Nothing>()
}
