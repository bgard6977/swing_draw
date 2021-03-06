package net.squarelabs

import net.squarelabs.listeners.WidgetListener
import net.squarelabs.widgets.Widget
import java.awt.*
import java.awt.event.*
import javax.swing.JFrame
import java.awt.image.BufferStrategy



class DrawingWindow(var root: Widget) : JFrame(), MouseListener, MouseMotionListener, KeyListener, ComponentListener {

    init {
        addComponentListener(this)
        addMouseListener(this)
        addMouseMotionListener(this)
        addKeyListener(this)
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(400, 400)
        isVisible = true
        createBufferStrategy(2)
        root.addListener(object : WidgetListener {
            override fun invalidated(region: Rect) {
                // TODO: invalidation rectangle logic
                repaint()
            }
        })
    }

    fun getTop(): Int {
        return height - contentPane.height
    }

    override fun paint(graphics: Graphics?) {
        if(bufferStrategy == null) {
            repaint()
            return
        }
        val g = bufferStrategy!!.drawGraphics as Graphics2D
        try {
            g.translate(0, getTop()) // don't draw behind OS title bar
            g.translate(root.bounds.origin.x, root.bounds.origin.y)
            g.clipRect(0, 0, root.bounds.size.x, root.bounds.size.y)
            root.paint(g, width, height)
            bufferStrategy.show()
        } finally {
            g.dispose()
        }
    }

    override fun componentResized(me: ComponentEvent?) {
        println("componentResized")
        val origin = Point(0, 0)
        val size = Point(contentPane.width, contentPane.height)
        root.layout(Rect(origin, size))
    }

    override fun mouseEntered(p0: MouseEvent?) {
        println("mouseEntered")
    }

    override fun mouseClicked(p0: MouseEvent?) {
        println("mouseClicked")
    }

    override fun mouseExited(p0: MouseEvent?) {
        println("mouseExited")
    }

    override fun mousePressed(event: MouseEvent?) {
        root.mousePressed(Point(event!!.x, event.y - getTop()))
    }

    override fun mouseMoved(event: MouseEvent?) {
        root.mouseMoved(Point(event!!.x, event.y - getTop()))
    }

    override fun mouseDragged(event: MouseEvent?) {
        root.mouseMoved(Point(event!!.x, event.y - getTop()))
    }

    override fun mouseReleased(event: MouseEvent?) {
        root.mouseReleased(Point(event!!.x, event.y - getTop()))
    }

    override fun keyTyped(p0: KeyEvent?) {
        println("keyTyped")
    }

    override fun keyPressed(p0: KeyEvent?) {
        println("keyPressed")
    }

    override fun keyReleased(p0: KeyEvent?) {
        println("keyReleased")
    }

    override fun componentMoved(p0: ComponentEvent?) {
        println("componentMoved")
    }

    override fun componentHidden(p0: ComponentEvent?) {
        println("componentHidden")
    }

    override fun componentShown(p0: ComponentEvent?) {
        println("componentShown")
    }
}
