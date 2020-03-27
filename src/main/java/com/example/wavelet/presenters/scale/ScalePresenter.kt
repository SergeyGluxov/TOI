package com.example.wavelet.presenters.scale


import com.example.wavelet.views.IScaleView
import moxy.InjectViewState
import moxy.MvpPresenter


@InjectViewState
class ScalePresenter : MvpPresenter<IScaleView>() {
    val TAG = ScalePresenter::class.java.simpleName
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }
}