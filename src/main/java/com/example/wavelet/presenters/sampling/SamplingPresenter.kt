package com.example.wavelet.presenters.sampling

import android.util.Log
import com.example.wavelet.models.Coordinate
import com.example.wavelet.models.Function
import com.example.wavelet.models.Polinome
import com.example.wavelet.presenters.wavelet.MainPresenter
import com.example.wavelet.views.ISamplingView
import moxy.InjectViewState
import moxy.MvpPresenter
import java.lang.Math.pow
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

@InjectViewState
class SamplingPresenter : MvpPresenter<ISamplingView>() {
    val TAG = SamplingPresenter::class.java.simpleName

    companion object {
        private const val pointCount = 100;
        private const val period = Math.PI
        var step: Double = 4 * period / (pointCount - 1)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        //createSimpleFuncDraw()
        oversampling(createSimpleFunc(), 0)
    }

    override fun attachView(view: ISamplingView?) {
        super.attachView(view)
        oversampling(createSimpleFunc(), 0)
        //createSimpleFuncDraw()
    }

    private fun simpleFunc(j: Double): Double {
        return atan(cos(0.5 * j))
    }

    fun createSimpleFunc(): Function {
        var i: Double = 0.0
        val coordinate = ArrayList<Coordinate>()
        while (i < 4 * period) {
            coordinate.add(
                Coordinate(
                    i,
                    simpleFunc(i)
                )
            )
            i += step
        }
        return Function(coordinate)
    }

    fun createSimpleFuncDraw() {
        var i: Double = -4 * period
        val coordinate = ArrayList<Coordinate>()
        while (i <= 4 * period) {
            coordinate.add(
                Coordinate(
                    i,
                    simpleFunc(i)
                )
            )
            i += step
        }
        viewState.drawFunc(Function(coordinate), null)
    }

    fun oversampling(function: Function, coefficient: Int) {
        val coordinateSampling = ArrayList<Coordinate>()
        var time: Double = 0.0
        var i: Int = 0
        val shagNew = (4 * period) / ((pointCount - 1) * coefficient)

        while (time <= 4 * period) {
            val index = (time / step).toInt()
            if (index == 0) {
                coordinateSampling.add(Coordinate(0.0, 0.0))
                i++
            } else if (index < function.coordinateList.size - 2) {
                Log.d(TAG, "index: $index")
                val x =
                    (time - function.coordinateList[index - 1].x) * (3.0 / (function.coordinateList[index + 2].x - function.coordinateList[index - 1].x)) - 2.0;

                val y =
                    pow(x, 3.0) * Polinome.getA3(function, index) + pow(x, 2.0) * Polinome.getA2(
                        function,
                        index
                    ) + x * Polinome.getA1(function, index) + Polinome.getA0(function, index)
                Log.d(TAG, "x: $x " + " y: $y")
                coordinateSampling.add(Coordinate(time.toDouble(), y))
                i++
            }
            time += shagNew
        }
        viewState.drawFunc(function, Function(coordinateSampling))
    }
}