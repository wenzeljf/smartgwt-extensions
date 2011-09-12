package com.smartgwt.extensions.utility.requestrouter;

import com.google.gwt.core.client.JavaScriptObject;

public interface AJAXRequestHandler 
{
	public  void onSuccess(JavaScriptObject jso );
	public  void onFailure( @SuppressWarnings("rawtypes") AJAXRequest request );
	public  void onQueueFull( @SuppressWarnings("rawtypes") AJAXRequest request );	
}