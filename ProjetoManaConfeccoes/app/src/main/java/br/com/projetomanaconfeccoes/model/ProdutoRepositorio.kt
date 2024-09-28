package br.com.projetomanaconfeccoes.model

import br.com.projetomanaconfeccoes.data.Produto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class ProdutoRepositorio {

    private val db = FirebaseFirestore.getInstance()

    suspend fun adicionarProduto(produto: Produto): String {
        return try {
            val documentReference = db.collection("produtos")
                .add(produto)
                .await()
            documentReference.id
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun buscarProdutos(): List<Produto> {
        return try {
            val snapshot: QuerySnapshot = db.collection("produtos")
                .orderBy("data", Query.Direction.DESCENDING)  // Ordenando diretamente pelo campo "data"
                .get()
                .await()

            val produtos = snapshot.toObjects(Produto::class.java)
            produtos.forEachIndexed { index, produto ->
                produto.id = snapshot.documents[index].id
            }

            produtos // Já retornado ordenado pelo Firebase
        } catch (e: Exception) {
            println("Erro ao buscar produtos: ${e.message}")
            throw e
        }
    }

    suspend fun removerProduto(produtoId: String) {
        try {
            db.collection("produtos").document(produtoId).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }

}