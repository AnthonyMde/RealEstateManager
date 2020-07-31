package com.amamode.realestatemanager.ui.creation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.ui.EstatePhotoAdapter
import com.amamode.realestatemanager.ui.EstateViewModel
import kotlinx.android.synthetic.main.fragment_estate_creation_photo_step.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

private const val REQUEST_IMAGE_CAPTURE = 1034
private const val NUMBER_OF_COLUMNS = 2

class EstateCreationPhotoStepFragment : Fragment(R.layout.fragment_estate_creation_photo_step) {
    private val estateViewModel: EstateViewModel by sharedViewModel()
    private val safeArgs: EstateCreationPhotoStepFragmentArgs by navArgs()
    private val estateToModify: EstateDetails? by lazy { safeArgs.estateToModify }
    private val isTablet: Boolean by lazy { resources.getBoolean(R.bool.isTablet) }
    private lateinit var currentPhotoUri: String
    private lateinit var photoAdapter: EstatePhotoAdapter

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

        configureRV()
        estateViewModel.estatePhotos.observe(viewLifecycleOwner, Observer { photosUri ->
            goToFinalStepCTA.isEnabled = !photosUri.isNullOrEmpty()
            estateCreationEmptyPhotos.visibility =
                if (photosUri.isNullOrEmpty()) VISIBLE
                else INVISIBLE
        })
    }

    private fun configureRV() {
        photoAdapter = EstatePhotoAdapter(grid = true)
        estateCreationPhotoRV.adapter = photoAdapter
        estateCreationPhotoRV.layoutManager =
            GridLayoutManager(requireContext(), NUMBER_OF_COLUMNS)
        estateToModify?.estatePhotosUri?.let {
            photoAdapter.setPhotoUrlList(it)
            estateViewModel.setPhotos(*it.toTypedArray())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            showPhotoDescriptionDialog(data?.data)
        } else { // Result was a failure
            toast(getString(R.string.estate_creation_photo_not_taken))
        }
    }

    private fun savePhoto(uri: Uri?, description: String) {
        if (uri != null) { // from gallery
            estateViewModel.setPhotos(Pair(uri.toString(), description))
        } else { // from camera
            val fileUri = "file:///${currentPhotoUri}"
            estateViewModel.setPhotos(Pair(fileUri, description))
        }
        photoAdapter.setPhotoUrlList(estateViewModel.getPhotos())
    }

    private fun dispatchTakePictureIntent() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_OPEN_DOCUMENT

        val tookPictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = getPhotoFileUri() ?: return
        currentPhotoUri = photoFile.absolutePath

        val fileProvider: Uri =
            FileProvider.getUriForFile(requireContext(), "com.realestate.fileprovider", photoFile)
        tookPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        val chooser = Intent.createChooser(
            galleryIntent,
            getString(R.string.estate_creation_photo_intent_message)
        )

        chooser.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            arrayOf(tookPictureIntent)
        )

        // Start the image capture intent to take photo
        startActivityForResult(chooser, REQUEST_IMAGE_CAPTURE)
    }

    private fun getPhotoFileUri(): File? {
        return File(
            context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "share_image_" + System.currentTimeMillis() + ".png"
        )
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateCreationPhotoToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.estate_creation_photo_toolbar_title)
        }
    }

    private fun showPhotoDescriptionDialog(uri: Uri?) {
        val ctx = context ?: return
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(getString(R.string.estate_creation_photo_desc_popup_title))

        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(getString(R.string.estate_creation_photo_desc_popup_ok)) { _, _ ->
            val desc = input.text.toString()
            if (desc.isEmpty()) {
                toast(R.string.estate_creation_photo_desc_toast)
                showPhotoDescriptionDialog(uri)
            } else {
                savePhoto(uri, desc)
            }
        }
        builder.setCancelable(false)

        builder.show()
    }
}
