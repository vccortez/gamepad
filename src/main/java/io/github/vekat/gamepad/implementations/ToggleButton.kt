package io.github.vekat.gamepad.implementations

import android.content.Context
import io.github.vekat.gamepad.api.GamepadEvent
import io.github.vekat.gamepad.api.Input
import io.github.vekat.gamepad.api.Mapping

/**
 * Class description.
 */
class ToggleButton(context: Context) : Input<Boolean>(context) {
  // override var latestData: MutableMap<Source, ContextData> = mutableMapOf()

  override var state: Boolean = false

  override var eventMapper: Mapping<Boolean, GamepadEvent> = { state ->
    GamepadEvent(if (state) GamepadEvent.ACTION_DOWN else GamepadEvent.ACTION_UP, code)
  }

  // val sourceMap: MutableMap<String, Source> = mutableMapOf()

  /*override fun registerSource(name: String, source: Source) {
    sourceMap[name] = source
  }*/

  /*override fun aggregate(vararg dataset: Pair<ContextData, String>) {
    for ((map, name) in dataset) {
      val source = sourceMap[name]

      if (source != null) {
        latestData[source] = map
      }
    }
  }*/

  override fun intercept(nextState: Boolean): Boolean = state == nextState
}