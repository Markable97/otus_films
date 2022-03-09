package com.glushko.films.presentation.ui.favorite.decorate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.R


class FavoriteItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.purple_200)
        style = Paint.Style.FILL
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        /*if(parent.getChildAdapterPosition(view) != 0){

        }*/
        outRect.left = 10
        outRect.right = 10
        outRect.bottom = 10
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(view)
            /*if(index == 0){
                continue
            }*/
            val rect = Rect()
            rect.top = 0
            rect.left = 0
            rect.right = view.right + 10
            rect.bottom = view.bottom + 10
            c.drawRect(rect, paint)
        }

    }
}