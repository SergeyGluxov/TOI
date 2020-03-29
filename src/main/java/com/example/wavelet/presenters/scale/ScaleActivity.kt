package com.example.wavelet.presenters.scale

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import com.example.wavelet.R
import com.example.wavelet.models.Image
import com.example.wavelet.views.IScaleView
import kotlinx.android.synthetic.main.activity_scale.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter


class ScaleActivity : MvpAppCompatActivity(), IScaleView {

    val TAG = ScaleActivity::class.java.simpleName
    val PICK_IMAGE = 1
    lateinit var bitmap: Bitmap
    lateinit var scaleImage: Image

    @InjectPresenter
    lateinit var scalePresenter: ScalePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)
        nextLab.setOnClickListener {
            startActivity(Intent(this, ScaleActivity::class.java))
        }
        body.visibility = View.GONE
        var scale = 1.0

        btSelectImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }


        plusScale.setOnClickListener {
            if (scale <= 1.5) {
                scale += 0.2
                val endScale = (scale * 100).toInt()
                percentScale.text = "$endScale%"
                ivNoScale.setImageBitmap(
                    scaleImage.scale(
                        bitmap,
                        scale
                    )
                )
            }
        }

        minusScale.setOnClickListener {
            if (scale - 0.2 > 0.2) {
                scale -= 0.2
                val endScale = (scale * 100).toInt()
                percentScale.text = "$endScale%"
                ivNoScale.setImageBitmap(
                    scaleImage.scale(
                        bitmap,
                        scale
                    )
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE) {
            btSelectImage.visibility = View.GONE
            body.visibility = View.VISIBLE
            ivNoScale.setImageURI(data?.data) // handle chosen image
            bitmap = (ivNoScale.drawable as BitmapDrawable).bitmap
            scaleImage = Image(bitmap.width, bitmap.height)
        }
    }
}
