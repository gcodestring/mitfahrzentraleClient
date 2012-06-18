/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mfz.client;

import de.mfz.jaxb.Person;

/**
 *
 * @author Sascha Lemke
 */
public class TestPerson extends Person {

    public TestPerson() {
        this.name = "Hans Peter";
        this.hasRoute = false;
        this.password = "passwort";
        this.email = "email@email.com";
    }
}
