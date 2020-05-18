package pl.michalregulski.sensors

import android.os.Bundle
import android.text.InputType.TYPE_NULL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.michalregulski.sensors.databinding.GpsFragmentBinding

class GpsFragment : Fragment(R.layout.fragment_gps) {

    private val viewModel: MainActivityViewModel by sharedViewModel()
    private lateinit var binding: GpsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GpsFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            viewModel = this@GpsFragment.viewModel
        }
        return binding.root
    }

}
