/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.ws.core.jaxws;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Base class for JAX-WS wrapper generation.
 *
 * @author <a href="mailto:jason.greene@jboss.com">Jason T. Greene</a>
 * @version $Revision: 2210 $
 */
public abstract class AbstractWrapperGenerator implements WrapperGenerator
{
   private static Set<String> excludedGetters;
   protected ClassLoader loader;

   public AbstractWrapperGenerator(ClassLoader loader)
   {
      this.loader = loader;
   }
   
   public void reset(ClassLoader loader)
   {
      this.loader = loader;
   }
   
   static
   {
      excludedGetters = new HashSet<String>(4);
      excludedGetters.add("getCause");
      excludedGetters.add("getClass");
      excludedGetters.add("getLocalizedMessage");
      excludedGetters.add("getStackTrace");
      // patch for Java 1.7 and higher
      excludedGetters.add("getSuppressed");
   }

   protected SortedMap<String, Class<?>> getExceptionProperties(Class<?> exception)
   {
      if (! Exception.class.isAssignableFrom(exception))
         throw new IllegalArgumentException("Not an exception");

      TreeMap<String, Class<?>> sortedGetters = new TreeMap<String, Class<?>>();

      for (Method method : exception.getMethods())
      {
         if (java.lang.reflect.Modifier.isStatic(method.getModifiers()))
            continue;

         Class<?> returnType = method.getReturnType();
         if (returnType == void.class)
            continue;

         String name = method.getName();
         if (excludedGetters.contains(name))
            continue;

         int offset;
         if (name.startsWith("get"))
            offset = 3;
         else if (name.startsWith("is"))
            offset = 2;
         else
            continue;

         name = Introspector.decapitalize(name.substring(offset));
         sortedGetters.put(name, returnType);
      }

      return sortedGetters;
   }
}