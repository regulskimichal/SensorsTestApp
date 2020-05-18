package pl.michalregulski.sensors

import android.app.Application
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {

    private val module = module {
        single { LocationServices.getFusedLocationProviderClient(androidApplication()) }
        single { LocationService(get()) }
        fragment { NavHostFragment() }
        fragment { SensorsFragment() }
        fragment { GpsFragment() }
        fragment { GameFragment() }
        viewModel { MainActivityViewModel(androidApplication(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            fragmentFactory()
            modules(module)
        }
    }

}
