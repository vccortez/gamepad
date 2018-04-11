package io.github.vekat.gamepad.api

import android.content.Context
import android.view.KeyEvent
import android.view.View

/**
 * A Gamepad object is responsible for grouping related [inputs][Input] and redirecting their input
 * events to a [view holder][ViewHolder] implementation.
 *
 * @param viewHolder input events receiver
 */
class Gamepad(val viewHolder: ViewHolder) : GenericSubscriber<GamepadEvent, Int> {
  val latestCode: Int get() = currentKeyCode

  private var initialKeyCode = KeyEvent.getMaxKeyCode()

  private var currentKeyCode = 1

  private val view: View get() = viewHolder.instance

  private val inputs = mutableMapOf<Int, AnyInput>()

  /**
   * Disable all [inputs][Input] associated with this [gamepad][Gamepad].
   */
  fun disableInputEvents() {
    for ((_, input) in inputs) {
      input.pause()
    }
  }

  /**
   * Enable all [inputs][Input] associated with this [gamepad][Gamepad].
   */
  fun enableInputEvents() {
    for ((_, input) in inputs) {
      input.resume()
    }
  }

  fun <T : AnyInput> addInput(factory: Mapping<Context, T>, init: T.() -> Unit): T {
    val reference = factory(view.context).apply(init)
    register(reference)
    return reference
  }

  fun <T : AnyInput> removeInput(reference: T) {
    unregister(reference)
  }

  private fun register(input: AnyInput) {
    val nextCode = initialKeyCode + currentKeyCode++
    input.apply { code = nextCode; ready = true }
    subscribe(input.channel, input.code)
    inputs.put(input.code, input)
  }

  private fun unregister(input: AnyInput) {
    input.ready = false
    input.pause()
    unsubscribe(input.channel)
    inputs.remove(input.code, input)
  }

  override fun onData(data: Pair<GamepadEvent, Int>) {
    viewHolder.dispatchGamepadEvent(data.first)
  }

  private fun ViewHolder.dispatchGamepadEvent(event: GamepadEvent) {
    onGamepadEvent(event)
  }
}
