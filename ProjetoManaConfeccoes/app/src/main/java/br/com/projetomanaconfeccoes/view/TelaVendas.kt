package br.com.projetomanaconfeccoes.view

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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.projetomanaconfeccoes.R
import br.com.projetomanaconfeccoes.data.Venda
import br.com.projetomanaconfeccoes.view.Components.VendaItem
import br.com.projetomanaconfeccoes.viewmodel.VendaViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaVendas(navController: NavController) {

    val vendaViewModel: VendaViewModel = viewModel()

    // Obter mês e ano atuais
    val calendar = Calendar.getInstance()
    val mesAtual = calendar.get(Calendar.MONTH) + 1
    val anoAtual = calendar.get(Calendar.YEAR)

    // Lista com nomes dos meses para exibição
    val mesesNome = listOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )
    val mesAtualNome = mesesNome[mesAtual - 1] // Nome do mês atual

    var mesSelecionado by remember { mutableStateOf(mesAtualNome) }
    var anoSelecionado by remember { mutableStateOf(anoAtual) }
    var expanded by remember { mutableStateOf(false) }

    // Carregar meses disponíveis e vendas do mês atual
    LaunchedEffect(Unit) {
        vendaViewModel.carregarMesesDisponiveis()
        vendaViewModel.buscarVendasPorMes(anoAtual, mesAtual)
    }

    // Carregar vendas ao mudar mesSelecionado ou anoSelecionado
    LaunchedEffect(mesSelecionado, anoSelecionado) {
        val mesIndex = mesesNome.indexOf(mesSelecionado) + 1
        vendaViewModel.buscarVendasPorMes(anoSelecionado, mesIndex)
    }

    val vendas by vendaViewModel.vendas.collectAsState()
    val isLoading by vendaViewModel.isLoading.collectAsState()
    val mesesDisponiveis by vendaViewModel.mesesDisponiveis.collectAsState()
    val saldoMensal by vendaViewModel.saldoMensal.collectAsState()

    var showDialog1 by remember { mutableStateOf(false) }
    var data by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }



    var showDialog2 by remember { mutableStateOf(false) }
    var vendaParaExcluir by remember { mutableStateOf<Venda?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = "Maná Confecções", color = Color.White, fontSize = 30.sp, fontFamily = FontFamily.Cursive)},

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

                        Text(text = "Nova venda", modifier = Modifier.padding(start = 20.dp))
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF2196F3)
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFFE0F7FA), RoundedCornerShape(8.dp)) // Fundo com bordas arredondadas
                .border(2.dp, Color(0xFF00796B), RoundedCornerShape(8.dp)) // Borda
                .heightIn(min = 60.dp) // Altura mínima
        ) {
            Text(
                text = "Saldo do mês: R$ ${"%.2f".format(saldoMensal)}",
                fontSize = 24.sp, // Aumentar o tamanho da fonte
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D40), // Cor do texto
                modifier = Modifier
                    .align(Alignment.Center) // Centraliza o texto
                    .padding(12.dp) // Espaçamento interno
            )
        }

        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            // Botão do DropdownMenu com mês e ano atuais selecionados inicialmente
            Button(onClick = { expanded = !expanded }) {
                Text(text = "$mesSelecionado $anoSelecionado")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                mesesDisponiveis.forEach { mesAno ->
                    DropdownMenuItem(
                        text = { Text(mesAno) },
                        onClick = {
                            // Ao clicar em um item do DropdownMenu, atualiza o mês e o ano selecionados
                            val (mes, ano) = mesAno.split(" ")
                            mesSelecionado = mes
                            anoSelecionado = ano.toInt()

                            expanded = false
                        }
                    )
                }
            }
        }

        Box(modifier = Modifier
            .padding(bottom = 8.dp)
            .weight(1f)
            .fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (vendas.isEmpty()) {
                    Text(
                        text = "Nenhuma venda nesse mês.",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(vendas) { venda ->
                            VendaItem(venda = venda, onDelete = {
                                vendaParaExcluir = venda
                                showDialog2 = true
                            })
                        }
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
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = { }

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            tint = Color.White,
                            contentDescription = "Ir para vendas"
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color.White))

                    IconButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = { navController.navigate("clientes"){
                            popUpTo("vendas") { inclusive = true }
                        } }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.clientes),
                            tint = Color.White,
                            contentDescription = "Ir para clientes"
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color.White))

                    IconButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = { navController.navigate("produtos"){
                            popUpTo("vendas") { inclusive = true }
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
        )

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
                    valor = ""
                    descricao = "" },
                title = { Text(text = "Nova Venda") },
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

                        OutlinedTextField(
                            value = valor,
                            onValueChange = { valor = it },
                            label = { Text("Valor") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                        OutlinedTextField(
                            value = descricao,
                            onValueChange = { descricao = it },
                            label = { Text("Descrição") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (data.isNotBlank() && valor.isNotBlank()) {
                                val valorDouble = valor.replace(",", ".").toDoubleOrNull()
                                if (valorDouble != null) {
                                    vendaViewModel.adicionarNovaVenda(data, valorDouble, descricao.uppercase())
                                    showDialog1 = false
                                    data = ""
                                    valor = ""
                                    descricao = ""
                                } else {
                                    // Aqui você pode exibir uma mensagem de erro caso a conversão falhe
                                }
                            }
                        }
                    ) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog1 = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    if (showDialog2) {
        AlertDialog(
            onDismissRequest = { showDialog2 = false },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza de que deseja excluir esta venda?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vendaParaExcluir?.let { venda ->
                            vendaViewModel.deletarVenda(venda)
                            showDialog2 = false
                            vendaParaExcluir = null
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
                        vendaParaExcluir = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}