package io.github.vekat.gamepad.api

import android.view.View

/**
 * Class description.
 */
interface ViewHolder {
  val instance: View

  fun onGamepadEvent(event: GamepadEvent) = Unit
}