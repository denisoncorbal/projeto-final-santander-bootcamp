package org.dgc.expensecontrol.controller;

import java.util.List;

import org.dgc.expensecontrol.model.RegisterUser;
import org.dgc.expensecontrol.service.UserService;
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
@RequestMapping(path = "api/v1/user")
public class RegisterUserController {

    private UserService userService;

    public RegisterUserController(UserService userService) {
        this.userService = userService;
    }

    // create
    @Operation(summary = "Create a user registry on database", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The register object to be created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUser.class))))
    @ApiResponses(@ApiResponse(responseCode = "201", description = "The creation process ocurred with success", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUser.class))
    }))
    @PostMapping("")
    public ResponseEntity<RegisterUser> createUser(@RequestBody RegisterUser newUser) {
        return ResponseEntity.created(null).body(userService.createUser(newUser));
    }

    // read
    @Operation(summary = "Read a list of all users from database")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The reading process was a success and the list is on the body", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(items = @Schema(implementation = RegisterUser.class)))
    }))
    @GetMapping("")
    public ResponseEntity<List<RegisterUser>> readUsers() {
        return ResponseEntity.ok(userService.readUser());
    }

    @Operation(summary = "Read a user from database by its email")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The reading process was a success and the user object is on the body", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUser.class))
    }))
    @GetMapping("/{userEmail}")
    public ResponseEntity<RegisterUser> readUser(
            @Parameter(description = "email from the user to be searched") @PathVariable(name = "userEmail") String userEmail) {
        return ResponseEntity.ok(userService.readUser(userEmail));
    }

    // update
    @Operation(summary = "Update a user by its id", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Register object with updated information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUser.class))))
    @ApiResponses(@ApiResponse(responseCode = "200", description = "The updating process was a success and the user object updated is on the body", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUser.class))
    }))
    @PutMapping("/{id}")
    public ResponseEntity<RegisterUser> updateUser(
            @Parameter(description = "id from the user to be updated") @PathVariable("id") Long id,
            @RequestBody RegisterUser updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    // delete
    @Operation(summary = "Delete a user from database by its id")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "The delete process was a success"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "id from the user to be deleted") @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
