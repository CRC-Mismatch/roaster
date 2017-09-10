package org.jboss.forge.roaster.model.source;

import org.jboss.forge.roaster.model.Initializer;

/**
 * Represents a Java initalizer block in source form
 * 
 * @author <a href="mailto:kevin.mian@gmail.com">Kevin Mian Kraiker</a>
 *
 */
public interface InitializerSource<O extends InitializerHolderSource<O>> extends Initializer<O>, StaticCapableSource<InitializerSource<O>>, LocationCapable {
	/**
	 * Set the inner body of this {@link Initializer}
	 */
	InitializerSource<O> setBody(final String body);
}
