package apps.jizzu.pokeapi.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import apps.jizzu.pokeapi.data.api.PokeApiService
import apps.jizzu.pokeapi.data.model.PokemonNameUrl
import apps.jizzu.pokeapi.utils.*
import apps.jizzu.pokeapi.utils.POKEMONS_LIST_SIZE
import apps.jizzu.pokeapi.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PokemonsListViewModel(private val mServiceApi: PokeApiService) : ViewModel() {
    private var mData = arrayListOf<PokemonNameUrl>()
    val mLiveData = MutableLiveData<ViewStatePokemonsList>()
    val mLiveDataSingleEvent = SingleLiveEvent<SingleEvent>()

    init {
        getPokemonsList()
    }

    fun getPokemonsList(offset: Int = 0, isNeedToReinit: Boolean = false) {
        // TODO replace this with Repository method call
        mServiceApi.getPokemonsList(POKEMONS_LIST_SIZE, offset)
                .subscribeOn(Schedulers.io())
                .map { it.results }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (isNeedToReinit) {
                                mData = it
                                mLiveData.value = ViewStatePokemonsListSuccess(mData)
                            } else {
                                mData.addAll(it)
                                mLiveData.value = ViewStatePokemonsListSuccess(mData)
                            }
                        },
                        onError = {
                            if (mData.isEmpty() || isNeedToReinit) {
                                mLiveData.value = ViewStatePokemonsListError
                            } else {
                                mLiveDataSingleEvent.value = SingleEventToastLoadingError
                            }
                        }
                )
    }

    fun clearData() = mData.clear()

    fun sortData(key: Int) {
        if (mData.isNotEmpty()) {
            when (key) {
                SORT_AZ -> mData.sortWith(compareBy { it.name })
                SORT_ZA -> mData.sortWith(compareByDescending { it.name })
            }
            mLiveData.value = ViewStatePokemonsListSort(mData)
            mLiveDataSingleEvent.value = SingleEventToastSortSuccess(key)
        } else {
            mLiveDataSingleEvent.value = SingleEventToastSortError
        }
    }
}