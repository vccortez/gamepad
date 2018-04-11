package io.github.vekat.gamepad.implementations

/**
 * A one dimensional sensor data class.
 */
data class OneAxisData(val value: Float, val timestamp: Long)

/**
 * A three dimensional sensor data class.
 */
data class ThreeAxisData(val x: Float, val y: Float, val z: Float, val timestamp: Long)

/**
 * A three dimensional sensor data class representing the device's orientation.
 *
 * @param azimuth angle of rotation about the -z axis. This value represents the angle between the device's y axis and the magnetic north pole. When facing north, this angle is 0, when facing south, this angle is π. Likewise, when facing east, this angle is π/2, and when facing west, this angle is -π/2. The range of values is -π to π.
 * @param pitch angle of rotation about the x axis. This value represents the angle between a plane parallel to the device's screen and a plane parallel to the ground. Assuming that the bottom edge of the device faces the user and that the screen is face-up, tilting the top edge of the device toward the ground creates a positive pitch angle. The range of values is -π to π.
 * @param roll angle of rotation about the y axis. This value represents the angle between a plane perpendicular to the device's screen and a plane perpendicular to the ground. Assuming that the bottom edge of the device faces the user and that the screen is face-up, tilting the left edge of the device toward the ground creates a positive roll angle. The range of values is -π/2 to π/2.
 * @param timestamp timestamp of this event.
 */
data class EulerAngles(val azimuth: Float, val pitch: Float, val roll: Float, val timestamp: Long)