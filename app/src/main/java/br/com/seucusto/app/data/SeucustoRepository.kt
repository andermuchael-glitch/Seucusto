package br.com.seucusto.app.data
import kotlinx.coroutines.flow.Flow
import java.util.UUID
class SeucustoRepository(private val db:SeucustoDatabase){
 fun products():Flow<List<ProductEntity>>=db.productDao().observeAll()
 fun rawMaterials():Flow<List<RawMaterialEntity>>=db.rawMaterialDao().observeAll()
 fun components(productId:String):Flow<List<ProductComponentEntity>>=db.productComponentDao().observeForProduct(productId)
 suspend fun addProduct(name:String,sku:String,category:String,saleUnit:String,description:String){require(name.isNotBlank());require(sku.isNotBlank());db.productDao().insert(ProductEntity(UUID.randomUUID().toString(),name.trim(),sku.trim(),category.trim(),saleUnit.trim(),description.trim()))}
 suspend fun addRawMaterial(name:String,code:String,category:String,unit:String,minimum:Double,costCents:Long,supplier:String,location:String,notes:String){require(name.isNotBlank());require(costCents>=0);db.rawMaterialDao().insert(RawMaterialEntity(UUID.randomUUID().toString(),name.trim(),code.trim(),category.trim(),unit.trim(),minimum,costCents,supplier.trim(),location.trim(),notes.trim()))}
 suspend fun saveComponent(productId:String,rawMaterialId:String,quantity:Double,physicalPercent:Double?,observation:String){require(quantity>0);require(physicalPercent==null||physicalPercent>=0);db.productComponentDao().upsert(ProductComponentEntity(productId,rawMaterialId,quantity,physicalPercent,observation))}
}