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
 * An sensor sensor data source.
 */
class Accelerometer private constructor(context: Context) : Source<ThreeAxisData>(context) {
  companion object : SingletonHolder<Accelerometer, Context>(::Accelerometer) {
    val AXIS: String = "axis"
  }

  override val defaultData: ContextData by lazy {
    ContextData(
      System.currentTimeMillis(),
      mapOf(AXIS to FloatArray(3))
    )
  }

  private var latestTimestamp: Long = defaultData.timestamp
  private var latestX: Float = 0f
  private var latestY: Float = 0f
  private var latestZ: Float = 0f

  private val sensorManager by lazy { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
  private val sensor: Sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
  private val sensorRange: Float by lazy { sensor.maximumRange }

  private val sensorListener = object : SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent) {
      latestX = event.values[0].normalize(sensorRange, true)
      latestY = event.values[1].normalize(sensorRange, true)
      latestZ = event.values[2].normalize(sensorRange, true)
      latestTimestamp = event.timestamp

      channel.broadcast(ContextData(latestTimestamp, mapOf(AXIS to floatArrayOf(latestX, latestY, latestZ))))
    }
  }

  override fun onStart() {
    sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME)
  }

  override fun onEnd() {
    sensorManager.unregisterListener(sensorListener)
  }

  override fun transform(data: ContextData): ThreeAxisData {
    val (x, y, z) = data.values[AXIS]!!
    val timestamp = data.timestamp
    return ThreeAxisData(x, y, z, timestamp)
  }
}