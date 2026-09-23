package br.com.seucusto.app.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.seucusto.app.data.ProductEntity
import br.com.seucusto.app.data.SeucustoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeucustoApp(repo: SeucustoRepository) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var productId by rememberSaveable { mutableStateOf<String?>(null) }
    val labels = listOf("Início", "Produtos", "Estoque", "Produção", "Mais")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SEUCUSTO") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                labels.forEachIndexed { i, label ->
                    NavigationBarItem(
                        selected = tab == i,
                        onClick = { tab = i },
                        icon = {
                            Icon(
                                imageVector = when (i) {
                                    0 -> Icons.Default.Home
                                    1 -> Icons.Default.Inventory
                                    2 -> Icons.Default.Warehouse
                                    3 -> Icons.Default.Factory
                                    else -> Icons.Default.MoreHoriz
                                },
                                contentDescription = label
                            )
                        },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (tab) {
                0 -> Dashboard()
                1 -> {
                    val id = productId
                    if (id == null) {
                        ProductsScreen(repo) { productId = it }
                    } else {
                        ProductEditorRoute(repo, id) { productId = null }
                    }
                }
                2 -> RawMaterialsScreen(repo)
                3 -> Module(Modifier.fillMaxSize(), "PRODUÇÃO", "Módulo de produção será conectado após estoque.")
                else -> Module(Modifier.fillMaxSize(), "MAIS", "Custos, relatórios, histórico, configurações e backup.")
            }
        }
    }
}

@Composable
private fun ProductEditorRoute(repo: SeucustoRepository, id: String, onBack: () -> Unit) {
    var product by remember(id) { mutableStateOf<ProductEntity?>(null) }
    LaunchedEffect(id) { product = repo.findProduct(id) }
    val current = product
    if (current == null) {
        Column(Modifier.fillMaxSize().padding(24.dp)) {
            CircularProgressIndicator()
            TextButton(onClick = onBack) { Text("VOLTAR") }
        }
    } else {
        CompositionScreen(current, repo, onBack)
    }
}

@Composable
private fun Dashboard() {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Controle inteligente dos seus custos", style = MaterialTheme.typography.headlineSmall) }
        item { Text("Produtos, matérias-primas e composição conectados ao banco Room.") }
    }
}

@Composable
private fun Module(modifier: Modifier, title: String, description: String) {
    Column(modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Text(description)
    }
}
