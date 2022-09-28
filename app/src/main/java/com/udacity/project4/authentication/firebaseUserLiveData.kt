package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserLiveData:LiveData<FirebaseUser?>(){
    private val firebase=FirebaseAuth.getInstance()
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
    }

    override fun onActive() {
        firebase.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        firebase.removeAuthStateListener(authStateListener)
    }
}