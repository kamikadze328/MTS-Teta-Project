package com.kamikadze328.mtstetaproject.data.local.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(objects: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg obj: T): List<Long>

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}