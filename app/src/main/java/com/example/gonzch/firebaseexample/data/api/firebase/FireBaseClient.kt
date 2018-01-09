package com.example.gonzch.firebaseexample.data.api.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FireBaseClient {

    private lateinit var fireBaseService: FireBaseService
    private lateinit var fireBaseDb: FirebaseDatabase
    private lateinit var msgDataBaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener

    constructor(){
        initFireBase()
    }

    private fun initFireBase(){
        fireBaseDb = FirebaseDatabase.getInstance()
        msgDataBaseReference = fireBaseDb.reference.child("messages")
    }
}