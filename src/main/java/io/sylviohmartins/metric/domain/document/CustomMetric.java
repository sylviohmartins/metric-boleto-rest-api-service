package io.sylviohmartins.metric.domain.document;

import io.sylviohmartins.metric.domain.enumeration.ChannelSourceEnum;
import io.sylviohmartins.metric.domain.enumeration.MethodEnum;
import io.sylviohmartins.metric.domain.enumeration.StatusEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomMetric {

    private LocalDate date;

    private MethodEnum method;

    private ChannelSourceEnum channelSource;

    private Double paymentAmount;

    private Double maximumPaymentAmount;

    private StatusEnum status;

}
