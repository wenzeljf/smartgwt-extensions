/**
Copyright (c) <2009> <Pete Boysen>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.smartgwt.extensions.util.client.ds;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.extensions.util.client.Util;


/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class ProjectDataSource extends RestDataSource {
	private HashMap<String,String> params = new HashMap<String,String>();
	private RequestListener requestListener;
	private ResponseListener responseListener;
	private HashMap<String,String> defaultParams;
	private List<DataSourceField> dateFields = new ArrayList<DataSourceField>();
	
	public ProjectDataSource() {		
		setDataURL(Util.getDataURL());
		setDataFormat(DSDataFormat.XML);
	}
	
	public ProjectDataSource(String context, String model, String xq) {
		this();
		defaultParams = new HashMap<String,String>();
		defaultParams.put("context", context);
		defaultParams.put("model", model);
		defaultParams.put("xq", xq);
		setDefaultParams(defaultParams);
	}
	
	public void setFields(DataSourceField... fields) {
		for (DataSourceField field: fields)
			if (field.getType() == FieldType.DATE)
				dateFields.add(field);
		super.setFields(fields);
	}
	
	public void setProjectId(String projectId) {
		addParameter("projectId", projectId);
	}
	
	public HashMap<String,String> getParams() {
		return params;
	}
	
	public void addParameter(String name, String value) {
		params.put(name,value);
	}
	
	public void addParameters(Map<String,String> args) {
		for(Map.Entry<String,String> e: args.entrySet())
			addParameter(e.getKey(), e.getValue());
	}
	
	public RequestListener getRequestListener() {
		return requestListener;
	}

	public void setRequestListener(RequestListener requestListener) {
		this.requestListener = requestListener;
	}

	public ResponseListener getResponseListener() {
		return responseListener;
	}

	public void setResponseListener(ResponseListener listener) {
		this.responseListener = listener;
	}

	public ProjectDataSource copy() {
		ProjectDataSource pds = new ProjectDataSource();
		pds.setDefaultParams(defaultParams);
		pds.addParameters(params);
		return pds;
	}
	
	protected Object transformRequest(DSRequest request) {
		if (requestListener != null)
			requestListener.onRequest(request);
		request.setParams(params);
		request.setUseSimpleHttp(true);
		request.setPromptStyle(PromptStyle.CURSOR);
		setDateTimes(request.getData());
		return super.transformRequest(request);
	}
	
	protected void transformResponse(DSResponse response, DSRequest request, Object data) {
		String status = XMLTools.selectString(data,"/response/status/.");
		if (status.equals("0")) {
			if (responseListener != null)
				responseListener.onResponse(response,request,data);
			super.transformResponse(response, request, data);
		} else {
			response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);  
			Object errors = XMLTools.selectNodes(data, "/response/errors");  
			JavaScriptObject errorsJS = XMLTools.toJS(errors);
			response.setErrors(errorsJS);
		} 
	}
	
	private void setDateTimes(JavaScriptObject data) {
		if (dateFields == null) return;
		for (DataSourceField field: dateFields) {
			String name = field.getName();
			try {
			    Date date = JSOHelper.getAttributeAsDate(data, name);
			    if (date != null) {
				    JSOHelper.setAttribute(data, name+"Hour", String.valueOf(date.getHours()));				
				    JSOHelper.setAttribute(data, name+"Minute", String.valueOf(date.getMinutes()));
			    }
			} catch (Exception e) {
				// TODO: just ignore for now. figure out why it fails on delete
			}
		}
	}
}	
