package br.com.projetomanaconfeccoes.data

data class Venda(
    var id: String = "",
    val descricao: String? = null,
    val data: String? = null,
    val valor: Double = 0.0
)