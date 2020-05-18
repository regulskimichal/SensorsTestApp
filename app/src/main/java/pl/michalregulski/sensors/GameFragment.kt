package pl.michalregulski.sensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.michalregulski.sensors.databinding.GameFragmentBinding

class GameFragment : Fragment(R.layout.fragment_game) {

    private val viewModel: MainActivityViewModel by sharedViewModel()
    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            viewModel = this@GameFragment.viewModel
        }
        return binding.root
    }

}
