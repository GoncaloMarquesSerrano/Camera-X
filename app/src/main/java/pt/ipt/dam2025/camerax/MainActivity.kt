package pt.ipt.dam2025.camerax

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pt.ipt.dam2025.camerax.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // pedir permissão para usar a câmara
        if(allPermissonsGranted()){
            startCamera()
        }
        else{
            requestPermissions()
        }


        binding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }
    }

    fun takePhoto(){

    }

    /**
     * ask for permissions
     */
    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }



    /**
     * define if all permissions has been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * if it is necessary to ask for permissions,
     * this will evaluate the answers provided by user
     * and start the camera, or inform user that it can not use the camera
     */
    private val activityResultLauncher =
        registerForActivityResult(  ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->

            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                // test for all types of permissions
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                // ver também 'Snack Bar'
                Toast.makeText(
                    baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startCamera()
            }
        }
}