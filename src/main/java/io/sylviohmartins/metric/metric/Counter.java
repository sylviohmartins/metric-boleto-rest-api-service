package io.sylviohmartins.metric.metric;

import io.sylviohmartins.metric.metric.nested.Tag;

import java.lang.annotation.*;

/**
 * <h1>Counter</h1>
 * <p>Esta anotação é usada para marcar um método com informações de métricas de contador.</p>
 * <p>Pode ser aplicada apenas a métodos.</p>
 *
 * <p>
 * A anotação pode ser usada para ativar ou desativar o registro da métrica, fornecer um nome
 * e uma descrição para a métrica, associar tags a ela e indicar se devem ser geradas métricas personalizadas.
 * </p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Counter {

    /**
     * Indica se o registro da métrica está ativado ou desativado.
     *
     * @return true se o registro da métrica estiver ativado, false caso contrário.
     */
    boolean isEnabled() default true;

    /**
     * Obtém o nome da métrica.
     *
     * @return o nome da métrica.
     */
    String name();

    /**
     * Obtém a descrição da métrica.
     *
     * @return a descrição da métrica.
     */
    String description() default "";

    /**
     * Obtém as tags associadas à métrica.
     *
     * @return as tags associadas à métrica.
     */
    Tag[] tags() default {};

    /**
     * Indica se devem ser geradas métricas personalizadas.
     *
     * @return true se devem ser geradas métricas personalizadas, false caso contrário.
     */
    boolean shouldGenerate() default false;

}
