package com.example.wavelet.views

import com.example.wavelet.models.Function
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface IMainView : MvpView {
    //Построить функцию
    fun drawFunc(function: Function)

    //Вывести Wavelet-преобразование
    fun drawWaveletImage(array: Array<DoubleArray>)

    fun setProgressBar(percent: Int)
    fun hideProgressBar()
    fun showProgressBar()
    fun showTimeSuccess(time: String)
}