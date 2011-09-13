package com.smartgwt.extensions.utility.requestrouter;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.smartgwt.extensions.utility.requestrouter.javascriptobject.JsObjectRequestHandler;
import com.smartgwt.extensions.utility.requestrouter.jsarray.JsArrayRequestHandler;
import com.smartgwt.extensions.utility.requestrouter.json.JSONRequestHandler;

/**
 * This class is the representation of an AJAX request which can retrieve data in different formats:
 * 1) Javascript objects
 * 2) JsArray
 * 3) JSON String
 * 
 * The decision among data format is made automatically thanks to polymorphism and overloading
 * 
 * @author marcellolarocca@gmail.com
 *
 */
public class AJAXRequest<T extends JavaScriptObject> implements Comparable<AJAXRequest<T>> {
	
		private static int counter = 0;
		private int requestID;
		
		private String url;
		protected AJAXRequestRouter router;
		protected AJAXRequestHandler handler = null;
		
		private static final byte DEFAULT_TIMEOUT = 10;
		
		private short timeout = DEFAULT_TIMEOUT;	//Request Timeout (in seconds)
		
		private RequestPriority priority;
		
		private ResponseType responseType;

		private boolean completed;


		/**
		 * Constructor for an AJAXRequest which manages a JavaScriptObject response
		 * 
		 * @param _url The remote address where the call has to be made
		 * @param _router The AJAXRequestRouter which handles the request; MUST be notified when the request is completed
		 * @param _handler The AJAXRequestHandler which is going to handle the response (or the failure of the request)
		 */
		public AJAXRequest(String _url, AJAXRequestRouter _router, AJAXRequestHandler _handler ) {
			init(_url,_router,_handler, ResponseType.JAVASCRIPT_OBJECT);
		}

		
		/**
		 * Constructor for an AJAXRequest which manages a JsArray response
		 * 
		 * @param _url The remote address where the call has to be made
		 * @param _router The AJAXRequestRouter which handles the request; MUST be notified when the request is completed
		 * @param _handler The JsArrayRequestHandler<T> which is going to handle the response (or the failure of the request)
		 */
		public AJAXRequest(String _url, AJAXRequestRouter _router, JsArrayRequestHandler<T> _handler ) {
			init(_url,_router,_handler, ResponseType.JSARRAY);
		}
		
		
		/**
		 * Constructor for an AJAXRequest which manages a JSON response
		 * 
		 * @param _url The remote address where the call has to be made
		 * @param _router The AJAXRequestRouter which handles the request; MUST be notified when the request is completed
		 * @param _handler The JSONRequestHandler which is going to handle the response (or the failure of the request)
		 * 
		 */
		public AJAXRequest(String _url, AJAXRequestRouter _router, JSONRequestHandler _handler ) {
			init(_url,_router,_handler, ResponseType.JSON_STRING);
		}
		
		/**
		 * 
		 * @param _url The remote address where the call has to be made
		 * @param _router The AJAXRequestRouter which handles the request; MUST be notified when the request is completed
		 * @param _handler The handler which is going to handle the response (or the failure of the request)
		 * @param _responseType The responseType: There MUST be a correlation between this parameter
		 * 						and the type of _handler, so the class variable "responseType" must be
		 * 						set only here, and its value must be carefully chosen in the constructor
		 * 						according to overloading
		 */
		private void init(String _url, AJAXRequestRouter _router, AJAXRequestHandler _handler, ResponseType _responseType){
			requestID = ++counter;
			url = _url;
			router = _router;
			responseType = _responseType;
			handler = _handler;

			priority = RequestPriority.NORMAL;	//As Default
			completed = false;					//Not even started yet!
		}
	
		
		/**
		 * Starts the AJAX request
		 */
		public void getResponse(){
			getResponse(requestID, url, this, responseType);
		}
		
