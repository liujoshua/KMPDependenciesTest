/*
 * Copyright 2007 ZXing authors
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
package com.google.zxing.common.reedsolomon

import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 *
 * This class contains utility methods for performing mathematical operations over
 * the Galois Fields. Operations use a given primitive polynomial in calculations.
 *
 *
 * Throughout this package, elements of the GF are represented as an `int`
 * for convenience and speed (but at the cost of memory).
 *
 *
 * @author Sean Owen
 * @author David Olivier
 */
class GenericGF(private val primitive: Int, val size: Int, val generatorBase: Int) {
    private val expTable: IntArray
    private val logTable: IntArray
    internal val zero: GenericGFPoly
    internal val one: GenericGFPoly

    /**
     * @return the monomial representing coefficient * x^degree
     */
    internal fun buildMonomial(degree: Int, coefficient: Int): GenericGFPoly {
        require(degree >= 0)
        if (coefficient == 0) {
            return zero
        }
        val coefficients = IntArray(degree + 1)
        coefficients[0] = coefficient
        return GenericGFPoly(this, coefficients)
    }

    /**
     * @return 2 to the power of a in GF(size)
     */
    fun exp(a: Int): Int {
        return expTable[a]
    }

    /**
     * @return base 2 log of a in GF(size)
     */
    fun log(a: Int): Int {
        require(a != 0)
        return logTable[a]
    }

    /**
     * @return multiplicative inverse of a
     */
    fun inverse(a: Int): Int {
        if (a == 0) {
            throw ArithmeticException()
        }
        return expTable[size - logTable[a] - 1]
    }

    /**
     * @return product of a and b in GF(size)
     */
    fun multiply(a: Int, b: Int): Int {
        return if (a == 0 || b == 0) {
            0
        } else expTable[(logTable[a] + logTable[b]) % (size - 1)]
    }

    override fun toString(): String {

        return "GF(0x" + primitive.toString(16) + ',' + size + ')'
    }

    companion object {
        @JvmField
        val QR_CODE_FIELD_256 = GenericGF(0x011D, 256, 0) // x^8 + x^4 + x^3 + x^2 + 1

        /**
         * Implements both addition and subtraction -- they are the same in GF(size).
         *
         * @return sum/difference of a and b
         */
        @JvmStatic
        fun addOrSubtract(a: Int, b: Int): Int {
            return a xor b
        }
    }

    /**
     * Create a representation of GF(size) using the given primitive polynomial.
     *
     * @param primitive irreducible polynomial whose coefficients are represented by
     * the bits of an int, where the least-significant bit represents the constant
     * coefficient
     * @param size the size of the field
     * @param b the factor b in the generator polynomial can be 0- or 1-based
     * (g(x) = (x+a^b)(x+a^(b+1))...(x+a^(b+2t-1))).
     * In most cases it should be 1, but for QR code it is 0.
     */
    init {
        expTable = IntArray(size)
        logTable = IntArray(size)
        var x = 1
        for (i in 0 until size) {
            expTable[i] = x
            x *= 2 // we're assuming the generator alpha is 2
            if (x >= size) {
                x = x xor primitive
                x = x and size - 1
            }
        }
        for (i in 0 until size - 1) {
            logTable[expTable[i]] = i
        }
        // logTable[0] == 0 but this should never be used
        zero = GenericGFPoly(this, intArrayOf(0))
        one = GenericGFPoly(this, intArrayOf(1))
    }
}
