package org.dgc.expensecontrol.controller;

import java.util.List;

import org.dgc.expensecontrol.model.Register;
import org.dgc.expensecontrol.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(path = "api/v1/register")
public class RegisterController {
    private RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    // create
    @Operation(summary = "Create a register registry on database", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The register object to be created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Register.class))))
    @ApiResponses(@ApiResponse(responseCode = "201", description = "The creation process ocurred with success", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Register.class))
    }))
    @PostMapping("")
    public ResponseEntity<Register> createRegister(@RequestBody Register newRegister) {
        return ResponseEntity.created(null).body(registerService.createRegister(newRegister));
    }

    // read
    @Operation(summary = "Read a list of all registers from database")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The reading process was a success and the list is on the body", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(items = @Schema(implementation = Register.class)))
    }))
    @GetMapping("")
    public ResponseEntity<List<Register>> readRegisters() {
        return ResponseEntity.ok(registerService.readRegisters());
    }

    @Operation(summary = "Read a register from database by email from its user")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The reading process was a success and the register object is on the body", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Register.class))
    }))
    @GetMapping("/{userEmail}")
    public ResponseEntity<List<Register>> readRegistersByUser(@PathVariable(name = "userEmail") String userEmail) {
        return ResponseEntity.ok(registerService.readRegistersByUser(userEmail));
    }

    @Operation(summary = "Read a register from database by email from its user")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The reading process was a success and the register object is on the body", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Register.class))
    }))
    @GetMapping("/{userEmail}/{type}")
    public ResponseEntity<List<Register>> readRegistersByUserAndType(@PathVariable(name = "userEmail") String userEmail,
            @PathVariable(name = "type") String type) {
        return ResponseEntity.ok(registerService.readRegistersByUserAndType(userEmail, type));
    }

    // update
    @Operation(summary = "Update a register by its id", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Register object with updated information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Register.class))))
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The updating process was a success and the register object updated is on the body", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Register.class))
    }))
    @PutMapping("/{id}")
    public ResponseEntity<Register> updateRegister(
            @Parameter(description = "id from the register to be updated") @PathVariable("id") Long id,
            @RequestBody Register updatedRegister) {
        return ResponseEntity.ok(registerService.updateRegister(id, updatedRegister));
    }

    // delete
    @Operation(summary = "Delete a register from database by its id")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "The delete process was a success"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegister(
            @Parameter(description = "id from the register to be deleted") @PathVariable("id") Long id) {
        registerService.deleteRegister(id);
        return ResponseEntity.noContent().build();
    }
}
