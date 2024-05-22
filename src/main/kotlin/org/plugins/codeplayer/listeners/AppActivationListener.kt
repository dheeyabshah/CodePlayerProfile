package org.plugins.codeplayer.listeners

import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.wm.IdeFrame
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.ToolWindowManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.plugins.codeplayer.toolWindow.ContributorProfileToolWindowFactory

internal class AppActivationListener : ApplicationActivationListener {

    override fun applicationActivated(ideFrame: IdeFrame) {
        thisLogger().info("Application activated. Refreshing Contributor Profiles tool window.")
        refreshContributorProfilesToolWindow()
    }

    private fun refreshContributorProfilesToolWindow() {
        val project = ProjectManager.getInstance().openProjects.firstOrNull() ?: return
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Contributor Profiles")
        toolWindow?.let {
            if (!it.isAvailable) {
                it.isAvailable = true
            }
            CoroutineScope(Dispatchers.Main).launch {
                val contentManager = it.contentManager
                if (contentManager.contentCount == 0) {
                    ContributorProfileToolWindowFactory().createToolWindowContent(project, it)
                } else {
                    contentManager.removeAllContents(true)
                    ContributorProfileToolWindowFactory().createToolWindowContent(project, it)
                }
            }
        }
    }
}
