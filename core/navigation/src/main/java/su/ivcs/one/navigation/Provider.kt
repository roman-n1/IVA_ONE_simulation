package su.ivcs.one.navigation

// Интерфейс провайдера (аналог Provider<T> из javax.inject)
interface Provider<T> {
    fun get(): T
}