package pl.michalregulski.sensors

import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

@BindingAdapter("entries")
fun ChipGroup.setEntries(entries: List<String>?) {
    if (entries != null) {
        this.removeAllViews()
        entries.forEach {
            Chip(this.context, null, R.style.Widget_MaterialComponents_Chip_Entry)
                .apply { text = it }
                .also { addView(it) }
        }
    }
}
