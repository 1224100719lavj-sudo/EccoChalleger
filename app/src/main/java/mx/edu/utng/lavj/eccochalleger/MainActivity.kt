package mx.edu.utng.lavj.eccochalleger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.utng.lavj.eccochalleger.data.local.AppDatabase
import mx.edu.utng.lavj.eccochalleger.data.repository.*
import mx.edu.utng.lavj.eccochalleger.navigation.AppNavigation
import mx.edu.utng.lavj.eccochalleger.ui.theme.EccoChallegerTheme
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    private lateinit var database: AppDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inicializar Room Database
        database = AppDatabase.getDatabase(applicationContext)

        enableEdgeToEdge()
        setContent {
            EccoChallegerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Repositorios
                    val authRepository = AuthRepository(auth, firestore, database.usuarioDao())
                    val retoRepository = RetoRepository(
                        database.retoDao(),
                        database.usuarioDao(),
                        firestore
                    )
                    val rankingRepository = RankingRepository(database.rankingDao(), firestore)
                    val consejoRepository = ConsejoRepository(database.consejoDao())
                    val logroRepository = LogroRepository(database.logroDao())

                    // ViewModels
                    val authViewModel = viewModel<AuthViewModel>(
                        factory = AuthViewModelFactory(authRepository)
                    )
                    val retosViewModel = viewModel<RetosViewModel>(
                        factory = RetosViewModelFactory(retoRepository)
                    )
                    val rankingViewModel = viewModel<RankingViewModel>(
                        factory = RankingViewModelFactory(rankingRepository)
                    )
                    val consejosViewModel = viewModel<ConsejosViewModel>(
                        factory = ConsejosViewModelFactory(consejoRepository)
                    )
                    val perfilViewModel = viewModel<PerfilViewModel>(
                        factory = PerfilViewModelFactory(authRepository, logroRepository)
                    )

                    // Navegación
                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        retosViewModel = retosViewModel,
                        rankingViewModel = rankingViewModel,
                        consejosViewModel = consejosViewModel,
                        perfilViewModel = perfilViewModel
                    )
                }
            }
        }
    }
}

// -------------------------------
// ViewModel Factories
// -------------------------------

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RetosViewModelFactory(
    private val retoRepository: RetoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RetosViewModel(retoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RankingViewModelFactory(
    private val rankingRepository: RankingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RankingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RankingViewModel(rankingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ConsejosViewModelFactory(
    private val consejoRepository: ConsejoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsejosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsejosViewModel(consejoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PerfilViewModelFactory(
    private val authRepository: AuthRepository,
    private val logroRepository: LogroRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(authRepository, logroRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
