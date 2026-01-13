package com.codingshuttle.TestingApp.repository;

import com.codingshuttle.TestingApp.entities.Employee;
import com.codingshuttle.TestingApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


//@SpringBootTest

// @Import(TestContainerConfiguration.class)

@DataJpaTest
class EmployeeReposityoryTests {

   @Autowired
   private EmployeeRepository employeeRepository;
   private Employee employee;


   @BeforeEach
   void setUp(){
      employee = Employee.builder()
              .id(1L)
              .name("ashish kumar")
              .email("ashish@gmail.com")
              .salary(100l)
              .build();
   }

   @Test
   void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
      // arrange given  phle text data database me set krta
//       Employee employee = new Employee();
//       employee.setName("Amit Sharma");
//       employee.setEmail("amit.sharma@example.com");
      employeeRepository.save(employee);

      // Act (When) - fir data base se data search krta hai
      List<Employee> listEmployee = employeeRepository.findByEmail(employee.getEmail());

      // Assert (Then) - ab text krta h data shi hai ki nhi
      assertThat(listEmployee).isNotNull();  // लिस्ट null नहीं होनी चाहिए
      assertThat(listEmployee).isNotEmpty(); // लिस्ट खाली नहीं होनी चाहिए
      assertThat(listEmployee.get(0).getEmail()).isEqualTo(employee.getEmail()); // पहला employee का email सही होना चाहिए
   }


   @Test
   void testFindByEmail_whenEmailIsNotValid_thenReturnEmptyEmployee() {

      //given
      String email="notemail123@gmail.com";

      //when
      List<Employee> listemployee = employeeRepository.findByEmail(email);

      //then
      assertThat(listemployee).isNotNull();
      assertThat(listemployee).isEmpty();
   }

}
