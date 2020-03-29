package com.example.wavelet.models

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.roundToInt


class Image(val width: Int, val height: Int) {

    fun scale(bitmap: Bitmap, scale: Double): Bitmap {
        val matrixRGB = rgbBitmap(bitmap)
        //TODO: сделать по нормальному scale
        val scaleImage = Image((bitmap.width * scale).toInt(), (bitmap.height * scale).toInt())
        val stepH: Double = height.toDouble() / (bitmap.height * scale)
        val stepW: Double = width.toDouble() / (bitmap.width * scale)

        val arrayR = Array(scaleImage.width) { DoubleArray(scaleImage.height) }
        val arrayG = Array(scaleImage.width) { DoubleArray(scaleImage.height) }
        val arrayB = Array(scaleImage.width) { DoubleArray(scaleImage.height) }
        var y: Double = 0.0

        for (i in 0 until scaleImage.width - 1) {
            y = 0.0
            while (y <= height - 1) {
                val x: Double = i.toDouble() / (1.0 / stepW)
                val rX: Int = (x / stepW).roundToInt()
                val rY: Int = (y / stepH).roundToInt().toInt()
                var coordinateX1 = x.roundToInt().toDouble()
                var coordinateY1 = y.roundToInt().toDouble()
                var coordinateX2 = x.roundToInt().toDouble() + 1
                var coordinateY2 = y.roundToInt().toDouble() + 1
                if (coordinateX2 > width) {
                    coordinateX2 = coordinateX1
                }
                if (coordinateY2 > height) {
                    coordinateY2 = coordinateY1
                }
                val currentPoint = Coordinate(x, y)
                val firstPoint = Coordinate(coordinateX1, coordinateY1)
                val twoPoint = Coordinate(coordinateX2, coordinateY2)

                arrayR[rX][rY] =
                    calcRGB(matrixRGB.matrixR, currentPoint, firstPoint, twoPoint)
                arrayG[rX][rY] =
                    calcRGB(matrixRGB.matrixG, currentPoint, firstPoint, twoPoint)
                arrayB[rX][rY] =
                    calcRGB(matrixRGB.matrixB, currentPoint, firstPoint, twoPoint)
                y += stepH
            }
        }
        return createBitmap(scaleImage.width, scaleImage.height, ImageRGB(arrayR, arrayG, arrayB))
    }

    //Convert bitmap to RGB
    private fun rgbBitmap(bitmap: Bitmap): ImageRGB {
        val arrayR = Array(bitmap.width + 1) { DoubleArray(bitmap.height + 1) }
        val arrayG = Array(bitmap.width + 1) { DoubleArray(bitmap.height + 1) }
        val arrayB = Array(bitmap.width + 1) { DoubleArray(bitmap.height + 1) }
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                val pixel = bitmap.getPixel(i, j)
                arrayR[i][j] = Color.red(pixel).toDouble()
                arrayG[i][j] = Color.green(pixel).toDouble()
                arrayB[i][j] = Color.blue(pixel).toDouble()
            }
        }
        return ImageRGB(arrayR, arrayG, arrayB)
    }

    //Create bitmap from RGB
    private fun createBitmap(width: Int, height: Int, imageRGB: ImageRGB): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var r: Int
        var g: Int
        var b: Int
        for (i in 0 until width) {
            for (j in 0 until height) {
                r = imageRGB.matrixR[i][j].toInt()
                g = imageRGB.matrixG[i][j].toInt()
                b = imageRGB.matrixB[i][j].toInt()
                bitmap.setPixel(i, j, Color.rgb(r, g, b))
            }
        }
        return bitmap
    }


    private fun calcRGB(
        array: Array<DoubleArray>,
        currentPoint: Coordinate,
        firstPoint: Coordinate,
        twoPoint: Coordinate
    ): Double {
        return (array[firstPoint.x.toInt()][firstPoint.y.toInt()] * (twoPoint.x - currentPoint.x) * (twoPoint.y - currentPoint.y) +
                (array[twoPoint.x.toInt()][firstPoint.y.toInt()] * (currentPoint.x - firstPoint.x) * (twoPoint.y - currentPoint.y)) +
                (array[firstPoint.x.toInt()][twoPoint.y.toInt()] * (twoPoint.x - currentPoint.x) * (currentPoint.y - firstPoint.y)) +
                (array[twoPoint.x.toInt()][twoPoint.y.toInt()] * (currentPoint.x - firstPoint.x) * (currentPoint.y - firstPoint.y))
                / ((twoPoint.x - firstPoint.x) * (twoPoint.y - firstPoint.y)))
    }
}