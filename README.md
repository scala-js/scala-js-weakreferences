# scalajs-weakreferences

`scalajs-weakreferences` provides a correct implementation of `java.lang.ref.WeakReference` and `java.lang.ref.ReferenceQueue` in Scala.js.

It assumes that the target platform supports JavaScript's `WeakRef` and `FinalizationRegistry`, which are becoming standard in ECMAScript 2021.
Attempts to use `WeakReference` or `ReferenceQueue` will throw `ReferenceError`s if they are not supported.

## License

`scalajs-weakreferences` is distributed under the [Apache 2.0 license](./LICENSE.txt), like Scala.js itself.
