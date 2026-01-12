package com.github.zorojuro.ijzorojurosolana.runconfig

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class SolanaRunConfigurationFactory(type: SolanaRunConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "SolanaRunConfigurationFactory"

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return SolanaRunConfiguration(project, this, "Solana")
    }
}