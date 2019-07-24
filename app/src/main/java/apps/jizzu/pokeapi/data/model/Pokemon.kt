package apps.jizzu.pokeapi.data.model

import java.util.ArrayList

class Pokemon(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val types: ArrayList<Type>,
    val abilities: ArrayList<Ability>,
    val stats: ArrayList<Stats>
)