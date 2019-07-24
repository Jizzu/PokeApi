package apps.jizzu.pokeapi.data.api

import apps.jizzu.pokeapi.data.model.Pokemon
import apps.jizzu.pokeapi.data.model.PokemonResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    fun getPokemonsList(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetails(@Path("id") id: Int): Single<Pokemon>
}