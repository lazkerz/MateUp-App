package com.example.apps_magang.dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.core.domain.Product
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.example.apps_magang.core.view.ProductView
import com.example.apps_magang.dashboard.adapter.EyeshadowAdapter
import com.example.apps_magang.dashboard.presentation.presenter.PersonalizedPresenter
import com.example.mateup.data.remote.ApiConfig
import com.example.mateup.data.remote.ApiServicePersonalized
import io.realm.Realm

class DashboardFragment : Fragment(), ProductView {

    private lateinit var presenter: PersonalizedPresenter
    private lateinit var eyeshadowAdapter: EyeshadowAdapter

    // Inisialisasi view dapat dilakukan di dalam onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Inisialisasi view di sini
        val viewPager = view.findViewById<ViewPager2>(R.id.vp_carousel)
        val rv = view.findViewById<RecyclerView>(R.id.rv_eyeshadow)
        val rv2 = view.findViewById<RecyclerView>(R.id.rv_lipstick)
        val rv3 = view.findViewById<RecyclerView>(R.id.rv_foundation)
        val rv4 = view.findViewById<RecyclerView>(R.id.rv_mascara)

        // Inisialisasi adapter dan set ke RecyclerView
        eyeshadowAdapter = EyeshadowAdapter(requireContext())
        rv.adapter = eyeshadowAdapter

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(requireContext())
        RealmManager.initRealm()

        val apiServicePersonalized =
            ApiConfig.getApiService(requireContext(), "productBy") as? ApiServicePersonalized

        if (apiServicePersonalized != null) {
            presenter = PersonalizedPresenter(apiServicePersonalized, this)
            val userModel = UserModel()

            when (userModel.skinType) {
                "Sensitive" -> {
                    presenter.getProductPersonalized("alcohol free", "Canadian", "liquid", "foundation")
                    presenter.getProductPersonalized("alcohol free", "Canadian", "pallete", "eyeshadow")
                    presenter.getProductPersonalized("alcohol free", "Canadian", "lipstick", "lipstick")
                    presenter.getProductPersonalized("alcohol free", "Canadian", "powder", "blush")
                }
                "Normal" -> {
                    presenter.getProductPersonalized("Normal", "Canadian", "liquid", "foundation")
                    presenter.getProductPersonalized("Normal", "Canadian", "pallete", "eyeshadow")
                    presenter.getProductPersonalized("Normal", "Canadian", "lipstick", "lipstick")
                    presenter.getProductPersonalized("Normal", "Canadian", "powder", "blush")
                }
                "Oily" -> {
                    presenter.getProductPersonalized("Oil free", "no talc", "liquid", "foundation")
                    presenter.getProductPersonalized("Oil free", "no talc", "pallete", "eyeshadow")
                    presenter.getProductPersonalized("Oil free", "no talc", "lipstick", "lipstick")
                    presenter.getProductPersonalized("Oil free", "no talc", "powder", "blush")
                }
                "Dry" -> {
                    presenter.getProductPersonalized("certclean", "cruelty free", "liquid", "foundation")
                    presenter.getProductPersonalized("certclean", "no talc", "pallete", "eyeshadow")
                    presenter.getProductPersonalized("certclean", "chemical free", "lipstick", "lipstick")
                    presenter.getProductPersonalized("certclean", "Canadian", "powder", "blush")
                }
                else -> Log.e("Presenter", "Can't get presenter")
            }
        } else {
            Log.e("Dashboard", "Failed to initialize ApiServicePersonalized")
            Toast.makeText(
                requireContext(),
                "Failed to initialize ApiServicePersonalized",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun displayProduct(result: ResultState<List<Product>>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val productData = result.data
                eyeshadowAdapter.updateData(productData)
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
