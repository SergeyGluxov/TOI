package com.example.wavelet.views

import androidx.annotation.Nullable
import com.example.wavelet.models.Function
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface IScaleView : MvpView {

}