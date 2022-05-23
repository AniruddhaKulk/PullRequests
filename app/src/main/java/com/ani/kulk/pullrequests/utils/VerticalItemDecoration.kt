package com.ani.kulk.pullrequests.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

open class VerticalDividerDecoration(private val divider: Drawable,
                                     private val drawBeforeFirstItem: Boolean = true,
                                     private val drawAfterLastItem: Boolean = false) : RecyclerView.ItemDecoration() {

    init {
        divider.mutate()
    }

    private val bounds = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) return
        drawVertical(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (i == childCount - 1 && !drawAfterLastItem) continue
            val child = parent.getChildAt(i)
            if (!shouldDrawDivider(parent, child, i)) continue
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom: Int = bounds.bottom + child.translationY.roundToInt()
            val top: Int = bottom - divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
            if (i == 0 && drawBeforeFirstItem) {
                val firstItemDividerTop = bounds.top + child.translationY.roundToInt()
                val firstItemDividerBottom = firstItemDividerTop + divider.intrinsicHeight
                divider.setBounds(left, firstItemDividerTop, right, firstItemDividerBottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val isFirstItem = isFirstItem(parent, view)
        val isLastItem = isLastItemItem(parent, view)
        if (isFirstItem) {
            if (drawBeforeFirstItem) {
                outRect.set(0, divider.intrinsicHeight, 0, divider.intrinsicHeight)
            } else {
                outRect.set(0, 0, 0, 0)
            }
        } else if (isLastItem) {
            if (drawAfterLastItem) {
                outRect.set(0, 0, 0, divider.intrinsicHeight)
            } else {
                outRect.set(0, 0, 0, 0)
            }
        } else {
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        }
    }

    private fun shouldDrawDivider(recyclerView: RecyclerView, item: View, itemPosition: Int): Boolean = true

    private fun isFirstItem(recyclerView: RecyclerView, item: View): Boolean =
        recyclerView.getChildAdapterPosition(item) == 0

    private fun isLastItemItem(recyclerView: RecyclerView, item: View): Boolean =
        recyclerView.getChildAdapterPosition(item) == recyclerView.adapter!!.itemCount - 1
}