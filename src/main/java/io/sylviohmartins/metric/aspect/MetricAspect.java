package io.sylviohmartins.metric.aspect;

import io.micrometer.core.instrument.Tag;
import io.sylviohmartins.metric.domain.document.Metric;
import io.sylviohmartins.metric.exception.HandlerException;
import io.sylviohmartins.metric.exception.ProccedUnknownException;
import io.sylviohmartins.metric.handler.CounterMetricHandler;
import io.sylviohmartins.metric.handler.TimerMetricHandler;
import io.sylviohmartins.metric.metric.Counter;
import io.sylviohmartins.metric.metric.Timer;
import io.sylviohmartins.metric.util.TagUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * <h1>MetricAspect</h1>
 * <p>Classe que representa um aspecto para lidar com métricas.</p>
 * <p>Ela intercepta métodos anotados com as anotações @Counter ou @Timer e processa as métricas correspondentes.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricAspect.class);

    private static final String ERROR_MESSAGE = "Erro desconhecido ao processar a métrica: {}";

    private final CounterMetricHandler counterMetricHandler;

    private final TimerMetricHandler timerMetricHandler;

    /**
     * Intercepta métodos anotados com @Counter e processa a métrica de contador.
     *
     * @param joinPoint O ponto de junção em execução que representa o método interceptado.
     * @param counter   A anotação Counter aplicada ao método.
     * @return O resultado do método interceptado.
     * @throws ProccedUnknownException Se ocorrer uma exceção durante a interceptação.
     */
    @SuppressWarnings("unused")
    @Around("@annotation(counter)")
    public Object counterAspect(final ProceedingJoinPoint joinPoint, final Counter counter) throws ProccedUnknownException {
        if (!counter.isEnabled()) {
            return handleDisabledMetric(counter.name());
        }

        final List<Tag> tags = TagUtils.retrieveTags(joinPoint, counter.tags());

        final Metric metric = Metric.builder() //
                .name(counter.name()) //
                .description(counter.description()) //
                .tags(tags) //
                .value(1.0) //
                .shouldGenerate(counter.shouldGenerate()) //
                .build();

        try {
            return counterMetricHandler.process(joinPoint, metric);

        } catch (final HandlerException handlerException) {
            LOGGER.error(ERROR_MESSAGE, metric, handlerException);

            return null;
        }
    }

    /**
     * Intercepta métodos anotados com @Timer e processa a métrica de temporizador.
     *
     * @param joinPoint O ponto de junção em execução que representa o método interceptado.
     * @param timer     A anotação Timer aplicada ao método.
     * @return O resultado do método interceptado.
     * @throws ProccedUnknownException Se ocorrer uma exceção durante a interceptação.
     */
    @SuppressWarnings("unused")
    @Around("@annotation(timer)")
    public Object timerAspect(final ProceedingJoinPoint joinPoint, final Timer timer) throws ProccedUnknownException {
        if (!timer.isEnabled()) {
            return handleDisabledMetric(timer.name());
        }

        final List<Tag> tags = TagUtils.retrieveTags(joinPoint, timer.tags());

        final Metric metric = Metric.builder() //
                .name(timer.name()) //
                .description(timer.description()) //
                .tags(tags) //
                .build();

        try {
            return timerMetricHandler.process(joinPoint, metric);

        } catch (final HandlerException handlerException) {
            LOGGER.error(ERROR_MESSAGE, metric, handlerException);

            return null;
        }
    }

    /**
     * Lida com o caso em que uma métrica está desabilitada.
     * Este método registra uma mensagem indicando que a métrica está desabilitada.
     *
     * @param metricName O nome da métrica desabilitada.
     * @return null
     */
    private Object handleDisabledMetric(final String metricName) {
        LOGGER.info("O registro da métrica '{}' está desabilitado", metricName);
        return null;
    }

}
