package com.gymstudio.app.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update
import com.gymstudio.app.data.model.Exercise
import com.gymstudio.app.data.model.TrainingPlan
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    val description: String,
    val stepsJson: String,
    val hintsJson: String,
    val iconKey: String,
    val isCustom: Boolean,
)

@Entity(tableName = "plans")
data class PlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val goal: String,
    val category: String,
    val durationMinutes: Int,
    val level: String,
    val description: String,
    val exerciseIdsJson: String,
    val isCustom: Boolean,
)

class Converters {
    @TypeConverter
    fun stringsToJson(value: List<String>): String = JSONArray(value).toString()

    @TypeConverter
    fun jsonToStrings(json: String): List<String> =
        JSONArray(json).let { arr -> List(arr.length()) { arr.getString(it) } }

    @TypeConverter
    fun longsToJson(value: List<Long>): String = JSONArray(value).toString()

    @TypeConverter
    fun jsonToLongs(json: String): List<Long> =
        JSONArray(json).let { arr -> List(arr.length()) { arr.getLong(it) } }
}

private val converters = Converters()

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    category = category,
    description = description,
    steps = converters.jsonToStrings(stepsJson),
    hints = converters.jsonToStrings(hintsJson),
    iconKey = iconKey,
    isCustom = isCustom,
)

fun Exercise.toEntity(): ExerciseEntity = ExerciseEntity(
    id = if (id == 0L) 0 else id,
    name = name,
    category = category,
    description = description,
    stepsJson = converters.stringsToJson(steps),
    hintsJson = converters.stringsToJson(hints),
    iconKey = iconKey,
    isCustom = isCustom,
)

fun PlanEntity.toDomain(): TrainingPlan = TrainingPlan(
    id = id,
    name = name,
    goal = goal,
    category = category,
    durationMinutes = durationMinutes,
    level = level,
    description = description,
    exerciseIds = converters.jsonToLongs(exerciseIdsJson),
    isCustom = isCustom,
)

fun TrainingPlan.toEntity(): PlanEntity = PlanEntity(
    id = if (id == 0L) 0 else id,
    name = name,
    goal = goal,
    category = category,
    durationMinutes = durationMinutes,
    level = level,
    description = description,
    exerciseIdsJson = converters.longsToJson(exerciseIds),
    isCustom = isCustom,
)

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE id = :id")
    fun observeById(id: Long): Flow<ExerciseEntity?>

    @Query("SELECT * FROM exercises WHERE lower(name) = lower(:name) LIMIT 1")
    suspend fun findByName(name: String): ExerciseEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: ExerciseEntity): Long

    @Update
    suspend fun update(entity: ExerciseEntity)

    @Query("DELETE FROM exercises WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun count(): Int
}

@Dao
interface PlanDao {
    @Query("SELECT * FROM plans ORDER BY isCustom ASC, name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<PlanEntity>>

    @Query("SELECT * FROM plans WHERE id = :id")
    fun observeById(id: Long): Flow<PlanEntity?>

    @Query("SELECT * FROM plans WHERE lower(name) = lower(:name) LIMIT 1")
    suspend fun findByName(name: String): PlanEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: PlanEntity): Long

    @Query("SELECT COUNT(*) FROM plans")
    suspend fun count(): Int
}
