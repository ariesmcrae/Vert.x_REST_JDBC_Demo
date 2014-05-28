/** 
	The MIT License (MIT)
	
	Copyright (c) 2014 Aries McRae
	
	Permission is hereby granted, free of charge, to any person obtaining a copy of
	this software and associated documentation files (the "Software"), to deal in
	the Software without restriction, including without limitation the rights to
	use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
	the Software, and to permit persons to whom the Software is furnished to do so,
	subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
	FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
	COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
	CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package com.ariesmcrae.employeeservice;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
//import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

/**
 * @author <a href="http://ariesmcrae.com">aries@ariesmcrae.com</a>
 */
public class Server extends Verticle {

	private static EventBus eventBus;

	private static JsonObject webServerConfig;
	
	private static final String employeeSql = "select EMPLOYEE_TYPE_CD, EMP_TYPE_CD, EMPLOYEE_TYPE_DESC, REQUEST_EMPLOYEE_CAT_INDIC, REFERRAL_INDIC, PAY_IF_CLOSED_FLG, PAY_IF_NOT_LIAB_PENDING_INDIC, EMPLOYEE_TYPE_LMT_AMT, SME_EMPLOYEE_TYPE_LMT_AMT, EMPLOYEE_TYPE_BUYOUT_CD, EMPLOYEE_TYPE_ADDR_USAGE_CD, PROVNL_LIAB_DTH_EMPLOYEE_CAT_CD, REQUEST_ESTIM_COST_GRP_CD, REQUEST_ESTIM_COST_SUBGRP_CD, CONSULTATION_TRIGGER_POINT from SCHEMA.V_EMPLOYEE_TYPE where EMPLOYEE_TYPE_CD =";
    private static final String classificationSql = "select CD_ALPHA_REF from SCHEMA.V2_VALID_EMPLOYEE_TYPES_REF_CLSSN where CD_NUMERIC_REF=";
    private static final String departmentSql = "select INPUT_DEPT, DEPT_ON_OFF_INDIC from SCHEMA.V_INPUT_DEPT_EMPLOYEE_TYPE where HISTORY_STATUS_INDIC='C' and EMPLOYEE_TYPE_CD=";

    private JsonObject employee;
    private JsonObject classification;
    
    private JsonObject dbResultAggregate;    

    
    
	@Override
	public void start() {
		eventBus = vertx.eventBus();

		webServerConfig = container.config();
			
		vertx.createHttpServer()
			.requestHandler(routeMatcher())					
			//.setAcceptBacklog(10000000)
			//.setSendBufferSize(10000 * 1024)
			//.setReceiveBufferSize(10000 * 1024)
			.listen(webServerConfig.getInteger("port"), webServerConfig.getString("host"));			
	}

	
	
	private RouteMatcher routeMatcher() {
		return new RouteMatcher().get("/employees/:employeeNumber", restHandler());
	}

	
	
	private Handler<HttpServerRequest> restHandler() {
		return new Handler<HttpServerRequest>() {
			public void handle(final HttpServerRequest request) {
				final String employeeTypeCode = request.params().get("employeeTypeCode");

				//query employee
				JsonObject selectemployeeObj = new JsonObject().putString("action", "select").putString("stmt", employeeSql + employeeTypeCode);

				eventBus.send("vertx.jdbcpersistor", selectemployeeObj, new Handler<Message<JsonObject>>() {
					@Override
					public void handle(Message<JsonObject> dbResponse) {
						employee = dbResponse.body();
						
						if (employee.getArray("result").size() > 0) {
							//query classification
							JsonObject selectClassificationObj = new JsonObject().putString("action", "select").putString("stmt", classificationSql + employeeTypeCode);
							eventBus.send("vertx.jdbcpersistor", selectClassificationObj, classificationHandler(request, employeeTypeCode));						
						} else {
							request.response().end(new JsonObject().putValue("ObjectNotfound", objectNotFound()).encodePrettily());
						}
					}
				}); //eventBus
			}
		}; //handler
	}
	
	
	
	private Handler<Message<JsonObject>> classificationHandler(final HttpServerRequest request, final String employeeTypeCode) {
		return new Handler<Message<JsonObject>>() {
		
		    public void handle(Message<JsonObject> dbResponse) {
		    	classification = dbResponse.body();
		    	
		    	//query department
				JsonObject selectObj = new JsonObject().putString("action", "select").putString("stmt", departmentSql + employeeTypeCode);
				eventBus.send("vertx.jdbcpersistor", selectObj, departmentHandler(request));						
		    }
		};
	}
	
	


	private Handler<Message<JsonObject>> departmentHandler(final HttpServerRequest request) {
		return new Handler<Message<JsonObject>>() {
		
		    public void handle(Message<JsonObject> dbResponse) {
		    	dbResultAggregate = new JsonObject()
		    		.putObject("employee", employee)
		    		.putObject("classification", classification)
		    		.putObject("department", dbResponse.body());	    		
		    	
    			eventBus.send("mapper.address", dbResultAggregate, mapperHandler(request));					
		    }
		};
	}		
	
	
	
	private Handler<Message<JsonObject>> mapperHandler(final HttpServerRequest request) {
		return new Handler<Message<JsonObject>>() {
		
		    public void handle(Message<JsonObject> message) {
				request.response().putHeader("Content-Type", "application/json");				
		    	request.response().end(message.body().encodePrettily());		    	
		    }
		};
	}

	
	
	private static JsonObject objectNotFound() {
		JsonObject errorInfo = new JsonObject();
		errorInfo.putString("Severity", "Error");
		errorInfo.putString("Code", "");							
		errorInfo.putString("Message", "No matching records found.");							

		JsonObject objectNotFound = new JsonObject();							
		objectNotFound.putObject("errorInfo", errorInfo);
		
		return objectNotFound;
	}


}
