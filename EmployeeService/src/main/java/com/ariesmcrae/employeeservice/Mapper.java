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

import java.util.HashMap;
import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
//import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

/**
 * @author <a href="http://ariesmcrae.com">aries@ariesmcrae.com</a>
 */
public class Mapper extends Verticle {
	
	
	@Override
	public void start() {
		vertx.eventBus().registerHandler("mapper.address", handler());
	}	
	
	
	
	private Handler<Message<JsonObject>> handler() {
		return new Handler<Message<JsonObject>>() {
			
		    public void handle(Message<JsonObject> message) {
				//Logger logger = container.logger();
		    	
				JsonObject json = message.body();

				//employee
				JsonObject employee = (JsonObject)json.getObject("employee").getArray("result").iterator().next();
				Integer employeeTypeCode = employee.getInteger("EMP_TYPE_CD");
				String employeeTypeDesc = employee.getString("EMP_TYPE_DESC");
				String employeeCategory = employee.getString("REQUEST_EMP_CAT_INDIC");
				String referralIndicator = employee.getString("REFERRAL_INDIC");		
				String payIfClosedFlag = employee.getString("PAY_IF_CLOSED_FLG");				
				String employeeIfNOTLiabilityPending = employee.getString("PAY_IF_NOT_LIAB_PENDING_INDIC");				
				Long employeeLimitAmount = employee.getLong("EMP_TYPE_LMT_AMT");				
				Long smeemployeeLimitAmount = employee.getLong("SME_EMP_TYPE_LMT_AMT");
				String buyoutCode = buyoutCodeMap.get(employee.getString("EMP_TYPE_BUYOUT_CD"));					
				String addressUsageType = employeeTypeAddressUsageTypeMap.get(employee.getString("EMP_TYPE_ADDR_USAGE_CD"));				
				String provisionalLiabilityEmployeeCategoryCode = provLiabEmployeeCategoryMap.get(employee.getString("PROVNL_LIAB_DTH_EMP_CAT_CD"));
				String requestCaseEstimateGroup = requestCaseEstimateGroupMap.get(employee.getString("REQUEST_ESTIM_COST_GRP_CD"));
				String requestCaseEstimateSubGroup = requestCaseEstimateSubGroupMap.get(employee.getString("REQUEST_ESTIM_COST_SUBGRP_CD"));
				Integer consultationReviewThreshold = employee.getInteger("CONSULTATION_TRIGGER_POINT");
				
				JsonObject motherObject = new JsonObject();
				
				JsonObject employeeTypeBO = new JsonObject();
				employeeTypeBO.putNumber("EmployeeTypeCode", employeeTypeCode);
				employeeTypeBO.putString("EmployeeTypeDesc", employeeTypeDesc);				
				employeeTypeBO.putString("EmployeeCategory", employeeCategory);
				employeeTypeBO.putString("ReferralIndicator", referralIndicator);				
				employeeTypeBO.putString("PayIfClosedFlag", payIfClosedFlag);				
				employeeTypeBO.putString("EmployeeIfNOTLiabilityPending", employeeIfNOTLiabilityPending);				
				employeeTypeBO.putNumber("EmployeeLimitAmount", employeeLimitAmount);				
				employeeTypeBO.putNumber("SmeEmployeeLimitAmount", smeemployeeLimitAmount);				
				employeeTypeBO.putString("BuyoutCode", buyoutCode);				
				employeeTypeBO.putString("AddressUsageType", addressUsageType);				
				employeeTypeBO.putString("ProvisionalLiabilityemployeeCategoryCode", provisionalLiabilityEmployeeCategoryCode);				
				employeeTypeBO.putString("HolidayCaseEstimateGroup", requestCaseEstimateGroup);				
				employeeTypeBO.putString("HolidayCaseEstimateSubGroup", requestCaseEstimateSubGroup);				
				employeeTypeBO.putNumber("ConsultationReviewThreshold", consultationReviewThreshold);								
				
				//classification
				JsonArray referralClassifications = new JsonArray();
				JsonArray classificationList = (JsonArray)json.getObject("classification").getArray("result");
				for (Object classification : classificationList) {
					String classificationCode = ((JsonObject)classification).getString("CD_ALPHA_REF");
					referralClassifications.add(classificationCode);
				}
				
				//channel
				JsonArray employeeTypeChannelTypes = new JsonArray();
				JsonArray channelList = (JsonArray)json.getObject("channel").getArray("result");
				
				for (Object channel : channelList) {
					String channelType = ((JsonObject)channel).getString("INPUT_CHNL");
					employeeTypeChannelTypes.add(channelType);
					
					String channelTypeActiveFlag = ((JsonObject)channel).getString("CHNL_ON_OFF_INDIC");
					String flag = "false";
					if ("1".equals(channelTypeActiveFlag)) {
						flag = "true";
					}
					
					employeeTypeChannelTypes.add(flag);
				}

				//Assemble mother object
				motherObject.putArray("ReferralClassifications", referralClassifications);
				motherObject.putArray("employeeTypeChannelTypes", employeeTypeChannelTypes);				
				
				motherObject.putObject("employeeTypeBO", employeeTypeBO);				
				
		        message.reply(motherObject);
		    }
		};
	}


	
	
