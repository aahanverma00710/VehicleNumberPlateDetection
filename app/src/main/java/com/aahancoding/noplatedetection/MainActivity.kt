package com.aahancoding.noplatedetection

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.aahancoding.noplatedetection.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var inputImage: InputImage? = null
    lateinit var analysisUseCase: ImageAnalysis
    lateinit var imageCapture: ImageCapture
    private lateinit var preview: Preview
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null

    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private val cameraPermissions = arrayOf(Manifest.permission.CAMERA)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraPermissionLauncher.launch(cameraPermissions)
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
            if (result.containsValue(java.lang.Boolean.FALSE)) {
                onBackPressed()
            } else {
                setupCamera()
            }
        }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this@MainActivity)

        cameraProviderFuture.addListener({
            // Preview
            preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.pvCamera.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.root.display?.rotation ?: Surface.ROTATION_0)
                .build()

            // Used to bind the lifecycle of cameras to the lifecycle owner
            try {
                cameraProvider = cameraProviderFuture.get()
            } catch (e: Exception) {
                finish()
            }

            //Image analysis use case for barcode analysis
            val builder = ImageAnalysis.Builder().apply {
                setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                build()
            }
            analysisUseCase = builder.build()

            analysisUseCase.setAnalyzer(
                ContextCompat.getMainExecutor(this@MainActivity)
            ) { imageProxy ->
                /* Timber.d("imageInfo: height:" + imageProxy.height + ", width:" + imageProxy.width)*/
                processFrame(imageProxy)
            }
            resetAllCameraUseCase()
        }, ContextCompat.getMainExecutor(this@MainActivity))

    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processFrame(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            inputImage?.let { inputImage ->
                scanTextFromImage(inputImage, imageProxy)
            } ?: run {
                imageProxy.close()
            }
        } ?: run {
            imageProxy.close()
        }
    }

    private fun scanTextFromImage(inputImage: InputImage, imageProxy: ImageProxy) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(inputImage).addOnSuccessListener {
            for (block in it.textBlocks) {
                for (line in block.lines) {
                    Log.e("getTextFromBitMap Line", "${block.text}")
                    if (line.text.isCarNumber()) {
                        binding.tvDetectedNumber.animateAlpha( View.VISIBLE)
                        binding.tvDetectedNumber.text = line.text
                    } else {
                        for (element in line.elements) {
                            if (element.text.isCarNumber()) {
                                binding.tvDetectedNumber.animateAlpha( View.VISIBLE)
                                binding.tvDetectedNumber.text = line.text
                            }
                        }
                    }
                }
            }
            imageProxy.close()
        }.addOnFailureListener {
            imageProxy.close()
        }
    }


    private fun resetAllCameraUseCase() {
        try {
            cameraProvider?.unbindAll()

            // Bind use cases to camera
            camera = cameraProvider?.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                analysisUseCase
            )
        } catch (exc: Exception) {
            Log.e("Use case binding failed", exc.message.toString())
        }
    }


}