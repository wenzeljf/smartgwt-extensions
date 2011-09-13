package com.smartgwt.extensions.utility.requestrouter.javascriptobject;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.extensions.utility.requestrouter.AJAXRequestHandler;

public interface JsObjectRequestHandler extends AJAXRequestHandler {

	public abstract void onSuccess(JavaScriptObject jso );

}