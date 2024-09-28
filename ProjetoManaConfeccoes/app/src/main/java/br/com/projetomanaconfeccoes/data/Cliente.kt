package br.com.projetomanaconfeccoes.data

data class Cliente(
    val nome: String?,
    val telefone: String?,
    val cidade: String?,
    var id: String = ""
){
    constructor() : this("", "", "", "")
}