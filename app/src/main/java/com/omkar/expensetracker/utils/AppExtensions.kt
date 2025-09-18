package com.omkar.expensetracker.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.omkar.expensetracker.R
import com.omkar.expensetracker.expensereport.CategoryTotal
import com.omkar.expensetracker.expensereport.DailyTotal
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.collections.forEach


fun String.toFormattedDate(): String {
    val localDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    return localDate.format(DateUtils.MMM_dd_yyyy_FORMATTER)
}

fun Context.showToast(messageId: Int) {
    Toast.makeText(this, this.getString(messageId), Toast.LENGTH_SHORT).show()
}

fun Long.toFormattedDateString(): String {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
        .format(DateUtils.YYYY_MM_DD_FORMATTER)
}

fun Context.generateExpenseReportPdfVisual(
    dailyTotals: List<DailyTotal>, categoryTotals: List<CategoryTotal>
): File {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.textSize = 18f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    var y = 40f

    canvas.drawText(this.getString(R.string.expense_report_title), 40f, y, paint)
    y += 30f

    paint.textSize = 16f
    paint.typeface = Typeface.DEFAULT
    canvas.drawText(this.getString(R.string.daily_totals_title), 40f, y, paint)
    y += 20f

    val chartHeight = 200f
    val chartWidth = 400f
    val startX = 40f
    val startY = y + chartHeight

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

        paint.textSize = 12f
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(daily.dayOfWeek, left + barWidth * 0.3f, startY + 15f, paint)
    }

    y = startY + 50f

    paint.textAlign = Paint.Align.LEFT  // Important!
    paint.textSize = 16f
    paint.typeface = Typeface.DEFAULT
    paint.color = Color.BLACK
    canvas.drawText(this.getString(R.string.category_wise_totals_title), 40f, y, paint)
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
        val rect = RectF(40f, y, 555f, y + 40f)
        canvas.drawRoundRect(rect, 8f, 8f, rectPaint)

        canvas.drawText(cat.category, 50f, y + 25f, textPaint)
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("₹${cat.total}", 540f, y + 25f, textPaint)
        textPaint.textAlign = Paint.Align.LEFT
        y += 50f
    }

    pdfDocument.finishPage(page)

    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val fileName = "ExpenseReport_${sdf.format(Date())}.pdf"
    val file = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    return file
}
