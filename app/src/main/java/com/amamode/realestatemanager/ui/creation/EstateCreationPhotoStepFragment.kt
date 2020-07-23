package com.amamode.realestatemanager.ui.creation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.ui.EstateViewModel
import kotlinx.android.synthetic.main.fragment_estate_creation_photo_step.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val REQUEST_IMAGE_CAPTURE = 1

class EstateCreationPhotoStepFragment : Fragment(R.layout.fragment_estate_creation_photo_step) {
    private val estateViewModel: EstateViewModel by sharedViewModel()
    private val safeArgs: EstateCreationPhotoStepFragmentArgs by navArgs()
    private val estateToModify: EstateDetails? by lazy { safeArgs.estateToModify }
    private val isTablet: Boolean by lazy { resources.getBoolean(R.bool.isTablet) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        takePhotoCTA.setOnClickListener {
            dispatchTakePictureIntent()
        }

        goToFinalStepCTA.setOnClickListener {
            val action =
                EstateCreationPhotoStepFragmentDirections.goToFinalStep(estateToModify)
            findNavController().navigate(action)
        }

        if (!isTablet) {
            setupToolbar()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                imageViewTest.setImageBitmap(imageBitmap)
            } else {
                val uri = data?.data
                imageViewTest.setImageURI(uri)
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT

        val chooser = Intent.createChooser(
            galleryIntent,
            getString(R.string.estate_creation_photo_intent_message)
        )
        chooser.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            arrayOf(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        )
        startActivityForResult(chooser, REQUEST_IMAGE_CAPTURE)
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateCreationPhotoToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.estate_creation_photo_toolbar_title)
        }
    }
}
