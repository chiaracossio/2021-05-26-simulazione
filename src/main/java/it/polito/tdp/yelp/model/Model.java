package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Map<String, Business> idMap;
	private SimpleDirectedWeightedGraph<Business, DefaultWeightedEdge> grafo;
	private List<Business> best;
	
	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<>();
		dao.getAllBusiness(idMap);
	}
	
	public void creaGrafo(String b, Integer anno) {
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getAllVertici(idMap, b, anno));
		
		List<Adiacenza> archi = dao.getAllArchi(idMap, b, anno);
		
		for(Adiacenza a: archi) {
			Graphs.addEdge(this.grafo, a.getPartenza(), a.getArrivo(), a.getPeso());
		}
		
	}
	
	public Integer getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getAllCitta(){
		return dao.getAllCitta();
	}
	
	public Business getLocaleMigliore(){
		
		Business best = null;
		
		Double top = 0.0;
		
		for(Business b: this.grafo.vertexSet()) {
			
			Double parziale = 0.0;
			
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(b)) {
				parziale = parziale - this.grafo.getEdgeWeight(e);
			}
			
			for(DefaultWeightedEdge ee: this.grafo.incomingEdgesOf(b)) {
				parziale = parziale + this.grafo.getEdgeWeight(ee);
			}
			
			if(parziale > top) {
				top = parziale;
				best = b;
			}			
		}
		
		return best;
		
	}
	
	public Set<Business> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public boolean getGrafo() {
		if(this.grafo == null) {
			return false;
		}
		return true;
	}
	
	
	public List<Business> cercaCammino(Business partenza, Double peso){
		
		best = new ArrayList<>();
		
		Business arrivo = this.getLocaleMigliore();
		
		List<Business> parziale = new ArrayList<>();
		
		parziale.add(partenza);
		
		cerca(parziale, peso, arrivo);
		
		return best;
	}

	private void cerca(List<Business> parziale, Double peso, Business arrivo) {
		
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			
			if(parziale.size() < best.size() || best.size() == 0) {
				best = new ArrayList<>(parziale);				
			}
			
			return;
		}
		for(Business b: Graphs.successorListOf(this.grafo, parziale.get(parziale.size()-1))) {
			
			if(!parziale.contains(b)) {
				
				DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(parziale.size()-1), b);
				
				if(this.grafo.getEdgeWeight(e)  >= peso){
					parziale.add(b);
					cerca(parziale, peso, arrivo);
					parziale.remove(parziale.size()-1);
				}
				
			}
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
