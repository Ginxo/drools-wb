/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.screens.scenariosimulation.client.commands.actualcommands;

import javax.enterprise.context.Dependent;

import org.drools.workbench.screens.scenariosimulation.client.commands.ScenarioSimulationContext;
import org.drools.workbench.screens.scenariosimulation.client.enums.GridWidget;

@Dependent
public class ImportCommand extends AbstractScenarioGridCommand {

    protected String fileContent;

    public ImportCommand(GridWidget gridWidget, String fileContent) {
        super(gridWidget);
        this.fileContent = fileContent;
    }

    private ImportCommand() {
        // CDI
    }

    @Override
    protected void internalExecute(ScenarioSimulationContext context) {
        context.getScenarioSimulationEditorPresenter().onImport(fileContent, gridWidget);
    }
}
