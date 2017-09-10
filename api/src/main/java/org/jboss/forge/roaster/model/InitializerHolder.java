package org.jboss.forge.roaster.model;

import java.util.List;

/**
 * Represents an element capable of holding initializer blocks.
 *
 * @author <a href="mailto:kevin.mian@gmail.com">Kevin Mian Kraiker</a>
 */
public interface InitializerHolder<O extends InitializerHolder<O>> {
	/**
	 * Returns true if this {@link O} has any initializers
	 */
	boolean hasInitializers();

	/**
	 * Returns true if this {@link O} has an initializer block equal to the given
	 * one
	 */
	boolean hasInitializer(final Initializer<O> initializer);

	/**
	 * Get the {@link Initializer} at the given position, or null if there's no such one
	 */
	Initializer<O> getInitializer(final int position);

	/**
	 * Get a {@link List} of all {@link Initializer}s in this {@link O}
	 * instance, if any; otherwise, return an empty {@link List}
	 */
	List<? extends Initializer<O>> getInitializers();
	
	/**
	 * Get the position of the given {@link Initializer} in this {@ O}, or -1 if not found
	 */
	int getInitializerPosition(final Initializer<O> initializer);
}
