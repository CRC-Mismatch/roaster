/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.roaster.model.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Traverses the AST tree looking for Initializer declarations
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 * @author <a href="mailto:kevin.mian@gmail.com">Kevin Mian Kraiker</a>
 * 
 */
public class InitializerFinderVisitor extends ASTVisitor
{
   private final List<Initializer> initializers = new ArrayList<Initializer>();
   private ASTNode parent;

   @Override
   public boolean visit(final TypeDeclaration node)
   {
      parent = node;
      addMethods(node);
      return false;
   }

   @Override
   public boolean visit(EnumDeclaration node)
   {
      parent = node;
      addMethods(node);
      return false;
   }

   @Override
   public boolean visit(AnonymousClassDeclaration node)
   {
      parent = node;
      @SuppressWarnings("unchecked")
      final List<BodyDeclaration> bodyDeclarations = node.bodyDeclarations();
      addMethods(bodyDeclarations);
      return false;
   }

   public List<Initializer> getInitializers()
   {
      return Collections.unmodifiableList(initializers);
   }

   public TypeDeclaration getParent()
   {
      return (TypeDeclaration) parent;
   }

   private void addMethods(AbstractTypeDeclaration node)
   {
      @SuppressWarnings("unchecked")
      final List<BodyDeclaration> bodyDeclarations = node.bodyDeclarations();
      addMethods(bodyDeclarations);
   }

   private void addMethods(final List<BodyDeclaration> bodyDeclarations)
   {
      for (BodyDeclaration bodyDeclaration : bodyDeclarations)
      {
         if (bodyDeclaration instanceof Initializer)
         {
            initializers.add((Initializer) bodyDeclaration);
         }
      }
   }
}