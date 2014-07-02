/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package org.exoplatform.ide.operation.java;

import java.util.ArrayList;

public class JavaCommentsTest
{
   private ArrayList<Integer> numbers = new ArrayList<Integer>();

   public JavaCommentsTest()
   {
      numbers.add(1);
      numbers.add(2);
      numbers.add(3);
      numbers.add(4);
      numbers.add(5);
      numbers.add(6);
   }

   public ArrayList<Integer> getNumbers()
   {
      return numbers;
   }

   public Integer sum(Integer x, Integer y)
   {
      return x + y;
   }

   public Integer subtraction(Integer x, Integer y)
   {
      return x - y;
   }
}
