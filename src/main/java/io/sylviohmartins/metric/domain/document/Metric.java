package io.sylviohmartins.metric.domain.document;

import io.micrometer.core.instrument.Tag;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metric {

    private String name;

    private String description;

    private List<Tag> tags;

    private Object value;

    private String objective;

    private boolean shouldGenerate;

    private CustomMetric customMetric;

}
