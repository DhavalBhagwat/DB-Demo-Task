package com.example.holdings.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.holdings.data.dao.HoldingsDao
import com.example.holdings.data.model.HoldingEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var holdingsDao: HoldingsDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        holdingsDao = database.holdingsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testDatabaseSetup() = runBlocking {
        // Arrange
        val holding = HoldingEntity("ABC", 4, 60.0, 50.0, 55.0)
        holdingsDao.insertHoldings(listOf(holding))

        // Act
        val holdings = holdingsDao.getHoldings().first()

        // Assert
        assertEquals(1, holdings.size)
        assertEquals("ABC", holdings[0].symbol)
    }
}
