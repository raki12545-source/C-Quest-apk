package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CodeLine
import com.example.ui.theme.*

@Composable
fun CodeTerminal(
    sourceCode: List<CodeLine>,
    activeLineIndex: Int,
    fileName: String = "main.c",
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(activeLineIndex) {
        if (activeLineIndex in sourceCode.indices) {
            listState.animateScrollToItem(activeLineIndex)
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(BentoConsoleBackground)
            .border(1.dp, Color(0xFF2C2A30), RoundedCornerShape(24.dp))
            .padding(14.dp)
            .testTag("code_terminal")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Terminal Header with colored window traffic dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFF22C55E)))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = fileName,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White.copy(alpha = 0.45f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "ANSI C99",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White.copy(alpha = 0.3f)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.08f))
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Code lines
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                itemsIndexed(sourceCode) { index, line ->
                    val isActive = index == activeLineIndex
                    val lineBgColor by animateColorAsState(
                        targetValue = if (isActive) Color(0xFF383540) else Color.Transparent,
                        label = "lineHighlight"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(lineBgColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Line number
                        Text(
                            text = String.format("%2d", line.lineNumber),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = if (isActive) BentoBorderLight else Color.White.copy(alpha = 0.3f),
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.width(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Syntax highlighted line
                        Text(
                            text = formatCSyntax(line.text),
                            fontSize = 11.5.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.weight(1f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

fun formatCSyntax(code: String): AnnotatedString {
    return buildAnnotatedString {
        val keywords = listOf("int", "char", "float", "double", "void", "return", "if", "else", "while", "for", "switch", "case", "default", "break", "continue", "struct", "typedef", "enum", "sizeof")
        val functions = listOf("printf", "malloc", "free", "scanf", "main", "multiply", "next_state", "evaluate", "process_packet", "update_player_score")

        var i = 0
        while (i < code.length) {
            // Comments
            if (code.startsWith("//", i)) {
                pushStyle(SpanStyle(color = SynComment, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))
                append(code.substring(i))
                pop()
                break
            }

            // String literals
            if (code[i] == '"') {
                val endQuote = code.indexOf('"', i + 1)
                val literal = if (endQuote != -1) code.substring(i, endQuote + 1) else code.substring(i)
                pushStyle(SpanStyle(color = SynString))
                append(literal)
                pop()
                i += literal.length
                continue
            }

            // Char literals
            if (code[i] == '\'') {
                val endQuote = code.indexOf('\'', i + 1)
                val literal = if (endQuote != -1) code.substring(i, endQuote + 1) else code.substring(i)
                pushStyle(SpanStyle(color = SynLiteral))
                append(literal)
                pop()
                i += literal.length
                continue
            }

            // Word token
            if (code[i].isLetter() || code[i] == '_') {
                var j = i
                while (j < code.length && (code[j].isLetterOrDigit() || code[j] == '_')) {
                    j++
                }
                val word = code.substring(i, j)
                when {
                    word in keywords -> {
                        val color = if (word in listOf("int", "char", "float", "double", "void", "struct")) SynType else SynKeyword
                        pushStyle(SpanStyle(color = color, fontWeight = FontWeight.SemiBold))
                        append(word)
                        pop()
                    }
                    word in functions -> {
                        pushStyle(SpanStyle(color = Color(0xFF67E8F9), fontWeight = FontWeight.Medium))
                        append(word)
                        pop()
                    }
                    word == "NULL" -> {
                        pushStyle(SpanStyle(color = SynError, fontWeight = FontWeight.Bold))
                        append(word)
                        pop()
                    }
                    else -> {
                        pushStyle(SpanStyle(color = Color.White))
                        append(word)
                        pop()
                    }
                }
                i = j
                continue
            }

            // Numbers
            if (code[i].isDigit()) {
                var j = i
                while (j < code.length && (code[j].isLetterOrDigit() || code[j] == '.')) {
                    j++
                }
                val num = code.substring(i, j)
                pushStyle(SpanStyle(color = SynLiteral))
                append(num)
                pop()
                i = j
                continue
            }

            // Pointers & Operators
            if (code[i] in listOf('*', '&', '-', '>')) {
                pushStyle(SpanStyle(color = SynPointer, fontWeight = FontWeight.Bold))
                append(code[i].toString())
                pop()
                i++
                continue
            }

            // Other characters
            pushStyle(SpanStyle(color = Color.White.copy(alpha = 0.9f)))
            append(code[i].toString())
            pop()
            i++
        }
    }
}
