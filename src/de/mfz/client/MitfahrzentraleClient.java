package de.mfz.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import de.mfz.coordinator.RouteChangedCoordinator;
import de.mfz.jaxb.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.*;
/**
 * Die Klasse für den Client, regelt alle Abfragen und auch die Darstellung.
 * @author Sascha Lemke, Guido Schori, Rene Zwinge
 */
public class MitfahrzentraleClient extends JFrame {
    
	private final String serverurl = "http://localhost:4434/mitfahrzentrale";
    private Connection xmppcon;
    private PubSubManager pubsub;
    private LeafNode leafs[];
    private Client client;
    private WebResource resource;
    private Mitfahrzentrale mfz;
    private Person loggedPerson;
    private DefaultListModel<String> model;
    
    /**
     * Creates new form MitfahrzentraleClient
     */
    public MitfahrzentraleClient() {
        // initalisieren der components
        initComponents();        
        
        
        // Erstelltung des Clients
        ClientConfig cc = new DefaultClientConfig();
        client = Client.create(cc);
        resource = client.resource(serverurl);
        
        // Beispielnutzer wegen login
        this.loggedPerson = new Person();
        this.loggedPerson.setName("Hans Peter");
        this.loggedPerson.setHasRoute(false);
        this.loggedPerson.setPassword("passwort");
        this.loggedPerson.setEmail("email@email.com");
        
        // abrufen der daten für die Hauptseite
        this.mfz = get("");
        this.model = new DefaultListModel();
        Routenliste.setModel(this.model);
        for(int i = 0; i < this.mfz.getFahrten().size(); i++) {
            String item = "";
            item += (i +1) + ". ";
            item += this.mfz.getFahrten().get(i).getRoute().getStartpunkt();
            item += " > ";
            item += this.mfz.getFahrten().get(i).getRoute().getZielpunkt();
            this.model.add(i, item);
        }
        
        this.setLocationRelativeTo(null);

        /**
         * XMPP Verbindung starten
         */ 
        this.xmppcon = new XMPPConnection("localhost");
        try {
            this.xmppcon.connect();
        } catch (XMPPException ex) {
            System.out.println("Konnte nicht zum XMPP Server verbinden.");
        }

        /**
         * Am XMPP Server anmelden
         */ 
        try {
            this.xmppcon.login("admin", "admin");
        } catch( IllegalStateException | XMPPException e) {
            System.out.println("Login fehlgeschlagen.");
        }
 
        /**
         * PubSubManager erstellen
         */ 
        this.pubsub = new PubSubManager(this.xmppcon);
        
        ConfigureForm form = new ConfigureForm(FormType.submit);
        form.setAccessModel(AccessModel.open);
        form.setDeliverPayloads(true);
        form.setNotifyRetract(true);
        form.setPersistentItems(true);
        form.setPublishModel(PublishModel.open);
        
        /**
         * Fahrten Nodes erstellen
         */ 
        this.leafs = new LeafNode[this.mfz.getFahrten().size()];
        for(int i = 0; i < this.mfz.getFahrten().size(); i++) {            
            try {
                try {
                    // testen ob node vorhanden und wenn ja, löschen!
                    Node n = this.pubsub.getNode("Route" + i);
                    this.pubsub.deleteNode("Route" + i);
                } catch(XMPPException e) {
                    // keine Node vorhanden
                }
                this.leafs[i] = (LeafNode) this.pubsub.createNode("Route" + i, form);
            } catch (XMPPException ex) {
                System.out.println("Konnte keine Node erstellen. Überprüfen Sie ihre Verbindung!");
            }
        }
        
        /**
         * Erstelle Subscriptions
         * Node zu dieser ge�nderten Fahrt abrufen
         * Listener f�r diese Node f�r ge�nderte Routen in diesem Client starten
         * User abonniert diesen Node
         */
        for (int i = 0; i < this.mfz.getFahrten().size(); i++) { 
        	try {
        		LeafNode node = (LeafNode) this.pubsub.getNode("Route" + i);  
        		for (int a = 0; a < this.mfz.getFahrten().get(i).getMitfahrer().getPerson().size(); a++) { 
                    node.addItemEventListener(new RouteChangedCoordinator());
    				node.subscribe(this.mfz.getFahrten().get(i).getMitfahrer().getPerson().get(a).getEmail());
    			}
        	} catch (XMPPException e) {
				e.printStackTrace();
			}
        }
        
        this.mfz.getFahrten().get(0).getMitfahrer().getPerson().get(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        RouteNeuDialog = new javax.swing.JDialog();
        RouteNeuPanel = new javax.swing.JPanel();
        RouteNeuStartortFeld = new javax.swing.JTextField();
        RouteNeuStartortLabel = new javax.swing.JLabel();
        RouteNeuZielortFeld = new javax.swing.JTextField();
        RouteNeuZielortLabel = new javax.swing.JLabel();
        RouteNeuAbfahrtFeld = new javax.swing.JTextField();
        RouteNeuAbfahrtLabel = new javax.swing.JLabel();
        RouteNeuMobiltelefonFeld = new javax.swing.JTextField();
        RouteNeuMobiltelefonLabel = new javax.swing.JLabel();
        RouteNeuSaveButton = new javax.swing.JButton();
        RouteNeuSitzplaetzeFeld = new javax.swing.JTextField();
        RouteNeuSitzplaetzeLabel = new javax.swing.JLabel();
        RouteEditDialog = new javax.swing.JDialog();
        RouteEditPanel = new javax.swing.JPanel();
        RouteEditStartortFeld = new javax.swing.JTextField();
        RouteEditStartortLabel = new javax.swing.JLabel();
        RouteEditZielortLabel = new javax.swing.JLabel();
        RouteEditZielortFeld = new javax.swing.JTextField();
        RouteEditAbfahrtFeld = new javax.swing.JTextField();
        RouteEditMobiletelefonFeld = new javax.swing.JTextField();
        RouteEditAbfahrtLabel = new javax.swing.JLabel();
        RouteEditMobiltelefonLabel = new javax.swing.JLabel();
        RouteEditSitzplaetzeFeld = new javax.swing.JTextField();
        RouteEditSitzplaetzeLabel = new javax.swing.JLabel();
        RouteEditSaveButton = new javax.swing.JButton();
        UserCreateDialog = new javax.swing.JDialog();
        UserCreatePanel = new javax.swing.JPanel();
        UserCreateNameFeld = new javax.swing.JTextField();
        UserCreateNameLabel = new javax.swing.JLabel();
        UserCreatePasswortFeld = new javax.swing.JTextField();
        UserCreatePasswortLabel = new javax.swing.JLabel();
        UserCreateEmailFeld = new javax.swing.JTextField();
        UserCreateEmailLabel = new javax.swing.JLabel();
        UserCreateSaveButton = new javax.swing.JButton();
        UserEditDialog = new javax.swing.JDialog();
        UserEditPanel = new javax.swing.JPanel();
        UserEditNameFeld = new javax.swing.JTextField();
        UserEditNameLabel = new javax.swing.JLabel();
        UserEditEmailFeld = new javax.swing.JTextField();
        UserEditEmailLabel = new javax.swing.JLabel();
        UserEditPasswortFeld = new javax.swing.JTextField();
        UserEditPasswortLabel = new javax.swing.JLabel();
        UserEditSaveButton = new javax.swing.JButton();
        UserDeleteDialog = new javax.swing.JDialog();
        UserDeletePanel = new javax.swing.JPanel();
        UserDeleteUsernameFeld = new javax.swing.JTextField();
        UserDeleteUsernameLabel = new javax.swing.JLabel();
        UserDeleteSaveButton = new javax.swing.JButton();
        RouteDeleteDialog = new javax.swing.JDialog();
        RouteDeletePanel = new javax.swing.JPanel();
        RouteDeleteNrFeld = new javax.swing.JTextField();
        RouteDeleteNrLabel = new javax.swing.JLabel();
        RouteDeleteSaveButton = new javax.swing.JButton();
        UserEditQDialog = new javax.swing.JDialog();
        UserDeleteQPanel = new javax.swing.JPanel();
        UserEditQNameFeld = new javax.swing.JTextField();
        UserEditQNameLabel = new javax.swing.JLabel();
        UserEditQSaveButton = new javax.swing.JButton();
        RouteEditQDialog = new javax.swing.JDialog();
        RouteEditQPanel = new javax.swing.JPanel();
        RouteEditQNrFeld = new javax.swing.JTextField();
        RouteEditQNrLabel = new javax.swing.JLabel();
        RouteEditQSaveButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Routenliste = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        Startort = new javax.swing.JLabel();
        startortfeld = new javax.swing.JTextField();
        zielortfeld = new javax.swing.JTextField();
        zielort = new javax.swing.JLabel();
        abfahrtstdfeld = new javax.swing.JTextField();
        abfahrt = new javax.swing.JLabel();
        sitzplaetze = new javax.swing.JLabel();
        sitzplaetzefeld = new javax.swing.JTextField();
        anmeldenbutton = new javax.swing.JButton();
        fahrerfeld = new javax.swing.JTextField();
        fahrerlabel = new javax.swing.JLabel();
        mitfahrerlabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mitfahrerlist = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        RouteMenu = new javax.swing.JMenu();
        RouteButton = new javax.swing.JMenuItem();
        RouteEditButton = new javax.swing.JMenuItem();
        RouteDeleteButton = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        CloseButton = new javax.swing.JMenuItem();
        Usermenu = new javax.swing.JMenu();
        UserCreateButton = new javax.swing.JMenuItem();
        UserEditButton = new javax.swing.JMenuItem();
        UserDeleteButton = new javax.swing.JMenuItem();

        RouteNeuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Neue Route anlegen"));

        RouteNeuStartortLabel.setText("Startort:");

        RouteNeuZielortLabel.setText("Zielort:");

        RouteNeuAbfahrtLabel.setText("Abfahrt:");

        RouteNeuMobiltelefonLabel.setText("Mobiltelefon:");

        RouteNeuSaveButton.setText("Route speichern");
        RouteNeuSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteNeuSaveButtonActionPerformed(evt);
            }
        });

        RouteNeuSitzplaetzeLabel.setText("Sitzplätze:");

        javax.swing.GroupLayout RouteNeuPanelLayout = new javax.swing.GroupLayout(RouteNeuPanel);
        RouteNeuPanel.setLayout(RouteNeuPanelLayout);
        RouteNeuPanelLayout.setHorizontalGroup(
            RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteNeuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RouteNeuPanelLayout.createSequentialGroup()
                        .addGroup(RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RouteNeuStartortLabel)
                            .addComponent(RouteNeuZielortLabel)
                            .addComponent(RouteNeuAbfahrtLabel)
                            .addComponent(RouteNeuMobiltelefonLabel)
                            .addComponent(RouteNeuSitzplaetzeLabel))
                        .addGap(18, 18, 18)
                        .addGroup(RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RouteNeuStartortFeld)
                            .addComponent(RouteNeuZielortFeld)
                            .addComponent(RouteNeuAbfahrtFeld)
                            .addComponent(RouteNeuMobiltelefonFeld)
                            .addComponent(RouteNeuSitzplaetzeFeld, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RouteNeuPanelLayout.createSequentialGroup()
                        .addGap(0, 233, Short.MAX_VALUE)
                        .addComponent(RouteNeuSaveButton)))
                .addContainerGap())
        );
        RouteNeuPanelLayout.setVerticalGroup(
            RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteNeuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteNeuStartortFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteNeuStartortLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteNeuZielortFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteNeuZielortLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteNeuAbfahrtFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteNeuAbfahrtLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteNeuMobiltelefonFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteNeuMobiltelefonLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RouteNeuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteNeuSitzplaetzeFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteNeuSitzplaetzeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RouteNeuSaveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout RouteNeuDialogLayout = new javax.swing.GroupLayout(RouteNeuDialog.getContentPane());
        RouteNeuDialog.getContentPane().setLayout(RouteNeuDialogLayout);
        RouteNeuDialogLayout.setHorizontalGroup(
            RouteNeuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteNeuDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RouteNeuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        RouteNeuDialogLayout.setVerticalGroup(
            RouteNeuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteNeuDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RouteNeuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RouteEditPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Route bearbeiten"));

        RouteEditStartortLabel.setText("Startort:");

        RouteEditZielortLabel.setText("Zielort:");

        RouteEditAbfahrtLabel.setText("Abfahrt:");

        RouteEditMobiltelefonLabel.setText("Mobiltelefon:");

        RouteEditSitzplaetzeLabel.setText("Sitzplätze:");

        RouteEditSaveButton.setText("Route speichern");
        RouteEditSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteEditSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RouteEditPanelLayout = new javax.swing.GroupLayout(RouteEditPanel);
        RouteEditPanel.setLayout(RouteEditPanelLayout);
        RouteEditPanelLayout.setHorizontalGroup(
            RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RouteEditPanelLayout.createSequentialGroup()
                        .addGroup(RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RouteEditStartortLabel)
                            .addComponent(RouteEditZielortLabel)
                            .addComponent(RouteEditAbfahrtLabel)
                            .addComponent(RouteEditMobiltelefonLabel)
                            .addComponent(RouteEditSitzplaetzeLabel))
                        .addGap(18, 18, 18)
                        .addGroup(RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RouteEditZielortFeld, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                            .addComponent(RouteEditStartortFeld)
                            .addComponent(RouteEditAbfahrtFeld)
                            .addComponent(RouteEditMobiletelefonFeld)
                            .addComponent(RouteEditSitzplaetzeFeld)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RouteEditPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(RouteEditSaveButton)))
                .addContainerGap())
        );
        RouteEditPanelLayout.setVerticalGroup(
            RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteEditPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteEditStartortFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteEditStartortLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteEditZielortFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteEditZielortLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteEditAbfahrtFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteEditAbfahrtLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteEditMobiletelefonFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteEditMobiltelefonLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RouteEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteEditSitzplaetzeFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteEditSitzplaetzeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RouteEditSaveButton)
                .addGap(79, 79, 79))
        );

        javax.swing.GroupLayout RouteEditDialogLayout = new javax.swing.GroupLayout(RouteEditDialog.getContentPane());
        RouteEditDialog.getContentPane().setLayout(RouteEditDialogLayout);
        RouteEditDialogLayout.setHorizontalGroup(
            RouteEditDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteEditDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RouteEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        RouteEditDialogLayout.setVerticalGroup(
            RouteEditDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteEditDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RouteEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        UserCreatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("User erstellen"));

        UserCreateNameLabel.setText(" Name:");

        UserCreatePasswortLabel.setText("Passwort:");

        UserCreateEmailLabel.setText("E-Mail:");

        UserCreateSaveButton.setText("User erstellen");
        UserCreateSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserCreateSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UserCreatePanelLayout = new javax.swing.GroupLayout(UserCreatePanel);
        UserCreatePanel.setLayout(UserCreatePanelLayout);
        UserCreatePanelLayout.setHorizontalGroup(
            UserCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserCreatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UserCreatePanelLayout.createSequentialGroup()
                        .addGroup(UserCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserCreatePasswortLabel)
                            .addComponent(UserCreateNameLabel)
                            .addComponent(UserCreateEmailLabel))
                        .addGap(18, 18, 18)
                        .addGroup(UserCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserCreateNameFeld)
                            .addComponent(UserCreatePasswortFeld)
                            .addComponent(UserCreateEmailFeld)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UserCreatePanelLayout.createSequentialGroup()
                        .addGap(0, 245, Short.MAX_VALUE)
                        .addComponent(UserCreateSaveButton)))
                .addContainerGap())
        );
        UserCreatePanelLayout.setVerticalGroup(
            UserCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserCreatePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(UserCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserCreateNameFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserCreateNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(UserCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserCreateEmailFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserCreateEmailLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(UserCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserCreatePasswortFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserCreatePasswortLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UserCreateSaveButton))
        );

        javax.swing.GroupLayout UserCreateDialogLayout = new javax.swing.GroupLayout(UserCreateDialog.getContentPane());
        UserCreateDialog.getContentPane().setLayout(UserCreateDialogLayout);
        UserCreateDialogLayout.setHorizontalGroup(
            UserCreateDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserCreateDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserCreatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        UserCreateDialogLayout.setVerticalGroup(
            UserCreateDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserCreateDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserCreatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        UserEditPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("User bearbeiten"));

        UserEditNameLabel.setText("Name:");

        UserEditEmailLabel.setText("E-Mail:");

        UserEditPasswortLabel.setText("Passwort:");

        UserEditSaveButton.setText("User speichern");
        UserEditSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserEditSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UserEditPanelLayout = new javax.swing.GroupLayout(UserEditPanel);
        UserEditPanel.setLayout(UserEditPanelLayout);
        UserEditPanelLayout.setHorizontalGroup(
            UserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UserEditPanelLayout.createSequentialGroup()
                        .addGroup(UserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserEditPasswortLabel)
                            .addComponent(UserEditEmailLabel)
                            .addComponent(UserEditNameLabel))
                        .addGap(18, 18, 18)
                        .addGroup(UserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserEditNameFeld)
                            .addComponent(UserEditEmailFeld)
                            .addComponent(UserEditPasswortFeld)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UserEditPanelLayout.createSequentialGroup()
                        .addGap(0, 241, Short.MAX_VALUE)
                        .addComponent(UserEditSaveButton)))
                .addContainerGap())
        );
        UserEditPanelLayout.setVerticalGroup(
            UserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserEditNameFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserEditNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(UserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserEditEmailFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserEditEmailLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(UserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserEditPasswortFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserEditPasswortLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UserEditSaveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout UserEditDialogLayout = new javax.swing.GroupLayout(UserEditDialog.getContentPane());
        UserEditDialog.getContentPane().setLayout(UserEditDialogLayout);
        UserEditDialogLayout.setHorizontalGroup(
            UserEditDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserEditDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        UserEditDialogLayout.setVerticalGroup(
            UserEditDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserEditDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        UserDeletePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("User löschen"));

        UserDeleteUsernameLabel.setText("Username:");

        UserDeleteSaveButton.setText("User löschen");
        UserDeleteSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserDeleteSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UserDeletePanelLayout = new javax.swing.GroupLayout(UserDeletePanel);
        UserDeletePanel.setLayout(UserDeletePanelLayout);
        UserDeletePanelLayout.setHorizontalGroup(
            UserDeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserDeletePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserDeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UserDeletePanelLayout.createSequentialGroup()
                        .addComponent(UserDeleteUsernameLabel)
                        .addGap(18, 18, 18)
                        .addComponent(UserDeleteUsernameFeld))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UserDeletePanelLayout.createSequentialGroup()
                        .addGap(0, 251, Short.MAX_VALUE)
                        .addComponent(UserDeleteSaveButton)))
                .addContainerGap())
        );
        UserDeletePanelLayout.setVerticalGroup(
            UserDeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserDeletePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserDeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserDeleteUsernameFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserDeleteUsernameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UserDeleteSaveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout UserDeleteDialogLayout = new javax.swing.GroupLayout(UserDeleteDialog.getContentPane());
        UserDeleteDialog.getContentPane().setLayout(UserDeleteDialogLayout);
        UserDeleteDialogLayout.setHorizontalGroup(
            UserDeleteDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserDeleteDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserDeletePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        UserDeleteDialogLayout.setVerticalGroup(
            UserDeleteDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserDeleteDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserDeletePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RouteDeletePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Route löschen"));

        RouteDeleteNrLabel.setText("Routennr:");

        RouteDeleteSaveButton.setText("Route löschen");
        RouteDeleteSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteDeleteSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RouteDeletePanelLayout = new javax.swing.GroupLayout(RouteDeletePanel);
        RouteDeletePanel.setLayout(RouteDeletePanelLayout);
        RouteDeletePanelLayout.setHorizontalGroup(
            RouteDeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteDeletePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RouteDeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RouteDeletePanelLayout.createSequentialGroup()
                        .addComponent(RouteDeleteNrLabel)
                        .addGap(18, 18, 18)
                        .addComponent(RouteDeleteNrFeld))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RouteDeletePanelLayout.createSequentialGroup()
                        .addGap(0, 243, Short.MAX_VALUE)
                        .addComponent(RouteDeleteSaveButton)))
                .addContainerGap())
        );
        RouteDeletePanelLayout.setVerticalGroup(
            RouteDeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteDeletePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RouteDeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteDeleteNrFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteDeleteNrLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RouteDeleteSaveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout RouteDeleteDialogLayout = new javax.swing.GroupLayout(RouteDeleteDialog.getContentPane());
        RouteDeleteDialog.getContentPane().setLayout(RouteDeleteDialogLayout);
        RouteDeleteDialogLayout.setHorizontalGroup(
            RouteDeleteDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteDeleteDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RouteDeletePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        RouteDeleteDialogLayout.setVerticalGroup(
            RouteDeleteDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteDeleteDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RouteDeletePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        UserDeleteQPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("User bearbeiten"));

        UserEditQNameLabel.setText("Username:");

        UserEditQSaveButton.setText("User bearbeiten");
        UserEditQSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserEditQSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UserDeleteQPanelLayout = new javax.swing.GroupLayout(UserDeleteQPanel);
        UserDeleteQPanel.setLayout(UserDeleteQPanelLayout);
        UserDeleteQPanelLayout.setHorizontalGroup(
            UserDeleteQPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserDeleteQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserDeleteQPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UserDeleteQPanelLayout.createSequentialGroup()
                        .addComponent(UserEditQNameLabel)
                        .addGap(18, 18, 18)
                        .addComponent(UserEditQNameFeld))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UserDeleteQPanelLayout.createSequentialGroup()
                        .addGap(0, 235, Short.MAX_VALUE)
                        .addComponent(UserEditQSaveButton)))
                .addContainerGap())
        );
        UserDeleteQPanelLayout.setVerticalGroup(
            UserDeleteQPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserDeleteQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserDeleteQPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserEditQNameFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserEditQNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UserEditQSaveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout UserEditQDialogLayout = new javax.swing.GroupLayout(UserEditQDialog.getContentPane());
        UserEditQDialog.getContentPane().setLayout(UserEditQDialogLayout);
        UserEditQDialogLayout.setHorizontalGroup(
            UserEditQDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserEditQDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserDeleteQPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        UserEditQDialogLayout.setVerticalGroup(
            UserEditQDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserEditQDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserDeleteQPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RouteEditQPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Route bearbeiten"));

        RouteEditQNrLabel.setText("Routennr:");

        RouteEditQSaveButton.setText("Route bearbeiten");
        RouteEditQSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteEditQSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RouteEditQPanelLayout = new javax.swing.GroupLayout(RouteEditQPanel);
        RouteEditQPanel.setLayout(RouteEditQPanelLayout);
        RouteEditQPanelLayout.setHorizontalGroup(
            RouteEditQPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteEditQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RouteEditQPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RouteEditQPanelLayout.createSequentialGroup()
                        .addComponent(RouteEditQNrLabel)
                        .addGap(18, 18, 18)
                        .addComponent(RouteEditQNrFeld))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RouteEditQPanelLayout.createSequentialGroup()
                        .addGap(0, 227, Short.MAX_VALUE)
                        .addComponent(RouteEditQSaveButton)))
                .addContainerGap())
        );
        RouteEditQPanelLayout.setVerticalGroup(
            RouteEditQPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteEditQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RouteEditQPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RouteEditQNrFeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RouteEditQNrLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RouteEditQSaveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout RouteEditQDialogLayout = new javax.swing.GroupLayout(RouteEditQDialog.getContentPane());
        RouteEditQDialog.getContentPane().setLayout(RouteEditQDialogLayout);
        RouteEditQDialogLayout.setHorizontalGroup(
            RouteEditQDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteEditQDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RouteEditQPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        RouteEditQDialogLayout.setVerticalGroup(
            RouteEditQDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RouteEditQDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RouteEditQPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mitfahrzentrale");
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Routen"));

        Routenliste.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                RoutenlisteValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(Routenliste);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informationen"));

        Startort.setText("Startort:");

        startortfeld.setEditable(false);
        startortfeld.setToolTipText("Startort");

        zielortfeld.setEditable(false);
        zielortfeld.setToolTipText("Zielort");

        zielort.setText("Zielort:");

        abfahrtstdfeld.setEditable(false);
        abfahrtstdfeld.setToolTipText("Abfahrtszeit");

        abfahrt.setText("Abfahrt:");

        sitzplaetze.setText("Sitzplätze:");

        sitzplaetzefeld.setEditable(false);
        sitzplaetzefeld.setToolTipText("Die Anzahl der Sitzplätze");

        anmeldenbutton.setText("Anmelden");
        anmeldenbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anmeldenbuttonActionPerformed(evt);
            }
        });

        fahrerfeld.setEditable(false);
        fahrerfeld.setToolTipText("");

        fahrerlabel.setText("Fahrer:");

        mitfahrerlabel.setText("Mitfahrer:");

        jScrollPane3.setViewportView(mitfahrerlist);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(abfahrt)
                        .addGap(20, 20, 20)
                        .addComponent(abfahrtstdfeld))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Startort)
                            .addComponent(zielort))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(zielortfeld)
                            .addComponent(startortfeld)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(anmeldenbutton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sitzplaetze)
                            .addComponent(fahrerlabel)
                            .addComponent(mitfahrerlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addComponent(sitzplaetzefeld)
                            .addComponent(fahrerfeld))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startortfeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Startort))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(zielortfeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zielort))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(abfahrtstdfeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(abfahrt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sitzplaetzefeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sitzplaetze))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fahrerfeld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fahrerlabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mitfahrerlabel)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(anmeldenbutton)
                .addContainerGap(120, Short.MAX_VALUE))
        );

        RouteMenu.setText("Routen");
        RouteMenu.setMargin(new java.awt.Insets(0, 10, 0, 10));

        RouteButton.setText("Neue Route");
        RouteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteButtonActionPerformed(evt);
            }
        });
        RouteMenu.add(RouteButton);

        RouteEditButton.setText("Route bearbeiten");
        RouteEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteEditButtonActionPerformed(evt);
            }
        });
        RouteMenu.add(RouteEditButton);

        RouteDeleteButton.setText("Route löschen");
        RouteDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteDeleteButtonActionPerformed(evt);
            }
        });
        RouteMenu.add(RouteDeleteButton);
        RouteMenu.add(jSeparator1);

        CloseButton.setText("Beenden");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });
        RouteMenu.add(CloseButton);

        jMenuBar1.add(RouteMenu);

        Usermenu.setText("User");
        Usermenu.setMargin(new java.awt.Insets(0, 10, 0, 10));

        UserCreateButton.setText("User erstellen");
        UserCreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserCreateButtonActionPerformed(evt);
            }
        });
        Usermenu.add(UserCreateButton);

        UserEditButton.setText("User bearbeiten");
        UserEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserEditButtonActionPerformed(evt);
            }
        });
        Usermenu.add(UserEditButton);

        UserDeleteButton.setText("User löschen");
        UserDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserDeleteButtonActionPerformed(evt);
            }
        });
        Usermenu.add(UserDeleteButton);

        jMenuBar1.add(Usermenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Zeigt den Dialog zur Routenerstellung an, auf Basis eines Events.
     * @param evt Das Event
     */
    private void RouteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteButtonActionPerformed
        displayDialog(this.RouteNeuDialog, 400, 260, "Neue Route anlegen");
    }//GEN-LAST:event_RouteButtonActionPerformed

    /**
     * Beendet das Programm auf Basis eines Events.
     * @param evt Das Event
     */
    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        this.dispose();
        this.xmppcon.disconnect();
    }//GEN-LAST:event_CloseButtonActionPerformed

    /**
     * Zeigt den Dialog zum Bearbeiten eines Users an, auf Basis eines Events.
     * @param evt Das Event
     */
    private void UserEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserEditButtonActionPerformed
        displayDialog(this.UserEditQDialog, 400, 150, "User bearbeiten");
    }//GEN-LAST:event_UserEditButtonActionPerformed

    /**
     * Zeigt den Dialog zum Bearbeiten einer Route an, auf Basis eines Events.
     * @param evt Das Event
     */
    private void RouteEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteEditButtonActionPerformed
        displayDialog(this.RouteEditQDialog, 400, 150, "Route bearbeiten");
    }//GEN-LAST:event_RouteEditButtonActionPerformed

    /**
     * Zeigt den Dialog zum Erstellen eines Users an, auf Basis eines Events.
     * @param evt Das Event
     */
    private void UserCreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserCreateButtonActionPerformed
        displayDialog(this.UserCreateDialog, 400, 200, "User erstellen");
    }//GEN-LAST:event_UserCreateButtonActionPerformed

    /**
     * Zeigt den Dialog zum Löschen eines Users an, auf Basis eines Events.
     * @param evt Das Event
     */
    private void UserDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserDeleteButtonActionPerformed
        displayDialog(this.UserDeleteDialog, 400, 150, "User löschen");
    }//GEN-LAST:event_UserDeleteButtonActionPerformed

    /**
     * Meldet den derzeitigen User für die ausgewählte Fahrt an.
     * @param evt Das Event
     */
    /* HIER ERG�NZUNG XMPP SUBSCRIPTION */
    private void anmeldenbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anmeldenbuttonActionPerformed
        int index = Routenliste.getSelectedIndex();
        if(index != -1) {
            mfz = get("");
            Fahrten f = mfz.getFahrten().get(index);
            Mitfahrer mf = new Mitfahrer();
            mf.getPerson().add(this.loggedPerson);
            f.setMitfahrer(mf);
            index++;
            this.put(f, index);
            
            /** 
             * Node zu dieser ge�nderten Fahrt abrufen
             * Listener f�r diese Node f�r ge�nderte Routen in diesem Client starten
             * User abonniert diesen Node
             */
            try {
            	LeafNode node = (LeafNode) this.pubsub.getNode("Route" + --index);  
                node.addItemEventListener(new RouteChangedCoordinator());
				node.subscribe(this.loggedPerson.getEmail());
			} catch (XMPPException e) {
				e.printStackTrace();
			}
            
            mfz = get("");
            index--;
            f = mfz.getFahrten().get(index);
            DefaultListModel m = new DefaultListModel();
            this.mitfahrerlist.setModel(m);
            this.mitfahrerlist.setModel(m);
            if(f.getMitfahrer() != null) {
                for(int i = 0; i < f.getMitfahrer().getPerson().size(); i++) {
                    String output = "";
                    output += i + ". ";
                    output += f.getMitfahrer().getPerson().get(i).getName();
                    m.add(i, output);
                }
            }
        }
    }//GEN-LAST:event_anmeldenbuttonActionPerformed

    /**
     * Sorgt für die Einstellungen eines übergebenen JDialog's und gibt ihn aus.
     * @param dialog Der Dialog
     * @param width Die Breite des Dialog's
     * @param height Die Höhe des Dialog's 
     * @param title Der Titel des Dialog's
     */
    public void displayDialog(JDialog dialog, int width, int height, String title) {
        dialog.setModal(true);
        
        dialog.setAlwaysOnTop(true);
        dialog.setSize(width, height);
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int top = ((int)d.getHeight() - dialog.getHeight()) /2;
        int left = ((int)d.getWidth() - dialog.getWidth()) /2;
        dialog.setLocation(left, top);
        dialog.setResizable(false);
        
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle(title);
        dialog.setVisible(true);
    }
    
    /**
     * Fühlt die Informationen mit Inhalt, sobald eine Fahrt selektiert wurde.
     * @param evt Das Event
     */
    private void RoutenlisteValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_RoutenlisteValueChanged
        JList list = (JList) evt.getSource();
        
        int index = list.getSelectedIndex();
        if(index != -1) {
        
            this.mfz = get("");
            Fahrten f = this.mfz.getFahrten().get(index);
            startortfeld.setText(f.getRoute().getStartpunkt());
            zielortfeld.setText(f.getRoute().getZielpunkt());
            abfahrtstdfeld.setText(f.getRoute().getStartzeitpunkt());
            sitzplaetzefeld.setText(String.valueOf(f.getRoute().getSitze()));
            fahrerfeld.setText(f.getFahrer().getPerson().getName());
            DefaultListModel m = new DefaultListModel();
            this.mitfahrerlist.setModel(m);
            if(f.getMitfahrer() != null) {
                for(int i = 0; i < f.getMitfahrer().getPerson().size(); i++) {
                    String output = "";
                    output += i + ". ";
                    output += f.getMitfahrer().getPerson().get(i).getName();
                    m.add(i, output);
                }
            }
        }
    }//GEN-LAST:event_RoutenlisteValueChanged

    /**
     * Ruft die Daten aus dem "Route erstellen"-Formular ab und speichert diese mit POST auf dem Server.
     * @param evt Das Event
     */
    private void RouteNeuSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteNeuSaveButtonActionPerformed
        // instanzieren der notwendigen objekte
        Fahrten f = new Fahrten();
        Contact c = new Contact();
        Route r = new Route();
        Fahrer fr = new Fahrer();
        
        // Dateneingabe
        c.setMobil(BigInteger.valueOf(Long.valueOf(this.RouteNeuMobiltelefonFeld.getText())));
        r.setStartpunkt(this.RouteNeuStartortFeld.getText());
        r.setZielpunkt(this.RouteNeuZielortFeld.getText());
        r.setStartzeitpunkt(this.RouteNeuAbfahrtFeld.getText());
        r.setSitze(BigInteger.valueOf(Long.valueOf(this.RouteNeuSitzplaetzeFeld.getText())));
        fr.setPerson(loggedPerson);
        
        f.setFahrer(fr);
        f.setRoute(r);
        f.setContact(c);
        
        this.post(f);
        
        this.mfz = get("");
        this.model = new DefaultListModel();
        this.Routenliste.setModel(this.model);
        for(int i = 0; i < this.mfz.getFahrten().size(); i++) {
            String item = "";
            item += (i +1) + ". ";
            item += this.mfz.getFahrten().get(i).getRoute().getStartpunkt();
            item += " > ";
            item += this.mfz.getFahrten().get(i).getRoute().getZielpunkt();
            this.model.add(i, item);
        }
        this.RouteNeuStartortFeld.setText("");
        this.RouteNeuZielortFeld.setText("");
        this.RouteNeuAbfahrtFeld.setText("");
        this.RouteNeuSitzplaetzeFeld.setText("");
        this.RouteNeuMobiltelefonFeld.setText("");
        
        try {
            LeafNode n = (LeafNode) this.pubsub.getNode("Route" + (this.mfz.getFahrten().size()-1));
            n.send(new Item("0"));
        } catch (XMPPException ex) {
            Logger.getLogger(MitfahrzentraleClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        this.RouteNeuDialog.dispose();
    }//GEN-LAST:event_RouteNeuSaveButtonActionPerformed

    /**
     * Ruft die Daten aus dem "Route bearbeiten"-Formular ab und speichert diese mit PUT auf dem Server.
     * @param evt Das Event
     */
    private void RouteEditSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteEditSaveButtonActionPerformed
        int index = Integer.valueOf(this.RouteEditQNrFeld.getText());
        this.RouteEditQNrFeld.setText("");
        mfz = get("");
        Fahrten f = mfz.getFahrten().get(index-1);
        
        Fahrten rt = new Fahrten();
        Contact c = new Contact();
        Route r = new Route();
        
        c.setMobil(BigInteger.valueOf(Long.valueOf(this.RouteEditMobiletelefonFeld.getText())));
        rt.setContact(c);
        r.setStartpunkt(this.RouteEditStartortFeld.getText());
        r.setStartzeitpunkt(this.RouteEditAbfahrtFeld.getText());
        r.setZielpunkt(this.RouteEditZielortFeld.getText());
        r.setSitze(BigInteger.valueOf(Long.valueOf(this.RouteEditSitzplaetzeFeld.getText())));
        rt.setRoute(r);
        rt.setFahrer(f.getFahrer());
       
        this.put(rt, index);
        
        /*
         * Prüfen ob sich daten geändert haben 
         */
        boolean mobile = false;
        boolean startpunkt = false;
        boolean endpunkt = false;
        boolean startzeit = false;
        boolean sitze = false;
        
        if(!r.getStartpunkt().equals(f.getRoute().getStartpunkt())) startpunkt = true;
        if(!r.getZielpunkt().equals(f.getRoute().getZielpunkt())) endpunkt = true;
        if(!c.getMobil().equals(f.getContact().getMobil())) mobile = true;
        if(!r.getStartzeitpunkt().equals(f.getRoute().getStartzeitpunkt())) startzeit = true;
        if(!r.getSitze().equals(f.getRoute().getSitze())) sitze = true;
        
        String payload = "";
        
        payload += "<fahrten>";
        if(mobile) {
            payload += "<contact>";
            payload += "<mobil>" + rt.getContact().getMobil() + "</mobil>";
            payload += "</contact>";
        }
        
        if(startpunkt  || endpunkt || startzeit || sitze) {
            payload += "<route>";
            if(startzeit) {
                payload += "<startzeitpunkt>" + rt.getRoute().getStartzeitpunkt() + "</startzeitpunkt>";
            }    
            if(startpunkt) {
                payload += "<startpunkt>" + rt.getRoute().getStartpunkt() + "</startpunkt>";
            }
            if(endpunkt) {
                payload += "<zielpunkt>" + rt.getRoute().getZielpunkt() + "</zielpunkt>";
            }
            if(sitze) {
                payload += "<sitze>" + rt.getRoute().getSitze() + "</sitze>";
            }
            payload += "</route>";
        }
        payload += "</fahrten>";
        
        try {
            LeafNode n = (LeafNode) this.pubsub.getNode("Route" + index);
            SimplePayload sp =  new SimplePayload("mitfahrzentrale", "mitfahrzentrale:fahrten", payload);
            PayloadItem i = new PayloadItem("mitfahrzentrale", sp);
            n.send(i);
        } catch (XMPPException ex) {
            Logger.getLogger(MitfahrzentraleClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*
         * Update der JList
         */
        this.mfz = get("");
        this.model = new DefaultListModel();
        this.Routenliste.setModel(this.model);
        for(int i = 0; i < this.mfz.getFahrten().size(); i++) {
            String item = "";
            item += (i +1) + ". ";
            item += this.mfz.getFahrten().get(i).getRoute().getStartpunkt();
            item += " > ";
            item += this.mfz.getFahrten().get(i).getRoute().getZielpunkt();
            this.model.add(i, item);
        }
        this.RouteEditDialog.dispose();
    }//GEN-LAST:event_RouteEditSaveButtonActionPerformed

    /**
     * Ruft die Daten aus dem "User erstellen"-Formular ab und speichert diese mit POST auf dem Server.
     * @param evt Das Event
     */
    private void UserCreateSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserCreateSaveButtonActionPerformed
        Person p = new Person();
        p.setHasRoute(false);
        p.setEmail(this.UserCreateEmailFeld.getText());
        p.setName(this.UserCreateNameFeld.getText());
        p.setPassword(this.UserCreatePasswortFeld.getText());
        
        this.post(p);
        this.UserCreateNameFeld.setText("");
        this.UserCreateEmailFeld.setText("");
        this.UserCreatePasswortFeld.setText("");
        this.UserCreateDialog.dispose();
    }//GEN-LAST:event_UserCreateSaveButtonActionPerformed

    /**
     * Ruft die Daten aus dem "User bearbeiten"-Formular ab und speichert diese mit PUT auf dem Server.
     * @param evt Das Event
     */
    private void UserEditSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserEditSaveButtonActionPerformed
        String name = this.UserEditNameFeld.getText();
        this.UserEditQNameFeld.setText("");
        int index = 0;
        for(Person p : mfz.getUsers().getPerson()) {
            if(p.getName().equals(name)) {
                index = mfz.getUsers().getPerson().indexOf(p);
            }
        }
        Person rt = new Person();
        rt.setEmail(this.UserEditEmailFeld.getText());
        rt.setName(this.UserEditNameFeld.getText());
        rt.setPassword(this.UserEditPasswortFeld.getText());
        this.put(rt, index);
        this.UserEditDialog.dispose();
    }//GEN-LAST:event_UserEditSaveButtonActionPerformed

    /**
     * Ruft die Daten aus dem "User löschen"-Formular ab und löscht diese mit DELETE vom Server.
     * @param evt Das Event
     */
    private void UserDeleteSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserDeleteSaveButtonActionPerformed
        String name = this.UserDeleteUsernameFeld.getText();
        this.delete(name);
        this.UserDeleteDialog.dispose();
    }//GEN-LAST:event_UserDeleteSaveButtonActionPerformed

    /**
     * Zeigt den Dialog zum Löschen einer Route an, auf Basis eines Events.
     * @param evt Das Event
     */
    private void RouteDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteDeleteButtonActionPerformed
        displayDialog(this.RouteDeleteDialog, 400, 150, "Route löschen");
    }//GEN-LAST:event_RouteDeleteButtonActionPerformed

    /**
     * Ruft die Daten aus dem "Route löschen"-Formular ab und löscht diese mit DELETE vom Server.
     * @param evt Das Event
     */
    private void RouteDeleteSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteDeleteSaveButtonActionPerformed
        String id = this.RouteDeleteNrFeld.getText();
        this.delete(Integer.valueOf(id));
        
        this.mfz = get("");
        this.model = new DefaultListModel();
        this.Routenliste.setModel(this.model);
        for(int i = 0; i < this.mfz.getFahrten().size(); i++) {
            String item = "";
            item += (i +1) + ". ";
            item += this.mfz.getFahrten().get(i).getRoute().getStartpunkt();
            item += " > ";
            item += this.mfz.getFahrten().get(i).getRoute().getZielpunkt();
            this.model.add(i, item);
        }
        try {
            this.pubsub.deleteNode("Route" + id);
        } catch (XMPPException ex) {
            Logger.getLogger(MitfahrzentraleClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.RouteDeleteNrFeld.setText("");
        this.RouteDeleteDialog.dispose();
    }//GEN-LAST:event_RouteDeleteSaveButtonActionPerformed

    /**
     * Ruft die Daten aus dem "Username angeben"-Formular ab und ruft den "User bearbeiten" Dialog mit den passenden Inhalten auf.
     * @param evt Das Event
     */
    private void UserEditQSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserEditQSaveButtonActionPerformed
        String username = this.UserEditQNameFeld.getText();
        mfz = get("");
        for(Person p : mfz.getUsers().getPerson()) {
            if(p.getName().equals(username)) {
                this.UserEditEmailFeld.setText(p.getEmail());
                this.UserEditNameFeld.setText(p.getName());
                this.UserEditPasswortFeld.setText(p.getPassword());
            }
        }
        this.UserEditQDialog.dispose();
        displayDialog(this.UserEditDialog, 400, 200, "User bearbeiten");
    }//GEN-LAST:event_UserEditQSaveButtonActionPerformed

    /**
     * Ruft die Daten aus dem "Route angeben"-Formular ab und ruft den "Route bearbeiten" Dialog mit den passenden Inhalten auf.
     * @param evt Das Event
     */
    private void RouteEditQSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteEditQSaveButtonActionPerformed
        int routenid = Integer.valueOf(this.RouteEditQNrFeld.getText());
        this.RouteEditQDialog.dispose();
        
        Fahrten f = mfz.getFahrten().get(routenid-1);
        this.RouteEditStartortFeld.setText(f.getRoute().getStartpunkt());
        this.RouteEditZielortFeld.setText(f.getRoute().getZielpunkt());
        this.RouteEditSitzplaetzeFeld.setText(f.getRoute().getSitze().toString());
        this.RouteEditAbfahrtFeld.setText(f.getRoute().getStartzeitpunkt());
        this.RouteEditMobiletelefonFeld.setText(f.getContact().getMobil().toString());
        
        displayDialog(this.RouteEditDialog, 400, 270, "Route bearbeiten");
    }//GEN-LAST:event_RouteEditQSaveButtonActionPerformed
    
    /**
     * Ruft die Daten über GET vom Grizzleyserver ab.
     * @param url Pfad der Ausgabe auf dem Grizzleyserver
     * @return Gibt ein Objekt der Mitfahrzentrale, mit den abgerufenen Inhalten, zurück.
     */
    private Mitfahrzentrale get(String url) {
        ClientResponse cr = resource.path(url).accept("application/xml").get(ClientResponse.class);
        Mitfahrzentrale emfz = cr.getEntity(Mitfahrzentrale.class);
        return emfz;
    }

    /**
     * Erstellt eine Person auf dem Server mit den übergebenen Daten.
     * @param p Die Person die erstellt wird
     */
    private void post(Person p) {
        resource.path("user/create/").post(p);
    }
    
    /**
     * Erstellt eine Fahrt auf dem Server mit den übergebenen Daten.
     * @param f Die Fahrt die erstellt wird
     */
    private void post(Fahrten f) {
        resource.path("fahrten/create").post(ClientResponse.class, f);
    }
    /**
     * Updatet eine Person auf dem Server mit den angegebenen Daten.
     * @param p Die neuen Personendaten
     * @param userid Die Personnr
     */
    private void put(Person p, int userid) {
        resource.path("user/edit/" + userid).put(ClientResponse.class, p);
    }
    
    /**
     * Updatet eine Fahrt auf dem Server mit den angegebenen Daten.
     * @param f Die neuen Fahrtdaten
     * @param fahrtenid Die Fahrtnr
     */
    private void put(Fahrten f, int fahrtenid) {
        resource.path("fahrten/edit/" + fahrtenid).put(ClientResponse.class, f);
    }
    
    /**
     * Löscht einen User auf dem Server.
     * @param name Der Name des Users
     */
    private void delete(String name) {
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
    private void delete(int fahrtID) {
        resource.path("fahrten/delete/" + fahrtID).delete();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {        
        
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");        
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MitfahrzentraleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MitfahrzentraleClient().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem CloseButton;
    private javax.swing.JMenuItem RouteButton;
    private javax.swing.JMenuItem RouteDeleteButton;
    private javax.swing.JDialog RouteDeleteDialog;
    private javax.swing.JTextField RouteDeleteNrFeld;
    private javax.swing.JLabel RouteDeleteNrLabel;
    private javax.swing.JPanel RouteDeletePanel;
    private javax.swing.JButton RouteDeleteSaveButton;
    private javax.swing.JTextField RouteEditAbfahrtFeld;
    private javax.swing.JLabel RouteEditAbfahrtLabel;
    private javax.swing.JMenuItem RouteEditButton;
    private javax.swing.JDialog RouteEditDialog;
    private javax.swing.JTextField RouteEditMobiletelefonFeld;
    private javax.swing.JLabel RouteEditMobiltelefonLabel;
    private javax.swing.JPanel RouteEditPanel;
    private javax.swing.JDialog RouteEditQDialog;
    private javax.swing.JTextField RouteEditQNrFeld;
    private javax.swing.JLabel RouteEditQNrLabel;
    private javax.swing.JPanel RouteEditQPanel;
    private javax.swing.JButton RouteEditQSaveButton;
    private javax.swing.JButton RouteEditSaveButton;
    private javax.swing.JTextField RouteEditSitzplaetzeFeld;
    private javax.swing.JLabel RouteEditSitzplaetzeLabel;
    private javax.swing.JTextField RouteEditStartortFeld;
    private javax.swing.JLabel RouteEditStartortLabel;
    private javax.swing.JTextField RouteEditZielortFeld;
    private javax.swing.JLabel RouteEditZielortLabel;
    private javax.swing.JMenu RouteMenu;
    private javax.swing.JTextField RouteNeuAbfahrtFeld;
    private javax.swing.JLabel RouteNeuAbfahrtLabel;
    private javax.swing.JDialog RouteNeuDialog;
    private javax.swing.JTextField RouteNeuMobiltelefonFeld;
    private javax.swing.JLabel RouteNeuMobiltelefonLabel;
    private javax.swing.JPanel RouteNeuPanel;
    private javax.swing.JButton RouteNeuSaveButton;
    private javax.swing.JTextField RouteNeuSitzplaetzeFeld;
    private javax.swing.JLabel RouteNeuSitzplaetzeLabel;
    private javax.swing.JTextField RouteNeuStartortFeld;
    private javax.swing.JLabel RouteNeuStartortLabel;
    private javax.swing.JTextField RouteNeuZielortFeld;
    private javax.swing.JLabel RouteNeuZielortLabel;
    private javax.swing.JList Routenliste;
    private javax.swing.JLabel Startort;
    private javax.swing.JMenuItem UserCreateButton;
    private javax.swing.JDialog UserCreateDialog;
    private javax.swing.JTextField UserCreateEmailFeld;
    private javax.swing.JLabel UserCreateEmailLabel;
    private javax.swing.JTextField UserCreateNameFeld;
    private javax.swing.JLabel UserCreateNameLabel;
    private javax.swing.JPanel UserCreatePanel;
    private javax.swing.JTextField UserCreatePasswortFeld;
    private javax.swing.JLabel UserCreatePasswortLabel;
    private javax.swing.JButton UserCreateSaveButton;
    private javax.swing.JMenuItem UserDeleteButton;
    private javax.swing.JDialog UserDeleteDialog;
    private javax.swing.JPanel UserDeletePanel;
    private javax.swing.JPanel UserDeleteQPanel;
    private javax.swing.JButton UserDeleteSaveButton;
    private javax.swing.JTextField UserDeleteUsernameFeld;
    private javax.swing.JLabel UserDeleteUsernameLabel;
    private javax.swing.JMenuItem UserEditButton;
    private javax.swing.JDialog UserEditDialog;
    private javax.swing.JTextField UserEditEmailFeld;
    private javax.swing.JLabel UserEditEmailLabel;
    private javax.swing.JTextField UserEditNameFeld;
    private javax.swing.JLabel UserEditNameLabel;
    private javax.swing.JPanel UserEditPanel;
    private javax.swing.JTextField UserEditPasswortFeld;
    private javax.swing.JLabel UserEditPasswortLabel;
    private javax.swing.JDialog UserEditQDialog;
    private javax.swing.JTextField UserEditQNameFeld;
    private javax.swing.JLabel UserEditQNameLabel;
    private javax.swing.JButton UserEditQSaveButton;
    private javax.swing.JButton UserEditSaveButton;
    private javax.swing.JMenu Usermenu;
    private javax.swing.JLabel abfahrt;
    private javax.swing.JTextField abfahrtstdfeld;
    private javax.swing.JButton anmeldenbutton;
    private javax.swing.JTextField fahrerfeld;
    private javax.swing.JLabel fahrerlabel;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel mitfahrerlabel;
    private javax.swing.JList mitfahrerlist;
    private javax.swing.JLabel sitzplaetze;
    private javax.swing.JTextField sitzplaetzefeld;
    private javax.swing.JTextField startortfeld;
    private javax.swing.JLabel zielort;
    private javax.swing.JTextField zielortfeld;
    // End of variables declaration//GEN-END:variables

}
