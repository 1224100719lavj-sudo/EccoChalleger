package mx.edu.utng.lavj.eccochalleger.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import mx.edu.utng.lavj.eccochalleger.data.local.dao.UsuarioDao
import mx.edu.utng.lavj.eccochalleger.data.local.entities.UsuarioEntity
import mx.edu.utng.lavj.eccochalleger.data.remote.models.UsuarioFirebase
import mx.edu.utng.lavj.eccochalleger.utils.Resource

class AuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val usuarioDao: UsuarioDao
) {

    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun login(email: String, password: String): Resource<UsuarioEntity> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: return Resource.Error("Error al obtener usuario")

            // Obtener datos de Firestore
            val userDoc = firestore.collection("usuarios").document(userId).get().await()
            val userFirebase = userDoc.toObject(UsuarioFirebase::class.java)
                ?: return Resource.Error("Usuario no encontrado")

            // Guardar en Room
            val userEntity = UsuarioEntity(
                id = userFirebase.id,
                nombre = userFirebase.nombre,
                email = userFirebase.email,
                puntos = userFirebase.puntos,
                ubicacion = userFirebase.ubicacion,
                fotoUrl = userFirebase.fotoUrl,
                descripcion = userFirebase.descripcion,
                fechaRegistro = userFirebase.fechaRegistro,
                esPremium = userFirebase.esPremium
            )
            usuarioDao.insertUsuario(userEntity)

            Resource.Success(userEntity)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    suspend fun register(email: String, password: String, nombre: String): Resource<UsuarioEntity> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: return Resource.Error("Error al crear usuario")

            // Crear usuario en Firestore
            val userFirebase = UsuarioFirebase(
                id = userId,
                nombre = nombre,
                email = email,
                puntos = 0,
                ubicacion = ""
            )
            firestore.collection("usuarios").document(userId).set(userFirebase).await()

            // Guardar en Room
            val userEntity = UsuarioEntity(
                id = userId,
                nombre = nombre,
                email = email,
                puntos = 0
            )
            usuarioDao.insertUsuario(userEntity)

            Resource.Success(userEntity)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al registrarse")
        }
    }

    suspend fun logout() {
        auth.signOut()
        usuarioDao.deleteAll()
    }

    fun getUsuarioActual(): Flow<UsuarioEntity?> {
        return usuarioDao.getUsuarioActual()
    }
}