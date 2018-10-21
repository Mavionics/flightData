package com.github.mavionics.fligt_data

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.Button
import com.github.mavionics.fligt_data.R.styleable.View
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import com.crashlytics.android.Crashlytics

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()

        val constraintLayout = findViewById(R.id.mainActivityConstraintLayout) as ConstraintLayout
        val button = Button(this)
        button.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        button.text = "Chrash Button"
        button.setOnClickListener( {
            Crashlytics.getInstance().crash(); // Force a crash
        })
        constraintLayout.addView(button);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
