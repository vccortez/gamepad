package io.github.vekat.gamepad.implementations

import android.content.Context
import io.github.vekat.gamepad.api.GamepadEvent
import io.github.vekat.gamepad.api.Input
import io.github.vekat.gamepad.api.Mapping
import io.github.vekat.gamepad.api.almostEqualsTo

class AxisButton(context: Context) : Input<Float>(context) {
  override var state: Float = 0f

  override val eventMapper: Mapping<Float, GamepadEvent> = {
    GamepadEvent(action = GamepadEvent.ACTION_AXIS, axis = floatArrayOf(it))
  }

  override fun intercept(nextState: Float): Boolean = state.almostEqualsTo(nextState, 5)
}