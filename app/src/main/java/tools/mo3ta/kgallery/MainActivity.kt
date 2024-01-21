package tools.mo3ta.kgallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import tools.mo3ta.kgallery.databinding.ActivityMainBinding
import tools.mo3ta.kgallery.ui.addImage.AddImageDialogFragment

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}


    private val navController by lazy { findNavController(R.id.nav_host_fragment_content_main)}
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        checkPostNotificationPermission()

        checkShareIntentForImage()

        ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(ApplicationObserver(binding.main.status))

    }



    private fun checkShareIntentForImage() {
       if( intent?.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true) {
           handleSendImage(intent)
        }
    }
    private fun handleSendImage(intent: Intent) {
        lifecycleScope.launchWhenResumed {
            (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
                // Update UI to reflect image being shared
                Log.d("TestTest", "handleSendImage: ${it}")
                val fragment = AddImageDialogFragment()
                fragment.arguments = bundleOf(
                    AddImageDialogFragment.KEY_IMAGE_URI to it,
                )
                fragment.show(supportFragmentManager, AddImageDialogFragment.TAG)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPostNotificationPermission() {
        // ask for permission to show notifications on android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            || ContextCompat.checkSelfPermission(
               this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}