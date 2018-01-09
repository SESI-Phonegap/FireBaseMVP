package com.example.gonzch.firebaseexample.interactor

import com.example.gonzch.firebaseexample.data.api.client.ChatService
import com.example.gonzch.firebaseexample.data.model.FriendlyMessage

class MessageInteractor(private var chatService: ChatService) {

    fun updateChat(): FriendlyMessage{
        return chatService.updateMessages()
    }

    fun sendMessage(friendlyMessage: FriendlyMessage){
        chatService.sendMessage(friendlyMessage)
    }

}