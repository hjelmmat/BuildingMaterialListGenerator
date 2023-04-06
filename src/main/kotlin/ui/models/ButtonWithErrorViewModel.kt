package ui.models

class ButtonWithErrorViewModel(
    val buttonOnClick: () -> Unit,
    val buttonEnabled: () -> Boolean,
    val buttonText: String,
    val shouldDisplayError: () -> Boolean,
    val errorMessage: () -> String,
)
