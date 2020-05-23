package com.google.zxing

fun arraycopy(
    src: IntArray, srcPos: Int,
    dest: IntArray, destPos: Int,
    length: Int
) {
    src.copyInto(dest, destPos, srcPos, length + srcPos)
}
