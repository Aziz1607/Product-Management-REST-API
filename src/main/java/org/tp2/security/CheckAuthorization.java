package org.tp2.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Définition de l'annotation @CheckAuthorization
@Target(ElementType.METHOD) // L'annotation peut être utilisée sur les méthodes
@Retention(RetentionPolicy.RUNTIME) // Elle est disponible à l'exécution
public @interface CheckAuthorization {

    // Liste des pays autorisés (par exemple, ["FR", "US"])
    String[] countries() default {};

    // Liste des adresses IP non autorisées (par exemple, ["192.168.1.1", "203.0.113.5"])
    String[] blockedIPs() default {};
}
