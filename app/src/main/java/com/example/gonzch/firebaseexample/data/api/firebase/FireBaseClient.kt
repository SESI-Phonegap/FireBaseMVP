package com.example.gonzch.firebaseexample.data.api.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireBaseClient {

    private lateinit var fireBaseService: FireBaseService
    private lateinit var fireBaseDb: FirebaseDatabase
    private lateinit var msgDataBaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
    private lateinit var firebaAuth: FirebaseAuth
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private lateinit var fireBaseStorage: FirebaseStorage
    private lateinit var chatPhotoStorageReference: StorageReference

    constructor(){
        initFireBase()
    }

    private fun initFireBase(){
        fireBaseDb = FirebaseDatabase.getInstance()
        firebaAuth = FirebaseAuth.getInstance()
        fireBaseStorage = FirebaseStorage.getInstance()
        chatPhotoStorageReference = fireBaseStorage.getReference().child("chat_photos")
        msgDataBaseReference = fireBaseDb.reference.child("messages")
    }
}