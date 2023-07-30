package com.mitchelldederer.employeeapp

/**
 * Object for doing validation operations.
 *
 * Note: An 'object' in Kotlin seems to be similiar to a Java singleton class
 * or a class that only contains static members.
 */
object Validator {
    // Regex patterns
    private val emailRegex = Regex(pattern = "[A-Za-z0-9]+@[A-Za-z]+\\.[A-Za-z]+")
    private val idRegex = Regex(pattern = "^0\\d{6}$")
    private val nameRegex = Regex(pattern = "^[A-Za-z.\\-\\s]+$")

    // Validate inputs with the regex patterns
    fun isValidEmail(email: String) = emailRegex.matches(email)
    fun isValidId(id: String) = idRegex.matches(id)
    fun isValidName(name: String) = nameRegex.matches(name)
}