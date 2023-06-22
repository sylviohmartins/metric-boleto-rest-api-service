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

import static io.sylviohmartins.metric.constant.ObjectiveMetricConstants.CONTAR;
import static io.sylviohmartins.metric.util.MetricUtils.format;
import static io.sylviohmartins.metric.util.TagUtils.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


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
     * @throws ProccedUnknownException Se ocorrer um erro durante a execução do ponto de corte.
     * @throws HandlerException        Se ocorrer um erro ao lidar com as métricas.
     */
    public Object process(final ProceedingJoinPoint joinPoint, final Metric metric) throws ProccedUnknownException, HandlerException {
        final StopWatch watchRuntime = new StopWatch();
        watchRuntime.start();

        try {
            final Object proceed = joinPoint.proceed();

            execute(null, metric, joinPoint.getArgs());

            return proceed;

        } catch (final HandlerException handlerException) {
            throw handlerException;

        } catch (final Throwable proccedUnknownException) {
            execute(proccedUnknownException, metric, joinPoint.getArgs());

            throw new ProccedUnknownException(proccedUnknownException.getMessage(), BAD_REQUEST);

        } finally {
            watchRuntime.stop();

            LOGGER.info("Tempo de execução: {} ms", watchRuntime.getTime(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * Executa o registro das métricas do tipo contador.
     * Este método é responsável por adicionar tags à métrica, enviá-la para registro e processar métricas personalizadas adicionais, se necessário.
     *
     * @param proccedUnknownException A exceção desconhecida que ocorreu durante a execução da operação, ou null se nenhuma exceção ocorreu.
     * @param genericMetric           A métrica genérica associada à operação executada.
     * @param args                    Os argumentos do ponto de corte.
     * @throws HandlerException Se ocorrer um erro ao lidar com as métricas.
     */
    public void execute(final Throwable proccedUnknownException, final Metric genericMetric, final Object[] args) throws HandlerException {
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
            genericMetric.setObjective(CONTAR);

            sendCounter(genericMetric);

            if (genericMetric.isShouldGenerate()) {
                final String customMetricName = format(genericMetric.getName(), "custom");

                genericMetric.setName(customMetricName);
                genericMetric.setObjective(CONTAR);

                counterCustomMetricHandler.process(args, genericMetric);
            }

        } catch (final Exception unknownException) {
            throw new HandlerException(unknownException.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

}
