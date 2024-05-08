package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.PlaceRequest;
import org.itmo.eventapp.main.model.dto.response.PlaceResponse;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.mapper.PlaceMapper;
import org.itmo.eventapp.main.service.PlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/places")
public class PlaceController {
    private final PlaceService placeService;

    @Operation(summary = "Фильтрация списка площадок")
    @GetMapping
    public ResponseEntity<List<PlaceResponse>> getAllOrFilteredPlaces(@Min(0) @RequestParam(value = "page", defaultValue = "0") @Parameter(name = "page", description = "Номер страницы, с которой начать показ площадок", example = "0") int page,
                                                                      @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "5") @Parameter(name = "size", description = "Число площадок на странице", example = "15") int size,
                                                                      @RequestParam(required = false) @Parameter(name = "name", description = "Имя площадки", example = "Университет ИТМО") String name) {
        return ResponseEntity.ok().body(PlaceMapper.placesToPlaceResponseList(
            placeService.getAllOrFilteredPlaces(page, size, name)));
    }

    @Operation(summary = "Получение площадки по id")
    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> placeGet(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                  @PathVariable @Parameter(name = "id", description = "ID площадки", example = "1") Integer id) {
        return ResponseEntity.ok().body(PlaceMapper.placeToPlaceResponse(placeService.findById(id)));
    }

    @Operation(summary = "Создание площадки")
    @PreAuthorize("@placeSecurityExpression.canCreatePlace()")
    @PostMapping
    public ResponseEntity<Integer> placeAdd(@Valid @RequestBody PlaceRequest placeRequest) {
        Place place = placeService.save(PlaceMapper.placeRequestToPlace(placeRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(place.getId());
    }

    @Operation(summary = "Редактирование площадки")
    @PreAuthorize("@placeSecurityExpression.canEditPlace()")
    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponse> placeEdit(@Min(value = 1, message = "Параметр id не может быть меньше 1!")
                                                   @PathVariable @Parameter(name = "id", description = "ID площадки", example = "1") Integer id,
                                                   @Valid @RequestBody PlaceRequest placeRequest) {
        Place edited = placeService.edit(id, placeRequest);
        return ResponseEntity.ok().body(PlaceMapper.placeToPlaceResponse(edited));
    }

    @Operation(summary = "Удаление площадки")
    @PreAuthorize("@placeSecurityExpression.canDeletePlace()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> placeDelete(@Min(value = 4, message = "Параметр id не может быть меньше 4!")
                                            @PathVariable @Parameter(name = "id", description = "ID площадки", example = "4") Integer id) {
        placeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
