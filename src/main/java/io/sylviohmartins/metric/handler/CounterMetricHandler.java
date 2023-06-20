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
 * <h1>CounterMetricHandler</h1>
 * <p>Manipulador de métricas do contador.</p>
 * <p>Este manipulador é responsável por medir e registrar métricas do tipo contador para as operações executadas.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Component
@AllArgsConstructor
public class CounterMetricHandler extends BaseMetricHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CounterMetricHandler.class);

    private final CounterCustomMetricHandler counterCustomMetricHandler;

    /**
     * Processa a execução do ponto de corte e registra as métricas do tipo contador.
     * Este método é executado antes e depois do ponto de corte para medir o tempo de execução e registrar as métricas correspondentes.
     *
     * @param joinPoint O ponto de corte a ser executado.
     * @param metric    A métrica associada à operação executada.
     * @return O resultado da execução do ponto de corte.
     * @throws Throwable Se ocorrer um erro durante a execução do ponto de corte.
     */
    public Object process(final ProceedingJoinPoint joinPoint, final Metric metric) throws Throwable {
        final StopWatch watchRuntime = new StopWatch();
        watchRuntime.start();

        try {
            final Object proceed = joinPoint.proceed();

            execute(null, metric, joinPoint.getArgs());

            return proceed;

        } catch (final Exception unknownException) {
            execute(unknownException, metric, joinPoint.getArgs());

            throw unknownException;

        } finally {
            watchRuntime.stop();

            LOGGER.info("Tempo de execução: {} ms", watchRuntime.getTime(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * Executa o registro das métricas do tipo contador.
     * Este método é responsável por adicionar tags às métricas, enviá-las para registro e processar métricas personalizadas adicionais, se necessário.
     *
     * @param unknownException A exceção desconhecida que ocorreu durante a execução da operação, ou null se nenhuma exceção ocorreu.
     * @param genericMetric    A métrica genérica associada à operação executada.
     * @param args             Os argumentos do ponto de corte.
     */
    public void execute(final Exception unknownException, final Metric genericMetric, final Object[] args) {
        final List<Tag> executionTags = new LinkedList<>();

        if (unknownException == null) {
            executionTags.add(TagUtils.createExecpetion(null));
            executionTags.add(TagUtils.createSuccess());

        } else {
            executionTags.add(TagUtils.createExecpetion(unknownException));
            executionTags.add(TagUtils.createFail());
        }

        genericMetric.getTags().addAll(executionTags);

        sendCounter(genericMetric);

        if (genericMetric.isShouldGenerate()) {
            counterCustomMetricHandler.process(args, genericMetric);
        }
    }

}
