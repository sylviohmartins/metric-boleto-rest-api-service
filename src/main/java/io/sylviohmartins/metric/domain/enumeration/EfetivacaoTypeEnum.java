package io.sylviohmartins.metric.domain.enumeration;

import lombok.Getter;

public enum EfetivacaoTypeEnum {

    SOLICITACAO("solicitacao"),
    RESPOSTA("resposta");

    @Getter
    private final String name;

    EfetivacaoTypeEnum(final String name) {
        this.name = name;
    }

    public String toStringValue() {
        return name;
    }

}
