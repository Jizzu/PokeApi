package apps.jizzu.pokeapi.data.model

class PokemonNameUrl(
        number: Int,
        val name: String,
        val url: String
) {
    var number = number
        private set
        get() {
            val splitedUrl = url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return Integer.parseInt(splitedUrl[splitedUrl.size - 1])
        }
}