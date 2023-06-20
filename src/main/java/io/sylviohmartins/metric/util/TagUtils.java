package io.sylviohmartins.metric.util;

import io.micrometer.core.instrument.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>TagUtils</h1>
 * <p>Esta classe fornece utilitários para criação e manipulação de tags.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagUtils {

    /**
     * Cria uma tag com a chave e o valor fornecidos.
     *
     * @param key   A chave da tag.
     * @param value O valor da tag.
     * @return A tag criada.
     */
    public static Tag createTag(final String key, final String value) {
        return Tag.of(key, value != null ? value : "");
    }

    /**
     * Cria uma tag padrão com o nome do método.
     *
     * @param joinPoint O ponto de corte da execução do método.
     * @return A tag padrão criada.
     */
    public static Tag createDefault(final ProceedingJoinPoint joinPoint) {
        final String className = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();

        return createTag("methodName", MetricUtils.format(className, methodName));
    }

    /**
     * Cria uma tag para indicar falha.
     *
     * @return A tag de falha.
     */
    public static Tag createFail() {
        return createTag("outcome", "FAIL");
    }

    /**
     * Cria uma tag para indicar uma exceção.
     *
     * @param unknownException A exceção desconhecida.
     * @return A tag de exceção.
     */
    public static Tag createExecpetion(final Exception unknownException) {
        final String value = unknownException != null ? unknownException.getClass().getName() : "";

        return createTag("exception", value);
    }

    /**
     * Cria uma tag para indicar sucesso.
     *
     * @return A tag de sucesso.
     */
    public static Tag createSuccess() {
        return createTag("outcome", "SUCCESS");
    }

    /**
     * Recupera as tags com base nos valores fornecidos.
     *
     * @param joinPoint O ponto de corte da execução do método.
     * @param arrayTags As tags fornecidas.
     * @return A lista de tags.
     */
    public static List<Tag> retrieveTags(final ProceedingJoinPoint joinPoint, final io.sylviohmartins.metric.metric.nested.Tag[] arrayTags) {
        final List<Tag> tags = new LinkedList<>();

        for (io.sylviohmartins.metric.metric.nested.Tag tag : arrayTags) {
            tags.add(TagUtils.createTag(tag.name(), tag.value()));
        }

        tags.add(TagUtils.createDefault(joinPoint));

        return tags;
    }

    /**
     * Adiciona todas as tags fornecidas à lista de tags personalizadas.
     *
     * @param customTags As tags personalizadas.
     * @param tags       As tags a serem adicionadas.
     */
    public static void addAllTags(final List<Tag> customTags, final Tag... tags) {
        customTags.addAll(Arrays.asList(tags));
    }

}
