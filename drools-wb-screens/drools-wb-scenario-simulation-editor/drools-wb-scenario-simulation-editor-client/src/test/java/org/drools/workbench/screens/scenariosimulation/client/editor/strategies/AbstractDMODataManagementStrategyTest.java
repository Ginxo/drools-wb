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

package org.drools.workbench.screens.scenariosimulation.client.editor.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.screens.scenariosimulation.client.TestProperties;
import org.drools.workbench.screens.scenariosimulation.client.commands.ScenarioSimulationContext;
import org.drools.workbench.screens.scenariosimulation.client.editor.AbstractScenarioSimulationEditorTest;
import org.drools.workbench.screens.scenariosimulation.client.enums.GridWidget;
import org.drools.workbench.screens.scenariosimulation.client.rightpanel.TestToolsView;
import org.drools.workbench.screens.scenariosimulation.model.ScenarioSimulationModelContent;
import org.drools.workbench.screens.scenariosimulation.model.typedescriptor.FactModelTree;
import org.drools.workbench.screens.scenariosimulation.model.typedescriptor.FactModelTuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.client.callbacks.Callback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(GwtMockitoTestRunner.class)
public class AbstractDMODataManagementStrategyTest extends AbstractScenarioSimulationEditorTest {


    private final static String PARAMETRIC_FIELD_TYPE = "ParametricFieldType";
    private final static String FQCN_BY_FACTNAME = "FQCNByFactName";
    private AbstractDMODataManagementStrategy abstractDMODataManagementStrategySpy;
    private AbstractDMODataManagementStrategy.ResultHolder factModelTreeHolderlocal;

    private FactModelTuple factModelTupleLocal;
    private SortedMap<String, FactModelTree> visibleFactsLocal;
    private SortedMap<String, FactModelTree> hiddenFactsLocal;
    private List<String> factTypes;

    @Before
    public void setup() {
        super.setup();
        factTypes = new ArrayList<>();
        visibleFactsLocal = new TreeMap<>();
        hiddenFactsLocal = new TreeMap<>();
        factModelTupleLocal = new FactModelTuple(visibleFactsLocal, hiddenFactsLocal);
        factModelTreeHolderlocal = new AbstractDataManagementStrategy.ResultHolder();
        factModelTreeHolderlocal.setFactModelTuple(factModelTupleLocal);
        abstractDMODataManagementStrategySpy = spy(new AbstractDMODataManagementStrategy() {

            @Override
            public void manageScenarioSimulationModelContent(ObservablePath currentPath, ScenarioSimulationModelContent toManage) {

            }

            @Override
            public boolean isADataType(String value) {
                return false;
            }

            @Override
            protected String getFQCNByFactName(String factName) {
                return FQCN_BY_FACTNAME;
            }

            @Override
            protected String getParametricFieldType(String factName, String propertyName) {
                return PARAMETRIC_FIELD_TYPE;
            }

            @Override
            protected List<String> getFactTypes() {
                return factTypes;
            }

            @Override
            protected void getSuperType(String factType, Callback<String> callback) {
                // Do Nothing
            }

            @Override
            protected boolean skipPopulateTestTools() {
                return false;
            }

            @Override
            protected void manageDataObjects(List<String> dataObjectsTypes, Map<String, String> superTypeMap, TestToolsView.Presenter testToolsPresenter, int expectedElements, SortedMap<String, FactModelTree> dataObjectsFieldsMap, ScenarioSimulationContext context, List<String> simpleJavaTypes, GridWidget gridWidget) {

            }

            {
                this.model = modelLocal;
                this.factModelTreeHolder = factModelTreeHolderlocal;
            }


        });
    }

    @Test
    public void populateTestToolsWithoutFactModelTuple() {
        factModelTreeHolderlocal.setFactModelTuple(null);
        abstractDMODataManagementStrategySpy.populateTestTools(testToolsPresenterMock, scenarioSimulationContextLocal, GridWidget.SIMULATION);
        verify(abstractDMODataManagementStrategySpy, never()).storeData(eq(factModelTupleLocal), eq(testToolsPresenterMock), eq(scenarioSimulationContextLocal), eq(GridWidget.SIMULATION));
        verify(abstractDMODataManagementStrategySpy, times(1)).aggregatorCallbackMethod(eq(testToolsPresenterMock), eq(0), isA(SortedMap.class), eq(scenarioSimulationContextLocal), eq(null), isA(List.class), eq(GridWidget.SIMULATION));
        verify(abstractDMODataManagementStrategySpy, never()).loadSuperTypes(any(), any(), anyInt(), any(), any(), any(), any(), any());
    }

    @Test
    public void populateTestToolsWithoutFactModelTupleWithFactTypes() {
        factTypes.add(TestProperties.CLASS_NAME);
        factModelTreeHolderlocal.setFactModelTuple(null);
        abstractDMODataManagementStrategySpy.populateTestTools(testToolsPresenterMock, scenarioSimulationContextLocal, GridWidget.SIMULATION);
        verify(abstractDMODataManagementStrategySpy, never()).storeData(eq(factModelTupleLocal), eq(testToolsPresenterMock), eq(scenarioSimulationContextLocal), eq(GridWidget.SIMULATION));
        verify(abstractDMODataManagementStrategySpy, never()).aggregatorCallbackMethod(any(), anyInt(), any(), any(), any(), any(), any());
        verify(abstractDMODataManagementStrategySpy, times(1)).loadSuperTypes(isA(List.class), eq(testToolsPresenterMock), eq(1), isA(SortedMap.class), isA(Map.class), eq(scenarioSimulationContextLocal), isA(List.class), eq(GridWidget.SIMULATION));
    }

