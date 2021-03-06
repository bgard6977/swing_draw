package net.squarelabs.widgets

import net.squarelabs.GraphUtils
import net.squarelabs.Rect
import net.squarelabs.listeners.ScalarListener
import net.squarelabs.listeners.WidgetListener
import net.squarelabs.sources.ScalarSource
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point

class HScroll(val dataSource: ScalarSource) : Widget {
    override var listeners: MutableList<WidgetListener>
        get() = widget.listeners
        set(value) { widget.listeners = value }

    override var children: MutableList<Widget>
        get() = widget.children
        set(value) { widget.children = value }

    override var bounds: Rect
        get() = widget.bounds
        set(value) { widget.bounds = value }

    private val scrollBarWidth = 15
    private val widget = WidgetImpl()
    private val scrollListeners = mutableListOf<ScalarListener>()
    private var downPos: Double? = null

    override fun layout(rect: Rect) {
        val origin = Point(0, rect.size.y - scrollBarWidth - 1)
        val size = Point(rect.size.x - scrollBarWidth, scrollBarWidth)
        widget.bounds = Rect(origin, size)
    }

    override fun paint(graphics: Graphics2D, width: Int, height: Int) {
        super.paint(graphics, width, height)

        val barOrigin = Point(0, 0)
        val barSize = Point(bounds.size.x, scrollBarWidth)
        val sliderOrigin = Point(sliderLeft, barOrigin.y + 1)
        val sliderSize = Point(sliderWidth, scrollBarWidth - 2)
        GraphUtils.drawEmbossedRect(graphics, Rect(barOrigin, barSize), true, Color.DARK_GRAY)
        GraphUtils.drawEmbossedRect(graphics, Rect(sliderOrigin, sliderSize), false, Color.GRAY)
    }

    val dataRange: Double
        get() = dataSource.getMax() - dataSource.getMin()

    val sliderWidth: Int
        get() = ((bounds.size.x - 2) * (bounds.size.x / dataRange)).toInt()

    val sliderRange: Int
        get() = bounds.size.x - sliderWidth

    val sliderLeft: Int
        get() = (dataSource.getValue() / dataRange * sliderRange).toInt()

    fun addScrollListener(listener: ScalarListener) {
        scrollListeners.add(listener)
    }

    override fun mousePressed(position: Point) {
        downPos = (position.x - sliderLeft).toDouble() / sliderWidth
    }

    override fun mouseMoved(position: Point) {
        if (downPos == null) return
        val newLeft = position.x - sliderWidth * downPos!!
        val newVal = (newLeft / sliderRange) * dataRange + dataSource.getMin()
        val clamped = Math.min(Math.max(newVal, dataSource.getMin()), dataSource.getMax())
        scrollListeners.forEach { it.onChange(clamped) }
    }

    override fun mouseReleased(position: Point) {
        downPos = null
    }
}
