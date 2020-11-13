package org.sewsec;

import static java.lang.reflect.Array.set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/*
 * Label representation and principals
 */

/**
 *
 * @author GHRIBI SAIF EDDINE
 */
public class Label {
  /**
  * Each label contains a map of principals (owner, readers)
  */
  private HashMap < Principal,
  ArrayList > lmap = null;

  /**
  * Constructor1 : creates a label with a an owner and a reader
  * @param owner
  * @param reader 
  */

  public Label(Principal owner, Principal reader) { //Constructeur 
    if (owner != null) {
      ArrayList < Principal > aux = new ArrayList < >();
      aux.add(reader);
      this.addPolicy(owner, aux); // on appelle addPolicy afin d'ajouter une ligne dans la HashMap 
    }
    else lmap = new HashMap < >(); //on alloue la zone memoire pour lmap 
  }

  public HashMap < Principal,
  ArrayList > getLmap() {

    return lmap;
  }

  public int compareTo(Label l) {
    int test1 = 1; //test1 pour l'inclusion dans le sens direct 
    int test2 = 1; // test2 pour tester l'inclusion dans le sens inverse 
    if (l.lmap == null) { //label trivial il est moins restrective que tous les labels 
      return - 1;
    }
    if (l.lmap.keySet().containsAll(lmap.keySet())) //owners l1 inclus dans owners l2
    {
      Set s = lmap.keySet(); 
      //parcourir les owners 
      Iterator < Principal > it = s.iterator();

      while (it.hasNext()) {
        Principal aux = it.next();
        if (!lmap.get(aux).containsAll(l.lmap.get(aux))) test1 = 0; //tous les readers d'un owner de l2 sont  inclus dans ceux de l1 
      }

    }

    if (lmap.keySet().containsAll(l.lmap.keySet())) //owners l2 inclus owners l1 
    {
      Set s = l.lmap.keySet();
      int test = 1;
      Iterator < Principal > it = s.iterator();

      while (it.hasNext()) {
        Principal aux = it.next();
        if (!l.lmap.get(aux).containsAll(lmap.get(aux))) test2 = 0; //tous les readers d'un owner de l1 sont  inclus dans ceux de l2 
      }

    }
    if (test1 == 1 && test2 == 1) return 0; //double inclusion implique l1=l2 
    if (test1 == 1) return 1; // l2 est plus restrictif que l1 
    if (test2 == 1) return - 1; // l1 est plus restrictif que l2 
    //

    return - 42000; //l1 est l2 sont incomparables comme l'exemple suivant {hopital:patient} et {hopitam:medecin}

  }

  public void addPolicy(Principal owner, ArrayList < Principal > readers) {

    if (lmap != null)

    {
      //ajouter un policy :les readers si l'owner deja existe dans lmap 
      if (lmap.containsKey(owner)) {
        lmap.get(owner).addAll(readers);
      } else {
        //sinon toute une ligne (owner,readers) 

        lmap.put(owner, readers);
      }
    } else {
      lmap = new HashMap < >();
      lmap.put(owner, readers);
    }

  }

  public Label min(Label l) {
    Label aux = new Label(null, null); //declarer le label resultant et allouer sa memoire
    HashMap < Principal,
    ArrayList > auxmap = new HashMap < >(); //de meme pour son lmap
    auxmap = lmap; //initialiser lmap resultant a lmap de l1 afin de preparer l'a  //l1 ce label ,l2 label passé en parametre 
    Set s = l.lmap.keySet();
    Iterator < Principal > it = s.iterator();
    while (it.hasNext()) { //parcourir l.lmap
      Principal auxp = it.next();
      if (!auxmap.containsKey(auxp)) { //si un owner de l2 n'existe pas   
        auxmap.put(auxp, l.lmap.get(auxp));
      } //on l'ajoute directement au lmap resultant  

      else { //sinon on fait l'intersections des readers 
        ArrayList < Principal > tab = new ArrayList < >();
        tab.addAll(auxmap.get(auxp));
        tab.retainAll(l.lmap.get(auxp)); //l'intersection 
        auxmap.get(auxp).clear();
        auxmap.get(auxp).addAll(tab);

      }
    }
    aux.setLmap(auxmap);
    return aux;
  }

