package pl.michalregulski.sensors

import android.content.Context
import androidx.core.content.ContextCompat

inline fun <reified T : Any> getSystemService(context: Context): T? {
    return ContextCompat.getSystemService(context, T::class.java)
}
