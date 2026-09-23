package br.com.seucusto.app.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.seucusto.app.data.RawMaterialEntity
import br.com.seucusto.app.data.SeucustoRepository
import kotlinx.coroutines.launch

@Composable
fun RawMaterialsScreen(repo: SeucustoRepository) {
    val materials by repo.rawMaterials().collectAsState(initial = emptyList())
    var editing by remember { mutableStateOf<RawMaterialEntity?>(null) }
    var form by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<RawMaterialEntity?>(null) }
    val message by repo.lastMessage.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("MATÉRIAS-PRIMAS", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { editing = null; form = true }, modifier = Modifier.fillMaxWidth()) {
            Text("+ NOVA MATÉRIA-PRIMA")
        }
        message?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (form) {
            RawMaterialForm(
                editing,
                save = { d ->
                    scope.launch {
                        repo.saveRawMaterial(
                            editing?.id, d.name, d.code, d.category, d.unit, d.minimum,
                            d.costCents, d.supplier, d.location, d.notes
                        )
                        form = false
                        editing = null
                    }
                },
                cancel = { form = false; editing = null }
            )
        }
        LazyColumn {
            items(materials, key = { it.id }) { m ->
                Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(m.name, style = MaterialTheme.typography.titleMedium)
                        Text("Unidade: " + m.unit)
                        Text("Custo: R$ " + "%.2f".format(m.unitCostCents / 100.0))
                        Text("Estoque mínimo: " + m.minimumQuantity)
                        Text("Fornecedor: " + m.supplier.ifBlank { "Não informado" })
                        Text("Localização: " + m.location.ifBlank { "Não informada" })
                        Row {
                            TextButton(onClick = { editing = m; form = true }) { Text("EDITAR") }
                            TextButton(onClick = { deleting = m }) { Text("EXCLUIR") }
                        }
                    }
                }
            }
        }
    }
    deleting?.let { m ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Excluir matéria-prima?") },
            text = { Text("A exclusão só será permitida quando não houver vínculo com composição ou histórico.") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        repo.deleteRawMaterialSafely(m.id)
                        deleting = null
                    }
                }) { Text("CONFIRMAR") }
            },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("CANCELAR") } }
        )
    }
}

private data class D(
    val name: String, val code: String, val category: String, val unit: String,
    val minimum: Double, val costCents: Long, val supplier: String,
    val location: String, val notes: String
)

@Composable
private fun RawMaterialForm(i: RawMaterialEntity?, save: (D) -> Unit, cancel: () -> Unit) {
    var n by remember(i?.id) { mutableStateOf(i?.name ?: "") }
    var u by remember(i?.id) { mutableStateOf(i?.unit ?: "UN") }
    var c by remember(i?.id) { mutableStateOf(i?.unitCostCents?.div(100.0)?.toString() ?: "0") }
    var min by remember(i?.id) { mutableStateOf(i?.minimumQuantity?.toString() ?: "0") }
    var s by remember(i?.id) { mutableStateOf(i?.supplier ?: "") }
    var l by remember(i?.id) { mutableStateOf(i?.location ?: "") }
    var code by remember(i?.id) { mutableStateOf(i?.code ?: "") }
    var cat by remember(i?.id) { mutableStateOf(i?.category ?: "") }
    var notes by remember(i?.id) { mutableStateOf(i?.notes ?: "") }
    var err by remember(i?.id) { mutableStateOf<String?>(null) }
    Column {
        OutlinedTextField(n, { n = it }, label = { Text("Nome *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(u, { u = it }, label = { Text("Unidade *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(c, { c = it }, label = { Text("Custo por unidade *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(min, { min = it }, label = { Text("Estoque mínimo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(s, { s = it }, label = { Text("Fornecedor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(l, { l = it }, label = { Text("Localização") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(code, { code = it }, label = { Text("Código") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(cat, { cat = it }, label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(notes, { notes = it }, label = { Text("Observações") }, modifier = Modifier.fillMaxWidth())
        err?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Row {
            TextButton(onClick = cancel) { Text("CANCELAR") }
            Button(onClick = {
                val cv = c.replace(",", ".").toDoubleOrNull()
                val mv = min.replace(",", ".").toDoubleOrNull()
                when {
                    n.isBlank() -> err = "Informe o nome."
                    u.isBlank() -> err = "Informe a unidade."
                    cv == null || cv < 0.0 -> err = "Custo inválido."
                    mv == null || mv < 0.0 -> err = "Estoque mínimo inválido."
                    else -> save(
                        D(
                            n.trim(), code.trim(), cat.trim(), u.trim(), mv,
                            kotlin.math.round(cv * 100.0).toLong(),
                            s.trim(), l.trim(), notes.trim()
                        )
                    )
                }
            }) { Text("SALVAR") }
        }
    }
}
