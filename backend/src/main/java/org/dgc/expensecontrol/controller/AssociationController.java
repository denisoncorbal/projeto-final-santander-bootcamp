package org.dgc.expensecontrol.controller;

import java.util.Optional;

import org.dgc.expensecontrol.model.Register;
import org.dgc.expensecontrol.model.RegisterClass;
import org.dgc.expensecontrol.service.AssociationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v1/association")
public class AssociationController {
    private AssociationService associationService;

    public AssociationController(AssociationService associationService) {
        this.associationService = associationService;
    }

    @Operation(summary = "Associate a register by its id to a user by his e-mail and to a class by his name")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The association ocurred with success", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Register.class))
    }))
    @PostMapping("/register/{registerId}")
    public ResponseEntity<Register> associateRegister(
            @Parameter(description = "id of the register to be associated with the user and the class") @PathVariable("registerId") Long registerId,
            @Parameter(description = "email from the user to be associated with the register") @RequestParam(name = "userEmail") Optional<String> userEmail,
            @Parameter(description = "name from the class to be associated with the register") @RequestParam(name = "className") Optional<String> className) {
        if (userEmail.isPresent() && className.isPresent())
            return ResponseEntity
                    .ok(associationService.associateRegister(registerId, userEmail.get(), className.get()));
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Associate a class by its id to a user by his e-mail")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The association ocurred with success", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterClass.class))
    }))
    @PostMapping("/class/{classId}")
    public ResponseEntity<RegisterClass> associateRegisterClass(
            @Parameter(description = "id of the class to be associated with the user") @PathVariable("classId") Long classId,
            @Parameter(description = "email from the user to be associated with the class") @RequestParam(name = "userEmail") Optional<String> userEmail) {
        if (userEmail.isPresent())
            return ResponseEntity.ok(associationService.associateRegisterClass(classId, userEmail.get()));
        return ResponseEntity.badRequest().build();
    }

}
