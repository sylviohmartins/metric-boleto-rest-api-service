package io.sylviohmartins.metric.handler;

import io.micrometer.core.instrument.Tag;
import io.sylviohmartins.metric.domain.document.Metric;
import io.sylviohmartins.metric.exception.HandlerException;
import io.sylviohmartins.metric.exception.ProccedUnknownException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.sylviohmartins.metric.util.TagUtils.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


/**
 * <h1>TimerMetricHandler</h1>
 * <p>Manipulador de métricas de tempo.</p>
 * <p>Este manipulador é responsável por medir e registrar métricas do tipo tempo para as operações executadas.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Component
@AllArgsConstructor
public class TimerMetricHandler extends BaseMetricHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimerMetricHandler.class);

    /**
     * Processa a execução do ponto de corte e registra as métricas do tipo tempo.
     * Este método é executado antes e depois do ponto de corte para medir o tempo de execução e registrar as métricas correspondentes.
     *
     * @param joinPoint O ponto de corte a ser executado.
     * @param metric    A métrica associada à operação executada.
     * @return O resultado da execução do ponto de corte.
     * @throws ProccedUnknownException Se ocorrer um erro durante a execução do ponto de corte.
     * @throws HandlerException        Se ocorrer um erro ao lidar com as métricas.
     */
    public Object process(final ProceedingJoinPoint joinPoint, final Metric metric) throws ProccedUnknownException, HandlerException {
        final StopWatch watchRuntime = new StopWatch();
        watchRuntime.start();

        try {
            final StopWatch watchProceed = new StopWatch();
            watchProceed.start();

            final Object proceed = joinPoint.proceed();

            watchProceed.stop();

            metric.setValue(watchProceed);

            execute(null, metric);

            return proceed;

        } catch (final Throwable proccedUnknownException) {
            execute(proccedUnknownException, metric);

            throw new ProccedUnknownException(proccedUnknownException.getMessage(), BAD_REQUEST);

        } finally {
            watchRuntime.stop();

            LOGGER.info("Tempo de execução: {} ms", watchRuntime.getTime(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * Executa o registro das métricas do tipo tempo.
     * Este método é responsável por adicionar tags à métrica e enviá-la para registro.
     *
     * @param proccedUnknownException A exceção desconhecida que ocorreu durante a execução da operação, ou null se nenhuma exceção ocorreu.
     * @param genericMetric           A métrica genérica associada à operação executada.
     * @throws HandlerException Se ocorrer um erro ao lidar com as métricas.
     */
    public void execute(final Throwable proccedUnknownException, final Metric genericMetric) throws HandlerException {
        try {
            final List<Tag> executionTags = new LinkedList<>();

            if (proccedUnknownException == null) {
                executionTags.add(createException(null));
                executionTags.add(createSuccess());

            } else {
                executionTags.add(createException(proccedUnknownException));
                executionTags.add(createFail());
            }

            genericMetric.getTags().addAll(executionTags);

            sendTimer(genericMetric);

        } catch (final Exception unknownException) {
            throw new HandlerException(unknownException.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

}
