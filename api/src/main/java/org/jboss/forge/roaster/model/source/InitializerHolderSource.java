package org.jboss.forge.roaster.model.source;

import java.util.List;

import org.jboss.forge.roaster.model.Initializer;
import org.jboss.forge.roaster.model.InitializerHolder;

/**
 * Represents an element capable of initializer blocks.
 *
 * @author <a href="mailto:kevin.mian@gmail.com">Kevin Mian Kraiker</a>
 */
public interface InitializerHolderSource<O extends InitializerHolderSource<O>> extends InitializerHolder<O> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	InitializerSource<O> getInitializer(final int position);

	/**
	 * {@inheritDoc}
	 */
	@Override
	List<InitializerSource<O>> getInitializers();

	/**
	 * Add an empty {@link InitializerSource} after the last existing one in this {@link O}
	 * @return the newly created initializer block
	 */
	InitializerSource<O> addInitializer();
	
	/**
	 * Add an empty {@link InitializerSource} at the given position in this {@link O}, pushing forward the existing ones at and after it. 
	 * @return the newly created initializer block
	 */
	InitializerSource<O> addInitializer(final int position);
	
	/**
	 * Add a new initializer block after the last existing one in this {@link O}, with the given body
	 * @param body The new initializer block's body
	 * @return the newly created initializer block
	 */
	InitializerSource<O> addInitializer(final String body);
	
	/**
	 * Add an {@link InitializerSource} after the last existing one in this {@link O}, with the same body and static attribute as the given one
	 * @param initializer The initializer block to copy the body and static attribute from
	 * @return The newly created initializer block
	 */
	InitializerSource<O> addInitializer(final Initializer<O> initializer);
	
	/**
	 * Add an {@link InitializerSource} at the given position in this {@link O}, with the same body and static attribute as the given one
	 * @param initializer The initializer block to copy the body and static attribute from
	 * @return The newly created initializer block
	 */
	InitializerSource<O> addInitializer(int position, final Initializer<O> initializer);
	
	/**
	 * Remove the initializer at this position if it exists; otherwise, do nothing
	 * @param position The position of the initializer to be removed
	 * @return this {@link O} itself
	 */
	O removeInitializer(final int position);
	
	/**
	 * Remove the given initializer from this {@ O} if it exists; otherwise, do nothing
	 * @param initializer The initializer to look for and remove
	 * @return this {@link O} itself
	 */
	O removeInitializer(final Initializer<O> initializer);
}
