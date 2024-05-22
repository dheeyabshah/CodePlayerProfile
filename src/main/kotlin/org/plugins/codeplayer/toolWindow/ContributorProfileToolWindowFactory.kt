package org.plugins.codeplayer.toolWindow

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.JBUI
import git4idea.repo.GitRepositoryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.plugins.codeplayer.services.ContributorProfile
import org.plugins.codeplayer.services.parseGitLog
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.*


class ContributorProfileToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val profilePanel = ContributorProfilePanel()
        val contributorList = JBList<ContributorProfile>()

        contributorList.cellRenderer =
            ListCellRenderer { list, value, _, isSelected, _ ->
                val panel = JPanel(BorderLayout())
                panel.border = JBUI.Borders.empty(5)
                panel.background = if (isSelected) list.selectionBackground else list.background

                val iconLabel = JLabel(AllIcons.General.User)  // Replace with a suitable icon
                val nameLabel = JBLabel(" ${value.name}").apply {
                    font = Font("Arial", Font.BOLD, 14)
                    foreground = if (isSelected) list.selectionForeground else JBColor.BLACK
                }


                panel.add(iconLabel, BorderLayout.WEST)
                panel.add(nameLabel, BorderLayout.CENTER)

                panel.toolTipText = "<html><b>Name:</b> ${value.name}</html>"

                panel
            }

        val contributorListPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(JScrollPane(contributorList))
        }

        val mainPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(contributorListPanel)
            add(profilePanel)
        }

        CoroutineScope(Dispatchers.Main).launch {
            val repository = GitRepositoryManager.getInstance(project).repositories.firstOrNull()
            if (repository != null) {
                val profiles = parseGitLog(project, repository).values.toList()
                contributorList.setListData(profiles.toTypedArray())
            }
        }

        contributorList.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                val selectedProfile = contributorList.selectedValue
                if (selectedProfile != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val repository = GitRepositoryManager.getInstance(project).repositories.firstOrNull()
                        if (repository != null) {
                            parseGitLog(project, repository)
                            profilePanel.updateProfile(selectedProfile)
                        }
                    }
                }
            }
        }

        val content = contentFactory.createContent(mainPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
