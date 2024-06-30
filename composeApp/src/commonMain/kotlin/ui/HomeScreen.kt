package ui
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalFocusManager
import com.app.compose_navigation_mvvm_flow.utils.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import models.ApiResponse
import org.koin.compose.viewmodel.koinViewModel
import viewmodel.HomeViewModel

@OptIn(FlowPreview::class)
@Composable
fun HomeScreen(){
    val viewModel: HomeViewModel= koinViewModel()
    val state = viewModel.uiStateProductList.collectAsState()
    val searchQuery = remember { MutableStateFlow("") }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        viewModel.getProductList()
    }

    LaunchedEffect(searchQuery) {
        searchQuery
            .debounce(300) // debounce time in milliseconds
            .distinctUntilChanged()
            .collectLatest { query ->
                if(query.isEmpty()) {
                  viewModel.getProductList()
                } else{
                    viewModel.searchProducts(query)
                }
                focusManager.clearFocus()
            }
    }
        Column {
            SearchBar(searchQuery)
            when (state.value) {
                is UiState.Error -> {
                    ProgressLoader(isLoading = false)
                }
                UiState.Loading -> {
                    ProgressLoader(isLoading = true)
                }
                is UiState.Success -> {
                    ProgressLoader(isLoading = false)
                    (state.value as UiState.Success<ApiResponse>).data?.let {
                        ProductCard(it.list)
                    }
                }
            }
        }
}
