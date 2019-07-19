/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.runtime.api.impl;

import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.MapExceptionEntry;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.CallActivityBehavior;
import org.activiti.engine.repository.ProcessDefinition;

public class MappingAwareCallActivityBehavior extends CallActivityBehavior {

    private VariablesMappingProvider mappingProvider;

    public MappingAwareCallActivityBehavior(String processDefinitionKey,
                                            List<MapExceptionEntry> mapExceptions,
                                            VariablesMappingProvider mappingProvider) {
        super(processDefinitionKey,
              mapExceptions);
        this.mappingProvider = mappingProvider;
    }

    public MappingAwareCallActivityBehavior(Expression processDefinitionExpression,
                                            List<MapExceptionEntry> mapExceptions,
                                            VariablesMappingProvider mappingProvider) {
        super(processDefinitionExpression,
              mapExceptions);
        this.mappingProvider = mappingProvider;
    }

    @Override
    protected Map<String, Object> calculateInboundVariables(DelegateExecution execution,
                                                            ProcessDefinition processDefinition) {

        Map<String, Object> inputVariables = mappingProvider.calculateInputVariables(execution);
        return mappingProvider.getProcessVariablesInitiator().calculateVariablesFromExtensionFile(processDefinition,
                                                                                                  inputVariables);
    }

    @Override
    protected Map<String, Object> calculateOutBoundVariables(DelegateExecution execution,
                                                             Map<String, Object> taskCompleteVariables) {

        return mappingProvider.calculateOutPutVariables(execution,
                                                        taskCompleteVariables);
    }
}
