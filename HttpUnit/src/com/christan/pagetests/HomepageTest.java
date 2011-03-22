package com.christan.pagetests;

import org.junit.Before;

public class HomepageTest extends GenericPageTest {
	@Before
	public void setUp() {
		url = "http://www.pingidentity.com/";
		initialize();
	}
}
