package br.com.projetomanaconfeccoes.model

import br.com.projetomanaconfeccoes.data.Cliente
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClienteRepositorio {

    private val db = FirebaseFirestore.getInstance()

    suspend fun adicionarCliente(cliente: Cliente): String {
        return try {
            val documentReference = db.collection("clientes")
                .add(cliente)
                .await()
            documentReference.id
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun retornarClientes(): List<Cliente> {
        return try {
            val result = db.collection("clientes")
                .get()
                .await()
            result.map { document ->
                val cliente = document.toObject(Cliente::class.java)
                cliente.id = document.id
                cliente
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun removerCliente(clienteId: String) {
        try {
            db.collection("clientes").document(clienteId)
                .delete()
                .await()
        } catch (e: Exception) {
            throw e
        }
    }
}