package pl.michalregulski.sensors

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.*
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.michalregulski.sensors.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    private val binding by lazy {
        MainActivityBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }
    }

    private val navigationController by lazy { findNavController(R.id.navFragment) }

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.fragment_sensors,
                R.id.fragment_gps,
                R.id.fragment_game
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavigation(binding.nav)
        setupFab(binding.fab)
    }

    private fun setupNavigation(
        bottomNavigationView: BottomNavigationView
    ) {
        setupActionBarWithNavController(navigationController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navigationController)
    }

    private fun setupFab(fab: FloatingActionButton) {
        fab.setOnClickListener {
            if (checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
            ) {
                viewModel.updateLocation()
            } else {
                if (shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_permission_title)
                        .setMessage(R.string.dialog_permission_details)
                        .setPositiveButton(R.string.btn_ok) { _, _ ->
                            requestPermissions()
                        }
                        .create()
                        .show()
                } else {
                    requestPermissions()
                }
            }
        }

        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragment_gps) {
                fab.show()
            } else {
                fab.hide()
            }
        }
    }

    private fun requestPermissions() {
        requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.all { it != PERMISSION_GRANTED }) {
                Snackbar.make(binding.layoutMain, "Permission denied", Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.fab) //https://material.io/components/snackbars#behavior
                    .show()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_CODE = 101
    }

}
