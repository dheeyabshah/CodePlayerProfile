package org.plugins.codeplayer.toolWindow

import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import org.plugins.codeplayer.services.ContributorProfile
import java.awt.*
import javax.swing.*
import javax.swing.border.LineBorder

class ContributorProfilePanel : SimpleToolWindowPanel(true) {
    private val profilePanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        border = JBUI.Borders.empty(10)  // Add padding around the panel
    }

    init {
        val scrollPane = JBScrollPane(profilePanel).apply {
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        }
        setContent(scrollPane)
    }

    fun updateProfile(profile: ContributorProfile) {
        profilePanel.removeAll()

        profilePanel.add(createSectionLabel("User"))
        profilePanel.add(createContentLabel(profile.name))

        profilePanel.add(createSectionLabel("Commits"))
        profilePanel.add(createContentLabel(profile.commitCount.toString()))

        profilePanel.add(createSectionLabel("Languages"))
        profilePanel.add(createContentLabel(profile.languages.keys.joinToString(", ")))

        profilePanel.add(createSectionLabel("Technologies"))
        profilePanel.add(createContentLabel(profile.technologies.keys.joinToString(", ")))

        profilePanel.add(createSectionLabel("Libraries"))
        profilePanel.add(createContentLabel(profile.libraries.keys.joinToString(", ")))

        profilePanel.revalidate()
        profilePanel.repaint()
    }

    private fun createSectionLabel(text: String): JLabel {
        return JBLabel(text).apply {
            font = Font("Arial", Font.BOLD, 18)
            foreground = JBColor(0x5DADE2, 0x5DADE2)
            border = JBUI.Borders.empty(10, 0, 5, 0)
        }
    }

    private fun createContentLabel(text: String): JLabel {
        return JBLabel(text).apply {
            font = Font("Arial", Font.PLAIN, 16)
            foreground = JBColor(0xffffff, 0xffffff)
            border = LineBorder(JBColor(0xD5D8DC, 0x566573), 1, true)
            background = JBColor(0xF0F3F4, 0x2C3E50)
            isOpaque = true
            border = JBUI.Borders.empty(5)
            maximumSize = Dimension(Int.MAX_VALUE, minimumSize.height)
        }
    }
}
