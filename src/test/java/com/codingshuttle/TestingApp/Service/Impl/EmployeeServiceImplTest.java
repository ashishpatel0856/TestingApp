
package com.codingshuttle.TestingApp.Service.Impl;

import com.codingshuttle.TestingApp.TestContainerConfiguration;
import com.codingshuttle.TestingApp.dto.EmployeeDto;
import com.codingshuttle.TestingApp.entities.Employee;
import com.codingshuttle.TestingApp.exceptions.ResourceNotFoundException;
import com.codingshuttle.TestingApp.repositories.EmployeeRepository;
import com.codingshuttle.TestingApp.services.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService; // Changed to the correct implementation class

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp() {
        mockEmployee = Employee.builder()
                .id(1L)
                .name("Ashish Patel")
                .email("ashish.patel@gmail.com")
                .salary(145L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee, EmployeeDto.class);
    }



    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto() {
        // Arrange
        Long id = mockEmployee.getId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));

        // Act
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        // Assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(mockEmployee.getId()); // Fixed assertion
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository, only()).findById(id);
    }


    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenThrowExceptions(){
        //arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act and assert

        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee with id 1 not found");


        verify(employeeRepository).findById(1L);
    }
    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee() {
        // Arrange

        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        // Act
        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);

        // Assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository, only()).save(any(Employee.class));
    }

    @Test
    void testCreateNewEmployee_WhenAttemptingToCreateNewEmployee_ThenThrowExceptions(){
        // arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));
        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDto))
        .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee with email '" + mockEmployeeDto.getEmail() + "' not found");


        //act and assert
        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository, never()).save(any(Employee.class));

    }

    @Test
    void testUpdateEmployee_WhenEmployeeNotExist_ThenThrowExceptions(){
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() ->employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee with id 1 not found");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).findById(1L);
    }


    @Test
    void testUpdateEmployee_WhenAttemptimgExistEmployee_ThenThrowExceptions(){
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployee.setName("ashish kumar");
        mockEmployee.setEmail("ashish.kumar@gmail.com");
        assertThatThrownBy(() ->employeeService.updateEmployee(1L,mockEmployeeDto))
        .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee with id 1 can not be updated");

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository, never()).findById(mockEmployeeDto.getId());

    }


    @Test
    void testUpdateEmployee_WhenValidEmployee_ThenUpdateEmployee() {

        // arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("nanke");
        mockEmployeeDto.setSalary(200l);
        Employee newEmployee = modelMapper.map(mockEmployeeDto, Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        // act and assert

        EmployeeDto updatedEmployeeDto = employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);
        assertThat(updatedEmployeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee_WhenEmployeeNotExist_ThenThrowExceptions(){
        when(employeeRepository.existsById(1L)).thenReturn(false);

        //act
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
        .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee with id 1 not found");

        verify(employeeRepository,never()).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_WhenValidEmployee_ThenDeleteEmployee() {
        // arrange
        when(employeeRepository.existsById(mockEmployeeDto.getId())).thenReturn(true);

        assertThatCode(()-> employeeService.deleteEmployee(mockEmployeeDto.getId()))
                .doesNotThrowAnyException();

        // act
        verify(employeeRepository).deleteById(mockEmployeeDto.getId());
    }
}
