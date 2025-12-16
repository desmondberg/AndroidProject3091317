package com.griffith.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import java.util.UUID


//the journal item entity to be stored in the database
//startPosition and endPosition fields in the JournalItem model are separated into their respective latitudes and longitudes
//as GeoPoint objects can't be stored here
@Entity(tableName = "journal_items")
data class JournalItemEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val title: String,
    val description: String,
    val journeyType: String,

    val startTime: Long,
    val endTime: Long,

    val startGeoPointLat: Double?,
    val startGeoPointLng: Double?,
    val endGeoPointLat: Double?,
    val endGeoPointLng: Double?
)


//data access object -- provides methods to access the database with
@Dao
interface JournalItemDao {

    //get all journal items as a flow
    @Query("SELECT * FROM journal_items ORDER BY startTime DESC")
    fun getJournalItems(): Flow<List<JournalItemEntity>>

    //get a single journal item by its id
    @Query("SELECT * FROM journal_items WHERE id = :id")
    suspend fun getJournalItemById(id: String): JournalItemEntity?

    //add a new journal item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: JournalItemEntity)

    //remove a journal item
    @Delete
    suspend fun delete(item: JournalItemEntity)
}

//database
@Database(
    entities = [JournalItemEntity::class],
    version = 1,
    exportSchema = true
)
abstract class JournalItemDatabase : RoomDatabase() {
    abstract fun journalItemDao(): JournalItemDao

    companion object {

        //database instance, initially null
        @Volatile
        private var db: JournalItemDatabase? = null
        fun getInstance(context: Context): JournalItemDatabase {
            //synchronised block to ensure only one database gets created if there isn't already an instance
            return db ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    JournalItemDatabase::class.java,
                    //name of the database file
                    "journal_item.db"
                ).build().also {
                    //assign this database as the instance
                    db = it
                }
            }
        }
    }
}
