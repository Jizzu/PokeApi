package apps.jizzu.pokeapi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apps.jizzu.pokeapi.R
import apps.jizzu.pokeapi.data.model.PokemonNameUrl
import apps.jizzu.pokeapi.utils.loadImage
import kotterknife.bindView
import java.util.*

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    private var mPokemons = ArrayList<PokemonNameUrl>()
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        mContext = parent.context
        return PokemonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false))
    }

    override fun getItemCount() = mPokemons.size

    fun setPokemons(pokemons: ArrayList<PokemonNameUrl>) {
        mPokemons = pokemons
        notifyDataSetChanged()
    }

    fun getPokemonAtPosition(position: Int) = mPokemons[position]

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = mPokemons[position]
        val number = pokemon.number

        holder.tvName.text = pokemon.name.toUpperCase()
        loadImage(mContext, mContext.getString(R.string.imageURL, number), holder.ivPhoto)

        holder.itemView.setOnClickListener {
            onAdapterClickListener?.onItemClick(position, holder.ivPhoto)
        }
    }

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView by bindView(R.id.ivImage)
        val tvName: TextView by bindView(R.id.tvName)
    }

    fun setOnItemClickListener(onAdapterClickListener: OnAdapterClickListener) {
        Companion.onAdapterClickListener = onAdapterClickListener
    }

    interface OnAdapterClickListener {
        fun onItemClick(position: Int, imageView: ImageView)
    }

    companion object {
        private var onAdapterClickListener: OnAdapterClickListener? = null
    }
}