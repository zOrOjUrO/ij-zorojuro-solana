package com.github.zorojuro.ijzorojurosolana.runconfig

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.RawCommandLineEditor
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.ComponentPredicate
import javax.swing.JComponent

class SolanaRunConfigurationEditor(private val project: Project) : SettingsEditor<SolanaRunConfiguration>() {

    private lateinit var mainPanel: DialogPanel
    private val executableTypes = listOf("Cargo in PATH", "Rust Compiler in PATH", "Custom")

    // Backing properties
    private var executableType: String = "Custom"
    private var customPath: String = ""
    private var args: String = ""

    override fun createEditor(): JComponent {
        mainPanel = panel {
            lateinit var typeComboBox: ComboBox<String>

            row("Executable source:") {
                val comboBoxCell = comboBox(executableTypes)
                    .bindItem({ executableType }, { executableType = it ?: "Custom" })

                // Capture the actual Swing component
                typeComboBox = comboBoxCell.component
            }

            val isCustomSelected = object : ComponentPredicate() {
                override fun invoke(): Boolean = typeComboBox.selectedItem == "Custom"
                override fun addListener(listener: (Boolean) -> Unit) {
                    typeComboBox.addItemListener { listener(invoke()) }
                }
            }

            row("Executable path:") {
                val descriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor()
                    .withTitle("Select Executable")

                textFieldWithBrowseButton(descriptor, project)
                    .bindText({ customPath }, { customPath = it })
                    .columns(30)
                    .enabledIf(isCustomSelected)
            }

            row("Program arguments:") {
                cell(RawCommandLineEditor())
                    .bind(
                        RawCommandLineEditor::getText,
                        RawCommandLineEditor::setText,
                        MutableProperty({ args }, { args = it })
                    )
            }
        }
        return mainPanel
    }

    override fun resetEditorFrom(config: SolanaRunConfiguration) {
        val path = config.executablePath
        when (path) {
            "cargo" -> executableType = "Cargo in PATH"
            "rustc" -> executableType = "Rust Compiler in PATH"
            else -> {
                executableType = "Custom"
                customPath = path ?: ""
            }
        }
        args = config.programArguments ?: ""

        mainPanel.reset()
    }

    override fun applyEditorTo(config: SolanaRunConfiguration) {
        mainPanel.apply()

        config.programArguments = args
        config.executablePath = when (executableType) {
            "Cargo in PATH" -> "cargo"
            "Rust Compiler in PATH" -> "rustc"
            else -> customPath
        }
    }
}