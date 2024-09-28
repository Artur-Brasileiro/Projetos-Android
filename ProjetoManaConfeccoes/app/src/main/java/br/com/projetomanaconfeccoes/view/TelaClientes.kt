package br.com.projetomanaconfeccoes.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.projetomanaconfeccoes.R
import br.com.projetomanaconfeccoes.data.Cliente
import br.com.projetomanaconfeccoes.view.Components.ClienteItem
import br.com.projetomanaconfeccoes.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaClientes(navController: NavController) {

    val context = LocalContext.current
    val clienteViewModel: ClienteViewModel = viewModel()

    val clientes by clienteViewModel.filteredClientes.collectAsState()
    val errorMessage by clienteViewModel.errorMessage.collectAsState()
    val isLoading by clienteViewModel.isLoading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    var showDialog1 by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }

    var showDialog2 by remember { mutableStateOf(false) }
    var clienteParaExcluir by remember { mutableStateOf<Cliente?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 56.dp)
        ) {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            clienteViewModel.filtrarClientes(it)
                        },
                        placeholder = { Text(text = "Buscar...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedPlaceholderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White,
                            focusedIndicatorColor = Color(0xFF2196F3),
                            unfocusedIndicatorColor = Color(0xFF2196F3)
                        )
                    )
                },
                actions = {
                    Button(
                        onClick = { showDialog1 = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painter = painterResource(
                                id = R.drawable.baseline_add_24),
                                contentDescription = "botao_adicionar")

                            Text(text = "Adicionar cliente", modifier = Modifier.padding(start = 20.dp))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )

            Column(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                } else if (clientes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum cliente cadastrado.",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f).padding(bottom = 26.dp)) {
                        items(clientes) { cliente ->
                            ClienteItem(cliente = cliente, onDelete = {
                                clienteParaExcluir = cliente
                                showDialog2 = true
                            })
                        }
                    }
                }
            }
        }

        BottomAppBar(
            containerColor = Color(0xFF2196F3),
            modifier = Modifier.align(Alignment.BottomCenter) // Garante que o BottomAppBar fique na parte inferior
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = {
                        navController.navigate("vendas") {
                            popUpTo("clientes") { inclusive = true }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        tint = Color.White,
                        contentDescription = "Ir para vendas"
                    )
                }

                Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White))

                IconButton(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clientes),
                        tint = Color.White,
                        contentDescription = "Ir para clientes"
                    )
                }

                Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White))

                IconButton(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = { navController.navigate("produtos"){
                        popUpTo("clientes") { inclusive = true }
                    } }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.caixa),
                        tint = Color.White,
                        contentDescription = "Ir para produtos"
                    )
                }
            }
        }

        if (showDialog1) {
            AlertDialog(
                onDismissRequest = {
                    showDialog1 = false
                    nome = ""
                    telefone = ""
                    cidade = ""
                },
                title = { Text(text = "Adicionar Cliente") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedLabelColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = telefone,
                            onValueChange = { telefone = it },
                            label = { Text("Telefone") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Phone
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedLabelColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = cidade,
                            onValueChange = { cidade = it },
                            label = { Text("Cidade") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedLabelColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (nome.isBlank() || telefone.isBlank() || cidade.isBlank()) {
                                Toast.makeText(context, "Todos os campos devem ser preenchidos.", Toast.LENGTH_SHORT).show()
                            } else {
                                val telefoneFormatado = formatarNumero(telefone)
                                clienteViewModel.adicionarNovoCliente(nome.uppercase(), telefoneFormatado, cidade.uppercase())
                                showDialog1 = false
                                nome = ""
                                telefone = ""
                                cidade = ""
                            }
                        }
                    ) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog1 = false
                        nome = ""
                        telefone = ""
                        cidade = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showDialog2) {
            AlertDialog(
                onDismissRequest = { showDialog2 = false },
                title = { Text("Confirmar Exclusão") },
                text = { Text("Tem certeza de que deseja excluir este cliente?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            clienteParaExcluir?.let { cliente ->
                                clienteViewModel.removerCliente(cliente)
                                showDialog2 = false
                                clienteParaExcluir = null
                            }
                        }
                    ) {
                        Text(text = "Excluir", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog2 = false
                            clienteParaExcluir = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

fun formatarNumero(numero: String): String {
    val cleaned = numero.replace(Regex("[^\\d]"), "")
    return when {
        cleaned.length <= 10 -> cleaned
        cleaned.length == 11 -> "(${cleaned.substring(0, 2)}) ${cleaned.substring(2, 7)}-${cleaned.substring(7)}"
        else -> numero
    }
}
