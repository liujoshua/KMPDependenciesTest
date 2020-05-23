/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.zxing.qrcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.Writer
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder.encode
import com.google.zxing.qrcode.encoder.QRCode
import kotlin.math.max
import kotlin.math.min

/**
 * This object renders a QR Code as a BitMatrix 2D array of greyscale values.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
class QRCodeWriter : Writer {
    
    @ExperimentalStdlibApi
    override fun encode(
        contents: String?,
        format: BarcodeFormat?,
        width: Int,
        height: Int
    ): BitMatrix {
        return encode(contents, format, width, height, null)
    }

    
    @ExperimentalStdlibApi
    override fun encode(
        contents: String?,
        format: BarcodeFormat?,
        width: Int,
        height: Int,
        hints: Map<EncodeHintType, *>?
    ): BitMatrix {
        require(!contents!!.isEmpty()) { "Found empty contents" }
        require(!(format !== BarcodeFormat.QR_CODE)) { "Can only encode QR_CODE, but got $format" }
        require(!(width < 0 || height < 0)) {
            "Requested dimensions are too small: " + width + 'x' +
                    height
        }
        var errorCorrectionLevel = ErrorCorrectionLevel.L
        var quietZone = QUIET_ZONE_SIZE
        if (hints != null) {
            if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(
                    hints[EncodeHintType.ERROR_CORRECTION].toString()
                )
            }
            if (hints.containsKey(EncodeHintType.MARGIN)) {
                quietZone = hints[EncodeHintType.MARGIN].toString().toInt()
            }
        }
        val code =
            encode(contents, errorCorrectionLevel, hints)
        return renderResult(code, width, height, quietZone)
    }

    companion object {
        private const val QUIET_ZONE_SIZE = 4

        // Note that the input matrix uses 0 == white, 1 == black, while the output matrix uses
        // 0 == black, 255 == white (i.e. an 8 bit greyscale bitmap).
        private fun renderResult(code: QRCode, width: Int, height: Int, quietZone: Int): BitMatrix {
            val input = code.matrix ?: throw IllegalStateException()
            val inputWidth = input.width
            val inputHeight = input.height
            val qrWidth = inputWidth + quietZone * 2
            val qrHeight = inputHeight + quietZone * 2
            val outputWidth = max(width, qrWidth)
            val outputHeight = max(height, qrHeight)
            val multiple = min(outputWidth / qrWidth, outputHeight / qrHeight)
            // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
            // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
            // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
            // handle all the padding from 100x100 (the actual QR) up to 200x160.
            val leftPadding = (outputWidth - inputWidth * multiple) / 2
            val topPadding = (outputHeight - inputHeight * multiple) / 2
            val output = BitMatrix(outputWidth, outputHeight)
            var inputY = 0
            var outputY = topPadding
            while (inputY < inputHeight) {

                // Write the contents of this row of the barcode
                var inputX = 0
                var outputX = leftPadding
                while (inputX < inputWidth) {
                    if (input[inputX, inputY].toInt() == 1) {
                        output.setRegion(outputX, outputY, multiple, multiple)
                    }
                    inputX++
                    outputX += multiple
                }
                inputY++
                outputY += multiple
            }
            return output
        }
    }
}
