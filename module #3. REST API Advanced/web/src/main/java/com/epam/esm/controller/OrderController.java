package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = OrderController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    public static final String URL = "/order";
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable long id){
        OrderDto orderDto = orderService.getById(id);
        return ResponseEntity.ok().body(orderDto);
    }

    @GetMapping("/by")
    public ResponseEntity<List<OrderDto>> getByUserId(@RequestParam(value = "user") long userId){
        List<OrderDto> orderDto = orderService.getByUserId(userId);
        return ResponseEntity.ok().body(orderDto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto){
        long generatedId = orderService.save(orderDto);
        URI resourceUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/" + generatedId).build().toUri();
        return ResponseEntity.created(resourceUri).build();
    }
}
