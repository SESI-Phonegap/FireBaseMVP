package com.example.gonzch.firebaseexample.presenter

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class Presenter<T : Presenter.View> {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var view: T

    fun getView(): T? {
        return view
    }

    fun setView(view: T) {
        this.view = view
    }

    fun initialize() {

    }

    fun terminate() {
        dispose()
    }

    private fun addDisposableObserver(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun dispose() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    interface View{
        fun context(): Context
    }
}