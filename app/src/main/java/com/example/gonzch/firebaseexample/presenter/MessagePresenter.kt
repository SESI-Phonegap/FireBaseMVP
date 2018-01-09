package com.example.gonzch.firebaseexample.presenter


class MessagePresenter : Presenter<MessagePresenter.View>() {

    
    interface View : Presenter.View{
        fun showLoading()

        fun hideLoading()

        fun showMessageNotFoundMsg()

        fun showConnectionErrorMessage()

        fun showServerError()

        fun renderMessage()
    }
}