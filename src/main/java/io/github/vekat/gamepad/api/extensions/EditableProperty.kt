package io.github.vekat.gamepad.api

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Implements the core logic of a property delegate for a read/write property that calls callback functions when changed.
 * @param initialValue the initial value of the property.
 */
abstract class EditableProperty<T>(initialValue: T) : ReadWriteProperty<Any?, T> {
  protected var value = initialValue

  /**
   *  The callback which is called before a change to the property value is attempted.
   *  The value of the property hasn't been changed yet, when this callback is invoked.
   *  If the callback returns `true` the value of the property is being set to the new value,
   *  and if the callback returns `false` the new value is discarded and the property remains its old value.
   */
  protected open fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Option<T> = Option.Nothing()

  /**
   * The callback which is called after the change of the property is made. The value of the property
   * has already been changed when this callback is invoked.
   */
  protected open fun afterChange(property: KProperty<*>, oldValue: T, newValue: T): Unit {}

  override fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return value
  }

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    val oldValue = this.value

    val result = beforeChange(property, oldValue, value)

    when (result) {
      is Option.Nothing -> return
      is Option.Something<T> -> this.value = result.data
    }

    afterChange(property, oldValue, value)
  }
}

sealed class Option<T> {
  class Nothing<T> : Option<T>()
  data class Something<T>(val data: T) : Option<T>()
}

/**
 * Returns a property delegate for a read/write property that calls a specified callback function when changed,
 * allowing the callback to edit the modification.
 * @param i the initial value of the property.
 * @param onChange the callback which is called before a change to the property value is attempted.
 *  The value of the property hasn't been changed yet, when this callback is invoked.
 *  If the callback returns `true` the value of the property is being set to the new value,
 *  and if the callback returns `false` the new value is discarded and the property remains its old value.
 */
inline fun <T> editable(i: T, crossinline onChange: (p: KProperty<*>, o: T, n: T) -> Option<T>): ReadWriteProperty<Any?, T> {
  return object : EditableProperty<T>(i) {
    override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Option<T> = onChange(property, oldValue, newValue)
  }
}