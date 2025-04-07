package gli.project.tripmate.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import gli.project.tripmate.domain.model.user.User
import gli.project.tripmate.domain.repository.UserRepository
import gli.project.tripmate.domain.util.ErrorMessage
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    db : FirebaseFirestore
) : UserRepository {
    private val userPath = db.collection("users")

    override suspend fun userRegister(
        email: String,
        password: String
    ): Flow<ResultResponse<Unit>> = flow {
        try {
            emit(ResultResponse.Loading)

            // Register with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).await()

            // get user data from Firebase Authentication
            val user = auth.currentUser ?: throw Exception(ErrorMessage.FAILED_LOGIN)

            // save user data to firestore
            val userData = User(
                id = user.uid,
                email = user.email ?: "",
                userName = email.substringBefore("@"),
                lastLogin = 0L
            )
            userPath.document(user.uid).set(userData).await()

            emit(ResultResponse.Success(Unit))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(ResultResponse.Error(ErrorMessage.INVALID_USER))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(ResultResponse.Error(ErrorMessage.PASS_ERROR))
        } catch (e: IOException) {
            emit(ResultResponse.Error(ErrorMessage.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message ?: ErrorMessage.UNKNOWN_ERROR))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun userLogin(email: String, password: String): Flow<ResultResponse<Unit>> = flow {
        try {
            emit(ResultResponse.Loading)

            // login process
            auth.signInWithEmailAndPassword(email, password).await()

            // update last login time on firestore
            val user = auth.currentUser ?: throw Exception(ErrorMessage.FAILED_LOGIN)
            userPath.document(user.uid).update("lastLogin", System.currentTimeMillis()).await()

            emit(ResultResponse.Success(Unit))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(ResultResponse.Error(ErrorMessage.INVALID_USER))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(ResultResponse.Error(ErrorMessage.PASS_ERROR))
        } catch (e: IOException) {
            emit(ResultResponse.Error(ErrorMessage.NETWORK_ERROR))
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message ?: ErrorMessage.UNKNOWN_ERROR))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun userLogout() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        return try {
            val userId = auth.currentUser?.uid ?: return null
            val snapshot = userPath.document(userId).get().await()
            val user = snapshot.toObject(User::class.java)

            if (user?.lastLogin != 0L) user else null
        } catch (e: Exception) {
            LogUtil.d("Repository", "Error mendapatkan user: ${e.message}")
            null
        }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Flow<ResultResponse<Unit>> = flow {
        try {
            emit(ResultResponse.Loading)

            // Check if user is logged in
            val user = auth.currentUser
            if (user != null && user.email != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

                user.reauthenticate(credential).await()

                // Change password
                user.updatePassword(newPassword).await()
                emit(ResultResponse.Success(Unit))
            } else {
                throw Exception("User not logged in")
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message ?: ErrorMessage.UNKNOWN_ERROR))
        }
    }

    override suspend fun editUserProfile(user: User): Flow<ResultResponse<Unit>> = flow {
        try {
            emit(ResultResponse.Loading)

            // Pastikan user yang diedit ada
            val existingUser = userPath.document(user.id).get().await()
            if (!existingUser.exists()) {
                throw Exception("User tidak ditemukan")
            }

            // Update data user di Firestore
            userPath.document(user.id).set(user).await()
            emit(ResultResponse.Success(Unit))
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message ?: ErrorMessage.UNKNOWN_ERROR))
        }
    }
}