package org.jboss.forge.roaster.model.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Statement;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Problem;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.ast.ModifierAccessor;
import org.jboss.forge.roaster.model.source.InitializerHolderSource;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaSource;

public class InitializerImpl<O extends InitializerHolderSource<O> & JavaSource<O>> implements InitializerSource<O> {
	private final ModifierAccessor modifiers = new ModifierAccessor();

	private O parent = null;
	private AST ast = null;
	private CompilationUnit cu = null;
	private final Initializer initializer;

	private void init(final O parent) {
		this.parent = parent;
		cu = (CompilationUnit) parent.getInternal();
		ast = cu.getAST();
	}

	public InitializerImpl(final O parent) {
		init(parent);
		initializer = ast.newInitializer();
	}

	public InitializerImpl(final O parent, final Object internal) {
		init(parent);
		initializer = (Initializer) internal;
	}

	public InitializerImpl(final O parent, final String initializer) {
		init(parent);

		String stub = "public class Stub { " + initializer + " }";
		JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
		List<InitializerSource<JavaClassSource>> initializers = temp.getInitializers();
		Initializer newInitializer = (Initializer) initializers.get(0).getInternal();
		Initializer subtree = (Initializer) ASTNode.copySubtree(cu.getAST(), newInitializer);
		this.initializer = subtree;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getBody() {
		Block body = initializer.getBody();
		if (body != null) {
			StringBuilder result = new StringBuilder();
			List<Statement> statements = (List<Statement>) body.getStructuralProperty(Block.STATEMENTS_PROPERTY);
			for (Statement statement : statements) {
				result.append(statement).append(" ");
			}
			return result.toString().trim();
		} else {
			// No body found, shouldn't happen since the body is actually all there is, but whatever, better safe than sorry.
			return null;
		}
	}

	@Override
	public boolean isStatic() {
		return modifiers.hasModifier(initializer, ModifierKeyword.STATIC_KEYWORD);
	}

	@Override
	public InitializerSource<O> setStatic(boolean value) {
		if (value)
			modifiers.addModifier(initializer, ModifierKeyword.STATIC_KEYWORD);
		else
			modifiers.removeModifier(initializer, ModifierKeyword.STATIC_KEYWORD);
		return this;
	}

	@Override
	public InitializerSource<O> setBody(String body) {
		if (body == null)
	      {
	         throw new UnsupportedOperationException("Initializers must have a body.");
	      }
	      else
	      {
	         List<Problem> problems = Roaster.validateSnippet(body);
	         if (problems.size() > 0)
	         {
	            throw new ParserException(problems);
	         }
	         String stub = "public class Stub { {" + body + "} }";
	         JavaClassSource temp = (JavaClassSource) Roaster.parse(stub);
	         List<InitializerSource<JavaClassSource>> initializers = temp.getInitializers();
	         Block block = ((Initializer) initializers.get(0).getInternal()).getBody();
	         block = (Block) ASTNode.copySubtree(initializer.getAST(), block);
	         initializer.setBody(block);
	      }
	      return this;
	}

	@Override
	public O getOrigin() {
		return parent.getOrigin();
	}

	@Override
	public Object getInternal() {
		return initializer;
	}

	@Override
	public int getStartPosition() {
		return initializer.getStartPosition();
	}

	@Override
	public int getEndPosition() {
		int startPosition = getStartPosition();
		return (startPosition == -1) ? -1 : startPosition + initializer.getLength();
	}

	@Override
	public int getLineNumber() {
		return cu.getLineNumber(getStartPosition());
	}

	@Override
	public int getColumnNumber() {
		return cu.getColumnNumber(getStartPosition());
	}

	@Override
	public String toString() {
		return initializer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((initializer == null) ? 0 : initializer.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InitializerImpl<?> other = (InitializerImpl<?>) obj;
		if (initializer == null) {
			if (other.initializer != null)
				return false;
		} else if (!initializer.equals(other.initializer))
			return false;
		return true;
	}
}
