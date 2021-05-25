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

package java.lang.ref

import scala.scalajs.js

class ReferenceQueue[T] {
  /** The "enqueued" References.
   *
   *  Despite the name, this is used more as a stack (LIFO) than as a queue
   *  (FIFO). The JavaDoc of `ReferenceQueue` does not actually prescribe FIFO
   *  ordering, and experimentation shows that the JVM implementation does not
   *  guarantee that ordering.
   */
  private[this] val enqueuedRefs = js.Array[Reference[_ <: T]]()

  private[this] val finalizationRegistry = {
    new js.FinalizationRegistry[T, Reference[_ <: T], Reference[_ <: T]]({
      (ref: Reference[_ <: T]) => enqueue(ref)
    })
  }

  private[ref] def register(ref: Reference[_ <: T], referent: T): Unit =
    finalizationRegistry.register(referent, ref, ref)

  private[ref] def unregister(ref: Reference[_ <: T]): Unit =
    finalizationRegistry.unregister(ref)

  private[ref] def enqueue(ref: Reference[_ <: T]): Boolean = {
    if (ref.enqueued) {
      false
    } else {
      ref.enqueued = true
      enqueuedRefs.push(ref)
      true
    }
  }

  def poll(): Reference[_ <: T] = {
    if (enqueuedRefs.length == 0)
      null
    else
      enqueuedRefs.pop()
  }

  // Not implemented because they have a blocking contract:
  //def remove(timeout: Long): Reference[_ <: T] = ???
  //def remove(): Reference[_ <: T] = ???
}
