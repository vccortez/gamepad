package io.github.vekat.gamepad.implementations

import io.github.vekat.gamepad.api.AnyInput

fun <T : AnyInput> T.accelerometer(identifier: String): Accelerometer =
  addSource(Accelerometer.Companion::getInstance, identifier)

fun <T : AnyInput> T.proximity(identifier: String): Proximity =
  addSource(Proximity.Companion::getInstance, identifier)

fun <T : AnyInput> T.orientation(identifier: String): Orientation =
  addSource(Orientation.Companion::getInstance, identifier)