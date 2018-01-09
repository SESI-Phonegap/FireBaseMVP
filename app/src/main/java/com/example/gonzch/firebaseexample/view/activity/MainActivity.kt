package com.example.gonzch.firebaseexample.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import com.example.gonzch.firebaseexample.R
import com.example.gonzch.firebaseexample.view.adapter.MessageAdapter
import com.example.gonzch.firebaseexample.data.model.FriendlyMessage
import com.example.gonzch.firebaseexample.presenter.MessagePresenter
import com.example.gonzch.firebaseexample.view.adapter.MsgAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    val ANONYMOUS = "anonymous"
    val DEFAULT_MSG_LENGTH_LIMIT = 1000

    lateinit var messagePresenter:MessagePresenter

    private lateinit var mMessageAdapter: MsgAdapter
    private var mUsername: String = ""
    private lateinit var fireBaseDb: FirebaseDatabase
    private lateinit var msgDataBaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messagePresenter = MessagePresenter()
        mUsername = ANONYMOUS

        fireBaseDb = FirebaseDatabase.getInstance()
        msgDataBaseReference = fireBaseDb.reference.child("messages")


        // Initialize message ListView and its adapter
        val friendlyMessages: MutableList<FriendlyMessage> = arrayListOf()
        mMessageAdapter = MsgAdapter(this,friendlyMessages)
        messageRecyclerView.adapter = mMessageAdapter

        progressBar.visibility = ProgressBar.INVISIBLE

        photoPickerButton.setOnClickListener {

        }

        messageEditText.addTextChangedListener(object : TextWatcher{
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
            val objMessage = FriendlyMessage(messageEditText.text.toString(),mUsername,null)
            messageEditText.text.clear()
            msgDataBaseReference.push().setValue(objMessage)
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
