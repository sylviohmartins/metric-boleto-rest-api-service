package io.sylviohmartins.metric.domain.document;

import io.sylviohmartins.metric.domain.enumeration.ChannelSourceEnum;
import io.sylviohmartins.metric.domain.enumeration.MethodEnum;
import io.sylviohmartins.metric.domain.enumeration.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Boleto {

    private UUID id;

    private LocalDate date;

    private MethodEnum method;

    private ChannelSourceEnum channelSource;

    private Double paymentAmount;

    private Double maximumPaymentAmount;

    private StatusEnum status;

}
