package br.com.seucusto.app.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.seucusto.app.data.*
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun ProductsScreen(repo: SeucustoRepository, onOpen: (String) -> Unit) {
    val products by repo.products().collectAsState(initial = emptyList())
    var name by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("PRODUTOS", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(name, { name = it }, label = { Text("Nome do produto") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(sku, { sku = it }, label = { Text("SKU") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(category, { category = it }, label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            if (name.isNotBlank() && sku.isNotBlank()) scope.launch {
                repo.addProduct(name, sku, category, "UN", "")
                name = ""
                sku = ""
                category = ""
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("+ NOVO PRODUTO") }
        Spacer(Modifier.height(16.dp))
        Text("Produtos cadastrados", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(products, key = { it.id }) { p ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onOpen(p.id) }) {
                    Column(Modifier.padding(16.dp)) {
                        Text(p.name, style = MaterialTheme.typography.titleMedium)
                        Text("SKU: " + p.sku + " • " + p.saleUnit)
                    }
                }
            }
        }
    }
}

@Composable
fun CompositionScreen(product: ProductEntity, repo: SeucustoRepository, onBack: () -> Unit) {
    val materials by repo.rawMaterials().collectAsState(initial = emptyList())
    val components by repo.components(product.id).collectAsState(initial = emptyList())
    var selected by remember { mutableStateOf<RawMaterialEntity?>(null) }
    var qty by remember { mutableStateOf("") }
    var percent by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val total = components.sumOf { it.physicalPercent ?: 0.0 }
    val valid = abs(total - 100.0) < 0.001
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(product.name, style = MaterialTheme.typography.headlineSmall)
        Text("COMPOSIÇÃO", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(10.dp))
        Box {
            Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selected?.name ?: "Selecionar matéria-prima")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                materials.forEach { m ->
                    DropdownMenuItem(
                        text = { Text(m.name + " • " + m.unit) },
                        onClick = { selected = m; expanded = false }
                    )
                }
            }
        }
        OutlinedTextField(qty, { qty = it }, label = { Text("Quantidade") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(percent, { percent = it }, label = { Text("Composição física (%)") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            val m = selected
            val q = qty.replace(",", ".").toDoubleOrNull()
            val pc = percent.replace(",", ".").toDoubleOrNull()
            if (m != null && q != null && q > 0.0 && pc != null && pc in 0.0..100.0) scope.launch {
                repo.saveComponent(product.id, m.id, q, pc, "")
                qty = ""
                percent = ""
                selected = null
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("ADICIONAR À COMPOSIÇÃO") }
        Spacer(Modifier.height(12.dp))
        Text(
            "Composição física: " + "%.2f".format(total) + "% / 100%",
            style = MaterialTheme.typography.titleMedium
        )
        LinearProgressIndicator(
            progress = { (total / 100.0).coerceIn(0.0, 1.0).toFloat() },
            modifier = Modifier.fillMaxWidth()
        )
        when {
            total < 99.999 -> Text("⚠ Composição incompleta", color = MaterialTheme.colorScheme.tertiary)
            valid -> Text("✓ Composição completa: 100%", color = MaterialTheme.colorScheme.primary)
            else -> Text("✕ Composição acima de 100%", color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(components, key = { it.productId + ":" + it.rawMaterialId }) { c ->
                val m = materials.firstOrNull { it.id == c.rawMaterialId }
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(m?.name ?: "Matéria-prima")
                            Text(c.quantity.toString() + " " + (m?.unit ?: "") + " • " + (c.physicalPercent ?: 0.0).toString() + "%")
                        }
                        TextButton(onClick = { scope.launch { repo.deleteComponent(product.id, c.rawMaterialId) } }) { Text("REMOVER") }
                    }
                }
            }
        }
        TextButton(onClick = onBack) { Text("VOLTAR") }
    }
}
