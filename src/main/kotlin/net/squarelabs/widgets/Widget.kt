package net.squarelabs.widgets

import net.squarelabs.Rect
import java.awt.Graphics2D
import java.awt.Point
import java.awt.event.MouseEvent

interface Widget {
    // children
    var children: MutableList<Widget>

    // coordinate system
    var bounds: Rect

    // layout
    fun layout(rect: Rect) {
        bounds = rect
    }

    fun getChildBounds(): Rect {
        return children.fold(Rect.MIN) { acc, cur ->
            Rect.union(
                acc,
                cur.bounds
            )             
        }
    }

    fun paintChild(graphics: Graphics2D, width: Int, height: Int, child: Widget) {
        val prevClip = graphics.clip
        graphics.translate(child.bounds.origin.x, child.bounds.origin.y)
        graphics.clipRect(0, 0, child.bounds.size.x, child.bounds.size.y)

        val w = Math.min(width, child.bounds.size.x)
        val h = Math.min(height, child.bounds.size.y)
        child.paint(graphics, w, h)

        graphics.translate(-child.bounds.origin.x, -child.bounds.origin.y)
        graphics.clip = prevClip
    }

    // painting
    fun paint(graphics: Graphics2D, width: Int, height: Int) {
        children.forEach { paintChild(graphics, width, height, it) }
    }

    // events
    fun mousePressed(position: Point) {
        children.forEach { child ->
            val bounds = child.bounds
            if(bounds.contains(position)) {
                child.mousePressed(bounds.toLocal(position))
            }
        }
    }

    fun mouseMoved(position: Point) {
        children.forEach { child ->
            val bounds = child.bounds
            if(bounds.contains(position)) {
                child.mouseMoved(bounds.toLocal(position))
            }
        }
    }

    fun mouseReleased(position: Point) {
        children.forEach { child ->
            val bounds = child.bounds
            if(bounds.contains(position)) {
                child.mouseReleased(bounds.toLocal(position))
            }
        }
    }
}