package ec.edu.ups.icc.fundamentos01.security.filters;

import ec.edu.ups.icc.fundamentos01.core.exceptions.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

/*
 * Maneja los errores de autenticación (token ausente, inválido o expirado).
 *
 * Se ejecuta ANTES de llegar al controlador, en la cadena de filtros de
 * Spring Security. Por eso no se puede resolver con @RestControllerAdvice.
 *
 * Nota: usa JsonMapper (Jackson 3, paquete tools.jackson) en vez de
 * ObjectMapper (Jackson 2, com.fasterxml.jackson), porque este proyecto
 * corre sobre Spring Boot 4, que trae Jackson 3 por defecto.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private final JsonMapper jsonMapper;

    public JwtAuthenticationEntryPoint(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        logger.error("Error de autenticación: {}", authException.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Token de autenticación inválido o no proporcionado. " +
                        "Debe incluir un token válido en el header Authorization: Bearer <token>",
                request.getRequestURI()
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
    }
}