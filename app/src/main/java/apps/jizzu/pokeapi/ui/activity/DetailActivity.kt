package apps.jizzu.pokeapi.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import apps.jizzu.pokeapi.R
import apps.jizzu.pokeapi.data.model.Pokemon
import apps.jizzu.pokeapi.ui.activity.base.BaseActivity
import apps.jizzu.pokeapi.ui.vm.*
import apps.jizzu.pokeapi.utils.loadImage
import apps.jizzu.pokeapi.utils.toast
import kotterknife.bindView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : BaseActivity() {
    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mImageViewPokemon: ImageView by bindView(R.id.ivImage)
    private val mTextViewPokemonType: TextView by bindView(R.id.tvTypeValue)
    private val mTextViewPokemonId: TextView by bindView(R.id.tvIdValue)
    private val mTextViewPokemonWeight: TextView by bindView(R.id.tvWeightValue)
    private val mTextViewPokemonHeight: TextView by bindView(R.id.tvHeightValue)
    private val mTextViewPokemonSpeed: TextView by bindView(R.id.tvSpeedValue)
    private val mTextViewPokemonAbilities: TextView by bindView(R.id.tvAbilitiesValue)
    private val mTextViewPokemonSpecialDefence: TextView by bindView(R.id.tvSpecialDefenceValue)
    private val mTextViewPokemonSpecialAttack: TextView by bindView(R.id.tvSpecialAttackValue)
    private val mTextViewPokemonDefence: TextView by bindView(R.id.tvDefenceValue)
    private val mTextViewPokemonAttack: TextView by bindView(R.id.tvAttackValue)
    private val mTextViewPokemonHealth: TextView by bindView(R.id.tvHealthValue)

    private var mNumber = 0
    private val mViewModel: PokemonDetailViewModel by viewModel { parametersOf(mNumber) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initToolbar(mToolbar, true)
        getPokemonDetails()
        initObservers()
    }

    private fun getPokemonDetails() {
        mNumber = intent.getIntExtra("number", 0)
        title = intent.getStringExtra("name").capitalize()

        loadImage(this, getString(R.string.imageURL, mNumber), mImageViewPokemon)
    }

    private fun initObservers() {
        mViewModel.apply {
            mLiveData.observe(this@DetailActivity, Observer<ViewStatePokemonDetail> {
                state -> updateViewState(state)
            })
            mLiveDataSingleEvent.observe(this@DetailActivity, Observer<SingleEvent> {
                event -> showEvent(event)
            })
        }
    }

    private fun updateViewState(state: ViewStatePokemonDetail) {
        when (state) {
            is ViewStatePokemonDetailSuccess -> showPokemonDetails(state.pokemon)
        }
    }

    private fun showEvent(event: SingleEvent) {
        when (event) {
            is SingleEventToastLoadingError -> toast(getString(R.string.toast_error_loading_data))
        }
    }

    private fun showPokemonDetails(pokemon: Pokemon) {
        var pokemonTypes = ""
        for (i in 0 until pokemon.types.size) {
            pokemonTypes += "${pokemon.types[i].type.name}, "
        }
        mTextViewPokemonType.text = pokemonTypes.dropLast(2)
        mTextViewPokemonId.text = pokemon.id.toString()
        mTextViewPokemonWeight.text = pokemon.weight.toString()
        mTextViewPokemonHeight.text = pokemon.height.toString()

        mTextViewPokemonSpeed.text = pokemon.stats[0].base_stat.toString()
        mTextViewPokemonSpecialDefence.text = pokemon.stats[1].base_stat.toString()
        mTextViewPokemonSpecialAttack.text = pokemon.stats[2].base_stat.toString()
        mTextViewPokemonDefence.text = pokemon.stats[3].base_stat.toString()
        mTextViewPokemonAttack.text = pokemon.stats[4].base_stat.toString()
        mTextViewPokemonHealth.text = pokemon.stats[5].base_stat.toString()

        var pokemonAbilities = ""
        for (i in 0 until pokemon.abilities.size) {
            pokemonAbilities += if (i < 1) {
                "${pokemon.abilities[i].ability.name}, "
            } else {
                "\n${pokemon.abilities[i].ability.name}, "
            }
        }
        mTextViewPokemonAbilities.text = pokemonAbilities.dropLast(2)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}
