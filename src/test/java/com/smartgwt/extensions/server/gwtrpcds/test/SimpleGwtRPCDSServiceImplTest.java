package com.smartgwt.extensions.server.gwtrpcds.test;

import java.util.List;

import junit.framework.TestCase;

import com.smartgwt.extensions.client.gwtrpcds.test.SimpleGwtRPCDSTestConfig;
import com.smartgwt.extensions.gwtrpcds.client.example.SimpleGwtRPCDSRecord;
import com.smartgwt.extensions.gwtrpcds.server.example.SimpleGwtRPCDSServiceImpl;

public class SimpleGwtRPCDSServiceImplTest extends TestCase implements
		SimpleGwtRPCDSTestConfig {

	private SimpleGwtRPCDSServiceImpl service;

	protected void setUp() throws Exception {
		super.setUp();
		service = new SimpleGwtRPCDSServiceImpl();
	}

	public void testAdd() throws Exception {
		SimpleGwtRPCDSRecord dummy = new SimpleGwtRPCDSRecord();
		dummy.setName(TEST_RECORD_NAME);
		dummy.setDate(TEST_RECORD_DATE);
		SimpleGwtRPCDSRecord record = service.add(dummy);
		assertNotNull(record);
		assertTrue(record.getId() > 0);
		assertEquals(TEST_RECORD_NAME, record.getName());
		assertEquals(TEST_RECORD_DATE, record.getDate());
	}

	public void testFetch() throws Exception {
		List<SimpleGwtRPCDSRecord> records = service.fetch();
		assertNotNull(records);
		assertTrue(records.size() > 0);
	}

	public void testRemove() throws Exception {
		SimpleGwtRPCDSRecord dummy = new SimpleGwtRPCDSRecord();
		dummy.setName(TEST_RECORD_NAME);
		dummy.setDate(TEST_RECORD_DATE);
		SimpleGwtRPCDSRecord record = service.add(dummy);
		int recordSizeWithDummy = service.fetch().size();
		service.remove(record);
		assertTrue(service.fetch().size() < recordSizeWithDummy);
	}

	public void testUpdate() throws Exception {
		SimpleGwtRPCDSRecord dummy = new SimpleGwtRPCDSRecord();
		dummy.setId(TEST_RECORD_ID);
		dummy.setName(TEST_RECORD_NAME);
		dummy.setDate(TEST_RECORD_DATE);
		SimpleGwtRPCDSRecord record = service.update(dummy);
		assertNotNull(record);
		assertTrue(record.getId() == TEST_RECORD_ID);
		assertEquals(TEST_RECORD_NAME, record.getName());
		assertEquals(TEST_RECORD_DATE, record.getDate());
	}

}
