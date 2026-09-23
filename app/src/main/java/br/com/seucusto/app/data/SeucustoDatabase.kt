package br.com.seucusto.app.data
import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities=[ProductEntity::class,RawMaterialEntity::class,ProductComponentEntity::class,StockEntity::class,StockMovementEntity::class],version=1,exportSchema=false)
abstract class SeucustoDatabase:RoomDatabase(){
 abstract fun productDao():ProductDao
 abstract fun rawMaterialDao():RawMaterialDao
 abstract fun productComponentDao():ProductComponentDao
 abstract fun stockDao():StockDao
}
