package io.dontsayboj.birthdays.platform

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader

@JsName("encodeURIComponent")
external fun encodeURIComponent(str: String): String

actual class FileHandler {
    
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
}
