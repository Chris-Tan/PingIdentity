package com.christan.pagetests;

import org.junit.Before;

public class OurSolutionsPingConnect extends GenericPageTest {
	@Before
	public void setUp() {
		url = "http://www.pingidentity.com/our-solutions/pingconnect.cfm";
		initialize();
	}
}
