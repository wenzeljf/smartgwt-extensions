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
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
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
        // These can be used as parameters to create paging.
//        request.getStartRow ();
//        request.getEndRow ();
//        request.getSortBy ();
        SimpleGwtRPCDSServiceAsync service = GWT.create (SimpleGwtRPCDSService.class);
        service.fetch (new AsyncCallback<List<SimpleGwtRPCDSRecord>> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (List<SimpleGwtRPCDSRecord> result) {
                ListGridRecord[] list = new ListGridRecord[result.size ()];
                for (int i = 0; i < list.length; i++) {
                    ListGridRecord record = new ListGridRecord ();
                    copyValues (result.get (i), record);
                    list[i] = record;
                }
                response.setData (list);
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
        JavaScriptObject data = request.getData ();
        ListGridRecord rec = new ListGridRecord (data);
        // Find grid
        ListGrid grid = (ListGrid) Canvas.getById (request.getComponentId ());
        // Get record with old and new values combined
        int index = grid.getRecordIndex (rec);
        rec = (ListGridRecord) grid.getEditedRecord (index);
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
}
