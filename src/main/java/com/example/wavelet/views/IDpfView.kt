package com.example.wavelet.views

import com.example.wavelet.models.Function
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface IDpfView : MvpView {
    fun drawFunc(function: Function)
}