	private static Map<String, String> buyoutCodeMap; 
	private static Map<String, String> employeeTypeAddressUsageTypeMap;
	private static Map<String, String> provLiabEmployeeCategoryMap;	
	private static Map<String, String> requestCaseEstimateGroupMap;	
	private static Map<String, String> requestCaseEstimateSubGroupMap;		
	private static Map<String, String> areaMap;	
	
	
	static {
		//cache these lookups when JVM starts up.
		
		buyoutCodeMap = new HashMap<String, String>(3);
		buyoutCodeMap.put("CO", "Compensation");
		buyoutCodeMap.put("LE", "Lunch Expenses");		
		buyoutCodeMap.put(null, "Not Applicable");
			
		employeeTypeAddressUsageTypeMap = new HashMap<String, String>(4);
		employeeTypeAddressUsageTypeMap.put("P", "Payee's Address");		
		employeeTypeAddressUsageTypeMap.put("S", "Solicitor's Address");
		employeeTypeAddressUsageTypeMap.put("C", "Company Solicitor's Address");		
		employeeTypeAddressUsageTypeMap.put(null, "Worker's Current Solicitor's Address");	
		
		provLiabEmployeeCategoryMap = new HashMap<String, String>(5);
		provLiabEmployeeCategoryMap.put("D", "Death expenses");
		provLiabEmployeeCategoryMap.put("F", "Family expenses");
		provLiabEmployeeCategoryMap.put("L", "Lunch and Like expenses");
		provLiabEmployeeCategoryMap.put("P", "Partner and Super");		
		provLiabEmployeeCategoryMap.put(null, "Not Applicable");		
		
		requestCaseEstimateGroupMap = new HashMap<String, String>(10);
		requestCaseEstimateGroupMap.put("09", "Weekly Compensation");		
		requestCaseEstimateGroupMap.put("08", "Lunch and Like");
		requestCaseEstimateGroupMap.put("07", "Hospital");
		requestCaseEstimateGroupMap.put("06", "Rehabilitation");		
		requestCaseEstimateGroupMap.put("05", "Holidayant Expenses (Other - Stats)");
		requestCaseEstimateGroupMap.put("04", "Maims");
		requestCaseEstimateGroupMap.put("03", "Common Law");		
		requestCaseEstimateGroupMap.put("82", "Death");
		requestCaseEstimateGroupMap.put("91", "Legal");
		requestCaseEstimateGroupMap.put("100", "Holiday Management (Assessment - Stats)");		

		requestCaseEstimateSubGroupMap = new HashMap<String, String>(14);
		requestCaseEstimateSubGroupMap.put("010a", "Weekly Benefits/Settlements");		
		requestCaseEstimateSubGroupMap.put("020b", "Medical and Others");		
		requestCaseEstimateSubGroupMap.put("030c", "Hospital");
		requestCaseEstimateSubGroupMap.put("040d", "Rehabilitation");
		requestCaseEstimateSubGroupMap.put("050e", "Holidayant Expenses");
		requestCaseEstimateSubGroupMap.put("060f", "Maim/Pain and Suffering Settlement");
		requestCaseEstimateSubGroupMap.put("060g", "Maim Admin/Legal Fees");
		requestCaseEstimateSubGroupMap.put("060h", "Lump Sum - S.98C and E Impairment Benefits");
		requestCaseEstimateSubGroupMap.put("070i", "Common Law - Pain and Suffering/Pecuniary Loss");
		requestCaseEstimateSubGroupMap.put("070j", "Common Law Legal Costs");
		requestCaseEstimateSubGroupMap.put("080k", "Death Lump Sum");
		requestCaseEstimateSubGroupMap.put("080l", "Death Pension");		
		requestCaseEstimateSubGroupMap.put("090m", "Other Legal Fees");
		requestCaseEstimateSubGroupMap.put("0100n", "Holiday Management");
		
		areaMap = new HashMap<String, String>(3);		
		areaMap.put("01", "Area1");		
		areaMap.put("02", "Area1");
		areaMap.put(null, "Area1");		
	}
	
	
	
	
	
	
}
