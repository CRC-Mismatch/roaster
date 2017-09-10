/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.Initializer;
import org.jboss.forge.roaster.model.ast.InitializerFinderVisitor;
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * Represents a Java Source File containing an Enum Type.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaEnumImpl extends AbstractJavaSourceMemberHolder<JavaEnumSource>implements JavaEnumSource
{
   public JavaEnumImpl(JavaSource<?> enclosingType, final Document document, final CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   public List<EnumConstantSource> getEnumConstants()
   {
      List<EnumConstantSource> result = new ArrayList<EnumConstantSource>();

      for (Object o : ((EnumDeclaration) getBodyDeclaration()).enumConstants())
      {
         EnumConstantDeclaration constant = (EnumConstantDeclaration) o;
         result.add(new EnumConstantImpl(this, constant));
      }

      return Collections.unmodifiableList(result);
   }

   @Override
   @SuppressWarnings("unchecked")
   public EnumConstantSource addEnumConstant()
   {
      EnumConstantImpl enumConst = new EnumConstantImpl(this);
      EnumDeclaration enumDeclaration = (EnumDeclaration) getBodyDeclaration();
      List<EnumConstantDeclaration> constants = enumDeclaration.enumConstants();
      constants.add((EnumConstantDeclaration) enumConst.getInternal());

      return enumConst;
   }

   @Override
   @SuppressWarnings("unchecked")
   public EnumConstantSource addEnumConstant(final String declaration)
   {
      EnumConstantImpl enumConst = new EnumConstantImpl(this, declaration);

      EnumDeclaration enumDeclaration = (EnumDeclaration) getBodyDeclaration();
      List<EnumConstantDeclaration> constants = enumDeclaration.enumConstants();
      constants.add((EnumConstantDeclaration) enumConst.getInternal());

      return enumConst;
   }

   @Override
   public EnumConstantSource getEnumConstant(String name)
   {
      for (EnumConstantSource enumConst : getEnumConstants())
      {
         if (enumConst.getName().equals(name))
         {
            return enumConst;
         }
      }
      return null;
   }

   @Override
   protected JavaEnumSource updateTypeNames(final String newName)
   {
      return this;
   }

	@Override
	public boolean hasInitializers() {
		if (!getInitializers().isEmpty())
			return true;
		else
			return false;
	}

	@Override
	public boolean hasInitializer(Initializer<JavaEnumSource> initializer) {
		return getInitializers().contains(initializer);
	}

	@Override
	public int getInitializerPosition(Initializer<JavaEnumSource> initializer) {
		return getInitializers().indexOf(initializer);
	}

	@Override
	public InitializerSource<JavaEnumSource> getInitializer(int position) {
		return getInitializers().get(position);
	}

	@Override
	public List<InitializerSource<JavaEnumSource>> getInitializers() {
		List<InitializerSource<JavaEnumSource>> results = new ArrayList<InitializerSource<JavaEnumSource>>();
		
		InitializerFinderVisitor visitor = new InitializerFinderVisitor();
		body.accept(visitor);
		
		List<org.eclipse.jdt.core.dom.Initializer> initializers = visitor.getInitializers();
		for (org.eclipse.jdt.core.dom.Initializer initializer : initializers)
		{
			results.add(new InitializerImpl<JavaEnumSource>((JavaEnumSource) this, initializer));
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InitializerSource<JavaEnumSource> addInitializer() {
		InitializerSource<JavaEnumSource> i = new InitializerImpl<JavaEnumSource>((JavaEnumSource) this);
		getBodyDeclaration().bodyDeclarations().add(i.getInternal());
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InitializerSource<JavaEnumSource> addInitializer(int position) {
		List<InitializerSource<JavaEnumSource>> curIs = getInitializers();
		int internalPosition = curIs.size();
		if (curIs.size() - 1 >= position) {
			InitializerSource<JavaEnumSource> posI = getInitializers().get(position);
			internalPosition = getBodyDeclaration().bodyDeclarations().indexOf(posI.getInternal());
		}
		InitializerSource<JavaEnumSource> i = new InitializerImpl<JavaEnumSource>((JavaEnumSource) this);
		getBodyDeclaration().bodyDeclarations().add(internalPosition, i.getInternal());
		return i;
	}

	@Override
	public InitializerSource<JavaEnumSource> addInitializer(String body) {
		return addInitializer().setBody(body);
	}

	@Override
	public InitializerSource<JavaEnumSource> addInitializer(Initializer<JavaEnumSource> initializer) {
		return addInitializer().setBody(initializer.getBody()).setStatic(initializer.isStatic());
	}

	@Override
	public InitializerSource<JavaEnumSource> addInitializer(int position, Initializer<JavaEnumSource> initializer) {
		return addInitializer(position).setBody(initializer.getBody()).setStatic(initializer.isStatic());
	}

	@Override
	public JavaEnumSource removeInitializer(int position) {
		InitializerSource<JavaEnumSource> initializer = getInitializers().get(position);
		return removeInitializer(initializer);
	}

	@Override
	public JavaEnumSource removeInitializer(Initializer<JavaEnumSource> initializer) {
		getBodyDeclaration().bodyDeclarations().remove(initializer.getInternal());
		return (JavaEnumSource) this;
	}

}
