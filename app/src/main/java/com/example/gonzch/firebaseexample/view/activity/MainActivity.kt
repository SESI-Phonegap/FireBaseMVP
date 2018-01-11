package com.example.gonzch.firebaseexample.view.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import com.example.gonzch.firebaseexample.R
import com.example.gonzch.firebaseexample.view.adapter.MessageAdapter
import com.example.gonzch.firebaseexample.data.model.FriendlyMessage
import com.example.gonzch.firebaseexample.presenter.MessagePresenter
import com.example.gonzch.firebaseexample.view.adapter.MsgAdapter
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

   private val TAG = "MainActivity"

    val ANONYMOUS = "anonymous"
    val DEFAULT_MSG_LENGTH_LIMIT = 1000
    val RC_SIGN_IN = 1
    val RC_PHOTO_PICKER = 100


    private lateinit var mMessageAdapter: MsgAdapter
    private var mUsername: String = ""
    private lateinit var fireBaseDb: FirebaseDatabase
    private lateinit var msgDataBaseReference: DatabaseReference
    private var childEventListener: ChildEventListener? = null

    private lateinit var firebaAuth: FirebaseAuth
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private lateinit var fireBaseStorage: FirebaseStorage
    private lateinit var chatPhotoStorageReference: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUsername = ANONYMOUS

        fireBaseDb = FirebaseDatabase.getInstance()
        firebaAuth = FirebaseAuth.getInstance()
        fireBaseStorage = FirebaseStorage.getInstance()
        chatPhotoStorageReference = fireBaseStorage.getReference().child("chat_photos")
        msgDataBaseReference = fireBaseDb.reference.child("messages")


        // Initialize message ListView and its adapter
        val friendlyMessages: MutableList<FriendlyMessage> = arrayListOf()
        mMessageAdapter = MsgAdapter(this, friendlyMessages)
        messageRecyclerView.adapter = mMessageAdapter

        progressBar.visibility = ProgressBar.INVISIBLE

        photoPickerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER)
        }

        messageEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendButton.isEnabled = s.toString().trim().length > 0
            }

        })

        messageEditText.filters = (arrayOf<InputFilter>(InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)))

        sendButton.setOnClickListener {
            val objMessage = FriendlyMessage(messageEditText.text.toString(), mUsername, null)
            messageEditText.text.clear()
            msgDataBaseReference.push().setValue(objMessage)
        }



        authStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                val user: FirebaseUser = firebaseAuth.currentUser!!

                if (!user.uid.isEmpty()) {
                    onSignedInInitialize(user.displayName!!)
                    Toast.makeText(this@MainActivity, "Estas Logeado. Bienvenido !!", Toast.LENGTH_LONG).show()
                } else {
                    onSignedOutCleanup()
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                            .build(), RC_SIGN_IN)
                }
            }
        }
    }


    private fun onSignedInInitialize(userName: String) {
        this.mUsername = userName
        attachDatabaseReadListener()
    }

    private fun onSignedOutCleanup() {
        this.mUsername = ANONYMOUS
        mMessageAdapter.removeAll()
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                    val friendlyMessage = dataSnapshot!!.getValue(FriendlyMessage::class.java)
                    mMessageAdapter.setMessage(friendlyMessage!!)
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
            msgDataBaseReference.addChildEventListener(childEventListener)
        }
    }

    private fun detachDatabaseReadListener() {
        if (childEventListener != null) {
            msgDataBaseReference.removeEventListener(childEventListener)
            childEventListener = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sign_out_menu -> AuthUI.getInstance().signOut(this@MainActivity)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        this.firebaAuth.addAuthStateListener(this.authStateListener!!)
    }

    override fun onPause() {
        super.onPause()
        if (authStateListener != null) {
            this.firebaAuth.removeAuthStateListener(this.authStateListener!!)
        }
        detachDatabaseReadListener()
        mMessageAdapter.removeAll()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN -> {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this@MainActivity, "Logueado !!", Toast.LENGTH_LONG).show()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this@MainActivity, "Sesion cancelada !!", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            RC_PHOTO_PICKER -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedImageUri = data!!.data
                    val photoRef = chatPhotoStorageReference.child(selectedImageUri.lastPathSegment)
                    photoRef.putFile(selectedImageUri).addOnSuccessListener { taskSnapshot ->
                        // When the image has successfully uploaded, we get its download URL
                        val downloadUrl = taskSnapshot.downloadUrl

                        // Set the download URL to the message box, so that the user can send it to the database
                        val friendlyMessage = FriendlyMessage(null, mUsername, downloadUrl!!.toString())
                        msgDataBaseReference.push().setValue(friendlyMessage)
                    }
                }
            }
        }
    }
}
