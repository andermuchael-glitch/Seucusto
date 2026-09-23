package br.com.seucusto.app.data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
class SeucustoRepository(private val db:SeucustoDatabase){
 val lastMessage=MutableStateFlow<String?>(null)
 fun products():Flow<List<ProductEntity>>=db.productDao().observeAll()
 fun rawMaterials():Flow<List<RawMaterialEntity>>=db.rawMaterialDao().observeAll()
 fun components(productId:String):Flow<List<ProductComponentEntity>>=db.productComponentDao().observeForProduct(productId)
 suspend fun findProduct(id:String)=db.productDao().find(id)
 suspend fun addProduct(name:String,sku:String,category:String,saleUnit:String,description:String){require(name.isNotBlank());require(sku.isNotBlank());db.productDao().insert(ProductEntity(UUID.randomUUID().toString(),name.trim(),sku.trim(),category.trim(),saleUnit.trim(),description.trim()))}
 suspend fun saveRawMaterial(id:String?,name:String,code:String,category:String,unit:String,minimum:Double,costCents:Long,supplier:String,location:String,notes:String){require(name.isNotBlank());require(unit.isNotBlank());require(minimum>=0);require(costCents>=0);val item=RawMaterialEntity(id?:UUID.randomUUID().toString(),name.trim(),code.trim(),category.trim(),unit.trim(),minimum,costCents,supplier.trim(),location.trim(),notes.trim());if(id==null)db.rawMaterialDao().insert(item)else db.rawMaterialDao().update(item);lastMessage.value=null}
 suspend fun deleteRawMaterialSafely(id:String){if(db.productComponentDao().countByRawMaterial(id)>0){lastMessage.value="Não é possível excluir: matéria-prima está vinculada a uma composição.";return};db.rawMaterialDao().find(id)?.let{db.rawMaterialDao().delete(it)};lastMessage.value="Matéria-prima excluída com segurança."}
 suspend fun saveComponent(productId:String,rawMaterialId:String,quantity:Double,physicalPercent:Double,observation:String){require(quantity>0);require(physicalPercent in 0.0..100.0);db.productComponentDao().upsert(ProductComponentEntity(productId,rawMaterialId,quantity,physicalPercent,observation.trim()))}
 suspend fun deleteComponent(productId:String,rawMaterialId:String)=db.productComponentDao().delete(productId,rawMaterialId)
}