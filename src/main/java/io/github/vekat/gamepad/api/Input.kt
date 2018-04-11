package io.github.vekat.gamepad.api

import android.content.Context

/**
 * A generic input member of a [Gamepad] instance.
 *
 * @param context android application context
 * @param T type of internal state.
 */
abstract class Input<T>(context: Context) : ContextElement(context), Subscriber<ContextData> {
  open var ready: Boolean = false
  open var code: Int = -1
  open var handler: (ContextMap) -> T = { state }

  private val sourceMap: MutableMap<String, AnySource> = mutableMapOf()

  private val latestData: MutableMap<AnySource, ContextData> = mutableMapOf()

  abstract var state: T
  abstract val eventMapper: Mapping<T, GamepadEvent>

  internal val channel = object : GenericChannel<GamepadEvent, Int> {
    private val subscribers: MutableMap<GenericSubscriber<*, Int>, Pair<PairListener<GamepadEvent, Int>, Int>> = mutableMapOf()

    override fun register(subscriber: GenericSubscriber<*, Int>, listener: PairListener<GamepadEvent, Int>, metadata: Int) {
      subscribers.put(subscriber, Pair(listener, metadata))
    }

    override fun unregister(subscriber: GenericSubscriber<*, Int>) {
      subscribers.remove(subscriber)
    }

    override fun broadcast(data: GamepadEvent) =
      subscribers.forEach { (_, v) -> v.first.invoke(Pair(data, v.second)) }
  }

  private fun registerSource(name: String, source: AnySource) {
    sourceMap[name] = source
  }

  private fun aggregate(vararg dataset: Pair<ContextData, String>) {
    for ((contextData, name) in dataset) {
      val source = sourceMap[name]

      if (source != null) {
        latestData[source] = contextData
      }
    }
  }

  /**
   * Decides if the next event should be prevented based on the next state.
   * This method is called before assigning the new value to [state].
   *
   * @param nextState The next value being assigned to [state].
   */
  open fun intercept(nextState: T): Boolean = false

  private inline fun transformer(handler: (ContextMap) -> T): T {
    return handler.invoke(ContextMap(sourceMap.toMap(), latestData.toMap()))
  }

  private fun handle(): T = transformer(handler)

  override fun onData(data: Pair<ContextData, String>) {
    if (!ready) return

    aggregate(data)

    val nextState = handle()

    if (!intercept(nextState)) {
      channel.broadcast(eventMapper.invoke(nextState))
    }

    state = nextState
  }

  fun <A : AnySource> addSource(factory: Mapping<Context, A>, identifier: String): A {
    val source = factory.invoke(context)

    registerSource(identifier, source)

    aggregate(Pair(source.defaultData, identifier))

    return source
  }

  /**
   * Un-subscribe from all sources.
   */
  internal fun pause() {
    for ((_, source) in sourceMap) {
      unsubscribe(source.channel)
    }
  }

  /**
   * Subscribe to all sources.
   */
  internal fun resume() {
    for ((id, source) in sourceMap) {
      subscribe(source.channel, id)
    }
  }
}
