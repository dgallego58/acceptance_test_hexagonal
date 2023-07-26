package co.com.bancolombia.apirest;

import co.com.bancolombia.model.EventWrapper;
import co.com.bancolombia.usecase.EventAnswer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/e-commerce")
public class DiscountController {

    private final EventAnswer eventAnswer;

    public DiscountController(EventAnswer eventAnswer) {
        this.eventAnswer = eventAnswer;
    }

    @PutMapping(path = "/notify-subscribers", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<EventWrapper> notifyDiscount(@RequestBody String message) {
        return ResponseEntity.ok(eventAnswer.getEvents(message));
    }

}
