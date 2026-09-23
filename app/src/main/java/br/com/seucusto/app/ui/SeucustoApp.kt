package br.com.seucusto.app.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.seucusto.app.data.SeucustoRepository
@Composable
fun SeucustoApp(repo:SeucustoRepository){
 var tab by rememberSaveable{mutableIntStateOf(0)}
 var productId by rememberSaveable{mutableStateOf<String?>(null)}
 val labels=listOf("Início","Produtos","Estoque","Produção","Mais")
 Scaffold(topBar={TopAppBar(title={Text("SEUCUSTO")},colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.primary,titleContentColor=MaterialTheme.colorScheme.onPrimary))},bottomBar={NavigationBar{labels.forEachIndexed{i,label->NavigationBarItem(selected=tab==i,onClick={tab=i},icon={Icon(if(i==0)Icons.Default.Home else if(i==1)Icons.Default.Inventory else if(i==2)Icons.Default.Warehouse else if(i==3)Icons.Default.Factory else Icons.Default.MoreHoriz,label)},label={Text(label)})}}}){p->
  Box(Modifier.padding(p)){
   when(tab){
    0->Dashboard()
    1->if(productId==null)ProductsScreen(repo){productId=it}else ProductEditorRoute(repo,productId!!){productId=null}
    2->RawMaterialsScreen(repo)
    3->Module(Modifier.fillMaxSize(),"PRODUÇÃO","Módulo de produção será conectado após estoque.")
    else->Module(Modifier.fillMaxSize(),"MAIS","Custos, relatórios, histórico, configurações e backup.")
   }
  }
 }
}
@Composable private fun ProductEditorRoute(repo:SeucustoRepository,id:String,onBack:()->Unit){
 var product by remember(id){mutableStateOf<br.com.seucusto.app.data.ProductEntity?>(null)}
 LaunchedEffect(id){product=repo.findProduct(id)}
 val p=product
 if(p==null)Column(Modifier.fillMaxSize().padding(24.dp)){CircularProgressIndicator();TextButton(onClick=onBack){Text("VOLTAR")}} else CompositionScreen(p,repo,onBack)
}
@Composable private fun Dashboard(){LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){item{Text("Controle inteligente dos seus custos",style=MaterialTheme.typography.headlineSmall)};item{Text("Produtos, matérias-primas e composição conectados ao banco Room.")}}}
@Composable private fun Module(m:Modifier,title:String,description:String){Column(m.padding(24.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){Text(title,style=MaterialTheme.typography.headlineMedium);Text(description)}}
