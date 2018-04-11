package io.github.vekat.gamepad.implementations

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.github.vekat.gamepad.api.ContextData
import io.github.vekat.gamepad.api.SingletonHolder
import io.github.vekat.gamepad.api.Source

class Orientation private constructor(context: Context) : Source<EulerAngles>(context) {
  companion object : SingletonHolder<Orientation, Context>(::Orientation) {
    val ANGLES: String = "angles"
  }

  override val defaultData: ContextData by lazy {
    ContextData(
      System.currentTimeMillis(),
      mapOf(ANGLES to FloatArray(3))
    )
  }

  private var latestTimestamp: Long = defaultData.timestamp
  private var latestGravity: FloatArray? = null
  private var latestGeomagnetic: FloatArray? = null

  private val sensorManager by lazy { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
  private val gravity: Sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
  private val geomagnetic: Sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) }

  private val sensorListener = object : SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent) {
      latestTimestamp = event.timestamp

      when (event.sensor.type) {
        Sensor.TYPE_ACCELEROMETER -> latestGravity = event.values
        Sensor.TYPE_MAGNETIC_FIELD -> latestGeomagnetic = event.values
      }

      if (latestGravity != null && latestGeomagnetic != null) {
        val identityMatrix = FloatArray(9)
        val rotationMatrix = FloatArray(9)

        if (SensorManager.getRotationMatrix(rotationMatrix, identityMatrix, latestGravity, latestGeomagnetic)) {
          val orientation = FloatArray(3)

          SensorManager.getOrientation(rotationMatrix, orientation)

          channel.broadcast(ContextData(latestTimestamp, mapOf(ANGLES to orientation)))
        }
      }
    }
  }

  override fun onStart() {
    sensorManager.registerListener(sensorListener, gravity, SensorManager.SENSOR_DELAY_GAME)
    sensorManager.registerListener(sensorListener, geomagnetic, SensorManager.SENSOR_DELAY_GAME)
  }

  override fun onEnd() {
    sensorManager.unregisterListener(sensorListener)
  }

  override fun transform(data: ContextData): EulerAngles {
    val (azimuth, pitch, roll) = data.values[ANGLES]!!
    val timestamp = data.timestamp
    return EulerAngles(azimuth, pitch, roll, timestamp)
  }
}