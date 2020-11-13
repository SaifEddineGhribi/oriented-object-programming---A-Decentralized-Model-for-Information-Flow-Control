

package org.sewsec;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author GHRIBI SAIF EDDINE
 */

public class TestSewsec {
  /**
     * @param args the command line arguments
     */
  public static void main(String[] args) {
    Principal p1 = new Principal("p1");
    Principal p2 = new Principal("p2");
    Principal p3 = new Principal("p3");

    ArrayList < Principal > a = new ArrayList < >();
    a.add(p3);
    Label l1 = new Label(p1, p2); //l1={p1:p2}
    l1.afficheLab();
    Label l2 = new Label(p1, p2);
    l2.addPolicy(p1, a); //l2={p1:p2,p3}
    l2.afficheLab();
    l1.min(l2).afficheLab();
    System.out.println("resultat de compareTO=" + l1.compareTo(l2));
  

  }
}
