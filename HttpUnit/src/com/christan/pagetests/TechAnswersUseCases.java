package com.christan.pagetests;

import org.junit.Before;

public class TechAnswersUseCases extends GenericPageTest {
	@Before
	public void setUp() {
		url = "http://www.pingidentity.com/tech-answers/use-cases/";
		initialize();
	}
}
