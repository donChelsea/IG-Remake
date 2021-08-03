package com.example.ig_remake.presentation.authentication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ig_remake.MainActivity
import com.example.ig_remake.databinding.ActivityRegisterBinding
import com.example.ig_remake.models.User
import com.example.ig_remake.util.FirebaseUtil.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val TAG = "RegisterActivity"
    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegisterSelectPhoto.setOnClickListener {
            selectPhoto()
        }

        binding.registerFinishButton.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter an email and/or password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(email, password)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            Log.d(TAG, "photo uri: ${selectedPhotoUri?.path}")
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            binding.profileImage.setImageBitmap(bitmap)
            binding.buttonRegisterSelectPhoto.alpha = 0f
        }
    }

    private fun registerUser(email: String, password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }
                Log.d(TAG, "Added user: ${it.result?.user?.uid}")

                uploadPhotoToStorage()
            }.addOnFailureListener {
                Log.d(TAG, "Failed to add user: ${it.message}")
            }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun uploadPhotoToStorage() {
        if (selectedPhotoUri == null) {
            Log.d(TAG, "photo null")
            return
        }

        val filename = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance().getReference("/images/$filename")
        storage.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d(TAG, "photo uploaded: ${it.metadata?.path}")
            storage.downloadUrl.addOnSuccessListener {
                Log.d(TAG, "photo location: $it")
                saveUserToDatabase(it.toString())
            }
        }

    }

    private fun saveUserToDatabase(profileImageUrl: String) {
        val uid = auth.uid ?: ""
        val db = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, binding.registerUsername.text.toString(), profileImageUrl)
        db.setValue(user).addOnSuccessListener {
            Log.d(TAG, "User saved to db")

            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }.addOnFailureListener {
            Log.d(TAG, "User not saved to db: ${it.message}")
        }
    }
}