package io.dontsayboj.birthdays.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ParseVcfUseCaseTest {

    private val useCase = ParseVCFUseCase()

    // ==================== Basic Parsing Tests ====================

    @Test
    fun `Given single vcard with FN and full birthday When parsing Then returns correct birthday data`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:John Doe
            BDAY:19900528
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].name)
        assertEquals(1990, result[0].year)
        assertEquals(5, result[0].month)
        assertEquals(28, result[0].day)
    }

    @Test
    fun `Given single vcard with N field and birthday without year When parsing Then returns correct birthday data`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            N:Doe;Jane;;;
            BDAY:--0612
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals("Jane Doe", result[0].name)
        assertNull(result[0].year)
        assertEquals(6, result[0].month)
        assertEquals(12, result[0].day)
    }

    @Test
    fun `Given vcard with both FN and N fields When parsing Then FN field takes precedence`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            N:Smith;Bob;;;
            FN:Robert Smith
            BDAY:19850315
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals("Robert Smith", result[0].name)
    }

    @Test
    fun `Given multiple vcards When parsing Then returns all parsed birthdays`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Alice Johnson
            BDAY:19920703
            END:VCARD
            BEGIN:VCARD
            VERSION:3.0
            FN:Bob Wilson
            BDAY:--1125
            END:VCARD
            BEGIN:VCARD
            VERSION:3.0
            N:Brown;Charlie;;;
            BDAY:1988-09-10
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(3, result.size)

        assertEquals("Alice Johnson", result[0].name)
        assertEquals(1992, result[0].year)
        assertEquals(7, result[0].month)
        assertEquals(3, result[0].day)

        assertEquals("Bob Wilson", result[1].name)
        assertNull(result[1].year)
        assertEquals(11, result[1].month)
        assertEquals(25, result[1].day)

        assertEquals("Charlie Brown", result[2].name)
        assertEquals(1988, result[2].year)
        assertEquals(9, result[2].month)
        assertEquals(10, result[2].day)
    }

    // ==================== Date Format Tests ====================

    @Test
    fun `Given YYYYMMDD format When parsing Then parses date correctly`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            FN:Test User
            BDAY:19950415
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals(1995, result[0].year)
        assertEquals(4, result[0].month)
        assertEquals(15, result[0].day)
    }

    @Test
    fun `Given MMDD format without dashes When parsing Then parses date without year`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            FN:Test User
            BDAY:--0823
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertNull(result[0].year)
        assertEquals(8, result[0].month)
        assertEquals(23, result[0].day)
    }

    @Test
    fun `Given YYYY-MM-DD format with dashes When parsing Then parses date correctly`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            FN:Test User
            BDAY:1987-12-31
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals(1987, result[0].year)
        assertEquals(12, result[0].month)
        assertEquals(31, result[0].day)
    }

    @Test
    fun `Given MM-DD format with dashes When parsing Then parses date without year`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            FN:Test User
            BDAY:--02-14
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertNull(result[0].year)
        assertEquals(2, result[0].month)
        assertEquals(14, result[0].day)
    }

    // ==================== Apple-specific Tests ====================

    @Test
    fun `Given Apple format with omit year parameter When parsing Then year is null`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Apple User
            BDAY;X-APPLE-OMIT-YEAR=1604:1604-10-28
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals("Apple User", result[0].name)
        assertNull(result[0].year, "Year should be null when X-APPLE-OMIT-YEAR is present")
        assertEquals(10, result[0].month)
        assertEquals(28, result[0].day)
    }

    @Test
    fun `Given BDAY with parameters but without omit year When parsing Then year is parsed normally`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Standard User
            BDAY;VALUE=date:1990-05-20
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals(1990, result[0].year, "Year should be parsed normally without X-APPLE-OMIT-YEAR")
        assertEquals(5, result[0].month)
        assertEquals(20, result[0].day)
    }

    // ==================== Edge Cases Tests ====================

    @Test
    fun `Given empty vcf content When parsing Then returns empty list`() {
        // Given
        val vcf = ""

        // When
        val result = useCase(vcf)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `Given vcf without vcard blocks When parsing Then returns empty list`() {
        // Given
        val vcf = "Some random text without VCARD blocks"

        // When
        val result = useCase(vcf)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `Given vcard with missing name When parsing Then is not included`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            BDAY:19900528
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertTrue(result.isEmpty(), "VCard without name should not be included")
    }

    @Test
    fun `Given vcard with missing birthday When parsing Then is not included`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:John Doe
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertTrue(result.isEmpty(), "VCard without birthday should not be included")
    }

    @Test
    fun `Given vcard with invalid birthday format When parsing Then is not included`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:John Doe
            BDAY:invalid-date
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertTrue(result.isEmpty(), "VCard with invalid birthday should not be included")
    }

    @Test
    fun `Given vcard with empty birthday value When parsing Then is not included`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:John Doe
            BDAY:
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertTrue(result.isEmpty(), "VCard with empty birthday should not be included")
    }

    @Test
    fun `Given malformed vcard blocks When parsing Then valid ones are parsed gracefully`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            FN:Valid User
            BDAY:19900101
            END:VCARD
            BEGIN:VCARD
            This is malformed
            END:VCARD
            BEGIN:VCARD
            FN:Another Valid User
            BDAY:--0505
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(2, result.size, "Should parse valid vcards and skip malformed ones")
        assertEquals("Valid User", result[0].name)
        assertEquals("Another Valid User", result[1].name)
    }

    @Test
    fun `Given N field with only last name When parsing Then uses last name only`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            N:SingleName;;;;
            BDAY:19900101
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals("SingleName", result[0].name)
    }

    @Test
    fun `Given whitespace in name and date fields When parsing Then handles whitespace correctly`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:  Whitespace  User  
            BDAY:  19900101  
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals("Whitespace  User", result[0].name)
        assertEquals(1990, result[0].year)
        assertEquals(1, result[0].month)
        assertEquals(1, result[0].day)
    }

    @Test
    fun `Given multiple vcards with mixed valid and invalid entries When parsing Then only valid ones are included`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            FN:Valid User 1
            BDAY:19900101
            END:VCARD
            BEGIN:VCARD
            FN:No Birthday
            END:VCARD
            BEGIN:VCARD
            BDAY:19900202
            END:VCARD
            BEGIN:VCARD
            FN:Valid User 2
            BDAY:--0303
            END:VCARD
            BEGIN:VCARD
            FN:Invalid Birthday
            BDAY:not-a-date
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(2, result.size, "Should only include vcards with both valid name and birthday")
        assertEquals("Valid User 1", result[0].name)
        assertEquals("Valid User 2", result[1].name)
    }

    // ==================== Real-world Format Tests ====================

    @Test
    fun `Given realistic vcard from contacts app When parsing Then parses correctly`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            PRODID:-//Apple Inc.//iPhone OS 16.0//EN
            N:Doe;John;Middle;Mr.;Jr.
            FN:Mr. John Middle Doe Jr.
            ORG:Company Inc.;
            TITLE:Software Engineer
            EMAIL;type=INTERNET;type=WORK;type=pref:john@example.com
            TEL;type=CELL;type=VOICE;type=pref:+1234567890
            BDAY;X-APPLE-OMIT-YEAR=1604:1604-07-15
            item1.ADR;type=HOME:;;123 Main St;City;State;12345;Country
            item1.X-ABADR:us
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals("Mr. John Middle Doe Jr.", result[0].name)
        assertNull(result[0].year, "Year should be omitted for Apple format")
        assertEquals(7, result[0].month)
        assertEquals(15, result[0].day)
    }

    @Test
    fun `Given vcard with long name When parsing Then handles it correctly`() {
        // Given
        val vcf = """
            BEGIN:VCARD
            VERSION:3.0
            FN:User With Long Name
            BDAY:1990-06-15
            END:VCARD
        """.trimIndent()

        // When
        val result = useCase(vcf)

        // Then
        assertEquals(1, result.size)
        assertEquals("User With Long Name", result[0].name)
    }
}
