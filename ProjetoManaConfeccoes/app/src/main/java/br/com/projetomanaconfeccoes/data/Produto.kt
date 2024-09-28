package br.com.projetomanaconfeccoes.data

data class Produto(
    val data: String?,
    val material: String?,
    val valor: String?,
    val peso: String?,
    var id: String = ""
){
    constructor() : this("", "", "", "", "")
}
