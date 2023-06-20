package io.sylviohmartins.metric.handler;

import io.sylviohmartins.metric.domain.document.Metric;
import io.sylviohmartins.metric.util.TagUtils;
import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
     * Processa a lógica de medição do tempo de execução e envia a métrica.
     *
     * @param joinPoint O ponto de corte da execução a ser monitorado.
     * @param metric    A métrica a ser atualizada com o tempo de execução.
     * @return O resultado da execução do ponto de corte.
     * @throws Throwable Uma exceção que ocorreu durante a execução do ponto de corte.
     */
    public Object process(final ProceedingJoinPoint joinPoint, final Metric metric) throws Throwable {
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

        } catch (final Exception unknownException) {
            execute(unknownException, metric);

            throw unknownException;

        } finally {
            watchRuntime.stop();

            LOGGER.info("Tempo de execução: {} ms", watchRuntime.getTime(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * Executa ações adicionais com base nas informações fornecidas.
     *
     * @param unknownException Uma exceção desconhecida ocorrida durante a execução.
     * @param genericMetric    A métrica genérica que será atualizada com tags adicionais.
     */
    public void execute(final Exception unknownException, final Metric genericMetric) {
        final List<Tag> executionTags = new LinkedList<>();

        if (unknownException == null) {
            executionTags.add(TagUtils.createExecpetion(null));
            executionTags.add(TagUtils.createSuccess());

        } else {
            executionTags.add(TagUtils.createExecpetion(unknownException));
            executionTags.add(TagUtils.createFail());
        }

        genericMetric.getTags().addAll(executionTags);

        sendTimer(genericMetric);
    }
}
