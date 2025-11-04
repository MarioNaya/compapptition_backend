package com.compapptition.backend.user.aspect;

import com.compapptition.backend.exception.ForbiddenException;
import com.compapptition.backend.user.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CompeticionContextoAspect {

    @Around("@annotation(RequiereContextoCompeticion) && args(competicionId,..)")
    public Object validarContexto(
            ProceedingJoinPoint joinPoint,
            Long competicionId) throws Throwable {

        SecurityUser securityUser = (SecurityUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!competicionId.equals(securityUser.getCompeticionActual())) {
            throw new ForbiddenException(
                    "Debes seleccionar esta competición antes de realizar esta acción"
            );
        }

        if (!securityUser.tieneAccesoACompeticion(competicionId)) {
            throw new ForbiddenException("No tienes acceso a esta competición");
        }

        return joinPoint.proceed();
    }
}