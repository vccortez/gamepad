package io.github.vekat.gamepad.api

/**
 * Common types
 */
// typealias ContextData = MutableMap<String, FloatArray>

/**
 * Lambda types
 */
typealias Listener<T> = (data: T) -> Unit

typealias PairListener<T, U> = (pair: Pair<T, U>) -> Unit

typealias Mapping<T, U> = (data: T) -> U

typealias PairMapping<T, U> = (data: Pair<T, U>) -> T

/**
 * Generic shortcuts
 */
typealias Channel<T> = GenericChannel<T, String>

typealias Subscriber<T> = GenericSubscriber<T, String>

typealias SubscriberMap<T> = MutableMap<AnySubscriber, Pair<PairListener<T, String>, String>>

/** Alias for [inputs][Input] of wildcard type */
typealias AnyInput = Input<*>

/** Alias for [sources][Source] of wildcard type */
typealias AnySource = Source<*>

/** Alias for [subscribers][Subscriber] of wildcard type */
typealias AnySubscriber = Subscriber<*>