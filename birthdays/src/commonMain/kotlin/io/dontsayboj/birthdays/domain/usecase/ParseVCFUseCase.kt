package io.dontsayboj.birthdays.domain.usecase

import io.dontsayboj.birthdays.domain.model.Birthday

class ParseVCFUseCase {
    
    operator fun invoke(vcfContent: String): List<Birthday> {
        val birthdays = mutableListOf<Birthday>()
        
        // Split by VCARD blocks
        val vcardBlocks = vcfContent.split("END:VCARD")
            .filter { it.contains("BEGIN:VCARD") }
        
        for (block in vcardBlocks) {
            val lines = block.lines()
            var name: String? = null
            var birthday: Birthday? = null
            
            for (line in lines) {
                val trimmedLine = line.trim()
                
                // Extract name (FN or N field)
                if (trimmedLine.startsWith("FN:")) {
                    name = trimmedLine.substring(3).trim()
                } else if (trimmedLine.startsWith("N:") && name == null) {
                    // N format: LastName;FirstName;MiddleName;Prefix;Suffix
                    val parts = trimmedLine.substring(2).split(";")
                    name = if (parts.size >= 2) {
                        "${parts[1]} ${parts[0]}".trim()
                    } else {
                        parts[0].trim()
                    }
                }
                
                // Extract birthday (BDAY field)
                // Handle both "BDAY:" and "BDAY;..." (with parameters like Apple's X-APPLE-OMIT-YEAR)
                if (trimmedLine.startsWith("BDAY")) {
                    val colonIndex = trimmedLine.indexOf(':')
                    if (colonIndex != -1) {
                        val bdayValue = trimmedLine.substring(colonIndex + 1).trim()
                        val hasAppleOmitYear = trimmedLine.contains("X-APPLE-OMIT-YEAR")
                        birthday = parseBirthday(bdayValue, hasAppleOmitYear)
                    }
                }
            }
            
            // Add birthday if both name and date are found
            if (name != null && birthday != null) {
                birthdays.add(birthday.copy(name = name))
            }
        }
        
        return birthdays
    }
    
    private fun parseBirthday(value: String, hasAppleOmitYear: Boolean = false): Birthday? {
        return try {
            // Apple format: BDAY;X-APPLE-OMIT-YEAR=1604:1604-10-28
            // The year (1604) should be ignored when X-APPLE-OMIT-YEAR is present
            if (hasAppleOmitYear && value.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                val parts = value.split("-")
                return Birthday(
                    name = "",
                    month = parts[1].toInt(),
                    day = parts[2].toInt(),
                    year = null  // Ignore the year when Apple omit flag is present
                )
            }
            
            // Remove any dashes and colons for easier parsing
            val cleaned = value.replace("-", "").replace(":", "")
            
            when {
                // Format: YYYYMMDD (8 digits)
                cleaned.length == 8 && cleaned.all { it.isDigit() } -> {
                    Birthday(
                        name = "",
                        year = cleaned.take(4).toInt(),
                        month = cleaned.substring(4, 6).toInt(),
                        day = cleaned.substring(6, 8).toInt()
                    )
                }
                // Format: --MMDD (4 digits after removing --)
                cleaned.length == 4 && cleaned.all { it.isDigit() } -> {
                    Birthday(
                        name = "",
                        month = cleaned.take(2).toInt(),
                        day = cleaned.substring(2, 4).toInt(),
                        year = null
                    )
                }
                // Try parsing with original dashes: YYYY-MM-DD
                value.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) -> {
                    val parts = value.split("-")
                    Birthday(
                        name = "",
                        year = parts[0].toInt(),
                        month = parts[1].toInt(),
                        day = parts[2].toInt()
                    )
                }
                // Try parsing without year: --MM-DD
                value.matches(Regex("--\\d{2}-\\d{2}")) -> {
                    val parts = value.substring(2).split("-")
                    Birthday(
                        name = "",
                        month = parts[0].toInt(),
                        day = parts[1].toInt(),
                        year = null
                    )
                }
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }
}
