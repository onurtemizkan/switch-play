package com.fasterxml.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation that can be used to define constructors and factory
 * methods as one to use for instantiating new instances of the associated
 * class.
 *<p>
 * NOTE: when annotating creator methods (constructors, factory methods),
 * method must either be:
 *<ul>
 * <li>Single-argument constructor/factory method without {@link JsonProperty}
 *    annotation for the argument: if so, this is so-called "delegate creator",
 *    in which case Jackson first binds JSON into type of the argument, and
 *    then calls creator
 *   </li>
 * <li>Constructor/factory method where <b>every argument</b> is annotated with
 *   either {@link JsonProperty} or {@link JacksonInject}, to indicate name
 *   of property to bind to
 *  </li>
 * </ul>
 * Also note that all {@link JsonProperty} annotations MUST use actual name
 * (NOT empty String for "default"): this because Java bytecode does not
 * retain names of method or constructor arguments.
 *<br />
 * NOTE: as of JDK 8, some of above changes, with introduction of names for
 * constructor and method parameters.
 *
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonCreator
{
    /**
     * Property that is used to indicate how argument(s) is/are bound for creator,
     * in cases there may be multiple alternatives. Currently the one case is that
     * of a single-argument creator method, for which both so-called "delegating" and
     * "property-based" bindings are possible: since
     * delegating mode can not be used for multi-argument creators, the only choice
     * there is "property-based" mode.
     * Check {@link Mode} for more complete explanation of possible choices.
     *<p>
     * Default value of {@link Mode#DEFAULT} means that caller is to use standard
     * heuristics for choosing mode to use.
     * 
     * @since 2.5
     */
    public Mode mode() default Mode.DEFAULT;

    /**
     * @since 2.5
     */
    public enum Mode {
        /**
         * Pseudo-mode that indicates that caller is to use default heuristics for
         * choosing mode to use. This typically favors use of delegating mode for
         * single-argument creators that take structured types.
         */
        DEFAULT,

        /**
         * Mode that indicates that if creator takes a single argument, the whole incoming
         * data value is to be bound into declared type of that argument; this "delegate"
         * value is then passed as the argument to creator.
         */
        DELEGATING,

        /**
         * Mode that indicates that the argument(s) for creator are to be bound from matching
         * properties of incoming Object value, using creator argument names (explicit or implicit)
         * to match incoming Object properties to arguments.
         *<p>
         * Note that this mode is currently (2.5) always used for multiple-argument creators;
         * the only ambiguous case is that of a single-argument creator.
         */
        PROPERTIES,

        /**
         * Pseudo-mode that indicates that creator is not to be used. This can be used as a result
         * value for explicit disabling, usually either by custom annotation introspector,
         * or by annotation mix-ins (for example when choosing different creator).
         */
        DISABLED
    }
}
