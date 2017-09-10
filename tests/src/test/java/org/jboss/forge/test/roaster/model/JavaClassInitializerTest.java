/*
 * Copyright 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.roaster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.InitializerSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassInitializerTest
{
   private JavaClassSource javaClass;

   @Before
   public void reset()
   {
      InputStream stream = JavaClassInitializerTest.class.getResourceAsStream("/org/jboss/forge/grammar/java/MockClassWithInitializers.java");
      javaClass = Roaster.parse(JavaClassSource.class, stream);
   }

   @Test
   public void testGetInitializerByPosition() {
	   InitializerSource<JavaClassSource> initializer = javaClass.getInitializer(1);
	   assertEquals(1, javaClass.getInitializerPosition(initializer));
	   assertFalse(initializer.isStatic());
	   assertEquals("field=\"Field\";", initializer.getBody().trim());
   }
   
   @Test
   public void testAddInitializerByPositionWithInitializer() {
	   JavaClassSource newClass = Roaster.parse(JavaClassSource.class, "public class Clazz { { field=\"New Field\"; } }");
	   InitializerSource<JavaClassSource> initializer = newClass.getInitializer(0);
	   javaClass.addInitializer(1, initializer);
	   initializer = javaClass.getInitializer(1);
	   assertEquals("field=\"New Field\";", initializer.getBody().trim());
	   assertFalse(initializer.isStatic());
	   initializer = javaClass.getInitializer(2);
	   assertEquals("field=\"Field\";", initializer.getBody().trim());;
   }
}
