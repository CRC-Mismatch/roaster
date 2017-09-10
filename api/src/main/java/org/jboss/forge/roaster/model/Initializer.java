package org.jboss.forge.roaster.model;

import org.jboss.forge.roaster.Internal;
import org.jboss.forge.roaster.Origin;

/**
 * Represents a Java initalizer block
 * 
 * @author <a href="mailto:kevin.mian@gmail.com">Kevin Mian Kraiker</a>
 *
 */
public interface Initializer<O extends InitializerHolder<O>> extends StaticCapable, Origin<O>, Internal {
	/**
	 * Gets the inner body of this {@link Initializer} as {@link String}
	 */
	String getBody();
}
