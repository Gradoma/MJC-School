package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import org.springframework.hateoas.CollectionModel;
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

    @GetMapping("/by")
    public ResponseEntity<List<OrderDto>> getByUserId(@RequestParam(value = "user") long userId,
                                                      @RequestParam(value = "offset", required = false) String offset,
                                                      @RequestParam(value = "limit", required = false) Integer limit){
        List<OrderDto> orderDtoList = orderService.getByUserId(userId, offset, limit);
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
        orderDto.add(certificateLink);
        // todo add user link
    }
}
