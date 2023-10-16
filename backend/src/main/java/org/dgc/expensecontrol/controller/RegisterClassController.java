package org.dgc.expensecontrol.controller;

import java.util.List;

import org.dgc.expensecontrol.model.RegisterClass;
import org.dgc.expensecontrol.service.ClassService;
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
@RequestMapping(path = "api/v1/class")
public class RegisterClassController {
    private ClassService classService;

    public RegisterClassController(ClassService classService) {
        this.classService = classService;
    }

    // create
    @Operation(summary = "Create a class registry on database", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The class object to be created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterClass.class))))
    @ApiResponses(@ApiResponse(responseCode = "201", description = "The creation process ocurred with success", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterClass.class))
    }))
    @PostMapping("")
    public ResponseEntity<RegisterClass> createClass(@RequestBody RegisterClass newClass) {
        return ResponseEntity.created(null).body(classService.createClass(newClass));
    }

    // read
    @Operation(summary = "Read a list of all classess from database")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The reading process was a success and the list is on the body", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(items = @Schema(implementation = RegisterClass.class)))
    }))
    @GetMapping("")
    public ResponseEntity<List<RegisterClass>> readClasses() {
        return ResponseEntity.ok(classService.readClasses());
    }

    @Operation(summary = "Read a class from database by its email from its user")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The reading process was a success and the class object is on the body", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterClass.class))
    }))
    @GetMapping("/{userEmail}")
    public ResponseEntity<List<RegisterClass>> readClassesByUser(
            @Parameter(description = "email from the user associated with the class being searched for") @PathVariable(name = "userEmail") String userEmail) {
        return ResponseEntity.ok(classService.readClassesByUser(userEmail));
    }

    // update
    @Operation(summary = "Update a class by its id", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Class object with updated information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterClass.class))))
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The updating process was a success and the class object updated is on the body", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterClass.class))
    }))
    @PutMapping("/{id}")
    public ResponseEntity<RegisterClass> updateClass(
            @Parameter(description = "id from the class to be updated") @PathVariable("id") Long id,
            @RequestBody RegisterClass updatedClass) {
        return ResponseEntity.ok(classService.updateClass(id, updatedClass));
    }

    // delete
    @Operation(summary = "Delete a class from database by its id")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "The delete process was a success"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(
            @Parameter(description = "id from the class to be deleted") @PathVariable("id") Long id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}
