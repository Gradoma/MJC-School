package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@Validated
@RestController
@RequestMapping(value = OrderController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    public static final String URL = "/orders";
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable long id){
        OrderDto orderDto = orderService.getById(id);
        addLinks(orderDto);
        return ResponseEntity.ok().body(orderDto);
    }

    @GetMapping()
    public ResponseEntity<List<OrderDto>> getByUserId(@RequestParam(value = "user") long userId,
                                                      @RequestParam(value = "page", required = false,
                                                              defaultValue = "1") int page){
        List<OrderDto> orderDtoList = orderService.getByUserId(userId, page);
        orderDtoList.forEach(orderDto -> addLinks(orderDto));
        return ResponseEntity.ok().body(orderDtoList);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto){
        long generatedId = orderService.save(orderDto);
        URI resourceUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/" + generatedId).build().toUri();
        return ResponseEntity.created(resourceUri).build();
    }

    private void addLinks(OrderDto orderDto){
        long certificateId = orderDto.getCertificateId();
        Link certificateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                .getById(certificateId))
                .withRel("certificate");
        long userId = orderDto.getUserId();
        Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getById(userId))
                .withRel("user");
        orderDto.add(certificateLink);
        orderDto.add(userLink);
    }
}
