package io.dontsayboj.birthdays.platform

actual class FileHandler {
    actual fun pickFile(onFileSelected: (String) -> Unit) {
        // JVM implementation not needed for tests
        // This is just a stub to satisfy the expect/actual requirement
    }
    
    actual fun downloadFile(content: String, fileName: String) {
        // JVM implementation not needed for tests
        // This is just a stub to satisfy the expect/actual requirement
    }
}
