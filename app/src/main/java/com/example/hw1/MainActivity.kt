package com.example.hw1

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val containerLayout = findViewById<ViewGroup>(R.id.containerLayout)
        val columnWidths = mutableListOf<Int>()

        var isLayoutCompleted = false
        containerLayout.viewTreeObserver.addOnGlobalLayoutListener {

            if (!isLayoutCompleted) {
                isLayoutCompleted = true
                val containerHeight = containerLayout.height


                // create some data
                val dataList = mutableListOf<String>()
                for (i in 1..100) {
                    var randomNumber = Random.nextInt(2000000) + 1
                    dataList.add("Item $randomNumber")
                }

                var currentX = 0
                var currentY = 0

                var subLinearLayout = LinearLayout(this)
                subLinearLayout.orientation = LinearLayout.VERTICAL
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                subLinearLayout.layoutParams = layoutParams
                subLinearLayout.setPadding(20, 0, 20, 0)

                for (data in dataList) {
                    Log.e("bear", data)
                    val textView = TextView(this)
                    textView.text = data

                    // 测量文本视图的宽度和高度
                    textView.measure(0, 0)
                    val width = textView.measuredWidth
                    val height = textView.measuredHeight

                    // 检查当前行是否可以容纳新视图
                    Log.e("bear", "currentY + height = ${currentY + height}, containerHeight = $containerHeight")
                    if (currentY + height > containerHeight) {
                        // 切换到下一列
                        currentX += columnWidths.maxOrNull() ?: 0
                        currentY = 0
                        containerLayout.addView(subLinearLayout)
                        subLinearLayout = LinearLayout(this)
                        subLinearLayout.orientation = LinearLayout.VERTICAL
                        subLinearLayout.layoutParams = layoutParams
                        subLinearLayout.setPadding(20, 0, 20, 0)
                        Log.e("bear", "--next column--")
                    }

                    // 将视图放置在正确的位置
//                    textView.x = currentX.toFloat()
//                    textView.y = currentY.toFloat()

                    Log.e("bear", "x = ${textView.x}, y = ${textView.y}")

                    // 更新当前列的最大宽度
                    columnWidths.add(width)

                    // 更新当前y坐标
                    currentY += height

                    // 将视图添加到容器中
                    subLinearLayout.addView(textView)
                }

                containerLayout.addView(subLinearLayout)
            }

        }
    }
}