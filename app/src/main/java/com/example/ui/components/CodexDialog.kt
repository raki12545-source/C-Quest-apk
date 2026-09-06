package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun CodexDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = BentoBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("codex_dialog")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "C Language Codex",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimaryDark
                        )
                        Text(
                            text = "Syntax Rules, Specifiers & Architecture",
                            fontSize = 12.sp,
                            color = BentoTextSecondary
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(BentoSurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = BentoTextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        CodexSection(
                            title = "MEMORY LAYOUT (VIRTUAL MEMORY)",
                            items = listOf(
                                "Stack" to "Stores function frames, local variables, return addresses. Grows downward.",
                                "Heap" to "Dynamic allocation via malloc() and free(). Grows upward towards stack.",
                                "BSS / Data" to "Stores uninitialized (BSS) and initialized global/static variables.",
                                "Text (Code)" to "Read-only compiled machine code instructions."
                            )
                        )
                    }

                    item {
                        CodexSection(
                            title = "POINTER RULES",
                            items = listOf(
                                "&variable" to "Address-of operator: returns hex memory address of variable.",
                                "*pointer" to "Dereference operator: reads or writes value at target address.",
                                "ptr + 1" to "Pointer arithmetic: advances address by sizeof(T) bytes.",
                                "ptr->field" to "Arrow operator: syntactic sugar for (*ptr).field on struct pointers."
                            )
                        )
                    }

                    item {
                        CodexSection(
                            title = "PRINTF FORMAT SPECIFIERS",
                            items = listOf(
                                "%d / %i" to "Signed decimal integer (int)",
                                "%c" to "Single character (char)",
                                "%s" to "Null-terminated string (char*)",
                                "%f / %.2f" to "Floating point decimal (float, double)",
                                "%p" to "Pointer address in hexadecimal (e.g., 0x7ffd1000)",
                                "%x / %X" to "Unsigned hexadecimal integer"
                            )
                        )
                    }

                    item {
                        CodexSection(
                            title = "DYNAMIC MEMORY CHECKLIST",
                            items = listOf(
                                "malloc(bytes)" to "Allocates uninitialized memory on heap. Returns NULL on failure.",
                                "free(ptr)" to "Releases allocated heap memory back to OS.",
                                "ptr = NULL;" to "Cleans dangling pointer after free() to avert undefined behavior."
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CodexSection(
    title: String,
    items: List<Pair<String, String>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BentoSurfaceVariant)
            .border(1.dp, BentoBorderOutline, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BentoPrimary,
            letterSpacing = 0.5.sp
        )

        items.forEach { (token, desc) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = token,
                    fontSize = 11.5.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = BentoPrimaryDark,
                    modifier = Modifier.width(100.dp)
                )
                Text(
                    text = desc,
                    fontSize = 11.5.sp,
                    color = BentoTextPrimary,
                    lineHeight = 16.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
