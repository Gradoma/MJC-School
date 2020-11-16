package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
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
@RequestMapping(value = GiftCertificateController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    public static final String URL = "/certificate";
    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService){
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id){
        GiftCertificateDto certificateDto = giftCertificateService.getById(id);
        return ResponseEntity.ok().body(certificateDto);
    }

    @GetMapping("/by")
    public ResponseEntity<List<GiftCertificateDto>>
    getByCriteria(@RequestParam(value = "tag", required = false) String tag,
                  @RequestParam(value = "name", required = false) String name,
                  @RequestParam(value = "description", required = false) String description,
                  @RequestParam(value = "sort", required = false, defaultValue = "name") String sortBy,
                  @RequestParam(value = "order", required = false, defaultValue = "asc") String order){
        List<GiftCertificateDto> certificateDtoList = giftCertificateService
                .getByCriteria(tag, name, description, sortBy, order);
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
}
