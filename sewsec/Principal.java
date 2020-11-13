package org.sewsec;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author GHRIBI SAIF EDDINE
 */
public class Principal {

  public String getName() {
    return name;
  }
  public static HashMap < Principal,
  ArrayList > getAuthMap() {
    return authMap;
  }
  /**
     * A unique principal identifier
     */

  private String name;

  /**
     * Authority following the "actsFor" relation
     * (p,p1) belongs to authMap if p1 acts for p.
     */
  static HashMap < Principal,
  ArrayList > authMap = null;

  /**
     * Constructor
     * @param name 
     */

  public Principal(String name) { //Constructeur 
    this.name = name;

  }
  /**
     * Tests the relation between current principal and p
     * @param p
     * @return true if the current principal acts for the principal p
     */

  boolean actsFor(Principal p) {

    if (authMap.get(this).contains(p)) // si p appartient à l'authorithy map de ce Label on retourne true sinon false 

    return true;

    else

    return false;

  }

  /**
     * Adds p in the authority map
     * @param p 
     */

  void addPrincipal(Principal p) {
    ArrayList < Principal > aux = new ArrayList < >(); //declaratation d'un arrayList auxiliere
    if (authMap != null) {
      //ce principal est deja dans la authmap juste on ajoute p
      if (authMap.containsKey(this)) authMap.get(this).add(p);
      else {
        aux.add(p); //sion on ajout ce principal comme key et p comme valeur
        authMap.put(this, aux);
      }
    }
    else {
      authMap = new HashMap < >(); //Creation du HashMap pour la premiere fois seulement 
      aux.add(p);
      authMap.put(this, aux);

    }

  }

  /** 
     * the current principal delegates to p
     * p is then allowed to actsFor the current principal
     */

  public void delegates(Principal p) {
    ArrayList < Principal > aux = new ArrayList < >();
    if (authMap.containsKey(p)) authMap.get(p).add(this); // On ajoute ce principal à p s'il existe 
    else {
      authMap.put(p, aux);
    } //Sinon on ajoute toute une ligne de key p et valeur ce Principal    

  }

}