package modules;

import com.google.inject.AbstractModule;

/**
 * Classe Module que utiliza Guice atuando como service que subtitui o
 *  {@code Global.onStart()} das versões anteriores do Play.
 * @author Alessandro
 *
 */
public class OnStartupModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StartUpHandler.class).asEagerSingleton(); 
	}

}
