package apps.jizzu.pokeapi.ui.vm

sealed class SingleEvent

object SingleEventToastLoadingError : SingleEvent()

class SingleEventToastSortSuccess(val key: Int) : SingleEvent()

object SingleEventToastSortError : SingleEvent()