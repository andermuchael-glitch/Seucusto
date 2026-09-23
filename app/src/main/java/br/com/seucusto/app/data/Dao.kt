package br.com.seucusto.app.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao interface ProductDao{@Query("SELECT * FROM products ORDER BY name") fun observeAll():Flow<List<ProductEntity>>;@Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun upsert(item:ProductEntity)}
@Dao interface RawMaterialDao{@Query("SELECT * FROM raw_materials ORDER BY name") fun observeAll():Flow<List<RawMaterialEntity>>;@Query("SELECT * FROM raw_materials WHERE id=:id LIMIT 1") suspend fun find(id:String):RawMaterialEntity?;@Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun upsert(item:RawMaterialEntity)}
@Dao interface StockDao{@Query("SELECT * FROM stock ORDER BY rawMaterialId") fun observeAll():Flow<List<StockEntity>>;@Query("SELECT * FROM stock WHERE rawMaterialId=:id LIMIT 1") suspend fun find(id:String):StockEntity?;@Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun upsert(item:StockEntity);@Insert suspend fun addMovement(item:StockMovementEntity)}
