package com.sampleapp.restapi.controller;

import com.sampleapp.restapi.service.EmployeeService;
import com.sampleapp.restapi.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Get All Employees with search by Name functionality, pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Employees",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employees not found",
                    content = @Content)})
    @GetMapping("/")
    public ResponseEntity<Page<EmployeeWithItemsResponseDTO>> searchEmployees(
            @Parameter(description = "Search by Employee Name containing")
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageRequest.of(page, size, direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        return ResponseEntity.ok(employeeService.findByNameContaining(name, pageable));
    }

    @Operation(summary = "Get Summary Assets of Employees with search by Name functionality, pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Employees",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employees not found",
                    content = @Content)})
    @GetMapping("/summary")
    public Page<EmployeeAssetsDto> searchEmployeesSummary(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageRequest.of(page, size, direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        return employeeService.employeeAssetsFindByNameContaining(name, pageable);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<EmployeeWithItemsResponseDTO> getEmployeeWithItems(@PathVariable("id") Long id) {
        EmployeeWithItemsResponseDTO employeeWithItemsResponseDTO = employeeService.getEmployeeWithItems(id);
        return ResponseEntity.ok(employeeWithItemsResponseDTO);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemWithQuantityResponseDTO>> getEmployeeItemsById(@PathVariable("id") Long id) {
        List<ItemWithQuantityResponseDTO> items = employeeService.getEmployeeItemsById(id);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody CreateEmployeeRequestDTO employeeDto) {
        EmployeeResponseDTO savedEmployeeResponseDTO = employeeService.createEmployee(employeeDto);
        return ResponseEntity.created(URI.create("/" + savedEmployeeResponseDTO.getId()))
                .body(savedEmployeeResponseDTO);
    }

    @Operation(summary = "Add Employee with multiple items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Employee",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)})
    @PostMapping("/items")
    public ResponseEntity<EmployeeWithItemsResponseDTO> saveEmployeeWithItems(@RequestBody @Valid CreateEmployeeWithItemsRequestDTO request) {
        EmployeeWithItemsResponseDTO employeeWithItems = employeeService.createEmployeeWithItems(request);
        return ResponseEntity.created(URI.create("/" + employeeWithItems.getEmployee().getId()))
                .body(employeeWithItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody UpdateEmployeeRequestDTO employeeDto) {
        EmployeeResponseDTO updatedEmployeeDTO = employeeService.updateEmployee(id, employeeDto);
        return ResponseEntity.ok(updatedEmployeeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
