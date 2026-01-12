package com.github.zorojuro.ijzorojurosolana.runconfig

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import org.jetbrains.annotations.Nls
import javax.swing.Icon
class SolanaRunConfigurationType: ConfigurationType {
    override fun getDisplayName(): String = "ZJ Solana"
    override fun getConfigurationTypeDescription(): String =
        "Run Solana tools like Anchor, Cargo, or the Solana Validator"
    override fun getIcon(): Icon = AllIcons.General.RunWithCoverage
    override fun getId(): String = "SolanaRunConfiguration"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(SolanaRunConfigurationFactory(this))
    }
}