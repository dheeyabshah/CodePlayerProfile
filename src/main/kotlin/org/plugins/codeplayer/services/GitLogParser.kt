package org.plugins.codeplayer.services

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import git4idea.repo.GitRepository
import git4idea.history.GitHistoryUtils
import git4idea.commands.Git
import git4idea.commands.GitCommand
import git4idea.commands.GitLineHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ContributorProfile(
    val name: String,
    var commitCount: Int,
    val commitHashes: MutableList<String> = mutableListOf(),
    val languages: MutableMap<String, Int> = mutableMapOf(),
    val technologies: MutableMap<String, Int> = mutableMapOf(),
    val libraries: MutableMap<String, Int> = mutableMapOf()

)

suspend fun parseGitLog(project: Project, repository: GitRepository): Map<String, ContributorProfile> =
    withContext(Dispatchers.IO) {

        val commits = GitHistoryUtils.history(project, repository.root)
        val contributors = mutableMapOf<String, ContributorProfile>()

        for (commit in commits) {
            val name = commit.author

            val profile = contributors.getOrPut(name.toString()) {
                ContributorProfile(
                    name = name.toString(),
                    commitCount = 0
                )
            }

            profile.commitCount++
            profile.commitHashes.add(commit.id.asString())

            val langs = Extensions().languages
            val techs = Extensions().technologies
            profile.commitHashes.forEach { commitHash ->
                val changes = getDiffsForCommit(project, repository, commitHash)
                changes.forEach { change ->
                    if (change.startsWith("diff")) {
                        langs.forEach { (k, v) ->
                            if (change.contains("$k ")) {
                                profile.languages[v] = (profile.languages[v] ?: 0) + 1
                            }
                        }

                        techs.forEach { (k, v) ->
                            if (change.contains("$k ")) {
                                profile.technologies[v] = (profile.technologies[v] ?: 0) + 1
                            }
                        }
                    }
                    val lines = change.split("\n")
                    lines.forEach { line ->
                        if (line.startsWith("+++")) {
                            return@forEach
                        }
                        if (line.trim().startsWith("+import ")) {
                            val library = line.removePrefix("+import ").trim().substringBeforeLast('.')
                            profile.libraries[library] = (profile.libraries[library] ?: 0) + 1
                        }
                    }
                }
            }
        }

        return@withContext contributors
    }


suspend fun getDiffsForCommit(project: Project, repository: GitRepository, commitHash: String): List<String> {
    val git = ApplicationManager.getApplication().getService(Git::class.java)

    val handler = GitLineHandler(project, repository.root, GitCommand.SHOW)
    handler.addParameters(commitHash)

    return withContext(Dispatchers.IO) {
        val result = git.runCommand(handler)
        if (result.success()) {
            val rawOutput = result.outputAsJoinedString
            rawOutput.split("\n")
        } else {
            println("Failed to get diff for commit $commitHash: ${result.errorOutputAsJoinedString}")
            emptyList()
        }
    }
}