  public void setLmap(HashMap < Principal, ArrayList > lmap) {
    this.lmap = lmap;
  }

  public void afficheLab() {
    System.out.println("Il s'agit d'un nouveau Labels");
    Set auxs = lmap.keySet();
    Iterator < Principal > it = auxs.iterator();
    
    while (it.hasNext()) {
      Principal auxp = it.next();
      System.out.println("Owner--> " + auxp.getName());
      System.out.println(lmap.get(auxp).size());
      for (int i = 0; i < lmap.get(auxp).size(); i++) {
        Principal shit = (Principal) lmap.get(auxp).get(i);
        int x = i + 1;
        System.out.println("Reader num--> " + x + " -->" + shit.getName());
      }
    }
  }

  public boolean declassify(Principal p, Label l) {

    if (l.compareTo(this) == 0) // l et ce label sont identiques pas de declassification 
    return true;
    else if (l.compareTo(this) == -1) // l est plus restrictif que ce label on retourne false 
    return false;
    else {
      boolean result = true;
      Set < Principal > s = lmap.keySet();
      Iterator < Principal > it = s.iterator();

      while (it.hasNext()) // on parcoure les owners de ce label 
      {
        Principal aux = it.next();

        if (l.lmap.containsKey(aux)) //s'il ya un owner dans le label l et ce label 
        {

          if (l.lmap.get(aux).size() > lmap.get(aux).size()) //s'il contient plus de readers dans l2
          {
            if (!aux.actsFor(p)) //une declassification a eu lieu mais elle est legitime ssi p actsFor ce owner 
            {
              result = false; //on teste
              break;
            }
          }
        }
        else //Owner supprimé ssi p actsFor ce Owner 
        if (!aux.actsFor(p)) //on teste 
        {
          result = false;
          break;
        }

      }
      return result; //on retourne le resultat des tests.

    }

  }
  public boolean relabel(Label l) {

    Iterator < Principal > it = lmap.keySet().iterator(); //un iterator pour parcourir hashmap   
    while (it.hasNext()) {
      Principal aux = it.next();
      if (l.lmap.containsKey(aux)) if (l.lmap.get(aux).size() < lmap.get(aux).size()) //un reader est enlevé
      {
        return true;
      }

    }
    it = l.lmap.keySet().iterator();
    while (it.hasNext()) {
      Principal aux = it.next();
      if (!lmap.containsKey(aux)) //une policy est ajouté
      return true;

    }
    it = l.lmap.keySet().iterator();
    while (it.hasNext()) {
      Principal aux = it.next();
      if (lmap.containsKey(aux)) {
        if (l.lmap.get(aux).size() == lmap.get(aux).size() + 1) {
          ArrayList < Principal > a = new ArrayList < >();
          a.addAll(l.lmap.get(aux));
          a.retainAll(lmap.get(aux));
          Principal Reader_r_prime = a.get(0); //r prime 
          a.clear();
          a.addAll(lmap.get(aux));
          for (Principal r: a) // on cherche est ce qu'il ya un reader telque r pime actsFor r 
          {
            if (Reader_r_prime.actsFor(r)) return true;

          }

        }

      }

    }
    Set s1 = lmap.keySet();
    Set s2 = l.lmap.keySet();
    Iterator < Principal > it1 = s1.iterator();
    Iterator < Principal > it2 = s2.iterator();
    while (it1.hasNext())
    while (it2.hasNext()) {
      Principal aux1 = it1.next();
      Principal aux2 = it2.next();
      if (lmap.get(aux1).equals(l.lmap.get(aux2)) && !aux1.getName().equals(aux2.getName()) && aux2.actsFor(aux1))
      //Owner2 et owner 1 ont les memeS readers donc on verifie qu'ils sont differnts et que owner2 actsFor owner1
      {
        return true;

      }

    }

    return false; //sinon on retourne false 

  }
}