package com.sampleapp.restapi.service;

import com.sampleapp.restapi.model.Employee;
import com.sampleapp.restapi.model.EmployeeItem;
import com.sampleapp.restapi.model.Item;
import com.sampleapp.restapi.repository.EmployeeItemRepository;
import com.sampleapp.restapi.repository.EmployeeRepository;
import com.sampleapp.restapi.repository.ItemRepository;
import com.sampleapp.restapi.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ItemRepository itemRepository;

    private final EmployeeItemRepository employeeItemRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           ItemRepository itemRepository, EmployeeItemRepository employeeItemRepository) {
        this.employeeRepository = employeeRepository;
        this.itemRepository = itemRepository;
        this.employeeItemRepository = employeeItemRepository;
    }

    @Transactional(readOnly = true)
    public EmployeeWithItemsResponseDTO getEmployeeWithItems(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
        Set<EmployeeItem> employeeItems = employee.getEmployeeItems();

        Set<ItemWithQuantityResponseDTO> items = new HashSet<>();
        for (EmployeeItem employeeItem : employeeItems) {
            items.add(ItemWithQuantityResponseDTO.builder()
                    .id(employeeItem.getId())
                    .name(employeeItem.getItem().getName())
                    .quantity(employeeItem.getQuantity())
                    .price(employeeItem.getItem().getPrice())
                    .build()
            );
        }
        return new EmployeeWithItemsResponseDTO(EmployeeResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .build(), items);
    }

    public List<ItemWithQuantityResponseDTO> getEmployeeItemsById(Long id) {
        Employee foundEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id " + id));
        Set<EmployeeItem> employeeItems = foundEmployee.getEmployeeItems();

        List<ItemWithQuantityResponseDTO> itemWithQuantityResponseDTOS = new ArrayList<>();
        for (EmployeeItem employeeItem : employeeItems) {
            itemWithQuantityResponseDTOS.add(
                    ItemWithQuantityResponseDTO.builder()
                            .id(employeeItem.getItem().getId())
                            .name(employeeItem.getItem().getName())
                            .quantity(employeeItem.getQuantity())
                            .price(employeeItem.getItem().getPrice())
                            .build()
            );
        }
        return itemWithQuantityResponseDTOS;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO employeeDto) {
        Employee savedEmployee = employeeRepository.save(Employee.builder().name(employeeDto.getName()).build());
        return EmployeeResponseDTO.builder()
                .id(savedEmployee.getId())
                .name(savedEmployee.getName())
                .build();
    }

    @Transactional
    public EmployeeWithItemsResponseDTO createEmployeeWithItems(CreateEmployeeWithItemsRequestDTO requestDTO) {

        Employee employee = Employee.builder()
                .name(requestDTO.getEmployee().getName())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        Set<EmployeeItem> employeeItemSet = new HashSet<>();
        Set<Item> items = requestDTO.getItems().stream()
                .map(createItemRequestDTO -> {
                    Item item = Item.builder()
                            .name(createItemRequestDTO.getName())
                            .price(createItemRequestDTO.getPrice())
                            .build();
                    employeeItemSet.add(EmployeeItem.builder()
                            .employee(savedEmployee)
                            .item(item)
                            .quantity(createItemRequestDTO.getQuantity())
                            .build());
                    return item;
        }).collect(Collectors.toSet());

        List<Item> savedItems = itemRepository.saveAll(items);

        List<EmployeeItem> employeeItemList = employeeItemRepository.saveAll(employeeItemSet);

        Set<ItemWithQuantityResponseDTO> itemWithQuantityResponseDTOS = employeeItemList.stream()
                        .map(employeeItem ->
                            ItemWithQuantityResponseDTO.builder()
                                    .id(employeeItem.getItem().getId())
                                    .name(employeeItem.getItem().getName())
                                    .price(employeeItem.getItem().getPrice())
                                    .quantity(employeeItem.getQuantity())
                                    .build()
                        ).collect(Collectors.toSet());

        return EmployeeWithItemsResponseDTO.builder()
                .employee(EmployeeResponseDTO.builder()
                        .id(savedEmployee.getId())
                        .name(savedEmployee.getName())
                        .build())
                .items(itemWithQuantityResponseDTOS)
                .build();
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, UpdateEmployeeRequestDTO employeeDto) {
        Employee foundEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id " + id));

        foundEmployee.setName(employeeDto.getName());
        Employee updatedEmployee = employeeRepository.save(foundEmployee);
        return EmployeeResponseDTO.builder()
                .id(updatedEmployee.getId())
                .name(updatedEmployee.getName())
                .build();
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id " + id));

        employeeRepository.delete(employee);
    }

    public Page<EmployeeWithItemsResponseDTO> findByNameContaining(String name, Pageable pageable) {
        Page<Employee> employeePageList = employeeRepository.findByNameContaining(name, pageable);
        return mapEmployeePageToEmployeeWithItemsPage(employeePageList);
    }

    public Page<EmployeeAssetsDto> employeeAssetsFindByNameContaining(String name, Pageable pageable) {
        Page<Employee> employeePageList = employeeRepository.findByNameContaining(name, pageable);
        return mapEmployeePageToEmployeeAssetsDtoPage(employeePageList);
    }


    public Page<EmployeeAssetsDto> mapEmployeePageToEmployeeAssetsDtoPage(Page<Employee> employeePage) {
        return employeePage.map(employee -> EmployeeAssetsDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .itemQuantity(employee.getEmployeeItems().size())
                .itemsTotalPrice(employee.getEmployeeItems().stream()
                        .map(employeeItem -> {
                            return employeeItem.getItem().getPrice();
                        }).mapToDouble(Double::doubleValue).sum())
                .build());
    }

    public Page<EmployeeWithItemsResponseDTO> mapEmployeePageToEmployeeWithItemsPage(Page<Employee> employeePage) {
        return employeePage.map(employee -> EmployeeWithItemsResponseDTO.builder()

                .employee(EmployeeResponseDTO.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .build())
                .items(employee.getEmployeeItems().stream().map(
                        employeeItem -> ItemWithQuantityResponseDTO.builder()
                                .id(employeeItem.getItem().getId())
                                .name(employeeItem.getItem().getName())
                                .price(employeeItem.getItem().getPrice())
                                .quantity(employeeItem.getQuantity())
                                .build()
                ).collect(Collectors.toSet()))
                .build());
    }


}
