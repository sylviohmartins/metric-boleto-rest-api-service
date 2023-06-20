package io.sylviohmartins.metric.domain.vo;

import io.sylviohmartins.metric.domain.enumeration.ChannelSourceEnum;
import io.sylviohmartins.metric.domain.enumeration.MethodEnum;

import java.time.LocalDate;
import java.util.UUID;

public record BoletoVO(

        UUID id,

        LocalDate date,

        ChannelSourceEnum channelSource,

        MethodEnum method,

        Double paymentAmount,

        Double maximumPaymentAmount

) {
}
