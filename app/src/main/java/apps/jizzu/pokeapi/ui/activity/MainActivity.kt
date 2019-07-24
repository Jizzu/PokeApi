package apps.jizzu.pokeapi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import apps.jizzu.pokeapi.ui.decorator.GridSpacingItemDecoration
import apps.jizzu.pokeapi.R
import apps.jizzu.pokeapi.data.model.PokemonNameUrl
import apps.jizzu.pokeapi.ui.activity.base.BaseActivity
import apps.jizzu.pokeapi.ui.adapter.PokemonAdapter
import apps.jizzu.pokeapi.ui.vm.*
import apps.jizzu.pokeapi.utils.*
import kotterknife.bindView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mRecyclerView: RecyclerView by bindView(R.id.rvPokemonsList)
    private val mSwipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swipeRefresh)
    private val mGroupNoInternet: Group by bindView(R.id.gNoInternet)

    private val mAdapter: PokemonAdapter by inject()
    private val mLayoutManager: GridLayoutManager by inject()
    private val mViewModel: PokemonsListViewModel by viewModel()

    private var mOffset = 0
    private var mLoadNextPokemons = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        initListeners()
        showLoadingAnimation()
        initObservers()
    }

    private fun initUI() {
        initToolbar(mToolbar)

        mSwipeRefreshLayout.apply {
            setOnRefreshListener(this@MainActivity)
            setColorSchemeColors(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
        }

        mRecyclerView.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = mAdapter
            addItemDecoration(GridSpacingItemDecoration(GRID_LAYOUT_SPAN_COUNT, GRID_LAYOUT_ITEM_SIZE))
        }
    }

    private fun initListeners() {
        // TODO replace this with Paging Library
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val visiblePokemonsCount = mLayoutManager.childCount
                    val pastPokemonsCount = mLayoutManager.findFirstVisibleItemPosition()
                    val totalPokemonsCount = mLayoutManager.itemCount

                    if (mLoadNextPokemons && visiblePokemonsCount + pastPokemonsCount >= totalPokemonsCount) {
                        mLoadNextPokemons = false
                        mOffset += 30
                        title = getString(R.string.updating)
                        mViewModel.getPokemonsList(mOffset)
                    }
                }
            }
        })

        mAdapter.setOnItemClickListener(object : PokemonAdapter.OnAdapterClickListener {
            override fun onItemClick(position: Int, imageView: ImageView) =
                    showDetailActivity(mAdapter.getPokemonAtPosition(position), imageView)
        })
    }

    private fun initObservers() {
        mViewModel.apply {
            mLiveData.observe(this@MainActivity, Observer<ViewStatePokemonsList> {
                state -> updateViewState(state)
            })
            mLiveDataSingleEvent.observe(this@MainActivity, Observer<SingleEvent> {
                event -> showEvent(event)
            })
        }
    }

    private fun updateViewState(state: ViewStatePokemonsList) {
        mLoadNextPokemons = true

        when (state) {
            is ViewStatePokemonsListSuccess -> showPokemons(state.pokemons)
            is ViewStatePokemonsListSort -> sortPokemons(state.pokemons)
            is ViewStatePokemonsListError -> showLoadingError()
        }
    }

    private fun showEvent(event: SingleEvent) {
        mLoadNextPokemons = true

        when (event) {
            is SingleEventToastSortSuccess -> {
                when(event.key) {
                    SORT_AZ -> toast(getString(R.string.toast_sort_a_z))
                    SORT_ZA -> toast(getString(R.string.toast_sort_z_a))
                }
            }
            is SingleEventToastSortError -> {
                hideLoadingAnimation()
                toast(getString(R.string.toast_sort_no_data))
            }
            is SingleEventToastLoadingError -> {
                hideLoadingAnimation()
                toast(getString(R.string.toast_error_loading_data))
            }
        }
    }

    private fun showPokemons(pokemons: ArrayList<PokemonNameUrl>) {
        hideLoadingError()
        hideLoadingAnimation()
        mAdapter.setPokemons(pokemons)
    }

    private fun sortPokemons(pokemons: ArrayList<PokemonNameUrl>) {
        hideLoadingAnimation()
        mAdapter.setPokemons(pokemons)
        mLayoutManager.scrollToPosition(0)
    }

    private fun showDetailActivity(pokemon: PokemonNameUrl, imageView: ImageView) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("name", pokemon.name)
            putExtra("number", pokemon.number)
        }
        val options = makeSceneTransitionAnimation(this, imageView, "animation")
        startActivity(intent, options.toBundle())
    }

    private fun showLoadingAnimation() {
        mSwipeRefreshLayout.isRefreshing = true
        title = getString(R.string.updating)
    }

    private fun hideLoadingAnimation() {
        mSwipeRefreshLayout.isRefreshing = false
        title = getString(R.string.app_name)
    }

    private fun showLoadingError() {
        hideLoadingAnimation()
        mViewModel.clearData()
        mGroupNoInternet.visible()
    }

    private fun hideLoadingError() = mGroupNoInternet.gone()

    override fun onRefresh() {
        mOffset = rand(0, 500)
        mViewModel.getPokemonsList(mOffset, true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSortAZ -> {
                mViewModel.sortData(SORT_AZ)
            }

            R.id.actionSortZA -> {
                mViewModel.sortData(SORT_ZA)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private companion object {
        private const val GRID_LAYOUT_ITEM_SIZE = 350
    }
}
