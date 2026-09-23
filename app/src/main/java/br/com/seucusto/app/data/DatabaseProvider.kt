package br.com.seucusto.app.data
import android.content.Context
import androidx.room.Room
object DatabaseProvider{ @Volatile private var instance:SeucustoDatabase?=null; fun get(context:Context)=instance?:synchronized(this){instance?:Room.databaseBuilder(context.applicationContext,SeucustoDatabase::class.java,"seucusto.db").build().also{instance=it}}}
