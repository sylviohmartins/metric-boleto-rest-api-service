package io.sylviohmartins.metric.handler;

import io.sylviohmartins.metric.domain.document.Metric;
import io.sylviohmartins.metric.exception.ServiceException;
import io.sylviohmartins.metric.service.MetricService;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * <h1>BaseMetricHandler</h1>
 * <p>Classe abstrata que representa o manipulador base para métricas.</p>
 * <p>Essa classe contém métodos para enviar contadores e temporizadores de métricas.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Component
@NoArgsConstructor
public abstract class BaseMetricHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMetricHandler.class);
    public static final String ERROR_MESSAGE = "Erro ao solicitar o registro da métrica: {}";

    @Autowired
    public MetricService metricService;

    /**
     * Envia um contador de métrica para o serviço de métricas.
     *
     * @param metric A métrica a ser enviada.
     */
    public void sendCounter(final Metric metric) {
        try {
            metricService.counter(metric.getObjective(), metric.getName(), metric.getDescription(), null, metric.getTags(), (Double) metric.getValue());

        } catch (final ServiceException serviceException) {
            LOGGER.error(ERROR_MESSAGE, metric.getName(), serviceException);
        }
    }

    /**
     * Envia um temporizador de métrica para o serviço de métricas.
     *
     * @param metric A métrica a ser enviada.
     */
    public void sendTimer(final Metric metric) {
        try {
            metricService.timer(metric.getName(), metric.getDescription(), metric.getTags(), (StopWatch) metric.getValue());

        } catch (final ServiceException serviceException) {
            LOGGER.error(ERROR_MESSAGE, metric.getName(), serviceException);
        }
    }

}
