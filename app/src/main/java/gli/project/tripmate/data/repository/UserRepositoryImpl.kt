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
    ): ResultResponse<Unit> {
        return try {
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
            ResultResponse.Success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            ResultResponse.Error(ErrorMessage.INVALID_USER)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            ResultResponse.Error(ErrorMessage.PASS_ERROR)
        } catch (e: IOException) {
            ResultResponse.Error(ErrorMessage.NETWORK_ERROR)
        } catch (e: Exception) {
            ResultResponse.Error(e.message ?: ErrorMessage.UNKNOWN_ERROR)
        }
    }

    override suspend fun userLogin(email: String, password: String): ResultResponse<Unit> {
        return try {
            // login process
            auth.signInWithEmailAndPassword(email, password).await()

            // update last login time on firestore
            val user = auth.currentUser ?: throw Exception(ErrorMessage.FAILED_LOGIN)
            userPath.document(user.uid).update("lastLogin", System.currentTimeMillis()).await()

            ResultResponse.Success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            ResultResponse.Error(ErrorMessage.INVALID_USER)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            ResultResponse.Error(ErrorMessage.PASS_ERROR)
        } catch (e: IOException) {
            ResultResponse.Error(ErrorMessage.NETWORK_ERROR)
        } catch (e: Exception) {
            ResultResponse.Error(e.message ?: ErrorMessage.UNKNOWN_ERROR)
        }
    }

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
    ): ResultResponse<Unit> {
        return try {
            // Check if user is logged in
            val user = auth.currentUser
            if (user != null && user.email != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

                user.reauthenticate(credential).await()

                // Change password
                user.updatePassword(newPassword).await()
                ResultResponse.Success(Unit)
            } else {
                throw Exception("User not logged in")
            }
        } catch (e: Exception) {
            ResultResponse.Error(e.message ?: ErrorMessage.UNKNOWN_ERROR)
        }
    }

    override suspend fun editUserProfile(user: User): ResultResponse<Unit> {
        return try {
            // Pastikan user yang diedit ada
            val existingUser = userPath.document(user.id).get().await()
            if (!existingUser.exists()) {
                throw Exception("User tidak ditemukan")
            }

            // Update data user di Firestore
            userPath.document(user.id).set(user).await()
            ResultResponse.Success(Unit)
        } catch (e: Exception) {
            ResultResponse.Error(e.message ?: ErrorMessage.UNKNOWN_ERROR)
        }
    }
}