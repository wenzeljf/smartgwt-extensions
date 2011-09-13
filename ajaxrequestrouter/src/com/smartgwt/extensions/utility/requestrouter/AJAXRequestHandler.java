package com.smartgwt.extensions.utility.requestrouter;


public interface AJAXRequestHandler 
{
	public  void onFailure( @SuppressWarnings("rawtypes") AJAXRequest request );
	public  void onQueueFull( @SuppressWarnings("rawtypes") AJAXRequest request );	
}