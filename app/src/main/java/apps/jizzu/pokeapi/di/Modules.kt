package apps.jizzu.pokeapi.di

import androidx.recyclerview.widget.GridLayoutManager
import apps.jizzu.pokeapi.data.api.PokeApiService
import apps.jizzu.pokeapi.ui.adapter.PokemonAdapter
import apps.jizzu.pokeapi.ui.vm.PokemonDetailViewModel
import apps.jizzu.pokeapi.ui.vm.PokemonsListViewModel
import apps.jizzu.pokeapi.utils.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { PokemonAdapter() }
    factory { GridLayoutManager(androidContext(), GRID_LAYOUT_SPAN_COUNT) }
    viewModel { PokemonsListViewModel(get()) }
    viewModel { (id : Int) -> PokemonDetailViewModel(get(), id) }
}

val networkModule = module {
    factory { provideCryptocurrencyApi(get()) }
    single { provideRetrofit(get(), get(), get()) }
    factory { provideOkHttpClient(get()) }
    factory { provideHttpLoggingInterceptor() }
    factory { GsonConverterFactory.create() }
    factory { RxJava2CallAdapterFactory.create() }
}

fun provideCryptocurrencyApi(retrofit: Retrofit): PokeApiService = retrofit.create(PokeApiService::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient,
                    gsonConverterFactory: GsonConverterFactory,
                    rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): Retrofit {
    return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()
}

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient()
            .newBuilder()
            .protocols(listOf(Protocol.HTTP_1_1))
            .addInterceptor(httpLoggingInterceptor)
            .build()
}

fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}