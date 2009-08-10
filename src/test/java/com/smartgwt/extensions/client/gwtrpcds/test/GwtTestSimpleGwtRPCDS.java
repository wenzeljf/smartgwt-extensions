package com.smartgwt.extensions.client.gwtrpcds.test;

import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.extensions.gwtrpcds.client.example.SimpleGwtRPCDSRecord;
import com.smartgwt.extensions.gwtrpcds.client.example.SimpleGwtRPCDSService;

public class GwtTestSimpleGwtRPCDS extends GWTTestCase implements SimpleGwtRPCDSTestConfig {



    public String getModuleName() {
        return "com.smartgwt.extensions.Application";
    }

    public void testAdd() {

        delayTestFinish(10 * 1000);

        SimpleGwtRPCDSRecord dummy = new SimpleGwtRPCDSRecord();

        dummy.setName(TEST_RECORD_NAME);
        dummy.setDate(TEST_RECORD_DATE);

        SimpleGwtRPCDSService.Util.getInstance().add(dummy, new AsyncCallback<SimpleGwtRPCDSRecord>() {

            public void onFailure(Throwable cause) {
                fail(cause.getMessage());
            }

            public void onSuccess(SimpleGwtRPCDSRecord record) {

                assertNotNull(record);

                assertTrue(record.getId() > 0);

                assertEquals(TEST_RECORD_NAME, record.getName());

                assertEquals(TEST_RECORD_DATE, record.getDate());

                finishTest();
            }
        });
    }

    public void testFetch() {

        delayTestFinish(10 * 1000);

        SimpleGwtRPCDSService.Util.getInstance().fetch(new AsyncCallback<List<SimpleGwtRPCDSRecord>>() {

            public void onFailure(Throwable cause) {
                fail(cause.getMessage());
            }

            public void onSuccess(List<SimpleGwtRPCDSRecord> records) {

                assertNotNull(records);

                assertTrue(records.size() > 0);

                finishTest();
            }
        });
    }

    public void testRemove() {

        delayTestFinish(4 * 10 * 1000);

        SimpleGwtRPCDSRecord dummy = new SimpleGwtRPCDSRecord();

        dummy.setName(TEST_RECORD_NAME);
        dummy.setDate(TEST_RECORD_DATE);

        SimpleGwtRPCDSService.Util.getInstance().add(dummy, new AsyncCallback<SimpleGwtRPCDSRecord>() {

            public void onFailure(Throwable cause) {
                fail(cause.getMessage());
            }

            public void onSuccess(final SimpleGwtRPCDSRecord dummyRecord) {

                SimpleGwtRPCDSService.Util.getInstance().fetch(new AsyncCallback<List<SimpleGwtRPCDSRecord>>() {

                    public void onFailure(Throwable cause) {
                        fail(cause.getMessage());
                    }

                    public void onSuccess(List<SimpleGwtRPCDSRecord> records) {

                        assertTrue(records.size() > 0);

                        final int recordSizeWithDummy = records.size();

                        SimpleGwtRPCDSService.Util.getInstance().remove(dummyRecord, new AsyncCallback<Object>() {

                            public void onFailure(Throwable cause) {
                                fail(cause.getMessage());
                            }

                            public void onSuccess(Object result) {

                                SimpleGwtRPCDSService.Util.getInstance().fetch(new AsyncCallback<List<SimpleGwtRPCDSRecord>>() {

                                    public void onFailure(Throwable cause) {
                                        fail(cause.getMessage());
                                    }

                                    public void onSuccess(List<SimpleGwtRPCDSRecord> records) {
                                        assertTrue(records.size() < recordSizeWithDummy);
                                        finishTest();
                                    }
                                });

                            }
                        });
                    }
                });
            }
        });
    }
    
    public void testUpdate() {

        delayTestFinish(10 * 1000);

        SimpleGwtRPCDSRecord dummy = new SimpleGwtRPCDSRecord();

        dummy.setId(TEST_RECORD_ID);
        dummy.setName(TEST_RECORD_NAME);
        dummy.setDate(TEST_RECORD_DATE);

        SimpleGwtRPCDSService.Util.getInstance().update(dummy, new AsyncCallback<SimpleGwtRPCDSRecord>() {

            public void onFailure(Throwable cause) {
                fail(cause.getMessage());
            }

            public void onSuccess(SimpleGwtRPCDSRecord record) {

                assertNotNull(record);

                assertTrue(TEST_RECORD_ID.equals(record.getId()));

                assertEquals(TEST_RECORD_NAME, record.getName());

                assertEquals(TEST_RECORD_DATE, record.getDate());

                finishTest();
            }
        });
    }

}
