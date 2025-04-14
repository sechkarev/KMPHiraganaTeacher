package com.sechkarev.hiraganateacherkmp.database

import androidx.room.ConstructedBy
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import kotlinx.coroutines.flow.Flow

@Entity
data class ChallengeSolutionEntity(
    @PrimaryKey val challengeId: String,
    val solution: String,
)

@Dao
interface ChallengeSolutionDao {
    @Query("SELECT * FROM challengesolutionentity")
    fun retrieveAllSolutions(): Flow<List<ChallengeSolutionEntity>>

    @Insert
    suspend fun insertSolution(challengeSolutionEntity: ChallengeSolutionEntity)

    @Query("DELETE FROM challengesolutionentity")
    suspend fun deleteAllSolutions()
}

@Database(version = 1, entities = [ChallengeSolutionEntity::class])
@ConstructedBy(AppDatabaseConstructor::class)
abstract class ChallengeSolutionDatabase : RoomDatabase() {
    abstract fun challengeSolutionDao(): ChallengeSolutionDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<ChallengeSolutionDatabase> {
    override fun initialize(): ChallengeSolutionDatabase
}
