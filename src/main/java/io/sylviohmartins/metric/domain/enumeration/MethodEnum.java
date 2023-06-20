package io.sylviohmartins.metric.domain.enumeration;

import lombok.Getter;

@Getter
public enum MethodEnum {

    ITAU(30, "itau"),
    OUTROS_BANCOS(31, "outros"),
    OUTROS_BANCOS_ONLINE(38, "online");

    private final Integer id;

    private final String name;

    MethodEnum(final Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toStringValue() {
        return name;
    }

}
