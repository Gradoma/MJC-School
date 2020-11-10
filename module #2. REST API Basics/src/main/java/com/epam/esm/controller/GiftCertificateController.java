package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = GiftCertificateController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    public static final String URL = "/certificate";
    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService){
        this.giftCertificateService = giftCertificateService;
    }
//
//    @GetMapping
//    public ResponseEntity<List<GiftCertificateDto>> getAll(){
//        List<GiftCertificateDto> certificateDtoList = giftCertificateService.getAll();
//        return ResponseEntity.ok().body(certificateDtoList);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id){
        GiftCertificateDto certificateDto = giftCertificateService.getById(id);
        return ResponseEntity.ok().body(certificateDto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateDto> create(@RequestBody GiftCertificateDto certificateDto){
        long generatedId = giftCertificateService.add(certificateDto);
        URI resourceUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/" + generatedId).build().toUri();
        return ResponseEntity.created(resourceUri).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateDto> update(@RequestBody GiftCertificateDto certificateDto,
                                                     @PathVariable long id){
        giftCertificateService.update(certificateDto, id);
        return ResponseEntity.noContent().build();
    }
}
