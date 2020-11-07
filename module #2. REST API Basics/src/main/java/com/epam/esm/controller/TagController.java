package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = TagController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    public static final String URL = "/tag";
    private TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getAll(){
        return tagService.getAll();
    }

//    @GetMapping("{/id}")
//    public TagDto getById(@PathVariable("id")long id){
//        return tagService.getById(id);
//    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> create(@RequestBody TagDto tagDto){
        long generatedId = tagService.save(tagDto);
        URI resourceUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/" + generatedId).build().toUri();
        return ResponseEntity.created(resourceUri).build();
    }

    @GetMapping("/search")
    public ResponseEntity<TagDto> getByName(@RequestParam("name") String name){
        Optional<TagDto> optionalTagDto = tagService.getByName(name);
        return optionalTagDto.map(tagDto -> ResponseEntity.ok().body(tagDto))
                .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @ExceptionHandler({ResourceNotFoundException.class, ConstraintViolationException.class, DuplicateException.class})
    public ResponseEntity<String> handle(Exception ex) {
        if(ex.getClass().equals(ResourceNotFoundException.class)){
            return ResponseEntity.notFound().header("exception", ex.getMessage()).build();
        }
        if(ex.getClass().equals(ConstraintViolationException.class) | ex.getClass().equals(DuplicateException.class)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
