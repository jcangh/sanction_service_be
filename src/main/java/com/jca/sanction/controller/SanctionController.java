package com.jca.sanction.controller;

import com.jca.sanction.dto.Sanction;
import com.jca.sanction.service.SanctionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/sanctions")
public class SanctionController {

    private final SanctionService service;

    @PostMapping
    public ResponseEntity<Sanction> create(@RequestBody Sanction sanction){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(sanction));
    }


}
