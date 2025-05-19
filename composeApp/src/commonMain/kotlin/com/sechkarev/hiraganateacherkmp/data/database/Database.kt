package com.sechkarev.hiraganateacherkmp.data.database

import androidx.room.ConstructedBy
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Entity
data class ChallengeSolutionEntity(
    @PrimaryKey val challengeId: String,
    val solution: String,
)

@Entity
data class UserEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventName: String,
    val timestamp: Long,
)

@Dao
interface ChallengeSolutionDao {
    @Query("SELECT * FROM challengesolutionentity")
    suspend fun retrieveAllSolutions(): List<ChallengeSolutionEntity>

    @Insert
    suspend fun insertSolution(challengeSolutionEntity: ChallengeSolutionEntity)

    @Query("DELETE FROM challengesolutionentity")
    suspend fun deleteAllSolutions()
}

@Dao
interface UserEventDao {
    @Query("SELECT * FROM userevententity WHERE :name = eventName ORDER BY timestamp DESC LIMIT 1")
    suspend fun retrieveLastEventByName(name: String): UserEventEntity?

    @Insert
    suspend fun insertUserEvent(userEventEntity: UserEventEntity)
}

@Database(version = 1, entities = [ChallengeSolutionEntity::class, UserEventEntity::class])
@ConstructedBy(AppDatabaseConstructor::class)
abstract class ChallengeSolutionDatabase : RoomDatabase() {
    abstract fun challengeSolutionDao(): ChallengeSolutionDao

    abstract fun userEventDao(): UserEventDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<ChallengeSolutionDatabase> {
    override fun initialize(): ChallengeSolutionDatabase
}
