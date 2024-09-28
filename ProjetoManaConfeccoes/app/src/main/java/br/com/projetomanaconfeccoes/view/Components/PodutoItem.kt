package br.com.projetomanaconfeccoes.view.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import br.com.projetomanaconfeccoes.data.Produto

@Composable
fun ProdutoItem(produto: Produto, onDelete: (Produto) -> Unit) {
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
                text = produto.data ?: "Sem data",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
            )

            IconButton(onClick = { onDelete(produto) }) {
                Icon(
                    painter = painterResource(id = R.drawable.excluir),
                    contentDescription = "icone deletar",
                    tint = Color.Red
                )
            }
        }

        Text(
            text = "Material: ${produto.material ?: "Sem material"}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Simplesmente substituir a vírgula por ponto na exibição
        val valorComPonto = produto.valor?.replace(",", ".") ?: "0.00"

        Text(
            text = "Valor: R$ $valorComPonto",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Quantidade: ${produto.peso ?: "Sem quantidade"}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}