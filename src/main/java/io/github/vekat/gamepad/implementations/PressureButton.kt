package io.github.vekat.gamepad.implementations

import android.content.Context
import io.github.vekat.gamepad.api.*
import java.util.*

/**
 * A button that represents press intensity with a number between 0 and 1.
 */
class PressureButton(context: Context) : Input<FloatArray>(context) {
  override var state: FloatArray by editable(FloatArray(3), { _, old, new ->
    val next = new.map { it.coerceIn(0f..1f) }.toFloatArray()

    if (next.size != old.size || !old.withIndex().any { next[it.index].almostEqualsTo(it.value, 5) }) {
      Option.Something(next)
    } else {
      Option.Nothing()
    }
  })

  override var eventMapper: Mapping<FloatArray, GamepadEvent> = {
    GamepadEvent(GamepadEvent.ACTION_AXIS, code, axis = it)
  }

  override fun intercept(nextState: FloatArray): Boolean = Arrays.equals(state, nextState)
}