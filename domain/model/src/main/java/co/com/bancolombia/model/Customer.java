package co.com.bancolombia.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
@Getter
public class Customer {

    private final long id;
    private final String name;
    private final String dni;
    private final String type;

}
