package com.christan.pagetests;

import org.junit.Before;

public class OurSolutionsSsoCloudIdentity extends GenericPageTest {
	@Before
	public void setUp() {
		url = "http://www.pingidentity.com/our-solutions/sso-cloud-identity.cfm";
		initialize();
	}
}
