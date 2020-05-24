package pl.michalregulski.sensors

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.michalregulski.sensors.databinding.GameFragmentBinding
import java.util.*

class GameFragment : Fragment() {

    private val viewModel: MainActivityViewModel by sharedViewModel()
    private lateinit var binding: GameFragmentBinding

    private val sensorManager by lazy { getSystemService<SensorManager>(requireContext()) }
    private val accelerometer by lazy { sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    private val vibrator by lazy { getSystemService<Vibrator>(requireContext()) }
    private lateinit var animatedImage: CustomImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            viewModel = this@GameFragment.viewModel
        }

        animatedImage = CustomImageView(requireContext())
        binding.layoutGame.addView(animatedImage)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val handler = Handler {
            animatedImage.invalidate()
            true
        }

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(0)
            }
        }, 0, 15)
    }

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            animatedImage.updatePosition(event.values[0], event.values[1])
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(listener)
    }

    inner class CustomImageView(context: Context?) : AppCompatImageView(context) {
        private var positionX = 0f
        private var positionY = 0f

        private var lastX = 0f
        private var lastY = 0f

        private val radius = 40f

        private var noBorderX = false
        private var noBorderY = false

        private val pen = Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(requireContext(), R.color.orange)
        }

        override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWeight: Int, oldHeight: Int) {
            if (oldWeight == 0 && oldHeight == 0) {
                positionX = newWidth / 2f
                positionY = newHeight / 2f
            }
        }

        public override fun onDraw(canvas: Canvas) {
            canvas.drawColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            canvas.drawCircle(positionX, positionY, radius, pen)
        }

        fun updatePosition(dx: Float, dy: Float) {
            lastX -= dx
            lastY += dy

            positionX += lastX
            positionY += lastY

            when {
                positionX > width - radius -> {
                    positionX = width - radius
                    lastX = 0f
                    if (noBorderX) {
                        makeVibration()
                        noBorderX = false
                    }
                }
                positionX < 0 + radius -> {
                    positionX = 0 + radius
                    lastX = 0f
                    if (noBorderX) {
                        makeVibration()
                        noBorderX = false
                    }
                }
                else -> {
                    noBorderX = true
                }
            }

            when {
                positionY > height - radius -> {
                    positionY = height - radius
                    lastY = 0f
                    if (noBorderY) {
                        makeVibration()
                        noBorderY = false
                    }
                }
                positionY < 0 + radius -> {
                    positionY = 0 + radius
                    lastY = 0f
                    if (noBorderY) {
                        makeVibration()
                        noBorderY = false
                    }
                }
                else -> {
                    noBorderY = true
                }
            }
        }

        private fun makeVibration() {
            vibrator?.vibrate(VibrationEffect.createOneShot(100, 1))
        }
    }

}
