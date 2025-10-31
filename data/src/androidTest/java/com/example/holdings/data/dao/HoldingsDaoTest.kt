package com.example.holdings.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.holdings.data.db.AppDatabase
import com.example.holdings.data.model.HoldingEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HoldingsDaoTest {

    private lateinit var holdingsDao: HoldingsDao

    private lateinit var database: AppDatabase

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
    fun test_insert_and_get_Holdings() = runBlocking {
        // Arrange
        val holding = HoldingEntity("ABC", 4, 60.0, 50.0, 55.0)
        holdingsDao.insertHoldings(listOf(holding))

        // Act
        val holdings = holdingsDao.getHoldings().first()

        // Assert
        assertEquals(1, holdings.size)
        assertEquals("ABC", holdings[0].symbol)
    }

    @Test
    fun test_clear_all() = runBlocking {
        // Arrange
        val holding = HoldingEntity("ABC", 4, 60.0, 50.0, 55.0)
        holdingsDao.insertHoldings(listOf(holding))

        // Act
        holdingsDao.clearAll()
        val holdings = holdingsDao.getHoldings().first()

        // Assert
        assertEquals(0, holdings.size)
    }
}
