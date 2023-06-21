package io.sylviohmartins.metric.util;

import io.sylviohmartins.metric.domain.document.Boleto;
import io.sylviohmartins.metric.domain.document.CustomMetric;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
        if (args == null) {
            return "";
        }

        final StringBuilder complementaryName = new StringBuilder(0);

        for (final String arg : args) {
            if (arg != null) {
                complementaryName.append(String.format("%s.", arg));
            }
        }

        int length = complementaryName.length();

        if (length > 0) {
            complementaryName.deleteCharAt(length - 1);
        }

        return complementaryName.toString();
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
