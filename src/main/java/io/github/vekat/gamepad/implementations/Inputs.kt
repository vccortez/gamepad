package io.github.vekat.gamepad.implementations

import io.github.vekat.gamepad.api.Gamepad

fun Gamepad.toggleButton(init: ToggleButton.() -> Unit): ToggleButton =
  addInput(::ToggleButton, init)

fun Gamepad.pressureButton(init: PressureButton.() -> Unit): PressureButton =
  addInput(::PressureButton, init)

fun Gamepad.pressingButton(init: PressingButton.() -> Unit): PressingButton =
  addInput(::PressingButton, init)

fun Gamepad.axisButton(init: AxisButton.() -> Unit): AxisButton =
  addInput(::AxisButton, init)