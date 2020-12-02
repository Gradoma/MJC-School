package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = TagController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    public static final String URL = "/tags";
    private final TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getAll(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page){
        return tagService.getAll(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable long id){
        TagDto tagDto = tagService.getById(id);
        return ResponseEntity.ok().body(tagDto);
    }

    @GetMapping("/name")
    public ResponseEntity<TagDto> getByName(@RequestParam("name")
                                                @NotNull
                                                @Size(min = 1, max = 20)String name){
        TagDto tagDto = tagService.getByName(name);
        return ResponseEntity.ok().body(tagDto);
    }

    @GetMapping("/popular")
    public ResponseEntity<TagDto> getPopular(){
        TagDto tagDto = tagService.getMostPopular();
        return ResponseEntity.ok().body(tagDto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> create(@RequestBody @Valid TagDto tagDto){        //todo (rename to createTag?)
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
}
