package io.github.vekat.gamepad.api

data class GamepadEvent(
  val action: Int = ACTION_DOWN,
  val code: Int = CODE_UNKNOWN,
  val axis: FloatArray = FloatArray(0),
  val timestamp: Long = System.currentTimeMillis()
) {
  companion object {
    val CODE_UNKNOWN: Int = -1
    val ACTION_UP: Int = 1
    val ACTION_DOWN: Int = 2
    val ACTION_AXIS: Int = 3

    fun getNameFor(action: Int): String = when (action) {
      ACTION_DOWN -> "ACTION_DOWN"
      ACTION_UP -> "ACTION_UP"
      ACTION_AXIS -> "ACTION_AXIS"
      else -> "UNKNOWN"
    }
  }
}
