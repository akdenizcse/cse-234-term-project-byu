package com.example.sellstuff

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _user = MutableStateFlow(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        auth.addAuthStateListener {
            _user.value = it.currentUser
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                } else {
                    _errorMessage.value = task.exception?.message ?: "Login failed"
                }
            }
    }

    fun signup(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                } else {
                    _errorMessage.value = task.exception?.message ?: "Sign up failed"
                }
            }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun logout() {
        auth.signOut()
        _user.value = null
    }
}
