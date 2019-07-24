package apps.jizzu.pokeapi.ui.vm

import apps.jizzu.pokeapi.data.model.PokemonNameUrl

sealed class ViewStatePokemonsList

class ViewStatePokemonsListSuccess(val pokemons: ArrayList<PokemonNameUrl>) : ViewStatePokemonsList()

class ViewStatePokemonsListSort(val pokemons: ArrayList<PokemonNameUrl>) : ViewStatePokemonsList()

object ViewStatePokemonsListError : ViewStatePokemonsList()
