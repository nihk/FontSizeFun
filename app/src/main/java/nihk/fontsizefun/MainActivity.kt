package nihk.fontsizefun

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var fontSizeManager: FontSizeManager

    override fun attachBaseContext(newBase: Context) {
        fontSizeManager = FontSizeManager(newBase.prefs())
        val newConfig = Configuration(newBase.resources.configuration)
        newConfig.fontScale = fontSizeManager.fontSize.scale
        applyOverrideConfiguration(newConfig)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.small).setOnClickListener { updateFontSize(FontSize.SMALL) }
        findViewById<Button>(R.id._default).setOnClickListener { updateFontSize(FontSize.DEFAULT) }
        findViewById<Button>(R.id.large).setOnClickListener { updateFontSize(FontSize.LARGE) }
    }

    private fun updateFontSize(fontSize: FontSize) {
        fontSizeManager.fontSize = fontSize
        recreate()
    }
}

fun Context.prefs(): SharedPreferences = getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE)

class FontSizeManager(private val prefs: SharedPreferences) {

    private val unsetFontSizeValue = -1f

    var fontSize: FontSize
        get() {
            val scale = prefs.getFloat("font_scale", unsetFontSizeValue)
            return if (scale == unsetFontSizeValue) {
                FontSize.DEFAULT
            } else {
                FontSize.values().first { fontSize -> fontSize.scale == scale }
            }
        }
        set(value) {
            prefs.edit()
                .putFloat("font_scale", value.scale)
                .apply()
        }

}

enum class FontSize(val scale: Float) {
    SMALL(0.7f),
    DEFAULT(1.0f),
    LARGE(1.3f)
}