		/**
		 * Make call to remote server.
		 *
		 * @param requestID
		 * @param url
		 * @param request
		 * @param responseType
		 */
		private native void getResponse(int requestID, String url, AJAXRequest<T> request, ResponseType responseType ) /*-{
		 var callback = "callback" + requestID;
		
		 // [1] Create a script element.
		 var script = document.createElement("script");
		 script.setAttribute("src", url + callback);
		 script.setAttribute("type", "text/javascript");
		
		 // [2] Define the callback function on the window object.
		 window[callback] = function(jsonObj) {
		 // [3]
   			request.@com.smartgwt.extensions.utility.requestrouter.AJAXRequest::handleAJAXResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
		   	window[callback + "done"] = true;
		 }
		
		 // [4] JSON download has 10-second timeout.
		 setTimeout(
			 function() {
			   if (!window[callback + "done"]) {
			     request.@com.smartgwt.extensions.utility.requestrouter.AJAXRequest::handleAJAXResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
			   }
			
			   // [5] Cleanup. Remove script and callback elements.
			   document.body.removeChild(script);
			   delete window[callback];
			   delete window[callback + "done"];
			 }, 
			 10000
		 );
		
		 // [6] Attach the script element to the document body.
		 document.body.appendChild(script);
		}-*/;
		
	  
		/**
		 * Handle the response to the request for JavaScriptObject data from a remote server.
		 *
		 * @param jso The date retrieved from the remote application
		 */
		@SuppressWarnings("unchecked")//No need to check: _handler's type and responseType's value are correlated
		protected void handleAJAXResponse(JavaScriptObject jso) {
			markAsCompleted();					//Must keep track of the request status
			router.notifyCompletion(this);	//GOT to do it anyway
			if (jso == null) {
				switch(responseType){
		 			case JAVASCRIPT_OBJECT :
		 				((JsObjectRequestHandler)handler).onFailure(this);
			 			break;
			 		case JSARRAY:
			 			((JsArrayRequestHandler<T>)handler).onFailure(this);
			 			break;
			 		case JSON_STRING:
			 			((JSONRequestHandler)handler).onFailure(this);
			 			break;			 			
			 	}
			  
				return ;
		  }
		    //else
		    //alert(jso.toString());
			switch(responseType){
 			case JAVASCRIPT_OBJECT :
 				((JsObjectRequestHandler)handler).onSuccess(jso);
	 			break;
	 		case JSARRAY:
	 			((JsArrayRequestHandler<T>)handler).onSuccess(asArray(jso));
	 			break;
	 		case JSON_STRING:
	 			((JSONRequestHandler)handler).onSuccess(asString(jso));
	 			break;			 			
			}		  		    
	  }
		  
	  
	  /**
	   * Cast JavaScriptObject as JsArray of T
	   */
	  private final native JsArray<T> asArray(JavaScriptObject jso) /*-{
	    return jso;
	  }-*/;

	  /**
	   * Cast JavaScriptObject as a String
	   */
	  private final native String asString(JavaScriptObject jso) /*-{
	    return JSON.stringify(jso);
	  }-*/;
	  
	  
	    /**
	     * To order the requests, first matters priority, then (if it's the same) the request ID
	     * because the smaller it is, the earlier the request was created
	     * 
		 * @param arg0 The other AJAXRequest to be compared to.
	     */
		@Override
		public int compareTo(@SuppressWarnings("rawtypes") AJAXRequest arg0) {
			if ( priority == arg0.priority )
				return requestID - arg0.requestID;
			else{
				return priority.compareTo(arg0.priority);
			}
		}

		/**
		 * Let the handler know that the request could not be started right away
		 */
		public void notifyQueueFull(){
			handler.onQueueFull( this );
		}
		
		/**
		 * 
		 * @return true <=> this request has urgent priority, and so should be sent right away
		 */
		public boolean isUrgent(){
			return priority == RequestPriority.URGENT;
		}
		
		/**
		 * 
		 * @return True or false depending on the status of the request (Completed or failed, or pending)
		 */
		public boolean isCompleted() {
			return completed;
		}

		/**
		 * Marks the request as completed
		 */
		protected void markAsCompleted() {
			completed = true;
		}
		
		/**
		 * 
		 * @return The value set for the request timeout
		 */
		public short getTimeout() {
			return timeout;
		}

		/**
		 * Sets the request timeout
		 * @param _timeout
		 */
		public void setTimeout(short _timeout) {
			timeout = _timeout;
		}

		public RequestPriority getPriority() {
			return priority;
		}

		public void setPriority(RequestPriority priority) {
			this.priority = priority;
		}

		public ResponseType getResponseType() {
			return responseType;
		}
		
		
		/**
		 * 
		 * @param _priority
		 * @return
		 
		private String priorityNAME(RequestPriority _priority){
			switch (_priority){
				case LOW:
					return "low";
				case NORMAL:
					return "normal";
				case HIGH:
					return "high";
				case URGENT:
					return "urgent";
				default:
					return "INVALID";
 			}
		}
		*/
		/** DEBUG
		public int getID(){
			return requestID;
		}
		*/	
}