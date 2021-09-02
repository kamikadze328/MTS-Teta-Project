package com.kamikadze328.mtstetaproject.di

import com.kamikadze328.mtstetaproject.data.remote.Webservice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit() = Webservice.create()


}