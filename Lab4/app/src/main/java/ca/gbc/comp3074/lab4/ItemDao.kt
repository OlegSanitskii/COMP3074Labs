package ca.gbc.comp3074.lab4

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert
    suspend fun insert(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id=:itemId")
    fun getItemById(itemId: Long): Item

    @Query("SELECT * FROM items WHERE name LIKE :itemName")
    fun getItemByName(itemName: String): Flow<List<Item>>
}