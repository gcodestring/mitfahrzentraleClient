package de.mfz.client;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import de.mfz.jaxb.Fahrten;
import de.mfz.jaxb.Mitfahrzentrale;
import de.mfz.jaxb.Person;

/**
 * Klasse zum Bearbeiten der Serveroperationen.
 * @author Sascha Lemke
 */
public class Operationen {
    
    private Client client;
    private WebResource resource;
    
    public Operationen(String serverurl) {
        // Erstelltung des Clients
        ClientConfig cc = new DefaultClientConfig();
        client = Client.create(cc);
        resource = client.resource(serverurl);
    }
   
    /**
     * Ruft die Daten über GET vom Grizzleyserver ab.
     * @param url Pfad der Ausgabe auf dem Grizzleyserver
     * @return Gibt ein Objekt der Mitfahrzentrale, mit den abgerufenen Inhalten, zurück.
     */
    public Mitfahrzentrale get(String url) {
        ClientResponse cr = resource.path(url).accept("application/xml").get(ClientResponse.class);
        Mitfahrzentrale emfz = cr.getEntity(Mitfahrzentrale.class);
        return emfz;
    }

    /**
     * Erstellt eine Person auf dem Server mit den übergebenen Daten.
     * @param p Die Person die erstellt wird
     */
    public void post(Person p) {
        resource.path("user/create/").post(p);
    }
    
    /**
     * Erstellt eine Fahrt auf dem Server mit den übergebenen Daten.
     * @param f Die Fahrt die erstellt wird
     */
    public void post(Fahrten f) {
        resource.path("fahrten/create").post(ClientResponse.class, f);
    }
    
    /**
     * Updatet eine Person auf dem Server mit den angegebenen Daten.
     * @param p Die neuen Personendaten
     * @param userid Die Personnr
     */
    public void put(Person p, int userid) {
        resource.path("user/edit/" + userid).put(ClientResponse.class, p);
    }
    
    /**
     * Updatet eine Fahrt auf dem Server mit den angegebenen Daten.
     * @param f Die neuen Fahrtdaten
     * @param fahrtenid Die Fahrtnr
     */
    public void put(Fahrten f, int fahrtenid) {
        resource.path("fahrten/edit/" + fahrtenid).put(ClientResponse.class, f);
    }
    
    /**
     * Löscht einen User auf dem Server.
     * @param name Der Name des Users
     */
    public void delete(String name) {
        Mitfahrzentrale zentrale = get("user/");
        for(Person p : zentrale.getUsers().getPerson()) {
            if(p.getName().equals(name)) {
                int userid = zentrale.getUsers().getPerson().indexOf(p);
                resource.path("user/delete/" + userid).delete();
            }
        }
    }
    
    /**
     * Löscht eine Fahrt auf dem Server.
     * @param fahrtID Die Nummer der Fahrt
     */
    public void delete(int fahrtID) {
        resource.path("fahrten/delete/" + fahrtID).delete();
    }
}
