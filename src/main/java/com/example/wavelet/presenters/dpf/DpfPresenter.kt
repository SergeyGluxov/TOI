package com.example.wavelet.presenters.dpf

import com.example.wavelet.models.Coordinate
import com.example.wavelet.models.Function
import com.example.wavelet.views.IDpfView
import moxy.InjectViewState
import moxy.MvpPresenter
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

@InjectViewState
class DpfPresenter : MvpPresenter<IDpfView>() {

    companion object {
        private const val period = 10 * Math.PI
        var step: Double = period / (200 - 1)

    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        createSimpleFunc()
    }

    fun createSimpleFunc() {
        var i: Double = -period
        val coordinate = ArrayList<Coordinate>()
        while (i <= period) {
            coordinate.add(
                Coordinate(
                    i,
                    simpleFunc(i)
                )
            )
            i += step
        }
        viewState.drawFunc(Function(coordinate))
    }

    private fun simpleFunc(j: Double): Double {
        if (j < 4 * Math.PI)
            return atan(cos(0.5 * j));
        if ((j > 4 * Math.PI) && (j < 8 * Math.PI))
            return atan(cos(0.5 * j)) + 0.5 * sin(5 * j);
        else return sin(5 * j);
    }

}