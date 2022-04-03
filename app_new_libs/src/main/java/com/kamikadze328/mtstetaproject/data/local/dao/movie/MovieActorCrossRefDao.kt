package com.kamikadze328.mtstetaproject.data.local.dao.movie

import androidx.room.Dao
import com.kamikadze328.mtstetaproject.data.local.dao.BaseDao
import com.kamikadze328.mtstetaproject.data.local.entity.MovieActorCrossRef
import javax.inject.Singleton

@Singleton
@Dao
interface MovieActorCrossRefDao : BaseDao<MovieActorCrossRef>