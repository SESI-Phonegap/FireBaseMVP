package com.example.gonzch.firebaseexample.data.api.client

import com.example.gonzch.firebaseexample.data.model.FriendlyMessage

public interface ChatService {

    fun updateMessages():FriendlyMessage

    fun sendMessage(friendlyMessage: FriendlyMessage)
}