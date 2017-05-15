angular.module('controller.webpage.container.widget.check', [])
    .directive('widgetCheck', ['DataService', 'APP_CONFIG', function (DataService) {
        return {
            restrict: 'EA',
            templateUrl: '/app/controller/widget/checkbox/widget.check.tpl.html',
            replace: false,
            scope: {
            },
            link: function (scope, element, attrs) {
            	var currWidgetId = attrs.id;
            	scope.id = currWidgetId;
            	DataService.getWidgetDetailById(currWidgetId).then(function (response) {
            		var data = response.plain();
            		scope.attConfigs = data.attConfigs;
            		scope.elements = data.elements;
            		scope.initvalue = attrs.initvalue;
            		var initvalueList = [];
            		if(attrs.initvalue){
            			initvalueList = scope.initvalue.split(",");
            		}
        			scope.queryCondition = {};
            		for(index1 in data.attConfigs){
                		var currAttConfig = data.attConfigs[index1];
            			var currAttConfigType = currAttConfig.type;
            			if(currAttConfigType == "label"){
            				scope.label = currAttConfig.attValue;
            			}
            			if(currAttConfigType == "name"){
            				scope.name = currAttConfig.attValue;
            			}
            			if(currAttConfigType == "width"){
            				scope.width = currAttConfig.attValue;
            			}
            			if(currAttConfigType == "height"){
            				scope.height = currAttConfig.attValue;
            			}
            			if(currAttConfigType == "serviceName"){
            				scope.serviceName = currAttConfig.attValue;
            			}
            			if(currAttConfigType == "cascade"){
            				if(currAttConfig.attValue){
            					scope.cascade = eval('('+currAttConfig.attValue+')');
                        		scope.changeValue = function(elementId){
                        			var currValue = $("#input" + elementId).val();
                        			var dataStr = "{id:'" + scope.cascade.id + "',queryCondition:{" + scope.cascade.queryKey + ":'" +currValue+ "'}}";
                        			var dataObj = eval('(' + dataStr + ')');
            						scope.$emit('doAction', {actionName:'widget.reload.options', d:dataObj});
                        		};
            				}
            			}
            			if(currAttConfigType == "queryCondition"){
            				if(currAttConfig.attValue != null && currAttConfig.attValue !="" ){
                				scope.queryCondition = eval('('+currAttConfig.attValue+')');
            				}
            			}
            		}
            		if(scope.serviceName != null){
            			var reloadOptions = function(queryCondition){
	        				DataService.findDataSrcList(scope.serviceName, queryCondition).then(function (response){
	        					var optionList = response.plain();
	        					var elements = new Array(optionList.length);
	        					for(var i = 0; i < optionList.length; i ++ ){
	        						var currElement = {};
	        						currElement.id = optionList[i].id;
	        						currElement.label = optionList[i].label;
	        						currElement.value = optionList[i].value;
	        						currElement.checked = false;
	        						for(var j = 0; j < initvalueList.length; j ++ ){
	                    				if(initvalueList[j] == currElement.value){
	                						currElement.checked = true;
	                    				}
	        						}
	        						elements[i] = currElement;
	        					}
	        					scope.elements = elements;
	                    		scope.$apply();
	                    		scope.changeValue;
	        				});
	        			}
	        			reloadOptions(scope.queryCondition);
	            		scope.$on("widget.reload.options",function(e, data){
	            			if(data.id == currWidgetId){
                				scope.queryCondition = $.extend(true, scope.queryCondition, data.queryCondition);
                				reloadOptions(scope.queryCondition);
	            			}
	            		});
	            		scope.$on("widget.reload.initvalue",function(e, data){
	            			for(var i = 0; i < scope.elements.length; i ++ ){
	            				if(scope.elements[i].value == scope.initvalue){
	            					scope.elements[i].checked = true;
                					scope.changeValue();
	            				}
	            			}
	                		scope.$apply();
                    		scope.changeValue;
	            		});
        				
            		}else{
                		for(i=0; i< data.elements.length; i ++ ){
                			var currElementId = data.elements[i].id;
                			var currElementValue, currElementLabel, currElementChecked;
                			DataService.getAttConfigsByBelongId(currElementId).then(function (response){
                				eleAttConfigs = response.plain();
                    			for(index2 in eleAttConfigs){
                    				var currEleAttConfig = eleAttConfigs[index2];
                    				var currEleAttConfigType = currEleAttConfig.type;
                        			if(currEleAttConfigType == "label"){
                        				currElementLabel = currEleAttConfig.attValue;
                        				$("#span"+currEleAttConfig.belongId).html(currElementLabel);
                        			}
                        			if(currEleAttConfigType == "value"){
                        				currElementValue = currEleAttConfig.attValue;
                        				currElementChecked = false;
                						for(var j = 0; j < initvalueList.length; j ++ ){
                            				if(initvalueList[j] == currElementValue){
                            					currElementChecked = true;
                            				}
                						}
                        				$("#input"+currEleAttConfig.belongId).val(currElementValue);
                        				$("#input"+currEleAttConfig.belongId).attr("checked",currElementChecked);
                        			}
                    			}
                			});            			
                		}
            		}
            	});
            }
        };
    }])
;

