package mx.edu.utng.lavj.eccochalleger.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.edu.utng.lavj.eccochalleger.data.local.dao.*
import mx.edu.utng.lavj.eccochalleger.data.local.entities.*

@Database(
    entities = [
        UsuarioEntity::class,
        RetoEntity::class,
        LogroEntity::class,
        ConsejoEntity::class,
        LugarEcologicoEntity::class,
        RankingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun retoDao(): RetoDao
    abstract fun logroDao(): LogroDao
    abstract fun consejoDao(): ConsejoDao
    abstract fun lugarEcologicoDao(): LugarEcologicoDao
    abstract fun rankingDao(): RankingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ecochallenge_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(database: AppDatabase) {
            val retoDao = database.retoDao()
            val logroDao = database.logroDao()
            val consejoDao = database.consejoDao()

            // Retos iniciales
            val retosIniciales = listOf(
                RetoEntity(
                    id = "reto1",
                    titulo = "¡Recicla tus envases de plástico!",
                    descripcion = "Separa y recicla al menos 5 envases de plástico hoy",
                    tipo = "reciclaje",
                    puntos = 10,
                    iconoNombre = "Recycling"
                ),
                RetoEntity(
                    id = "reto2",
                    titulo = "Usa una botella reutilizable",
                    descripcion = "Evita comprar botellas desechables durante todo el día",
                    tipo = "agua",
                    puntos = 15,
                    iconoNombre = "LocalDrink"
                ),
                RetoEntity(
                    id = "reto3",
                    titulo = "Reduce tu consumo de agua",
                    descripcion = "Cierra el grifo mientras te cepillas los dientes",
                    tipo = "agua",
                    puntos = 10,
                    iconoNombre = "WaterDrop"
                )
            )
            retoDao.insertRetos(retosIniciales)

            // Logros iniciales
            val logrosIniciales = listOf(
                LogroEntity(
                    id = "logro1",
                    nombre = "Reforestación",
                    descripcion = "Planta tu primer árbol",
                    imagenNombre = "arbol",
                    requisitoPuntos = 50
                ),
                LogroEntity(
                    id = "logro2",
                    nombre = "Reciclaje",
                    descripcion = "Recicla durante 7 días consecutivos",
                    imagenNombre = "composta",
                    requisitoPuntos = 100
                ),
                LogroEntity(
                    id = "logro3",
                    nombre = "Ahorro de agua",
                    descripcion = "Ahorra 100 litros de agua",
                    imagenNombre = "regadera",
                    requisitoPuntos = 150
                )
            )
            logroDao.insertLogros(logrosIniciales)

            // Consejos iniciales
            val consejosIniciales = listOf(
                ConsejoEntity(
                    id = "consejo1",
                    titulo = "Ahorra agua:",
                    contenido = "Cierra el grifo mientras te cepillas los dientes o lavas los platos. Cada gota cuenta para el planeta 🌎",
                    categoria = "agua"
                ),
                ConsejoEntity(
                    id = "consejo2",
                    titulo = "Recicla correctamente:",
                    contenido = "Separa tus residuos en orgánicos e inorgánicos. Limpia los envases antes de reciclarlos para mejorar el proceso ♻️",
                    categoria = "reciclaje"
                ),
                ConsejoEntity(
                    id = "consejo3",
                    titulo = "Ahorra energía:",
                    contenido = "Desconecta los aparatos electrónicos cuando no los uses. Aprovecha la luz natural durante el día ☀️",
                    categoria = "energia"
                ),
                ConsejoEntity(
                    id = "consejo4",
                    titulo = "Consume responsable:",
                    contenido = "Lleva tus propias bolsas reutilizables al supermercado. Evita productos con exceso de empaque 🛍️",
                    categoria = "consumo"
                ),
                ConsejoEntity(
                    id = "consejo5",
                    titulo = "Movilidad sostenible:",
                    contenido = "Usa bicicleta o transporte público cuando sea posible. Comparte tu auto con amigos o colegas 🚴",
                    categoria = "transporte"
                )
            )
            consejoDao.insertConsejos(consejosIniciales)
        }
    }
}