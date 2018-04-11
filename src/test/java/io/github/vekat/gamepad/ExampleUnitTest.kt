package io.github.vekat.gamepad

import android.content.Context
import android.test.mock.MockContext
import android.view.View
import io.github.vekat.gamepad.api.Gamepad
import io.github.vekat.gamepad.api.ViewHolder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
  internal lateinit var context: Context

  @Before
  fun setup() {
    context = MockContext()
  }

  @Test
  @Throws(Exception::class)
  fun gamepad_isNotNull() {
    val mockView = object : View(context), ViewHolder { override val instance: View = this }

    val gamepad = Gamepad(mockView)

    assertNotNull(gamepad)

    assertEquals(1, gamepad.latestCode)
  }
}