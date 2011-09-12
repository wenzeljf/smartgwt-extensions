/**
 * 
 */
package com.smartgwt.extensions.utility.requestrouter.jsarray;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.smartgwt.extensions.utility.requestrouter.AJAXRequestHandler;

/**
 * @author marcellolarocca@gmail.com
 *
 */
public interface JsArrayRequestHandler<T extends JavaScriptObject> extends AJAXRequestHandler{
	public  void onSuccess(JsArray<T> jso );
}
