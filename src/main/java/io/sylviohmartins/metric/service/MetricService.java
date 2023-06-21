package io.sylviohmartins.metric.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.sylviohmartins.metric.constant.MessageConstants;
import io.sylviohmartins.metric.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

import static io.sylviohmartins.metric.util.MetricUtils.format;


/**
 * <h1>MetricService</h1>
 * <p>Serviço responsável pelo registro de métricas.<p>
 * <p> Este serviço fornece métodos para registrar contadores e temporizadores
 * utilizando o Micrometer e um MeterRegistry.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Service
@RequiredArgsConstructor
public class MetricService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricService.class);

    private final MeterRegistry meterRegistry;

    /**
     * Registra um contador com o valor especificado.
     *
     * @param objective   o objetivo do contador
     * @param name        o nome do contador
     * @param description a descrição do contador
     * @param unit        a unidade do contador
     * @param tags        as tags associadas ao contador
     * @param value       o valor do contador
     * @throws ServiceException se ocorrer um erro ao registrar a métrica
     */
    public void counter(final String objective, String name, final String description, final String unit, final List<Tag> tags, final Double value) throws ServiceException {
        register(objective, name, description, unit, tags, value);
    }

    /**
     * Método privado para registrar um contador com o valor especificado.
     *
     * @param objective   o objetivo do contador
     * @param name        o nome do contador
     * @param description a descrição do contador
     * @param unit        a unidade do contador
     * @param tags        as tags associadas ao contador
     * @param value       o valor do contador
     * @throws ServiceException se ocorrer um erro ao registrar a métrica
     */
    private void register(final String objective, final String name, final String description, final String unit, final List<Tag> tags, final Double value) throws ServiceException {
        final String metricName = format(name, objective != null ? objective : "counter");

        try {

            final Counter counter = Counter.builder(metricName) //
                    .description(description) //
                    .baseUnit(unit) //
                    .tags(tags) //
                    .register(meterRegistry);

            counter.increment(value);

            LOGGER.info("SUCESSO ao registrar a métrica: {}", counter.getId());

        } catch (final Exception unknownException) {
            LOGGER.error("ERRO desconhecido ao registrar a métrica: {}", metricName, unknownException);

            throw new ServiceException(MessageConstants.ERRO_SERVIDOR_PROMETHEUS, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Registra um temporizador com a duração especificada.
     *
     * @param name        o nome do temporizador
     * @param description a descrição do temporizador
     * @param tags        as tags associadas ao temporizador
     * @param watch       o cronômetro utilizado para medir a duração
     * @throws ServiceException se ocorrer um erro ao registrar a métrica
     */
    public void timer(final String name, final String description, final List<Tag> tags, final StopWatch watch) throws ServiceException {
        register("timer", name, description, tags, watch);
    }

    /**
     * Método privado para registrar um temporizador com a duração especificada.
     *
     * @param objective   o objetivo do contador
     * @param name        o nome do temporizador
     * @param description a descrição do temporizador
     * @param tags        as tags associadas ao temporizador
     * @param watch       o cronômetro utilizado para medir a duração
     * @throws ServiceException se ocorrer um erro ao registrar a métrica
     */
    private void register(final String objective, final String name, final String description, final List<Tag> tags, final StopWatch watch) throws ServiceException {
        final String metricName = format(name, objective);

        try {

            final Timer timer = Timer.builder(metricName) //
                    .description(description) //
                    .tags(tags) //
                    .register(meterRegistry);

            timer.record(Duration.ofMillis(watch.getTime()));

            LOGGER.info("SUCESSO ao registrar a métrica: {}", timer.getId());

        } catch (final Exception unknownException) {
            LOGGER.error("ERRO desconhecido ao registrar a métrica: {}", metricName, unknownException);

            throw new ServiceException(MessageConstants.ERRO_SERVIDOR_PROMETHEUS, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
