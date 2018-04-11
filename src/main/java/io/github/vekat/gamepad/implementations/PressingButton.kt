package io.github.vekat.gamepad.implementations

import android.content.Context
import io.github.vekat.gamepad.api.GamepadEvent
import io.github.vekat.gamepad.api.Input
import io.github.vekat.gamepad.api.Mapping
import kotlin.properties.Delegates

class PressingButton(context: Context) : Input<Boolean>(context) {
  override var state: Boolean by Delegates.observable(false, { _, _, condition ->
    machineState = if (machineState == STATE.RELEASED && condition)
      STATE.PRESSED
    else if (machineState == STATE.PRESSED && !condition)
      STATE.RELEASED
    else
      machineState
  })

  override val eventMapper: Mapping<Boolean, GamepadEvent> = { GamepadEvent(GamepadEvent.ACTION_AXIS) }

  enum class STATE { RELEASED, PRESSED }

  private var machineState: STATE = STATE.RELEASED

  override fun intercept(nextState: Boolean): Boolean {
    return !(machineState == STATE.PRESSED && state && !nextState)
  }
}