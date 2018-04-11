package io.github.vekat.gamepad.implementations

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.github.vekat.gamepad.api.ContextData
import io.github.vekat.gamepad.api.SingletonHolder
import io.github.vekat.gamepad.api.Source
import io.github.vekat.gamepad.api.normalize

/**
 * Class description.
 */
class Proximity private constructor(context: Context) : Source<OneAxisData>(context) {
  companion object : SingletonHolder<Proximity, Context>(::Proximity) {
    val DISTANCE: String = "distance"
  }

  override val defaultData: ContextData by lazy {
    ContextData(System.currentTimeMillis(), mapOf(DISTANCE to FloatArray(1)))
  }

  private var latestTimestamp: Long = defaultData.timestamp
  private var latestDistance: Float = 0f

  private val sensorManager by lazy { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
  private val sensor: Sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) }
  private val sensorRange: Float by lazy { sensor.maximumRange }

  private val sensorListener = object : SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent) {
      latestDistance = event.values[0].normalize(sensorRange)
      latestTimestamp = event.timestamp

      channel.broadcast(ContextData(latestTimestamp, mapOf(DISTANCE to floatArrayOf(latestDistance))))
    }
  }

  override fun onStart() {
    sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME)
  }

  override fun onEnd() {
    sensorManager.unregisterListener(sensorListener)
  }

  override fun transform(data: ContextData): OneAxisData {
    val (distance) = data.values[DISTANCE]!!
    val timestamp = data.timestamp
    return OneAxisData(distance, timestamp)
  }
}
