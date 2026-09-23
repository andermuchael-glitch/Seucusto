package br.com.seucusto.app.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.seucusto.app.data.SeucustoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class ProductViewModel(private val repository:SeucustoRepository):ViewModel(){
 val products=repository.products().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 fun add(name:String,sku:String,category:String,unit:String,description:String)=viewModelScope.launch{repository.addProduct(name,sku,category,unit,description)}
}