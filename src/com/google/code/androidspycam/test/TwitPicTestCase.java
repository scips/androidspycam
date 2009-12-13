package com.google.code.androidspycam.test;

import static org.junit.Assert.* ;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.code.androidspycam.TwitPic;


public class TwitPicTestCase {
	
	private TwitPic twitpic;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.twitpic = new TwitPic("test","test");
	}

	@After
	public void tearDown() throws Exception {
	}

	// Test connectivity
	@Test
	public void testConnect() {
		fail("Not yet implemented");
	}
	
	// Test class creation
	@Test
	public void testCreateClass() {
		assertEquals("class com.google.code.androidspycam.TwitPic", this.twitpic.getClass().toString());
	}

	// test image upload
	@Test
	public void testUploadImage() {
		int result = this.twitpic.upload("test message");
		assertTrue(result==1);
	}
}
