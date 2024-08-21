package nl.tiebe.otarium.ui.home.messages.folder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.coroutines.launch
import nl.tiebe.otarium.logic.root.home.children.messages.children.folder.FolderComponent
import nl.tiebe.otarium.logic.root.home.children.messages.children.folder.FolderSearchComponent
import nl.tiebe.otarium.ui.home.messages.MessageItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FolderSearchScreen(component: FolderSearchComponent) {
    val scope = rememberCoroutineScope()
    val messages = component.searchedItems.subscribeAsState().value

    SearchBar(
        query = component.searchQuery.subscribeAsState().value,
        onQueryChange = { component.setSearchQuery(it); scope.launch { component.search(it) } },
        onSearch = { scope.launch { component.search(it) } },
        active = component.searchActive.subscribeAsState().value,
        onActiveChange = { component.setSearchActive(it) },
        leadingIcon = { Icon(Icons.Default.Search, "Search") },
        modifier = Modifier
            .fillMaxWidth(),
        colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(top = 0.dp)
    ) {
        messages.forEach { message ->
            MessageItem(component.parentComponent::navigateToMessage, message)
        }
    }
}