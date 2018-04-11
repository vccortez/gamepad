package io.github.vekat.gamepad.api

/**
 * [Broadcasts][broadcast] data of type [T] to all [registered][register] [subscriber][GenericSubscriber].
 *
 * @param T data type produced by this channel
 * @param U metadata type attached to subscribers
 */
interface GenericChannel<T, U> {
  fun register(subscriber: GenericSubscriber<*, U>, listener: Listener<Pair<T, U>>, metadata: U)

  fun unregister(subscriber: GenericSubscriber<*, U>)

  fun broadcast(data: T)
}

/**
 * Used to [subscribe] to data [channels][GenericChannel].
 *
 * @param T data type received from channel
 * @param U metadata type received from channel
 */
interface GenericSubscriber<in T, in U> {
  fun onData(data: Pair<T, U>)

  fun <V : T, W : U> subscribe(channel: GenericChannel<V, W>, metadata: W) {
    channel.register(this, this::onData, metadata)
  }

  fun <V, W : U> subscribe(channel: GenericChannel<V, W>, metadata: W, adapter: Mapping<V, T>) {
    val mapper: Listener<Pair<V, U>> = { (this::onData)(Pair(adapter.invoke(it.first), it.second)) }
    channel.register(this, mapper, metadata)
  }

  fun <W : U> unsubscribe(channel: GenericChannel<*, W>) {
    channel.unregister(this)
  }
}