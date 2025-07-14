package co.com.bancolombia.util;

import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.ValidateMovementRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ValidateMovementUtil implements ValidateMovementRepository {

    @Override
    public Mono<Boolean> validateMovement(Movement movement, String boxId, Set<String> movementIds) {
        List<String> errors = new ArrayList<>();

        // Validate movementId
        if (movement.getMovementId() == null || movement.getMovementId().isEmpty()) {
            errors.add("Movement ID is mandatory");
        } else if (movementIds.contains(movement.getMovementId())) {
            errors.add("Duplicate Movement ID in file: " + movement.getMovementId());
        }

        // Validate boxId
        if (boxId == null || boxId.isEmpty()) {
            errors.add("Box ID is mandatory");
        } else if (!boxId.equals(movement.getBoxId())) {
            errors.add("The box ID of the transaction does not match the box ID of the uploaded file");
        }

        // Validate date
        if (movement.getDate() == null || !isValidISO8601Date(String.valueOf(movement.getDate()))) {
            errors.add("Invalid date format, must be ISO 8601");
        }

        // Validate type
        if (movement.getType() == null || (!"INCOME".equals(movement.getType()) && !"EXPENSE".equals(movement.getType()))) {
            errors.add("Invalid type, must be INCOME or EXPENSE");
        }

        // Validate amount
        if (movement.getAmount() == null || movement.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Amount must be a positive number");
        }

        // Validate currency
        if (!isValidCurrency(movement.getCurrency())) {
            errors.add("Invalid currency: " + movement.getCurrency());
        }

        // Validate description
        if (movement.getDescription() == null || movement.getDescription().isEmpty()) {
            errors.add("Description cannot be empty");
        }

        // Emit event if there are errors
        if (!errors.isEmpty()) {
            emitValidationErrorsEvent(movement, errors);
            return Mono.just(false);
        }

        // Add movementId to the set only after successful validation
        movementIds.add(movement.getMovementId());
        return Mono.just(true);
    }

    private static boolean isValidISO8601Date(String date) {
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isValidCurrency(String currency) {
        return "COP".equals(currency) || "USD".equals(currency) || isAllowedByPolicy(currency);
    }

    private static boolean isAllowedByPolicy(String currency) {
        // Implement policy-based currency validation logic here
        return false;
    }

    private void emitValidationErrorsEvent(Movement movement, List<String> errors) {
        // Implement the logic to emit an event with the movement and its validation errors
        System.out.println("Validation errors for movement ID " + movement.getMovementId() + ": " + errors);
    }
}