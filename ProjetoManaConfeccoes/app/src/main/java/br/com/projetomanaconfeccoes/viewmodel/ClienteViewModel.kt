package br.com.projetomanaconfeccoes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.projetomanaconfeccoes.data.Cliente
import br.com.projetomanaconfeccoes.model.ClienteRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClienteViewModel: ViewModel() {

    private val repositorio = ClienteRepositorio()

    private val _clientes = MutableStateFlow<List<Cliente>>(emptyList())
    val clientes: StateFlow<List<Cliente>> = _clientes

    private val _filteredClientes = MutableStateFlow<List<Cliente>>(emptyList())
    val filteredClientes: StateFlow<List<Cliente>> = _filteredClientes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        buscarClientes()
    }

    fun buscarClientes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val listaClientes = repositorio.retornarClientes()
                _clientes.value = listaClientes
                filtrarClientes(_searchQuery.value)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun adicionarNovoCliente(nome: String, telefone: String, cidade: String) {
        val cliente = Cliente(nome, telefone, cidade)

        viewModelScope.launch {
            try {
                val id = repositorio.adicionarCliente(cliente)
                println("Cliente adicionado com ID: $id")
                buscarClientes()
            } catch (e: Exception) {
                println("Erro ao adicionar o cliente: $e")
            }
        }
    }

    fun removerCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                repositorio.removerCliente(cliente.id)
                buscarClientes()
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao apagar cliente: ${e.message}"
            }
        }
    }

    fun filtrarClientes(query: String) {
        _searchQuery.value = query
        _filteredClientes.value = if (query.isEmpty()) {
            _clientes.value
        } else {
            _clientes.value.filter { cliente ->
                cliente.nome?.contains(query, ignoreCase = true) == true
            }
        }
    }
}
