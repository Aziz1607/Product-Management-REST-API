package org.tp2.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class AuthorizationAspect {

    private final HttpServletRequest request;

    public AuthorizationAspect(HttpServletRequest request) {
        this.request = request;
    }

    // Définir un pointcut pour toutes les méthodes annotées avec @CheckAuthorization
    @Pointcut("@annotation(org.tp2.security.CheckAuthorization)")
    public void checkAuthorizationPointcut() {
    }

    // Méthode qui sera exécutée avant chaque méthode annotée @CheckAuthorization
    @Before("checkAuthorizationPointcut() && @annotation(checkAuthorization)")
    public void checkAuthorization(org.tp2.security.CheckAuthorization checkAuthorization) throws Throwable {

        // Récupérer l'adresse IP de l'utilisateur
        String clientIP = request.getRemoteAddr();

        // Vérifier si l'adresse IP est dans la liste des adresses bloquées
        List<String> blockedIPs = Arrays.asList(checkAuthorization.blockedIPs());
        if (blockedIPs.contains(clientIP)) {
            throw new SecurityException("Access denied: Your IP is blocked.");
        }

        // Vérifier si le pays de l'utilisateur est dans la liste des pays autorisés
        String clientCountry = getClientCountry(clientIP); // Cette méthode devra implémenter la logique de récupération du pays
        List<String> allowedCountries = Arrays.asList(checkAuthorization.countries());
        if (!allowedCountries.isEmpty() && !allowedCountries.contains(clientCountry)) {
            throw new SecurityException("Access denied: Your country is not authorized.");
        }
    }

    // Exemple de méthode pour déterminer le pays de l'utilisateur basé sur son adresse IP
    private String getClientCountry(String clientIP) {
        // Implémentation simplifiée : En réalité, vous pouvez utiliser une API tierce comme Ipstack, MaxMind, etc.
        // Par exemple, voici un pays fictif en fonction de l'IP.
        if (clientIP.startsWith("192.168")) {
            return "FR";  // Supposons que l'IP commence par 192.168 -> France
        } else if (clientIP.startsWith("203.0")) {
            return "US";  // Supposons que l'IP commence par 203.0 -> États-Unis
        }
        return "UNKNOWN"; // Valeur par défaut si le pays est inconnu
    }
}
