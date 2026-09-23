package br.com.seucusto.app.data
import androidx.room.Entity
@Entity(tableName="products") data class ProductEntity(@androidx.room.PrimaryKey val id:String,val name:String,val sku:String,val category:String="",val saleUnit:String="un",val description:String="",val photoUri:String?=null)
@Entity(tableName="raw_materials") data class RawMaterialEntity(@androidx.room.PrimaryKey val id:String,val name:String,val code:String="",val category:String="",val unit:String="un",val minimumQuantity:Double=0.0,val unitCostCents:Long=0L,val supplier:String="",val location:String="",val notes:String="")
@Entity(tableName="product_components",primaryKeys=["productId","rawMaterialId"]) data class ProductComponentEntity(val productId:String,val rawMaterialId:String,val quantity:Double,val physicalPercent:Double?=null,val observation:String="")
@Entity(tableName="stock") data class StockEntity(@androidx.room.PrimaryKey val rawMaterialId:String,val quantity:Double=0.0,val updatedAt:Long=System.currentTimeMillis())
@Entity(tableName="stock_movements") data class StockMovementEntity(@androidx.room.PrimaryKey val id:String,val rawMaterialId:String,val type:String,val quantity:Double,val unitCostCents:Long,val reason:String="",val date:Long=System.currentTimeMillis(),val reference:String?=null)
