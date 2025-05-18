package com.sebas.data.datasource.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sebas.data.datasource.LocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore("ml_products")
class LocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
): LocalDataSource {

    companion object {
        private val ID_PRODUCT_VIEWED = stringPreferencesKey("id_product_viewed")
    }

    override suspend fun saveRecentViewedProduct(id: String) {
        context.dataStore.edit { preference ->
            preference[ID_PRODUCT_VIEWED] = id
        }
    }

    override suspend fun getRecentViewedProduct(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[ID_PRODUCT_VIEWED]
    }
}