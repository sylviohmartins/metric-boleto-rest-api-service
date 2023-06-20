package io.sylviohmartins.metric.util;

import io.sylviohmartins.metric.domain.document.Boleto;
import io.sylviohmartins.metric.domain.document.CustomMetric;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h1>MetricUtils</h1>
 * <p>Esta classe fornece utilitários para manipulação de métricas.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MetricUtils {

    /**
     * Formata o nome concatenando uma lista de argumentos.
     *
     * @param args Os argumentos a serem concatenados.
     * @return O nome formatado.
     */
    public static String format(final String... args) {
        final StringBuilder complementaryName = new StringBuilder(0);

        for (final String arg : args) {
            if (arg != null) {
                complementaryName.append(String.format("%s.", arg));
            }
        }

        final int length = complementaryName.length();
        complementaryName.replace(length - 1, length, "");

        return complementaryName.toString();
    }

    /**
     * Adiciona nomes a uma lista de métricas, usando um prefixo e uma lista de nomes adicionais.
     *
     * @param metricNames A lista de métricas onde os nomes serão adicionados.
     * @param prefix      O prefixo a ser adicionado a cada nome.
     * @param names       Os nomes a serem adicionados.
     */
    public static void addNames(final List<String> metricNames, final String prefix, final String... names) {
        final StringBuilder metricNameBuilder = new StringBuilder(prefix + ".");

        for (final String name : names) {

            if (name == null) {
                break;
            }

            metricNameBuilder.append(name);

            metricNames.add(metricNameBuilder.toString());

            boolean isLastElement = name.equals(names[names.length - 1]);

            if (!isLastElement) {
                metricNameBuilder.append(".");
            }

        }

    }

    /**
     * Cria uma instância de CustomMetric com base em um boleto.
     *
     * @param boleto O boleto usado para criar a CustomMetric.
     * @return A instância de CustomMetric criada.
     */
    public static CustomMetric createCustomMetric(final Boleto boleto) {
        return CustomMetric.builder()
                .date(boleto.getDate()) //
                .method(boleto.getMethod()) //
                .channelSource(boleto.getChannelSource()) //
                .paymentAmount(boleto.getPaymentAmount()) //
                .maximumPaymentAmount(boleto.getMaximumPaymentAmount()) //
                .status(boleto.getStatus()) //
                .build();
    }

}
