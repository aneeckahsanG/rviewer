package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.domain.requests.DispenserSpendingLineRequest;
import com.rviewer.skeletons.domain.requests.DispenserRequest;
import com.rviewer.skeletons.domain.services.DispenserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping(value = "/dispenser")
public class DispenserController {
    @Autowired
    DispenserService dispenserService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity findById(@PathVariable String id) {
        return ResponseEntity.ok(dispenserService.findById(id));
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody @Valid DispenserRequest dispenserRequest) { //
        return ResponseEntity.ok(dispenserService.save(dispenserRequest));
    }
    @RequestMapping(value = "/{id}/spending", method = RequestMethod.GET)
    public ResponseEntity findAllByDispenserId(@PathVariable @NotEmpty String id) {
        return ResponseEntity.ok(dispenserService.findAllSpendingLineByDispenserId(id));
    }
    @RequestMapping(value = "{id}/status", method = RequestMethod.PUT)
    public ResponseEntity saveDispenserSpendingLine(@PathVariable @NotEmpty String id, @RequestBody @Valid DispenserSpendingLineRequest dispenserSpendingLineRequest) {
        return ResponseEntity.ok(dispenserService.saveDispenserSpendingLine(id, dispenserSpendingLineRequest));
    }
}
