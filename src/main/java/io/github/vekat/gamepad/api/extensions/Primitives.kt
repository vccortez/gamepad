package io.github.vekat.gamepad.api

internal val Float.intBits: Int
  get() = java.lang.Float.floatToIntBits(this)

/**
 * Function to compare floating point numbers considering the integer representation of a float's
 * bit pattern and the range of acceptable units around the representation to consider equal.
 *
 * @param other the other floating point to compare with.
 * @param maxUlps number of acceptable units around current float.
 * @return is true if is almost equals, false otherwise.
 *
 * @see [Comparing floating point numbers](https://goo.gl/chcF2Z)
 */
internal fun Float.almostEqualsTo(other: Float, maxUlps: Int): Boolean {
  // Make sure maxUlps is non-negative and small enough that the
  // default NAN won't compare as equal to anything.
  assert(maxUlps > 0 && maxUlps < 4 * 1024 * 1024)

  var aInt = this.intBits
  // Make aInt lexicographically ordered as a twos-complement int
  if (aInt < 0) aInt = -2147483648 - aInt

  var bInt = other.intBits
  // Make bInt lexicographically ordered as a twos-complement int
  if (bInt < 0) bInt = -2147483648 - bInt

  val intDiff = Math.abs(aInt - bInt)

  if (intDiff <= maxUlps) return true

  return false
}

internal fun Float.normalize(range: Float, negative: Boolean = false): Float {
  if (negative) return this.div(range.div(2)).coerceIn(-1f..1f)

  return this.div(range).coerceIn(0f..1f)
}

internal inline fun Boolean.ok(block: () -> Unit): Boolean {
  if (this) block(); return this
}