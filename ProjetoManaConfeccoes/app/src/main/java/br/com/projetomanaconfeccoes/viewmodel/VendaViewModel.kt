package br.com.projetomanaconfeccoes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.projetomanaconfeccoes.data.Venda
import br.com.projetomanaconfeccoes.model.VendaRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VendaViewModel : ViewModel() {

    private val repositorio = VendaRepositorio()

    private val _mesesDisponiveis = MutableStateFlow<List<String>>(emptyList())
    val mesesDisponiveis: StateFlow<List<String>> = _mesesDisponiveis

    private val _vendasDoMes = MutableStateFlow<List<Venda>>(emptyList())
    val vendasDoMes: StateFlow<List<Venda>> = _vendasDoMes

    private val _vendas = MutableStateFlow<List<Venda>>(emptyList())
    val vendas: StateFlow<List<Venda>> = _vendas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _saldoMensal = MutableStateFlow(0.0)
    val saldoMensal: StateFlow<Double> = _saldoMensal

    private var mesAtual: String = obterMesAtual()

    init {
        verificarViradaDeMes()
        buscarVendasDoMesAtual()
    }

    fun carregarMesesDisponiveis() {
        viewModelScope.launch {
            try {
                val mesesGravados = repositorio.getMesesGravados()

                if (!mesesGravados.contains(mesAtual)) {
                    repositorio.adicionarMesAnoColecao(mesAtual)
                    _mesesDisponiveis.value = formatarMesesGravados(repositorio.getMesesGravados())
                } else {
                    _mesesDisponiveis.value = formatarMesesGravados(mesesGravados)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar meses disponíveis: ${e.message}"
            }
        }
    }

    private fun verificarViradaDeMes() {
        val novoMes = obterMesAtual()
        if (novoMes != mesAtual) {
            salvarVendasDoMesAnterior(mesAtual)
            mesAtual = novoMes
        }
    }

    private fun obterMesAtual(): String {
        val sdf = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }

    private fun salvarVendasDoMesAnterior(mesAnterior: String) {
        viewModelScope.launch {
            try {
                repositorio.salvarVendasDoMes(_vendas.value, mesAnterior)
                _vendas.value = emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao salvar vendas do mês anterior: ${e.message}"
            }
        }
    }

    fun buscarVendasDoMesAtual() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val vendasList = repositorio.retornarVendasDoMes(mesAtual)
                _vendas.value = vendasList.sortedBy { venda ->
                    // Converte a data de String para Date para permitir a ordenação correta
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    formatter.parse(venda.data)
                }
                atualizarSaldoMensal(vendasList)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao buscar vendas do mês atual: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarVendasPorMes(ano: Int, mes: Int) {
        val mesAno = String.format("%02d-%d", mes, ano)
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val vendasList = repositorio.retornarVendasPorMes(ano, mes)
                _vendas.value = vendasList.sortedBy { venda ->
                    // Converte a data de String para Date para permitir a ordenação correta
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    formatter.parse(venda.data)
                }
                atualizarSaldoMensal(vendasList)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao buscar vendas por mês: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun adicionarNovaVenda(data: String, valor: Double, descricao: String) {
        val venda = Venda(data = data, valor = valor, descricao = descricao)
        viewModelScope.launch {
            try {
                repositorio.adicionarVenda(venda, mesAtual)
                buscarVendasDoMesAtual()
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar nova venda: ${e.message}"
            }
        }
    }

    fun deletarVenda(venda: Venda) {
        viewModelScope.launch {
            try {
                repositorio.deletarVenda(venda, mesAtual)
                buscarVendasDoMesAtual()
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao deletar venda: ${e.message}"
            }
        }
    }

    private fun atualizarSaldoMensal(vendasList: List<Venda>) {
        val total = vendasList.sumOf { it.valor }
        _saldoMensal.value = total
    }

    private fun formatarMesesGravados(meses: List<String>): List<String> {
        val mesesNome = listOf(
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        )

        return meses.map { mesAno ->
            val (mes, ano) = mesAno.split("-")
            "${mesesNome[mes.toInt() - 1]} $ano"
        }
    }
}