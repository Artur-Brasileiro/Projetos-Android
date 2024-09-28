package br.com.projetomanaconfeccoes.view.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import br.com.projetomanaconfeccoes.R
import br.com.projetomanaconfeccoes.data.Venda

@Composable
fun VendaItem(venda: Venda, onDelete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "${venda.data}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .weight(1f)
            )

            IconButton(onClick = { onDelete() }) {
                Icon(
                    painter = painterResource(id = R.drawable.excluir),
                    contentDescription = "icone deletar",
                    tint = Color.Red
                )
            }
        }

        Text(
            text = "Valor: R$ ${venda.valor}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Descrição: ${venda.descricao}",
            fontSize = 18.sp
        )
    }
}