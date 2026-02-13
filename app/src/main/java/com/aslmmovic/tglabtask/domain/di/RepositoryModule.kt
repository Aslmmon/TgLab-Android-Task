package com.aslmmovic.tglabtask.domain.di

import com.aslmmovic.tglabtask.data.remote.repository.NbaRepositoryImpl
import com.aslmmovic.tglabtask.domain.repository.NbaRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNbaRepository(
        impl: NbaRepositoryImpl
    ): NbaRepository
}
