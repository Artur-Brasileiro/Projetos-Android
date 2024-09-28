package br.com.projetomanaconfeccoes.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.projetomanaconfeccoes.data.Produto
import br.com.projetomanaconfeccoes.model.ProdutoRepositorio
import kotlinx.coroutines.launch

class ProdutoViewModel : ViewModel() {

    private val repositorio = ProdutoRepositorio()

    var produtos = mutableStateListOf<Produto>()
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun adicionarProduto(data: String, material: String, preco: String, peso: String) {
        val produto = Produto(data, material, preco, peso)

        viewModelScope.launch {
            try {
                isLoading = true
                repositorio.adicionarProduto(produto)
                buscarProdutos()
            } catch (e: Exception) {
                println("Erro ao adicionar o cliente: $e")
            } finally {
                isLoading = false
            }
        }
    }

    fun buscarProdutos() {
        viewModelScope.launch {
            try {
                isLoading = true
                produtos.clear()
                val produtosRecuperados = repositorio.buscarProdutos()

                // Ordenando pela data convertida para o formato yyyyMMdd
                val produtosOrdenados = produtosRecuperados.sortedByDescending { produto ->
                    produto.data?.let {
                        // Convertendo data de "dd/MM/yyyy" para "yyyyMMdd" para ordenar corretamente
                        it.split("/").reversed().joinToString("")
                    }
                }

                produtos.addAll(produtosOrdenados)
            } catch (e: Exception) {
                println("Erro ao buscar produtos: $e")
            } finally {
                isLoading = false
            }
        }
    }

    fun removerProduto(produto: Produto) {
        viewModelScope.launch {
            try {
                isLoading = true
                repositorio.removerProduto(produto.id)
                buscarProdutos()  // Atualiza a lista de produtos após a exclusão
            } catch (e: Exception) {
                println("Erro ao remover o produto: $e")
            } finally {
                isLoading = false
            }
        }
    }
}