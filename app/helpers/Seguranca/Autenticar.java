package helpers.Seguranca;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;

/**
 * Anotação responsável por abstrair a chamada da classe <strong>AutenticarAction.java</strong>,
 * que restringe o acesso somente para usuários autenticados no sistema.
 * 
 * @author allysonbarros
 */
@With(AutenticarAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autenticar {
	
}