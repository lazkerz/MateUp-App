package com.example.apps_magang.dashboard.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presentation.ViewProfileActivity
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.presentation.DetailActivity
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.example.apps_magang.core.utils.SpacesItemDecoration
import com.example.apps_magang.core.view.ProductView
import com.example.apps_magang.dashboard.adapter.BlushAdapter
import com.example.apps_magang.dashboard.adapter.CarouselAdapter
import com.example.apps_magang.dashboard.adapter.EyeshadowAdapter
import com.example.apps_magang.dashboard.adapter.FoundationAdapter
import com.example.apps_magang.dashboard.adapter.LipstickAdapter
import com.example.apps_magang.dashboard.presentation.presenter.PersonalizedPresenter
import com.example.apps_magang.dashboard.presenter.slider
import com.example.mateup.data.remote.ApiConfig
import com.example.mateup.data.remote.ApiServicePersonalized
import com.google.android.material.slider.Slider
import io.realm.Realm

class DashboardFragment : Fragment(), ProductView, user_view {

    private lateinit var presenter: PersonalizedPresenter
    private lateinit var presenterUser: UserPresenter

    private lateinit var eyeshadowAdapter: EyeshadowAdapter
    private lateinit var foundationAdapter: FoundationAdapter
    private lateinit var lipstickAdapter: LipstickAdapter
    private lateinit var blushAdapter: BlushAdapter

    private lateinit var rvEyeshadow: RecyclerView
    private lateinit var rvLipstick: RecyclerView
    private lateinit var rvFoundation: RecyclerView
    private lateinit var rvBlush: RecyclerView
    private lateinit var indicatorsContainer: LinearLayout

    private lateinit var user: TextView
    private lateinit var skinType: TextView

