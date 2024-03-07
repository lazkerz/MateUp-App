package com.example.apps_magang.dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.example.apps_magang.core.view.ProductView
import com.example.apps_magang.dashboard.adapter.BlushAdapter
import com.example.apps_magang.dashboard.adapter.EyeshadowAdapter
import com.example.apps_magang.dashboard.adapter.FoundationAdapter
import com.example.apps_magang.dashboard.adapter.LipstickAdapter
import com.example.apps_magang.dashboard.presentation.presenter.PersonalizedPresenter
import com.example.mateup.data.remote.ApiConfig
import com.example.mateup.data.remote.ApiServicePersonalized
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(requireContext())
        RealmManager.initRealm()

        // Inisialisasi adapter dan set ke RecyclerView
        eyeshadowAdapter = EyeshadowAdapter(requireContext())
        lipstickAdapter = LipstickAdapter(requireContext())
        foundationAdapter = FoundationAdapter(requireContext())
        blushAdapter = BlushAdapter(requireContext())

        val apiServicePersonalized =
            ApiConfig.getApiService(requireContext(), "productBy") as? ApiServicePersonalized
        Log.d("ApiServicePersonalized", "ApiServicePersonalized is not null: $apiServicePersonalized")

        if (apiServicePersonalized != null) {
            presenter = PersonalizedPresenter(apiServicePersonalized, this)
            presenterUser = UserPresenter(this)
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
                        presenter.getProductPersonalized("Natural",  "eyeshadow")
                        presenter.getProductPersonalized("Natural",   "lipstick")
                        presenter.getProductPersonalized("Natural",   "blush")
                    }
                    "Oily" -> {
                        presenter.getProductPersonalized("Oil free",   "foundation")
                        presenter.getProductPersonalized("Vegan",   "eyeshadow")
                        presenter.getProductPersonalized("Natural",  "lipstick")
                        presenter.getProductPersonalized("Organic",   "blush")
                    }
                    "Dry" -> {
                        presenter.getProductPersonalized("Cruelty Free",  "foundation")
                        presenter.getProductPersonalized("EWG Verified",   "eyeshadow")
                        presenter.getProductPersonalized("Certclean",   "lipstick")
                        presenter.getProductPersonalized("Purpicks",  "blush")
                    }
                    else -> Log.e("Presenter", "Unexpected skinType: ${data.skinType}")
                }
            } else{
                Log.e("Dashboard", "user model null")
            }
        } else {
            Log.e("Dashboard", "Failed to initialize ApiServicePersonalized")
            Toast.makeText(
                requireContext(),
                "Failed to initialize ApiServicePersonalized",
                Toast.LENGTH_SHORT
            ).show()
        }

        presenter.getProductFromRealm()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val viewPager = view.findViewById<ViewPager2>(R.id.vp_carousel)
        rvEyeshadow = view.findViewById(R.id.rv_eyeshadow)
        rvLipstick = view.findViewById(R.id.rv_lipstick)
        rvFoundation = view.findViewById(R.id.rv_foundation)
        rvBlush = view.findViewById(R.id.rv_mascara)

        initRecyclerView(eyeshadowAdapter, rvEyeshadow)
        initRecyclerView(lipstickAdapter, rvLipstick)
        initRecyclerView(foundationAdapter, rvFoundation)
        initRecyclerView(blushAdapter, rvBlush)

        return view
    }

    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun displayProduct(result: ResultState<List<Product>>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val productData = result.data
                for (product in productData) {
                    when (product.productType) {
                        "eyeshadow" -> eyeshadowAdapter.addData(product)
                        "foundation" -> foundationAdapter.addData(product)
                        "lipstick" -> lipstickAdapter.addData(product)
                        "blush" -> blushAdapter.addData(product)
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



    override fun displayUser(result: ResultState<UserModel>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val userData = result.data
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
