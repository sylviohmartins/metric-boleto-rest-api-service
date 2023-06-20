package io.sylviohmartins.metric.domain.vo;

import io.sylviohmartins.metric.domain.enumeration.StatusEnum;

public record FiltroBoletoVO(String cpf, String nome, StatusEnum status) {
}
