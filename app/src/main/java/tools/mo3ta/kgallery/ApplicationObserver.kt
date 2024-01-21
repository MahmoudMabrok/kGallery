package tools.mo3ta.kgallery

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ApplicationObserver(private val globaSatatus: View) : DefaultLifecycleObserver {


    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        globaSatatus.isVisible = false
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d("TestTest", "onPause: ")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d("TestTest", "onResume: ")
        globaSatatus.isVisible = true
    }
}