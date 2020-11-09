package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping(value = TagController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    public static final String URL = "/tag";
    private final TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getAll(){
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable long id){
        TagDto tagDto = tagService.getById(id);
        return ResponseEntity.ok().body(tagDto);
    }

    @GetMapping("/name")    //todo(remove /name - should work without it, and no conflict with getAll)
    public ResponseEntity<TagDto> getByName(@RequestParam("name") String name){
        Optional<TagDto> optionalTagDto = tagService.getByName(name);
        return optionalTagDto.map(tagDto -> ResponseEntity.ok().body(tagDto))
                .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> create(@RequestBody TagDto tagDto){
        long generatedId = tagService.save(tagDto);
        URI resourceUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/" + generatedId).build().toUri();
        return ResponseEntity.created(resourceUri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TagDto> deleteById(@PathVariable long id){
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @ExceptionHandler({ResourceNotFoundException.class, ConstraintViolationException.class, DuplicateException.class})
//    @ResponseBody
//    public ResponseEntity<String> handle(Exception ex, Locale locale) {
//        if(ex.getClass().equals(ResourceNotFoundException.class)){
//            String message = messageSource.getMessage("message.notFound", new Object[]{ex.getMessage()}, locale);
//            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
//        }
//        if(ex.getClass().equals(ConstraintViolationException.class)){
//            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//        if(ex.getClass().equals(DuplicateException.class)){
//            String message = messageSource.getMessage("message.duplicate", new Object[]{ex.getMessage()}, locale);
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
