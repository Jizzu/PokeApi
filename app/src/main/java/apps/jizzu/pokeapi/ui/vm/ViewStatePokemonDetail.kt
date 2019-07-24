package apps.jizzu.pokeapi.ui.vm

import apps.jizzu.pokeapi.data.model.Pokemon

sealed class ViewStatePokemonDetail

class ViewStatePokemonDetailSuccess(val pokemon: Pokemon) : ViewStatePokemonDetail()