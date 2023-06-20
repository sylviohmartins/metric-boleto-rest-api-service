package io.sylviohmartins.metric.metric.nested;

import java.lang.annotation.*;

/**
 * <h1>Tag</h1>
 * <p>Anotação usada para marcar métodos ou tipos com informações de tag.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@autor</strong> Sylvio Humberto Martins</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Tag {

    /**
     * Retorna o nome da tag.
     *
     * @return o nome da tag
     */
    String name();

    /**
     * Retorna o valor da tag.
     *
     * @return o valor da tag
     */
    String value();

}
