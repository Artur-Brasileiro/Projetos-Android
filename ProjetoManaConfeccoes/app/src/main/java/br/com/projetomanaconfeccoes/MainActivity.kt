package br.com.projetomanaconfeccoes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.projetomanaconfeccoes.ui.theme.ProjetoManaConfeccoesTheme
import br.com.projetomanaconfeccoes.view.TelaClientes
import br.com.projetomanaconfeccoes.view.TelaProdutos
import br.com.projetomanaconfeccoes.view.TelaVendas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetoManaConfeccoesTheme() {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "vendas") {
                    composable("clientes") {
                        TelaClientes(navController)
                    }

                    composable("produtos") {
                        TelaProdutos(navController)
                    }

                    composable("vendas") {
                        TelaVendas(navController)
                    }
                }
            }
        }
    }
}