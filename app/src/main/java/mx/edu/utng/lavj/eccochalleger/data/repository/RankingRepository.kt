package mx.edu.utng.lavj.eccochalleger.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import mx.edu.utng.lavj.eccochalleger.data.local.dao.RankingDao
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RankingEntity
import mx.edu.utng.lavj.eccochalleger.data.remote.models.RankingFirebase
import mx.edu.utng.lavj.eccochalleger.utils.Resource

class RankingRepository(
    private val rankingDao: RankingDao,
    private val firestore: FirebaseFirestore
) {

    fun getRankingLocal(): Flow<List<RankingEntity>> {
        return rankingDao.getRanking()
    }

    suspend fun sincronizarRanking(ubicacion: String): Resource<List<RankingEntity>> {
        return try {
            val rankingSnapshot = firestore.collection("usuarios")
                .whereEqualTo("ubicacion", ubicacion)
                .orderBy("puntos", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            val rankingFirebase = rankingSnapshot.toObjects(RankingFirebase::class.java)
            val rankingEntity = rankingFirebase.mapIndexed { index, user ->
                RankingEntity(
                    usuarioId = user.usuarioId,
                    nombre = user.nombre,
                    puntos = user.puntos,
                    posicion = index + 1,
                    ubicacion = user.ubicacion
                )
            }

            rankingDao.deleteAll()
            rankingDao.insertRanking(rankingEntity)

            Resource.Success(rankingEntity)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al sincronizar ranking")
        }
    }
}