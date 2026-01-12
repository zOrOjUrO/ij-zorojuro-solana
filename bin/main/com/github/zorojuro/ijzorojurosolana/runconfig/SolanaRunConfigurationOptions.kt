package com.github.zorojuro.ijzorojurosolana.runconfig

import com.intellij.execution.configurations.RunConfigurationOptions
import com.intellij.openapi.components.StoredProperty

class SolanaRunConfigurationOptions: RunConfigurationOptions(){
    private val myExecutablePath: StoredProperty<String?> = string(null).provideDelegate(this, "executablePath")
    private val myProgramArguments: StoredProperty<String?> = string(null).provideDelegate(this, "programArguments")

    var executablePath: String?
        get() = myExecutablePath.getValue(this)
        set(value) { myExecutablePath.setValue(this, value) }

    var programArguments: String?
        get() = myProgramArguments.getValue(this)
        set(value) { myProgramArguments.setValue(this, value) }
}
