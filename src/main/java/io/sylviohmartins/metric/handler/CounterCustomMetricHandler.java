package io.sylviohmartins.metric.handler;

import io.sylviohmartins.metric.domain.document.Boleto;
import io.sylviohmartins.metric.domain.document.CustomMetric;
import io.sylviohmartins.metric.domain.document.Metric;
import io.sylviohmartins.metric.domain.enumeration.ChannelSourceEnum;
import io.sylviohmartins.metric.domain.enumeration.MethodEnum;
import io.sylviohmartins.metric.domain.enumeration.StatusEnum;
import io.micrometer.core.instrument.Tag;
import io.sylviohmartins.metric.constant.CustomMetricsConstants;
import io.sylviohmartins.metric.util.TagUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static io.sylviohmartins.metric.util.MetricUtils.createCustomMetric;
import static io.sylviohmartins.metric.util.MetricUtils.format;


/**
 * <h1>CounterCustomMetricHandler</h1>
 * <p>Manipulador de métricas personalizadas do contador.</p>
 * <p>Este manipulador é responsável por processar e manipular métricas personalizadas relacionadas ao contador.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Component
@AllArgsConstructor
public class CounterCustomMetricHandler extends BaseMetricHandler {

    /**
     * Processa os argumentos e a métrica recebidos.
     *
     * @param args   Os argumentos a serem processados.
     * @param metric A métrica a ser manipulada.
     */
    public void process(final Object[] args, final Metric metric) {
        for (final Object arg : args) {

            if (arg instanceof Boleto boleto) {
                final CustomMetric customMetric = createCustomMetric(boleto);
                metric.setCustomMetric(customMetric);

                execute(metric);
            }

        }

    }

    /**
     * Executa a manipulação da métrica personalizada.
     *
     * @param metric A métrica a ser manipulada.
     */
    private void execute(final Metric metric) {
        final CustomMetric customMetric = metric.getCustomMetric();

        final StatusEnum status = customMetric.getStatus();
        final ChannelSourceEnum channelSource = customMetric.getChannelSource();
        final MethodEnum method = customMetric.getMethod();
        final LocalDate date = customMetric.getDate();
        final Double paymentAmount = customMetric.getPaymentAmount();
        final Double maximumPaymentAmount = customMetric.getMaximumPaymentAmount();

        sendCustomSumMetric(metric);

        final List<Tag> customMetricTags = new LinkedList<>();

        final String paymentTypeValue = isPaymentIntegral(paymentAmount, maximumPaymentAmount) ? CustomMetricsConstants.INTEGRAL : CustomMetricsConstants.PARCIAL;
        final String channelSourceValue = channelSource != null ? channelSource.toStringValue() : null;
        final String methodValue = method != null ? method.toStringValue() : null;
        final String dateValue = isPaymentToday(date) ? CustomMetricsConstants.DIA : CustomMetricsConstants.AGENDADA;
        final Boolean contingencyValue = metric.getTags() != null;

        // Mapeamento dos tipos de status para os tipos correspondentes
        final Map<StatusEnum, String> statusTypeMap = new HashMap<>();
        statusTypeMap.put(StatusEnum.INCLUIDO, null);
        statusTypeMap.put(StatusEnum.AUTORIZADO, null);
        statusTypeMap.put(StatusEnum.EFETIVADO, metric.getTags() != null ? "e" : "");
        statusTypeMap.put(StatusEnum.ALTERAD0, metric.getTags() != null ? "a" : "");
        statusTypeMap.put(StatusEnum.CANCELADO, metric.getTags() != null ? "c" : "");

        // Verifica se o status é válido
        if (!statusTypeMap.containsKey(status)) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }

        // Obtém o tipo correspondente ao status
        final String typeValue = statusTypeMap.get(status);

        // Adiciona as tags comuns a todos os casos
        TagUtils.addAllTags(
                customMetricTags,
                TagUtils.createTag("tipo", typeValue),
                TagUtils.createTag("canal", channelSourceValue),
                TagUtils.createTag("pagamento", paymentTypeValue),
                TagUtils.createTag("banco", methodValue),
                TagUtils.createTag("operacao", dateValue),
                TagUtils.createTag("contingencia", String.valueOf(contingencyValue))
        );

        metric.getTags().addAll(customMetricTags);

        sendCustomCounterMetric(metric);
    }

    /**
     * Envia uma métrica personalizada de soma.
     *
     * @param metric A métrica a ser enviada.
     */
    private void sendCustomSumMetric(final Metric metric) {
        final CustomMetric customMetric = metric.getCustomMetric();

        metric.setValue(customMetric.getPaymentAmount());
        metric.setObjective(format("custom", "sum"));
        sendCounter(metric);
    }

    /**
     * Envia uma métrica personalizada de contagem.
     *
     * @param metric A métrica a ser enviada.
     */
    private void sendCustomCounterMetric(final Metric metric) {
        metric.setValue(1.0);
        metric.setObjective(format("custom", "counter"));
        sendCounter(metric);
    }

    // Métodos auxiliares

    /**
     * Obtém a data atual.
     *
     * @return A data atual.
     */
    private static LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }

    /**
     * Verifica se o pagamento é feito hoje com base na data fornecida.
     *
     * @param date A data fornecida.
     * @return true se o pagamento for hoje, caso contrário, false.
     */
    private static boolean isPaymentToday(final LocalDate date) {
        return getCurrentDate().isEqual(date);
    }

    /**
     * Verifica se o pagamento é integral com base nos valores do pagamento e valor máximo do pagamento.
     *
     * @param paymentAmount        O valor do pagamento.
     * @param maximumPaymentAmount O valor máximo do pagamento.
     * @return true se o pagamento for integral, caso contrário, false.
     */
    private static boolean isPaymentIntegral(final Double paymentAmount, final Double maximumPaymentAmount) {
        return paymentAmount.equals(maximumPaymentAmount);
    }

}
