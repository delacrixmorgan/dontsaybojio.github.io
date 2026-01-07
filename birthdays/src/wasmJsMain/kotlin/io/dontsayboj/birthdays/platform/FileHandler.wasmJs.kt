package io.dontsayboj.birthdays.platform

import kotlinx.browser.document
import org.w3c.dom.DragEvent
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader

@JsName("encodeURIComponent")
external fun encodeURIComponent(str: String): String

actual class FileHandler {
    
    private var dragEnterCallback: (() -> Unit)? = null
    private var dragLeaveCallback: (() -> Unit)? = null
    private var fileDropCallback: ((String) -> Unit)? = null
    private var dragCounter = 0
    
    private val dragEnterHandler: (Event) -> Unit = { event ->
        event.preventDefault()
        dragCounter++
        if (dragCounter == 1) {
            dragEnterCallback?.invoke()
        }
    }
    
    private val dragOverHandler: (Event) -> Unit = { event ->
        event.preventDefault()
    }
    
    private val dragLeaveHandler: (Event) -> Unit = { event ->
        event.preventDefault()
        dragCounter--
        if (dragCounter == 0) {
            dragLeaveCallback?.invoke()
        }
    }
    
    private val dropHandler: (Event) -> Unit = { event ->
        event.preventDefault()
        dragCounter = 0
        dragLeaveCallback?.invoke()
        
        val dragEvent = event as? DragEvent
        val files = dragEvent?.dataTransfer?.files
        
        if (files != null && files.length > 0) {
            val file = files.item(0)
            if (file != null) {
                // Check if it's a VCF file
                val fileName = file.name.lowercase()
                if (fileName.endsWith(".vcf") || file.type == "text/vcard") {
                    val reader = FileReader()
                    reader.onload = {
                        try {
                            val result = reader.result
                            if (result != null) {
                                val text = result.toString()
                                fileDropCallback?.invoke(text)
                            }
                        } catch (e: Exception) {
                            // Silently fail for now
                        }
                        null
                    }
                    reader.onerror = {
                        null
                    }
                    reader.readAsText(file)
                }
            }
        }
    }
    
    actual fun pickFile(onFileSelected: (String) -> Unit) {
        val input = document.createElement("input") as HTMLInputElement
        input.type = "file"
        input.accept = ".vcf,text/vcard"
        
        input.onchange = {
            val files = input.files
            if (files != null && files.length > 0) {
                val file = files.item(0)
                if (file != null) {
                    val reader = FileReader()
                    reader.onload = {
                        try {
                            // In WASM, we need to convert the result properly
                            val result = reader.result
                            if (result != null) {
                                // Convert to string using toString()
                                val text = result.toString()
                                onFileSelected(text)
                            }
                        } catch (e: Exception) {
                            // Silently fail for now
                        }
                        null
                    }
                    reader.onerror = {
                        null
                    }
                    reader.readAsText(file)
                }
            }
            null
        }
        
        input.click()
    }
    
    actual fun downloadFile(content: String, fileName: String) {
        try {
            // Use data URL for download
            val encoded = encodeURIComponent(content)
            val dataUrl = "data:text/calendar;charset=utf-8,$encoded"
            val link = document.createElement("a") as HTMLAnchorElement
            link.href = dataUrl
            link.download = fileName
            
            // Trigger download
            document.body?.appendChild(link)
            link.click()
            
            // Cleanup
            document.body?.removeChild(link)
        } catch (e: Exception) {
            // Silently fail for now
        }
    }
    
    actual fun setupDragAndDrop(
        onDragEnter: () -> Unit,
        onDragLeave: () -> Unit,
        onFileDrop: (String) -> Unit
    ) {
        // Store callbacks
        dragEnterCallback = onDragEnter
        dragLeaveCallback = onDragLeave
        fileDropCallback = onFileDrop
        
        // Reset counter
        dragCounter = 0
        
        // Add event listeners to document body
        document.body?.let { body ->
            body.addEventListener("dragenter", dragEnterHandler)
            body.addEventListener("dragover", dragOverHandler)
            body.addEventListener("dragleave", dragLeaveHandler)
            body.addEventListener("drop", dropHandler)
        }
    }
    
    actual fun cleanup() {
        // Remove event listeners
        document.body?.let { body ->
            body.removeEventListener("dragenter", dragEnterHandler)
            body.removeEventListener("dragover", dragOverHandler)
            body.removeEventListener("dragleave", dragLeaveHandler)
            body.removeEventListener("drop", dropHandler)
        }
        
        // Clear callbacks
        dragEnterCallback = null
        dragLeaveCallback = null
        fileDropCallback = null
        dragCounter = 0
    }
}
