package io.dontsayboj.birthdays.platform

expect class FileHandler() {
    fun pickFile(onFileSelected: (String) -> Unit)
    fun downloadFile(content: String, fileName: String)
    fun setupDragAndDrop(
        onDragEnter: () -> Unit,
        onDragLeave: () -> Unit,
        onFileDrop: (String) -> Unit
    )
    fun cleanup()
}
