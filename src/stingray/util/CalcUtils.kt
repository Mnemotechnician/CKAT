package stingray.util

import arc.struct.*

/** Creates a string containing the integer part of the number and up to $digits digits of the decimal part. The argument must be > 0. */
fun Float.toFixed(digits: Int) = buildString {
	if (this@toFixed < 0) append('-')
	
	append(this@toFixed.toInt())
	append('.')
	
	var dec = this@toFixed
	repeat(digits) {
		dec = (dec * 10f) % 10f;
		append(dec.toInt())
	}
};