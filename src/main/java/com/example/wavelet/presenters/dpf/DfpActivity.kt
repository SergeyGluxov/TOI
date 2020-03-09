package com.example.wavelet.presenters.dpf

import android.content.Intent
import android.os.Bundle
import com.example.wavelet.R
import com.example.wavelet.models.Function
import com.example.wavelet.presenters.wavelet.MainActivity
import com.example.wavelet.views.IDpfView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_dfp.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.graph
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class DfpActivity : MvpAppCompatActivity(), IDpfView {

    @InjectPresenter
    lateinit var dpfPresenter: DpfPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dfp)
        nextLab.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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
}
