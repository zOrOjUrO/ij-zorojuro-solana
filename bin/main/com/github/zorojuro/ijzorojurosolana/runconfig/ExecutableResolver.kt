package com.github.zorojuro.ijzorojurosolana.runconfig

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import java.io.File

object ExecutableResolver {
    fun resolveSolanaExecutable(): String? {
        val pathEnv = System.getenv("PATH") ?: return null
        val paths = pathEnv.split(File.pathSeparator)
        val executableName = if (System.getProperty("os.name").lowercase().contains("win")) "solana.exe" else "solana"

        for (path in paths) {
            val file = File(path, executableName)
            if (file.exists() && file.canExecute()) {
                return file.absolutePath
            }
        }
        return null
    }

    fun commandExists(command: String): Boolean {
        if (command.isEmpty()) return false
        val file = File(command)
        if (file.isAbsolute && file.exists() && file.canExecute()) {
            return true
        }
        return PathEnvironmentVariableUtil.findInPath(command) != null
    }
}