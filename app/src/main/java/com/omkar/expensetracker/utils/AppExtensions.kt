package com.omkar.expensetracker.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.omkar.expensetracker.expensereport.CategoryTotal
import com.omkar.expensetracker.expensereport.DailyTotal
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.collections.forEach


fun String.toFormattedDate(): String {
    val localDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE) // parses yyyy-MM-dd
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
    return localDate.format(formatter)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long.toStartOfDayMillis(): Long {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.timeInMillis = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun Long.toFormattedDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}

fun Context.generateExpenseReportPdfVisual(
    dailyTotals: List<DailyTotal>,
    categoryTotals: List<CategoryTotal>
): File {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.textSize = 18f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    var y = 40f

    // Title
    canvas.drawText("Expense Report", 40f, y, paint)
    y += 30f

    // Daily Totals Section
    paint.textSize = 16f
    paint.typeface = Typeface.DEFAULT
    canvas.drawText("Daily Totals (Last 7 days)", 40f, y, paint)
    y += 20f

    val chartHeight = 200f
    val chartWidth = 400f
    val startX = 40f
    val startY = y + chartHeight

    // Draw Bar Chart
    val maxAmount = dailyTotals.maxOfOrNull { it.total } ?: 1.0
    val barWidth = chartWidth / dailyTotals.size
    val barPaint = Paint().apply { color = Color.parseColor("#243c5a") }

    dailyTotals.forEachIndexed { index, daily ->
        val barHeight = (daily.total / maxAmount * chartHeight).toFloat()
        val left = startX + index * barWidth
        val top = startY - barHeight
        val right = left + barWidth * 0.6f
        val bottom = startY
        canvas.drawRect(left, top, right, bottom, barPaint)

        // Draw day label
        paint.textSize = 12f
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(daily.dayOfWeek, left + barWidth * 0.3f, startY + 15f, paint)
    }

    y = startY + 50f

    // Category-wise Totals
    paint.textAlign = Paint.Align.LEFT  // Important!
    paint.textSize = 16f
    paint.typeface = Typeface.DEFAULT
    paint.color = Color.BLACK
    canvas.drawText("Category-wise Totals", 40f, y, paint)
    y += 20f

    val rectPaint = Paint().apply {
        color = Color.parseColor("#e0e0e0")
        style = Paint.Style.FILL
    }
    val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 14f
    }

    categoryTotals.forEach { cat ->
        // Draw rounded rectangle background
        val rect = RectF(40f, y, 555f, y + 40f)
        canvas.drawRoundRect(rect, 8f, 8f, rectPaint)

        // Draw category name and amount
        canvas.drawText(cat.category, 50f, y + 25f, textPaint)
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("₹${cat.total}", 540f, y + 25f, textPaint)
        textPaint.textAlign = Paint.Align.LEFT
        y += 50f
    }

    pdfDocument.finishPage(page)

    // Save file
    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val fileName = "ExpenseReport_${sdf.format(Date())}.pdf"
    val file = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    return file
}
