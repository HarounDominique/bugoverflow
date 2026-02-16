package com.haroun.bugoverflow.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.haroun.bugoverflow.service.PerfilUsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the usuario value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = PerfilUsuarioUsuarioUnique.PerfilUsuarioUsuarioUniqueValidator.class
)
public @interface PerfilUsuarioUsuarioUnique {

    String message() default "{Exists.perfilUsuario.usuario}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PerfilUsuarioUsuarioUniqueValidator implements ConstraintValidator<PerfilUsuarioUsuarioUnique, Long> {

        private final PerfilUsuarioService perfilUsuarioService;
        private final HttpServletRequest request;

        public PerfilUsuarioUsuarioUniqueValidator(final PerfilUsuarioService perfilUsuarioService,
                final HttpServletRequest request) {
            this.perfilUsuarioService = perfilUsuarioService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(perfilUsuarioService.get(Long.parseLong(currentId)).getUsuario())) {
                // value hasn't changed
                return true;
            }
            return !perfilUsuarioService.usuarioExists(value);
        }

    }

}
