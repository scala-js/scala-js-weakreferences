/*
 * scalajs-weakreferences (https://github.com/scala-js/scala-js-weakreferences)
 *
 * Copyright EPFL.
 *
 * Licensed under Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package org.scalajs.testsuite.javalib.lang.ref

import java.lang.ref._
import java.lang.StringBuilder

import org.junit.Test
import org.junit.Assert._

class WeakReferenceTest {
  @Test def allOperationsWithoutQueue(): Unit = {
    val obj = new StringBuilder("foo")
    val ref = new WeakReference(obj)
    assertSame(obj, ref.get())
    assertEquals(false, ref.enqueue())
    assertEquals(false, ref.isEnqueued())
    ref.clear()
    assertNull(ref.get())
    assertEquals(false, ref.isEnqueued())
  }

  @Test def explicitEnqueue(): Unit = {
    val obj = new StringBuilder("foo")
    val queue = new ReferenceQueue[Any]
    val ref = new WeakReference(obj, queue)
    assertSame(obj, ref.get())
    assertNull(queue.poll())
    assertFalse(ref.isEnqueued())
    assertTrue(ref.enqueue())
    assertTrue(ref.isEnqueued())
    assertSame(ref, queue.poll())
    assertNull(queue.poll())
    assertSame(obj, ref.get())
    assertFalse(ref.enqueue())
  }

  @Test def clearDoesNotEnqueue(): Unit = {
    val obj = new StringBuilder("foo")
    val queue = new ReferenceQueue[Any]
    val ref = new WeakReference(obj, queue)
    assertSame(obj, ref.get())
    assertNull(queue.poll())
    assertFalse(ref.isEnqueued())
    ref.clear()
    assertNull(ref.get())
    assertFalse(ref.isEnqueued())
    assertNull(queue.poll())

    // Can still enqueue after that
    assertTrue(ref.enqueue())
    assertTrue(ref.isEnqueued())
    assertSame(ref, queue.poll())
  }

  @Test def twoWeakReferencesOverTheSameObjectInQueue(): Unit = {
    val obj = new StringBuilder("foo")
    val queue = new ReferenceQueue[Any]
    val ref1 = new WeakReference(obj, queue)
    val ref2 = new WeakReference(obj, queue)
    assertSame(obj, ref1.get())
    assertSame(obj, ref2.get())

    ref1.enqueue()
    ref2.enqueue()
    val polled1 = queue.poll()
    val polled2 = queue.poll()
    assertTrue(polled1 == ref1 || polled1 == ref2)
    assertTrue(polled2 == ref1 || polled2 == ref2)
    assertNotSame(polled1, polled2)
    assertNull(queue.poll())
  }
}
