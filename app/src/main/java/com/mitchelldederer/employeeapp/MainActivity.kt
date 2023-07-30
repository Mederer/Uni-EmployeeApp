package com.mitchelldederer.employeeapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mitchelldederer.employeeapp.ui.theme.EmployeeAppTheme
import java.lang.StringBuilder

/**
 * Main application activity.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmployeeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmployeeApp()
                }
            }
        }
    }
}

/**
 * Main composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { EmployeeAppTopbar() }
    ) {
        EmployeeForm(modifier.padding(it))
    }
}

/**
 * Bar displayed at the top of the application window.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeAppTopbar() {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
    )
}

/**
 * Form for capturing employee details.
 *
 * Note: The @SuppressLint annotation on this function is
 * because of the 'errors' map. The correct way to do this seems to be to use
 * the 'mutableStateMapOf()', however this led to a runtime error when accessing 'containsKey()'.
 * Further research is needed by me to use 'mutableStateMapOf()' properly, but for now, this solution
 * seems to work fine.
 */
@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeForm(modifier: Modifier = Modifier) {
    // Form field state
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Map containing errors.
    // Key: ID of the field name
    // Value: ID of the corresponding error message
    var errors: MutableMap<Int, Int> by remember {
        mutableStateOf(mutableMapOf())
    }

    // Dialog state.
    var dialogOpen by remember { mutableStateOf(false) }

    // Callback that handles when the user clicks the 'Submit' button
    val handleSubmit = {
        errors = validateEmployeeFormInput(firstName, lastName, employeeId, email)
        dialogOpen = true
    }

    // Callback that handles when the user clicks the 'Reset' button
    val handleReset = {
        firstName = ""
        lastName = ""
        email = ""
        employeeId = ""
        errors.clear()
    }

    // Show the dialog when 'dialogOpen' state is set to true.
    // Set it back to false in the callback argument.
    if (dialogOpen) {
        ValidationDialog(errors, firstName, lastName, employeeId, email) {
            dialogOpen = false
        }
    }

    // Page content
    // Displays a column of textfields, with the last element in the column being
    // a Row containing buttons.
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Employee Details",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isError = errors.containsKey(R.string.first_name_label),
            supportingText = {
                if (errors.containsKey(R.string.first_name_label)) Text(stringResource(errors[R.string.first_name_label]!!))
            },
            label = { Text(stringResource(R.string.first_name_label)) },
            leadingIcon = {
                Image(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            }
        )
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.last_name_label)) },
            leadingIcon = {
                Image(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            isError = errors.containsKey(R.string.last_name_label),
            supportingText = {
                if (errors.containsKey(R.string.last_name_label)) Text(stringResource(errors[R.string.last_name_label]!!))
            }
        )
        TextField(
            value = employeeId,
            onValueChange = { employeeId = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.employee_id_label)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            leadingIcon = {
                Image(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                )
            },
            isError = errors.containsKey(R.string.employee_id_label),
            supportingText = {
                if (errors.containsKey(R.string.employee_id_label)) Text(stringResource(errors[R.string.employee_id_label]!!))
            }
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.email_label)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
            leadingIcon = {
                Image(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
            },
            isError = errors.containsKey(R.string.email_label),
            supportingText = {
                if (errors.containsKey(R.string.email_label)) Text(stringResource(errors[R.string.email_label]!!))
            }
        )
        Spacer(modifier = Modifier.weight(1f))  // This spacer will push the text fields up and the buttons down.
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = handleReset) {
                Text("Reset")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = handleSubmit) {
                Text("Submit")
            }
        }
    }
}

/**
 * Validate employee form input.
 * Returns a map containing errors ( Key: fieldname ID, Value: error message ID )
 * If there are no errors, the map will be empty.
 *
 * @see Validator
 */
fun validateEmployeeFormInput(
    firstName: String,
    lastName: String,
    employeeId: String,
    email: String
): MutableMap<Int, Int> {
    val errors = mutableMapOf<Int, Int>()

    // Check each field using the Validator object,
    // If it contains errors, add it to the 'errors' map.
    if (!Validator.isValidName(firstName)) {
        errors[R.string.first_name_label] = R.string.name_error_message
    }
    if (!Validator.isValidName(lastName)) {
        errors[R.string.last_name_label] = R.string.name_error_message
    }
    if (!Validator.isValidId(employeeId)) {
        errors[R.string.employee_id_label] = R.string.employee_id_error_message
    }
    if (!Validator.isValidEmail(email)) {
        errors[R.string.email_label] = R.string.email_error_message
    }

    return errors
}

/**
 * Dialog for showing an error message.
 * Displaying this composable should be controlled by a Boolean state variable,
 * and should be set to false in the 'closeDialog' callback parameter.
 */
@Composable
fun ValidationDialog(
    errors: Map<Int, Int>,
    firstName: String,
    lastName: String,
    employeeId: String,
    email: String,
    closeDialog: () -> Unit
) {
    val title: String
    val message: String

    // If errors map is empty, set 'message' to a confirmation
    // message with employee details, otherwise generate a string containing
    // each fields error message
    if (errors.isEmpty()) {
        title = "Submitted"
        message = """
            Employee created with the following details:
            First Name: $firstName
            Last Name: $lastName
            ID: $employeeId
            email: $email
        """.trimIndent()
    } else {
        title = "Oops!"
        val errorBuilder = StringBuilder("The follows errors were found:\n\n")
        errors.forEach { (key, value) ->
            errorBuilder.append("${stringResource(key)}: ${stringResource(value)}\n\n")
        }
        message = errorBuilder.toString()
    }

    AlertDialog(
        onDismissRequest = closeDialog,
        confirmButton = {
            Button(onClick = closeDialog) {
                Text("Ok")
            }
        },
        title = { Text(title) },
        text = { Text(message) }
    )
}

/**
 * Previews the app for development purposes.
 * This allows for some basic UI debugging without launching an emulator.
 */
@Preview(showBackground = true)
@Composable
fun EmployeeAppPreview() {
    EmployeeAppTheme {
        EmployeeApp()
    }
}