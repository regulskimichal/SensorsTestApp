package pl.michalregulski.sensors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.michalregulski.sensors.databinding.SensorsFragmentBinding

class SensorsFragment : Fragment(R.layout.fragment_sensors) {

    private val viewModel: MainActivityViewModel by sharedViewModel()
    private lateinit var binding: SensorsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SensorsFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            viewModel = this@SensorsFragment.viewModel
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        viewModel.updateSensors()
        super.onAttach(context)
    }

}
