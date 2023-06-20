package io.sylviohmartins.metric.domain.dto;

import io.sylviohmartins.metric.domain.enumeration.ChannelSourceEnum;
import io.sylviohmartins.metric.domain.enumeration.MethodEnum;
import io.sylviohmartins.metric.domain.enumeration.StatusEnum;

import java.time.LocalDate;
import java.util.UUID;

public record BoletoDTO(
        UUID id,

        LocalDate date,

        MethodEnum method,

        ChannelSourceEnum channelSource,

        Double paymentAmount,

        Double maximumPaymentAmount,

        StatusEnum status

) {
}
