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
  import ReferenceQueue._

  private[this] var first: Node[T] = null
  private[this] var last: Node[T] = null

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
      val newNode = new Node[T](ref, null)
      if (last == null)
        first = newNode
      else
        last.next = newNode
      last = newNode
      true
    }
  }

  def poll(): Reference[_ <: T] = {
    val result = first
    if (result != null) {
      first = result.next
      result.ref
    } else {
      null
    }
  }

  // Not implemented because they have a blocking contract:
  //def remove(timeout: Long): Reference[_ <: T] = ???
  //def remove(): Reference[_ <: T] = ???
}

private object ReferenceQueue {
  private final class Node[T](val ref: Reference[_ <: T], var next: Node[T])
}
