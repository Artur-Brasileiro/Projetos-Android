package br.com.projetomanaconfeccoes.view.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.projetomanaconfeccoes.data.Cliente
import br.com.projetomanaconfeccoes.R

@Composable
fun ClienteItem(cliente: Cliente, onDelete: (Cliente) -> Unit) {

    Column(modifier = Modifier
        .background(Color.White)
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp, top = 8.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${cliente.nome}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold)

                Row {
                    Text(text = "${cliente.telefone}")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "${cliente.cidade}")
                }
            }

            IconButton(onClick = { onDelete(cliente) }) {
                Icon(painter = painterResource(id = R.drawable.excluir),
                    contentDescription = "icone deletar",
                    tint = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Black))
    }
}