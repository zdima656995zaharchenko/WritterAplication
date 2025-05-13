
package com.example.writteraplication.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.writteraplication.local.model.PlotEntity
import com.example.writteraplication.local.model.NoteEntity
import com.example.writteraplication.local.model.TimelineEntity
import com.example.writteraplication.local.model.CharacterEntity
import java.io.File
import java.io.FileOutputStream

fun createPdfFromPlots(context: Context, plots: List<PlotEntity>, fileName: String): File {
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    var y = 50
    plots.forEach { plot ->
        canvas.drawText("Назва: ${plot.title}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Опис: ${plot.description}", 10f, y.toFloat(), paint)
        y += 20
        plot.eventDate?.let {
            canvas.drawText("Дата події: $it", 10f, y.toFloat(), paint)
            y += 20
        }
        plot.cause?.let {
            canvas.drawText("Причина: $it", 10f, y.toFloat(), paint)
            y += 20
        }
        plot.consequence?.let {
            canvas.drawText("Наслідок: $it", 10f, y.toFloat(), paint)
            y += 20
        }
        plot.relatedCharacters?.let {
            canvas.drawText("Персонажі: $it", 10f, y.toFloat(), paint)
            y += 40
        }
    }

    pdfDocument.finishPage(page)
    val file = File(context.getExternalFilesDir(null), "$fileName.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()
    return file
}

fun createPdfFromNotes(context: Context, notes: List<NoteEntity>, fileName: String): File {
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    var y = 50
    notes.forEach { note ->
        canvas.drawText("Назва: ${note.title}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Текст: ${note.content}", 10f, y.toFloat(), paint)
        y += 40
    }

    pdfDocument.finishPage(page)

    val file = File(context.getExternalFilesDir(null), "$fileName.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    return file
}

fun createPdfFromTimelines(context: Context, timelines: List<TimelineEntity>, fileName: String): File {
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    var y = 50
    timelines.forEach { timeline ->
        canvas.drawText("Назва: ${timeline.title}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Опис: ${timeline.description}", 10f, y.toFloat(), paint)
        y += 20
        timeline.eventDate?.let {
            canvas.drawText("Дата події: $it", 10f, y.toFloat(), paint)
            y += 20
        }
        canvas.drawText("Персонажі: ${timeline.characters.joinToString()}", 10f, y.toFloat(), paint)
        y += 40
    }

    pdfDocument.finishPage(page)
    val file = File(context.getExternalFilesDir(null), "$fileName.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()
    return file
}

fun createPdfFromCharacters(context: Context, characters: List<CharacterEntity>, fileName: String): File {
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    var y = 50
    characters.forEach { character ->
        canvas.drawText("Name: ${character.name}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Role: ${character.role}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Description: ${character.description}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Age: ${character.age}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Gender: ${character.gender}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Appearance: ${character.appearance}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Personality: ${character.personality}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Abilities: ${character.abilities}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Notes: ${character.notes}", 10f, y.toFloat(), paint)
        y += 40
    }

    pdfDocument.finishPage(page)
    val file = File(context.getExternalFilesDir(null), "$fileName.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()
    return file
}