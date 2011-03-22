package com.christan.pagetests;

import org.junit.Before;

public class AboutUsTest extends GenericPageTest {
	@Before
	public void setUp() {
		url = "http://www.pingidentity.com/about-us/";
		initialize();
	}
}