    private val carouselAdapter = CarouselAdapter(
        listOf(
            slider(R.drawable.image_carousel1),
            slider(R.drawable.image_carousel2),
            slider(R.drawable.image_carousel3)
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(requireContext())
        RealmManager.initRealm()

        // Inisialisasi adapter dan set ke RecyclerView
        eyeshadowAdapter = EyeshadowAdapter(requireContext(), object : EyeshadowAdapter.OnItemClickListener {
            override fun onItemClick(data: Product) {
                val productId = data.id ?: ""
                Log.d("MainActivity", "Product ID clicked: $productId")

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        })

        foundationAdapter = FoundationAdapter (requireContext(), object : FoundationAdapter.OnItemClickListener {
            override fun onItemClick(data: Product) {
                val productId = data.id ?: ""
                Log.d("MainActivity", "Product ID clicked: $productId")

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        })

        lipstickAdapter = LipstickAdapter (requireContext(), object : LipstickAdapter.OnItemClickListener {
            override fun onItemClick(data: Product) {
                val productId = data.id ?: ""
                Log.d("MainActivity", "Product ID clicked: $productId")

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        })

        blushAdapter = BlushAdapter (requireContext(), object : BlushAdapter.OnItemClickListener {
            override fun onItemClick(data: Product) {
                val productId = data.id ?: ""
                Log.d("MainActivity", "Product ID clicked: $productId")

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        })


        val apiServicePersonalized =
            ApiConfig.getApiService(requireContext(), "productBy") as? ApiServicePersonalized
        Log.d("ApiServicePersonalized", "ApiServicePersonalized is not null: $apiServicePersonalized")

        if (apiServicePersonalized != null) {
            presenter = PersonalizedPresenter(apiServicePersonalized, this)
            presenterUser = UserPresenter(requireContext(),this)
        } else {
            Log.e("Dashboard", "Failed to initialize ApiServicePersonalized")
            Toast.makeText(
                requireContext(),
                "Failed to initialize ApiServicePersonalized",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        rvEyeshadow = view.findViewById(R.id.rv_eyeshadow)
        rvLipstick = view.findViewById(R.id.rv_lipstick)
        rvFoundation = view.findViewById(R.id.rv_foundation)
        rvBlush = view.findViewById(R.id.rv_blush)
        val skinType = view.findViewById<TextView>(R.id.tv_skin_type)

        val user = view.findViewById<TextView>(R.id.user)

        val viewPager = view.findViewById<ViewPager2>(R.id.vp_carousel)
        indicatorsContainer = view.findViewById(R.id.indicatorsContainer)

        // Set adapter to ViewPager
        viewPager.adapter = carouselAdapter

        // Set up indicators
        setupIndicators(indicatorsContainer)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val currentPosition = viewPager.currentItem
                val newPosition = if (currentPosition == carouselAdapter.itemCount - 1) 0 else currentPosition + 1
                viewPager.setCurrentItem(newPosition, true)
                handler.postDelayed(this, 5000) // 5000 milliseconds = 5 seconds
            }
        }
        handler.postDelayed(runnable, 5000)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == carouselAdapter.itemCount - 1) {
                    // Jika sampai ke page terakhir, kembali ke page pertama setelah sedikit jeda
                    Handler().postDelayed({
                        viewPager.setCurrentItem(0, false) // Set animasi menjadi false
                    }, 2000) // Ganti 2000 dengan jeda yang diinginkan sebelum kembali ke page pertama (dalam milidetik)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    // Jika pengguna mencoba untuk menggeser halaman dari terakhir, langsung kembali ke halaman pertama
                    val currentItem = viewPager.currentItem
                    val lastItem = carouselAdapter.itemCount - 1
                    if (currentItem == lastItem) {
                        viewPager.setCurrentItem(0, false)
                    }
                }
            }
        })


        val profile = view.findViewById<ImageView>(R.id.profile)
        profile.setOnClickListener {
            val intent = Intent(requireContext(), ViewProfileActivity::class.java)
            startActivity(intent)
        }

        initRecyclerView(eyeshadowAdapter, rvEyeshadow)
        initRecyclerView(lipstickAdapter, rvLipstick)
        initRecyclerView(foundationAdapter, rvFoundation)
        initRecyclerView(blushAdapter, rvBlush)

        val userModel = presenterUser.getUser()
        Log.d("Profile", "Data User: ${userModel?.username}")
        Toast.makeText(context, "user: ${userModel?.username}", Toast.LENGTH_SHORT).show()

        if (userModel != null) {
            skinType.text = userModel.skinType
            user.text = userModel.username
        }

        getContent()

        val refresh =  view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        refresh.setOnRefreshListener{
            getContent()
            if (refresh.isRefreshing) {
                refresh.isRefreshing = false
            }
        }
        return view
    }

    private fun setupIndicators(indicatorsContainer: LinearLayout) {
        val indicators = arrayOfNulls<ImageView>(carouselAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.unselected_dot
                    )
                )
                this.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.selected_dot
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.unselected_dot
                    )
                )
            }
        }
    }

    private fun getContent() {
        // Panggil fungsi presenter untuk mendapatkan data dari API
        // Fungsi ini hanya dipanggil saat pembukaan pertama kali
        presenterUser = UserPresenter(requireContext(), this)

        val data = presenterUser.getUser()
        if (data != null){
            when (data.skinType) {
                "Sensitive" -> {
                    presenter.getProductPersonalized("Canadian",  "foundation")
                    presenter.getProductPersonalized("Canadian",   "eyeshadow")
                    presenter.getProductPersonalized("Gluten free",   "lipstick")
                    presenter.getProductPersonalized("Canadian",   "blush")
                }
                "Normal" -> {
                    presenter.getProductPersonalized("Natural",   "foundation")
                    presenter.getProductPersonalized("Vegan",  "eyeshadow")
                    presenter.getProductPersonalized("Natural",   "lipstick")
                    presenter.getProductPersonalized("Natural",   "blush")
                }
                "Oily" -> {
                    presenter.getProductPersonalized("Oil free",   "foundation")
                    presenter.getProductPersonalized("Natural",   "eyeshadow")
                    presenter.getProductPersonalized("Natural",  "lipstick")
                    presenter.getProductPersonalized("Natural",   "blush")
                }
                "Dry" -> {
                    presenter.getProductPersonalized("Canadian",  "foundation")
                    presenter.getProductPersonalized("Gluten Free",   "eyeshadow")
                    presenter.getProductPersonalized("Canadian",   "lipstick")
                    presenter.getProductPersonalized("Gluten Free",  "blush")
                }
                else -> Log.e("Presenter", "Unexpected skinType: ${data.skinType}")
            }
        } else {
            presenter.getProductFromRealm()
        }
    }

    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpacesItemDecoration(6))
    }

    private fun setLoading(isLoading: Boolean) {
        val viewLoading: RelativeLayout? = view?.findViewById(R.id.view_loading)
        val eyeshadow = view?.findViewById<TextView>(R.id.tv_eyeshadow)
        val foundation = view?.findViewById<TextView>(R.id.tv_foundation)
        val lipstick = view?.findViewById<TextView>(R.id.tv_lipstick)
        val blush = view?.findViewById<TextView>(R.id.tv_mascara)

        viewLoading?.visibility = if (isLoading) View.VISIBLE else View.GONE
        rvEyeshadow.visibility = if (isLoading) View.GONE else View.VISIBLE
        rvFoundation.visibility = if (isLoading) View.GONE else View.VISIBLE
        rvLipstick.visibility = if (isLoading) View.GONE else View.VISIBLE
        rvBlush.visibility = if (isLoading) View.GONE else View.VISIBLE

        // Menyembunyikan TextView saat isLoading true
        eyeshadow?.visibility = if (isLoading) View.GONE else View.VISIBLE
        foundation?.visibility = if (isLoading) View.GONE else View.VISIBLE
        lipstick?.visibility = if (isLoading) View.GONE else View.VISIBLE
        blush?.visibility = if (isLoading) View.GONE else View.VISIBLE
    }



    override fun displayProduct(result: ResultState<List<Product>>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val productData = result.data
                for (product in productData) {
                    when (product.productType) {
                        "eyeshadow" -> {
                            eyeshadowAdapter.addData(product)
                            setLoading(false)
                        }
                        "foundation" -> {
                            foundationAdapter.addData(product)
                            setLoading(false)
                        }
                        "lipstick" -> {
                            lipstickAdapter.addData(product)
                            setLoading(false)
                        }
                        "blush" -> {
                            blushAdapter.addData(product)
                            setLoading(false)
                        }
                    }
                }
            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUIWithUserData(userData: UserModel?) {
        if (userData != null) {
            skinType?.text = userData.skinType
            user?.text = userData.username
        } else {
            // Penanganan jika data pengguna null atau tidak valid
            // Misalnya, set nilai teks ke default atau tampilkan pesan kesalahan
            skinType.text = "Default Skin Type"
            user.text = "Default User"
            Toast.makeText(requireContext(), "User data is null", Toast.LENGTH_SHORT).show()
        }
    }
    override fun displayUser(result: ResultState<UserModel>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val userData = result.data
                updateUIWithUserData(userData)
            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
