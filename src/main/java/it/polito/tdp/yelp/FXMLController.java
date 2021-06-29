/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnLocaleMigliore"
    private Button btnLocaleMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	if(model.getGrafo() == false) {
    		txtResult.setText("Devi prima creare un grafo!");
    		return;
    	}
    	
    	if(cmbLocale.getValue() == null || txtX.getText() == "") {
    		txtResult.setText("Devi inserire un numero e selezionare un locale");
    		return;
    	}
    	
    	Double peso = 0.0;
    	try {
    		peso = Double.parseDouble(txtX.getText());
    	}
    	catch(NumberFormatException e) {
    		txtResult.setText("Devi inserire un numero valido");
    		return;
    	}
    	
    	txtResult.appendText(model.cercaCammino(cmbLocale.getValue(), peso).size()+"\n");
    	
    	for(Business b: model.cercaCammino(cmbLocale.getValue(), peso)) {
    		txtResult.appendText(b.getBusinessName()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	if(cmbAnno.getValue() == null || cmbCitta.getValue() == null) {
    		txtResult.setText("Devi selezionare un anno e una città!");
    		return;
    	}
    	
    	model.creaGrafo(cmbCitta.getValue(), cmbAnno.getValue());
    	cmbLocale.getItems().addAll(model.getVertici());
    	txtResult.appendText("Vertici: "+model.getNumeroVertici()+"\n");
    	txtResult.appendText("Archi: "+model.getNumeroArchi()+"\n");

    }

    @FXML
    void doLocaleMigliore(ActionEvent event) {
    	
    	if(model.getGrafo() == false) {
    		txtResult.setText("Devi prima creare un grafo!");
    		return;
    	}
    	
    	txtResult.appendText(model.getLocaleMigliore().getBusinessName()+"\n");

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbCitta.getItems().addAll(model.getAllCitta());
    	
    	LinkedList<Integer> anni = new LinkedList<Integer>();
    	
    	for(int i = 2005; i<2014; i++) {
    		anni.add(i);
    	}
    	
    	cmbAnno.getItems().addAll(anni);
    	
    }
}
