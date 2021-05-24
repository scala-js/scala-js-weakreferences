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

class WeakReference[T](referent: T, q: ReferenceQueue[_ >: T])
    extends Reference[T](referent, q) {

  def this(referent: T) = this(referent, null)
}
