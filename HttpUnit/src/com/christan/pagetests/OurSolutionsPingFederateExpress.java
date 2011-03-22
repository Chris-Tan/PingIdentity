package com.christan.pagetests;

import org.junit.Before;

public class OurSolutionsPingFederateExpress extends GenericPageTest {
	@Before
	public void setUp() {
		url = "http://www.pingidentity.com/our-solutions/pingfederate-express.cfm";
		initialize();
	}
}