    @Test
    public void populateTestToolsWithFactModelTuple() {
        abstractDMODataManagementStrategySpy.populateTestTools(testToolsPresenterMock, scenarioSimulationContextLocal, GridWidget.SIMULATION);
        verify(abstractDMODataManagementStrategySpy, times(1)).storeData(eq(factModelTupleLocal), eq(testToolsPresenterMock), eq(scenarioSimulationContextLocal), eq(GridWidget.SIMULATION));
        verify(abstractDMODataManagementStrategySpy, never()).loadSuperTypes(any(), any(), anyInt(), any(), any(), any(), any(), any());
        verify(abstractDMODataManagementStrategySpy, never()).aggregatorCallbackMethod(any(), anyInt(), any(), any(), any(), any(), any());
    }

    @Test
    public void loadSuperTypes() {
        String factType = "factType";
        int expectedElement = 1;
        List<String> dataObjectsType = Arrays.asList(factType);
        SortedMap<String, FactModelTree> dataObjectsFieldMap = new TreeMap<>();
        Map<String, String> superTypesMap = new HashMap<>();
        List<String> javaSimpleType = new ArrayList<>();
        abstractDMODataManagementStrategySpy.loadSuperTypes(dataObjectsType, testToolsPresenterMock, expectedElement, dataObjectsFieldMap, superTypesMap, scenarioSimulationContextLocal, javaSimpleType, GridWidget.SIMULATION);
        verify(abstractDMODataManagementStrategySpy, times(1)).superTypeAggregatorCallBack(eq(dataObjectsType), eq(superTypesMap), eq(testToolsPresenterMock), eq(expectedElement), eq(dataObjectsFieldMap), eq(scenarioSimulationContextLocal), eq(javaSimpleType), eq(GridWidget.SIMULATION), eq(factType));
        verify(abstractDMODataManagementStrategySpy, times(1)).getSuperType(eq(factType), isA(Callback.class));
    }

    @Test
    public void superTypeAggregatorCallBack() {
        String factType = "factType";
        String factType2 = "factType2";
        int expectedElement = 2;
        List<String> dataObjectsType = Arrays.asList(factType, factType2);
        SortedMap<String, FactModelTree> dataObjectsFieldMap = new TreeMap<>();
        Map<String, String> superTypesMap = new HashMap<>();
        List<String> javaSimpleType = new ArrayList<>();
        Callback<String> callback = abstractDMODataManagementStrategySpy.superTypeAggregatorCallBack(dataObjectsType, superTypesMap, testToolsPresenterMock, expectedElement, dataObjectsFieldMap, scenarioSimulationContextLocal, javaSimpleType, GridWidget.SIMULATION, factType);
        callback.callback(Object.class.getCanonicalName());
        assertTrue(superTypesMap.containsKey(factType));
        assertEquals(Object.class.getCanonicalName(), superTypesMap.get(factType));
        Callback<String> callback2 = abstractDMODataManagementStrategySpy.superTypeAggregatorCallBack(dataObjectsType, superTypesMap, testToolsPresenterMock, expectedElement, dataObjectsFieldMap, scenarioSimulationContextLocal, javaSimpleType, GridWidget.SIMULATION, factType2);
        callback2.callback(Class.class.getCanonicalName());
        assertTrue(superTypesMap.containsKey(factType2));
        assertEquals(Class.class.getCanonicalName(), superTypesMap.get(factType2));
        verify(abstractDMODataManagementStrategySpy, times(1)).manageDataObjects(eq(dataObjectsType), eq(superTypesMap), eq(testToolsPresenterMock), eq(expectedElement), eq(dataObjectsFieldMap), eq(scenarioSimulationContextLocal), eq(javaSimpleType), eq(GridWidget.SIMULATION));
    }

    @Test
    public void defineClassNameField_SimpleType() {
        Map<String, String> superTypesMap = new HashMap<>();
        String retrieved = abstractDMODataManagementStrategySpy.defineClassNameField(String.class.getSimpleName(), superTypesMap);
        assertEquals(String.class.getCanonicalName(), retrieved);
    }

    @Test
    public void defineClassNameField_NotSimpleNotEnum() {
        Map<String, String> superTypesMap = new HashMap<>();
        String retrieved = abstractDMODataManagementStrategySpy.defineClassNameField(TestProperties.CLASS_NAME, superTypesMap);
        assertEquals(TestProperties.CLASS_NAME, retrieved);
    }

    @Test
    public void defineClassNameField_NotSimpleEnum() {
        Map<String, String> superTypesMap = new HashMap<>();
        superTypesMap.put(TestProperties.CLASS_NAME, Enum.class.getCanonicalName());
        String retrieved = abstractDMODataManagementStrategySpy.defineClassNameField(TestProperties.CLASS_NAME, superTypesMap);
        assertEquals(FQCN_BY_FACTNAME, retrieved);
    }
}