package io.github.vekat.gamepad.api

fun <T : ViewHolder> T.gamepad(init: Gamepad.() -> Unit): Gamepad = Gamepad(this).apply(init)