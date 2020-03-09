package com.example.wavelet.presenters.wavelet

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import com.example.wavelet.R
import com.example.wavelet.models.Function
import com.example.wavelet.models.Image
import com.example.wavelet.views.IMainView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter


class MainActivity : MvpAppCompatActivity(),
    IMainView {

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    companion object {
        const val width = 500
        const val height = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter.image = Image(
            width,
            height
        )
        btSimpleFunc.setOnClickListener {
            mainPresenter.createSimpleFunc()
        }
        btWaveFunc.setOnClickListener {
            mainPresenter.createWaveletFunc()
            mainPresenter.createWaveletTransform()
        }
    }

    override fun drawFunc(function: Function) {
        graph.series.clear()
        val series = LineGraphSeries<DataPoint>()
        for (coordinate in function.coordinateList) {
            series.appendData(DataPoint(coordinate.x, coordinate.y), true, 1500)
        }
        graph.viewport.setMinX(-5.0)
        graph.viewport.setMaxX(5.0)
        graph.viewport.setMinY(-5.0)
        graph.viewport.setMaxY(5.0)
        graph.viewport.isScrollable = true
        graph.viewport.isScrollable = true
        graph.viewport.isScalable = true
        graph.addSeries(series)
    }

    override fun drawWaveletImage(array: Array<DoubleArray>) {
        val bitmap = Bitmap.createBitmap(
            width,
            height, Bitmap.Config.ARGB_8888);
        var r: Int
        var g: Int
        var b: Int
        for (i in 0 until width) {
            for (j in 0 until height) {
                r = array[i][j].toInt()
                bitmap.setPixel(i, j, Color.rgb(r, r, r))
            }
        }
        ivWavelet.setImageBitmap(bitmap)
    }
}
