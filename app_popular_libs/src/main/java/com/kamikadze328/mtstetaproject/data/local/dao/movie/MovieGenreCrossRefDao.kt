package com.kamikadze328.mtstetaproject.data.local.dao.movie

import androidx.room.Dao
import com.kamikadze328.mtstetaproject.data.local.dao.BaseDao
import com.kamikadze328.mtstetaproject.data.local.entity.MovieGenreCrossRef
import javax.inject.Singleton

@Singleton
@Dao
interface MovieGenreCrossRefDao : BaseDao<MovieGenreCrossRef>
