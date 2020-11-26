package com.epam.esm.controller;

import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.sorting.Order;
import com.epam.esm.service.sorting.SortingCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = GiftCertificateController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    public static final String URL = "/certificates";
    private final GiftCertificateService giftCertificateService;
    private static final Logger log = LogManager.getLogger();

    public GiftCertificateController(GiftCertificateService giftCertificateService){
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id){
        GiftCertificateDto certificateDto = giftCertificateService.getById(id);
        addLinks(certificateDto);
        return ResponseEntity.ok().body(certificateDto);
    }

    @GetMapping()
    public ResponseEntity<List<GiftCertificateDto>> getByCriteria(CertificateCriteria criteria){
        List<GiftCertificateDto> certificateDtoList = giftCertificateService
                .getByCriteria(criteria);
        certificateDtoList.forEach(certificateDto -> addLinks(certificateDto));
        return ResponseEntity.ok().body(certificateDtoList);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateDto> create(@RequestBody @Valid GiftCertificateDto certificateDto){
        long generatedId = giftCertificateService.add(certificateDto);
        URI resourceUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/" + generatedId).build().toUri();
        return ResponseEntity.created(resourceUri).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateDto> update(@RequestBody @Valid GiftCertificateDto certificateDto,
                                                     @PathVariable long id){
        giftCertificateService.update(certificateDto, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> deleteById(@PathVariable long id){
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinks(GiftCertificateDto certificateDto){
        certificateDto.getTags().forEach(tagDto -> {
            long tagId = Long.parseLong(tagDto.getId());
            Link tagLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                    .getById(tagId))
                    .withRel("tag");
            tagDto.add(tagLink);
        });
    }
}
