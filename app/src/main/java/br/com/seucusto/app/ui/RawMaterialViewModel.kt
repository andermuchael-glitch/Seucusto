package br.com.seucusto.app.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.seucusto.app.data.SeucustoRepository
import kotlinx.coroutines.launch
class RawMaterialViewModel(private val repository:SeucustoRepository):ViewModel(){
 fun add(name:String,code:String,category:String,unit:String,minimum:Double,costCents:Long,supplier:String,location:String,notes:String)=viewModelScope.launch{repository.addRawMaterial(name,code,category,unit,minimum,costCents,supplier,location,notes)}
}