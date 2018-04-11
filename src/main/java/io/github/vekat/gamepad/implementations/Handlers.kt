package io.github.vekat.gamepad.implementations

import com.fuzzylite.Engine
import io.github.vekat.gamepad.api.ContextMap

fun <T> fuzzyController(init: Engine.() -> Unit, before: (ContextMap) -> DoubleArray, after: (DoubleArray) -> T): (ContextMap) -> T {
  val engine = Engine().apply(init)

  return fun(sensorData: ContextMap): T {
    val inputArray = before(sensorData)

    assert(inputArray.size == engine.numberOfInputVariables())

    for (item in inputArray.withIndex()) {
      engine.getInputVariable(item.index).value = item.value
    }

    engine.process()

    val outputArray = DoubleArray(engine.numberOfOutputVariables(), { engine.getOutputVariable(it).value })

    return after(outputArray)
  }
}