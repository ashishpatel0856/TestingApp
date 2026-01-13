package com.codingshuttle.TestingApp;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
class TestingAppApplicationTests {

	@BeforeEach
	void setUp() {
		log.info("Starting the method, setting up config");
	}

	@AfterEach
	void tearDown() {
		log.info("Tearing down the method");
	}

	@BeforeAll
	static void setUpOnce() {
		log.info("Setup Once...");
	}

	@AfterAll
    static void tearDownOnce() {
		log.info("Tearing down all...");
	}

	@Test
//	@Disabled
	void testNumberOne() {
		int a = 5;
		int b = 3;

		int result = addTwoNumbers(a, b);

//		Assertions.assertEquals(8, result);
//		Assertions.

//		assertThat(result)
//				.isEqualTo(7)
//				.isCloseTo(9, Offset.offset(1));

//		assertThat("Apple")
//				.isEqualTo("Apple")
//				.startsWith("App")
//				.endsWith("l")
//				.hasSize(3);

	}

	@Test
//	@DisplayName("displayTestNameTwo")
	void testDivideTwoNumbers_whenDenominatorIsZero_ThenArithmeticException() {

		int a = 5;
		int b = 0;

		assertThatThrownBy(() -> divideTwoNumbers(a, b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Tried to divide by zero");

	}

	int addTwoNumbers(int a, int b) {
		return a+b;
	}

	double divideTwoNumbers(int a, int b) {
		try {
			return a/b;
		} catch (ArithmeticException e) {
			log.error("Arithmentic excepiton occured: "+e.getLocalizedMessage());
			throw new ArithmeticException("Tried to divide by zero");
		}
	}

}


