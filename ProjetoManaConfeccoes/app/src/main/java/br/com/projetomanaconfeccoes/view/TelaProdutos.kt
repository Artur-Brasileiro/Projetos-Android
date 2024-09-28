package br.com.projetomanaconfeccoes.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import br.com.projetomanaconfeccoes.data.Produto
import br.com.projetomanaconfeccoes.view.Components.ProdutoItem
import br.com.projetomanaconfeccoes.viewmodel.ProdutoViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaProdutos(navController: NavController) {

    val viewModel : ProdutoViewModel = viewModel()

    var showDialog1 by remember { mutableStateOf(false) }
    var data by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    var showDialog2 by remember { mutableStateOf(false) }
    var produtoParaExcluir by remember { mutableStateOf<Produto?>(null) }

    LaunchedEffect(Unit) {
        viewModel.buscarProdutos()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {

        TopAppBar(
            title = { },
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

                        Text(text = "Adicionar produto", modifier = Modifier.padding(start = 20.dp))
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF2196F3)
            )
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            } else if (viewModel.produtos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum produto cadastrado.",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn {
                    items(viewModel.produtos) { produto ->
                        ProdutoItem(produto = produto, onDelete = {
                            produtoParaExcluir = produto
                            showDialog2 = true
                        })
                    }
                }
            }
        }

        BottomAppBar(
            containerColor = Color(0xFF2196F3),
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        onClick = {
                            navController.navigate("vendas"){
                            popUpTo("produtos") {inclusive = true} }
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
                        onClick = { navController.navigate("clientes"){
                            popUpTo("produtos") {inclusive = true}
                        } }
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
                        onClick = {  }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.caixa),
                            tint = Color.White,
                            contentDescription = "Ir para produtos"
                        )
                    }
                }
            }
        )
    }

    if (showDialog1) {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val context = LocalContext.current

        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year1, month1, day1 ->
                val monthh = month1 + 1
                data = "$day1/$monthh/$year1"
            }, year, month, day
        )

        AlertDialog(
            onDismissRequest = {
                showDialog1 = false
                data = ""
                material = ""
                valor = "" },
            title = { Text(text = "Adicionar produto") },
            text = {
                Column {

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = data,
                            onValueChange = {data = it},
                            readOnly = true,
                            label = { Text(text = "Selecione a data")},
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                            contentDescription = "icone_calendario",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    datePickerDialog.show()
                                })
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = material,
                        onValueChange = { material = it },
                        label = { Text("Material") },
                        placeholder = { Text("Digite o material") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = valor,
                        onValueChange = { valor = it },
                        label = { Text("Valor") },
                        placeholder = { Text("Digite o valor") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedLabelColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = peso,
                        onValueChange = { peso = it },
                        label = { Text("Quantidade") },
                        placeholder = { Text("Quantidade em Kg") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedLabelColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (data.isBlank() || material.isBlank() || valor.isBlank() || peso.isBlank()) {
                        Toast.makeText(context, "Todos os campos devem ser preenchidos.", Toast.LENGTH_SHORT).show()
                    } else {
                        valor = "R$ $valor"
                        peso = "$peso Kg"
                        viewModel.adicionarProduto(data, material.uppercase(), valor, peso)
                        showDialog1 = false
                        data = ""
                        material = ""
                        valor = ""
                        peso = ""
                    } }) {
                    Text(text = "Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog1 = false
                    data = ""
                    material = ""
                    valor = ""
                    peso = ""}) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    if (showDialog2) {
        AlertDialog(
            onDismissRequest = { showDialog2 = false },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza de que deseja excluir este produto?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        produtoParaExcluir?.let { produto ->
                            viewModel.removerProduto(produto)
                            showDialog2 = false
                            produtoParaExcluir = null
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
                        produtoParaExcluir = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}