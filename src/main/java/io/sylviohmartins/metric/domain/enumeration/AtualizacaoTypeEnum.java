package io.sylviohmartins.metric.domain.enumeration;

import lombok.Getter;

public enum AtualizacaoTypeEnum {

    INCLUIDO("incluido"),
    INCLUIDO_LEGADO("incluido.legado"),
    AUTORIZADO("autorizado"),
    AUTORIZADO_LEGADO("autorizado.legado"),
    PARCIALMENTE_AUTORIZADO("parcialmente.autorizado"),
    PARCIALMENTE_AUTORIZADO_LEGADO("parcialmente.autorizado.legado"),
    NAO_AUTORIZADO("nao.autorizado"),
    DEVOLVIDO("devolvido"),
    PROCESSAMENTO("processamento"),
    AGENDADO("agendado"),
    REAGENDADO("reagendado");

    @Getter
    private final String name;

    AtualizacaoTypeEnum(final String name) {
        this.name = name;
    }

    public String toStringValue() {
        return name;
    }

}
