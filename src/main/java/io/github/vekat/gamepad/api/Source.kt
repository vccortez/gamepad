package io.github.vekat.gamepad.api

import android.content.Context
import kotlin.properties.Delegates

/**
 * Class description.
 */
abstract class Source<out T> constructor(context: Context) : ContextElement(context) {
  abstract val defaultData: ContextData

  abstract fun onStart()

  abstract fun onEnd()

  abstract fun transform(data: ContextData): T

  val channel = object : Channel<ContextData> {
    private val subscribers: SubscriberMap<ContextData> = mutableMapOf()

    private var subscriberCount: Int by Delegates.observable(subscribers.size) { _, _, newCount ->
      if (newCount > 0) onStart() else onEnd()
    }

    override fun register(subscriber: AnySubscriber, listener: PairListener<ContextData, String>, metadata: String) {
      if (subscribers.put(subscriber, Pair(listener, metadata)) == null) {
        subscriberCount = subscribers.size
      }
    }

    override fun broadcast(data: ContextData) =
      subscribers.forEach { it.value.first.invoke(Pair(data, it.value.second)) }

    override fun unregister(subscriber: AnySubscriber) {
      if (subscribers.remove(subscriber) != null) {
        subscriberCount = subscribers.size
      }
    }
  }
}