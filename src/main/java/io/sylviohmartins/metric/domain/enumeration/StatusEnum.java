package io.sylviohmartins.metric.domain.enumeration;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StatusEnum {

    INCLUIDO(1),
    AUTORIZADO(2),
    EFETIVADO(3),
    ALTERADO(4),
    CANCELADO(5);

    private static final Map<Integer, StatusEnum> IDS = Stream.of(values()).collect(Collectors.toMap(StatusEnum::getId, v -> v));

    @Getter
    private final Integer id;

    StatusEnum(final Integer id) {
        this.id = id;
    }

    //	@JsonValue
    public static StatusEnum valueOfId(final Integer id) {
        return IDS.getOrDefault(id, null);
    }

    public static StatusEnum valueOfName(final String name) {
        return Stream.of(values()).filter(v -> v.toString().toLowerCase().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
