package io.github.vekat.gamepad.api

/**
 * Class description.
 */
class ContextMap(val keyMap: Map<String, AnySource>, val dataMap: Map<AnySource, ContextData>) {
  operator fun <A, B> get(src: A): B? where A : Source<B> {
    return if (dataMap.containsKey(src)) {
      src.transform(dataMap[src]!!)
    } else {
      null
    }
  }

  inline operator fun <reified A, B> get(key: String): B? where A : Source<B> {
    return if (keyMap.containsKey(key) && keyMap[key] is A) {
      val src = keyMap[key] as A
      src.transform(dataMap[src]!!)
    } else {
      null
    }
  }
}