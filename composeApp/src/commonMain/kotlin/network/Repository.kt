package network

import com.app.compose_navigation_mvvm_flow.utils.UiState
import data.remote.RemoteDataSource
import data.remote.toResultFlow
import kotlinx.coroutines.flow.Flow
import models.ApiResponse
import models.Products

class Repository(private val remoteDataSource: RemoteDataSource) {
    suspend fun getProducts(): Flow<UiState<ApiResponse?>> {
        return toResultFlow {
            remoteDataSource.getProducts()
        }
    }

    suspend fun searchProducts(query:String): Flow<UiState<ApiResponse?>> {
        return toResultFlow {
            remoteDataSource.searchProducts(query)
        }
    }

}