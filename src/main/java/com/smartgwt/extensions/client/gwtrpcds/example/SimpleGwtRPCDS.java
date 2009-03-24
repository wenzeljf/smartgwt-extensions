package com.smartgwt.extensions.client.gwtrpcds.example;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.extensions.client.gwtrpcds.GwtRpcDataSource;
import java.util.List;

/**
 * Example <code>GwtRpcDataSource</code> implementation.
 *
 * @author Aleksandras Novikovas
 * @author System Tier
 * @version 1.0
 */
public class SimpleGwtRPCDS
    extends GwtRpcDataSource {

    public SimpleGwtRPCDS () {
        DataSourceField field;
        field = new DataSourceIntegerField ("id", "ID");
        field.setPrimaryKey (true);
        // AutoIncrement on server.
        field.setRequired (false);
        addField (field);
        field = new DataSourceTextField ("name", "NAME");
        field.setRequired (true);
        addField (field);
        field = new DataSourceDateField ("date", "DATE");
        field.setRequired (false);
        addField (field);
    }

    @Override
    protected void executeFetch (final String requestId, final DSRequest request, final DSResponse response) {
        // This can be used as parameter for server side sorting.
//        request.getSortBy ();

        // Finding which rows were requested
        // Normaly these two indexes should be passed to server
        // but for this example I will do "paging" on client side
        final int startIndex = (request.getStartRow () < 0)?0:request.getStartRow ();
        final int endIndex = (request.getEndRow () == null)?-1:request.getEndRow ();
        SimpleGwtRPCDSServiceAsync service = GWT.create (SimpleGwtRPCDSService.class);
        service.fetch (new AsyncCallback<List<SimpleGwtRPCDSRecord>> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (List<SimpleGwtRPCDSRecord> result) {
                // Calculating size of return list
                int size = result.size ();
                if (endIndex >= 0) {
                    if (endIndex < startIndex) {
                        size = 0;
                    }
                    else {
                        size = endIndex - startIndex + 1;
                    }
                }
                // Create list for return - it is just requested records
                ListGridRecord[] list = new ListGridRecord[size];
                if (size > 0) {
                    for (int i = 0; i < result.size (); i++) {
                        if (i >= startIndex && i <= endIndex) {
                            ListGridRecord record = new ListGridRecord ();
                            copyValues (result.get (i), record);
                            list[i - startIndex] = record;
                        }
                    }
                }
                response.setData (list);
                // IMPORTANT: for paging to work we have to specify size of full result set
                response.setTotalRows (result.size ());
                processResponse (requestId, response);
            }
        });
    }

    @Override
    protected void executeAdd (final String requestId, final DSRequest request, final DSResponse response) {
        // Retrieve record which should be added.
        JavaScriptObject data = request.getData ();
        ListGridRecord rec = new ListGridRecord (data);
        SimpleGwtRPCDSRecord testRec = new SimpleGwtRPCDSRecord ();
        copyValues (rec, testRec);
        SimpleGwtRPCDSServiceAsync service = GWT.create (SimpleGwtRPCDSService.class);
        service.add (testRec, new AsyncCallback<SimpleGwtRPCDSRecord> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (SimpleGwtRPCDSRecord result) {
                ListGridRecord[] list = new ListGridRecord[1];
                ListGridRecord newRec = new ListGridRecord ();
                copyValues (result, newRec);
                list[0] = newRec;
                response.setData (list);
                processResponse (requestId, response);
            }
        });
    }

    @Override
    protected void executeUpdate (final String requestId, final DSRequest request, final DSResponse response) {
        // Retrieve record which should be updated.
        // Next line would be nice to replace with line:
        // ListGridRecord rec = request.getEditedRecord ();
        ListGridRecord rec = getEditedRecord (request);
        SimpleGwtRPCDSRecord testRec = new SimpleGwtRPCDSRecord ();
        copyValues (rec, testRec);
        SimpleGwtRPCDSServiceAsync service = GWT.create (SimpleGwtRPCDSService.class);
        service.update (testRec, new AsyncCallback<SimpleGwtRPCDSRecord> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (SimpleGwtRPCDSRecord result) {
                ListGridRecord[] list = new ListGridRecord[1];
                ListGridRecord updRec = new ListGridRecord ();
                copyValues (result, updRec);
                list[0] = updRec;
                response.setData (list);
                processResponse (requestId, response);
            }
        });
    }

    @Override
    protected void executeRemove (final String requestId, final DSRequest request, final DSResponse response) {
        // Retrieve record which should be removed.
        JavaScriptObject data = request.getData ();
        final ListGridRecord rec = new ListGridRecord (data);
        SimpleGwtRPCDSRecord testRec = new SimpleGwtRPCDSRecord ();
        copyValues (rec, testRec);
        SimpleGwtRPCDSServiceAsync service = GWT.create (SimpleGwtRPCDSService.class);
        service.remove (testRec, new AsyncCallback<Object> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (Object result) {
                ListGridRecord[] list = new ListGridRecord[1];
                // We do not receive removed record from server.
                // Return record from request.
                list[0] = rec;
                response.setData (list);
                processResponse (requestId, response);
            }

        });
    }

    private static void copyValues (ListGridRecord from, SimpleGwtRPCDSRecord to) {
        to.setId (from.getAttributeAsInt ("id"));
        to.setName (from.getAttributeAsString ("name"));
        to.setDate (from.getAttributeAsDate ("date"));
    }

    private static void copyValues (SimpleGwtRPCDSRecord from, ListGridRecord to) {
        to.setAttribute ("id", from.getId ());
        to.setAttribute ("name", from.getName ());
        to.setAttribute ("date", from.getDate ());
    }

    private ListGridRecord getEditedRecord (DSRequest request) {
        // Retrieving values before edit
        JavaScriptObject oldValues = request.getAttributeAsJavaScriptObject ("oldValues");
        // Creating new record for combining old values with changes
        ListGridRecord newRecord = new ListGridRecord ();
        // Copying properties from old record
        JSOHelper.apply (oldValues, newRecord.getJsObj ());
        // Retrieving changed values
        JavaScriptObject data = request.getData ();
        // Apply changes
        JSOHelper.apply (data, newRecord.getJsObj ());
        return newRecord;
    }
}
