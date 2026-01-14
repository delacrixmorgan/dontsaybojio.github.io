package io.dontsayboj.birthdays.domain.model

data class Birthday(
    val name: String,
    val month: Int,
    val day: Int,
    val year: Int? = null
) {
    fun hasYear(): Boolean = year != null
    
    fun calculateAge(currentYear: Int): Int? {
        return year?.let { currentYear - it }
    }
}
