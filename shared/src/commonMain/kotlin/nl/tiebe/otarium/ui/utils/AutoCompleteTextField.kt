package nl.tiebe.otarium.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.ui.home.messages.composing.getName
import nl.tiebe.otarium.ui.utils.chips.Chip
import nl.tiebe.otarium.ui.utils.chips.ChipTextFieldState
import nl.tiebe.otarium.ui.utils.chips.OutlinedChipTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier = Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onDoneActionClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {},
    state: ChipTextFieldState<ContactChip>
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = modifier.heightIn(max = TextFieldDefaults.MinHeight * 6)
    ) {

        item {
            QuerySearch(
                query = query,
                label = queryLabel,
                onQueryChanged = onQueryChanged,
                onDoneActionClick = {
                    onDoneActionClick()
                },
                state = state
            )
        }

        if (predictions.isNotEmpty()) {
            items(predictions) { prediction ->
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(prediction)
                        }
                ) {
                    itemContent(prediction)
                }
            }
        }
    }
}

class ContactChip(public val contact: Contact) : Chip(getName(contact))


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit,
    state: ChipTextFieldState<ContactChip>
) {
    OutlinedChipTextField(
        state = state,
        modifier = modifier
            .fillMaxWidth(),
        value = query,
        onValueChange = onQueryChanged,
        onSubmit = { null },
        label = { Text(text = label) },
        readOnlyChips = true
    )
}