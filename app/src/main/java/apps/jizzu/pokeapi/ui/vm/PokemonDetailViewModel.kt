package apps.jizzu.pokeapi.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import apps.jizzu.pokeapi.data.api.PokeApiService
import apps.jizzu.pokeapi.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PokemonDetailViewModel(private val mServiceApi: PokeApiService,
                             private val mPokemonId: Int) : ViewModel() {
    val mLiveData = MutableLiveData<ViewStatePokemonDetail>()
    val mLiveDataSingleEvent = SingleLiveEvent<SingleEvent>()

    init {
        getPokemonDetails()
    }

    private fun getPokemonDetails() {
        // TODO replace this with Repository method call
        mServiceApi.getPokemonDetails(mPokemonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { mLiveData.value = ViewStatePokemonDetailSuccess(it) },
                        onError = { mLiveDataSingleEvent.value = SingleEventToastLoadingError }
                )
    }
}