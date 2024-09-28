package br.com.projetomanaconfeccoes.model

import android.util.Log
import br.com.projetomanaconfeccoes.data.Venda
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VendaRepositorio {

    private val db = FirebaseFirestore.getInstance()

    suspend fun adicionarVenda(venda: Venda, mesAno: String) {
        // Adicionar venda na coleção do mês específico
        db.collection("vendas_$mesAno").add(venda).await()

        // Adicionar mês na coleção "meses-gravados" caso ainda não exista
        adicionarMesAnoColecao(mesAno)
    }

    suspend fun retornarVendasDoMes(mesAno: String): List<Venda> {
        return try {
            val result = db.collection("vendas_$mesAno")
                .get()
                .await()
            result.documents.mapNotNull { document ->
                document.toObject(Venda::class.java)?.apply { id = document.id }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun retornarVendasPorMes(ano: Int, mes: Int): List<Venda> {
        val mesAno = String.format("%02d-%d", mes, ano)
        return try {
            val result = db.collection("vendas_$mesAno")
                .get()
                .await()
            result.documents.mapNotNull { document ->
                document.toObject(Venda::class.java)?.apply { id = document.id }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun salvarVendasDoMes(vendas: List<Venda>, mesAno: String) {
        val batch = db.batch()
        val vendasRef = db.collection("vendas_$mesAno")
        vendas.forEach { venda ->
            val docRef = vendasRef.document()
            batch.set(docRef, venda)
        }
        batch.commit().await()
    }

    suspend fun deletarVenda(venda: Venda, mesAno: String) {
        db.collection("vendas_$mesAno").document(venda.id).delete().await()
    }

    suspend fun retornarColecoesDisponiveis(): List<String> {
        return try {
            val collections = db.collectionGroup("vendas").get().await()
            collections.documents.map { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun adicionarMesAnoColecao(mesAno: String) {
        // Adiciona o mês na coleção "meses-gravados"
        val docRef = db.collection("meses-gravados").document(mesAno)
        docRef.set(hashMapOf("mesAno" to mesAno)).await()
    }

    suspend fun getMesesGravados(): List<String> {
        return try {
            val result = db.collection("meses-gravados").get().await()
            result.documents.map { it.id }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Erro ao carregar meses gravados: $e")
            emptyList()
        }
    }
}