package com.github.zorojuro.ijzorojurosolana.runconfig

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.execution.ParametersListUtil
import java.nio.charset.StandardCharsets

class SolanaRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) :
    RunConfigurationBase<SolanaRunConfigurationOptions>(project, factory, name) {

    var executablePath: String?
        get() = options.executablePath
        set(value) { options.executablePath = value }

    var programArguments: String?
        get() = options.programArguments
        set(value) { options.programArguments = value }

    override fun getOptions(): SolanaRunConfigurationOptions {
        return super.getOptions() as SolanaRunConfigurationOptions
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return SolanaRunConfigurationEditor(project)
    }

    @Throws(RuntimeConfigurationException::class)
    override fun checkConfiguration() {
        val path = executablePath
        if (path.isNullOrEmpty()) {
            throw RuntimeConfigurationException("Solana executable path is not specified")
        }
        if (!ExecutableResolver.commandExists(path)) {
            throw RuntimeConfigurationException("Executable not found or invalid: $path")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return object : CommandLineState(environment) {
            override fun startProcess(): ProcessHandler {
                val commandLine = GeneralCommandLine()
                    .withExePath(executablePath!!)
                    .withCharset(StandardCharsets.UTF_8)
                    .withWorkDirectory(project.basePath)

                programArguments?.let { args ->
                     if (args.isNotBlank()) {
                        commandLine.addParameters(ParametersListUtil.parse(args))
                     }
                }

                val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
                ProcessTerminatedListener.attach(processHandler)
                return processHandler
            }
        }
    }
}
