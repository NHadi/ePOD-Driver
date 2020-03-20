package sg.entvision.tmssystem.persistance

import androidx.room.*
import io.reactivex.Flowable
import sg.entvision.tmssystem.repository.LocationTable

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: LocationTable): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: List<LocationTable>): List<Long>

    @Query("SELECT * FROM LocationTable WHERE location_id = :id")
    fun selectById(id: Long): Flowable<LocationTable>

    @Query("SELECT * FROM LocationTable")
    fun selectAll(): Flowable<List<LocationTable>>

    @Update
    fun update(location : LocationTable): Int

    @Query("DELETE FROM LocationTable WHERE location_id = :id")
    fun deleteById(id: Long): Int

    @Query("DELETE FROM LocationTable")
    fun deleteAll(): Int
}