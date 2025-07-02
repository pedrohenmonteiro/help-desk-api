package br.com.pedromonteiro.user_service_api.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import models.responses.UserResponse;
import models.exceptions.StandardError;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;



@Tag(name = "UserController", description = "Controller responsible for user operations")
@RequestMapping("/api/users")
public interface UserController {
    
    @Operation(summary = "Find User by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ok"),
        @ApiResponse(
            responseCode = "404", description = "User not found", 
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)
        )),
        @ApiResponse(
            responseCode = "500", description = "Internal Server Error", 
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)
        )),

    })
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(
        @Parameter(description = "User ID", required = true, example = "64af1d9a2d9a2d9a2d9a2d9a")
        @PathVariable("id") final String id
        );


    @Operation(summary = "Save New User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created"),
        @ApiResponse( 
        responseCode = "400", description = "Bad Request", 
        content = @Content(mediaType = APPLICATION_JSON_VALUE,schema = @Schema(implementation = StandardError.class)
        )),
        @ApiResponse( 
        responseCode = "500", description = "Internal Server Error", 
        content = @Content(mediaType = APPLICATION_JSON_VALUE,schema = @Schema(implementation = StandardError.class)
        )),
    })
    @PostMapping
    ResponseEntity<Void> save(@Valid @RequestBody final CreateUserRequest createUserRequest);


    @Operation(summary = "Find all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ok",
            content = @Content(
                mediaType = APPLICATION_JSON_VALUE, 
                array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
        @ApiResponse(
            responseCode = "500", description = "Internal Server Error", 
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)
        )),

    })
    @GetMapping
    ResponseEntity<List<UserResponse>> findAll();

    @Operation(summary = "Update user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ok",
            content = @Content(
                mediaType = APPLICATION_JSON_VALUE, 
                array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
        @ApiResponse( 
        responseCode = "400", description = "Bad Request", 
        content = @Content(mediaType = APPLICATION_JSON_VALUE,schema = @Schema(implementation = StandardError.class)
        )),
        @ApiResponse( 
        responseCode = "404", description = "User not found", 
        content = @Content(mediaType = APPLICATION_JSON_VALUE,schema = @Schema(implementation = StandardError.class)
        )),
        @ApiResponse(
            responseCode = "500", description = "Internal Server Error", 
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)
        )),

    })
    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(
        @Parameter(description = "User ID", required = true, example = "64af1d9a2d9a2d9a2d9a2d9a")
        @PathVariable("id") final String id, @Valid @RequestBody final UpdateUserRequest updateUserRequest
        );
}
