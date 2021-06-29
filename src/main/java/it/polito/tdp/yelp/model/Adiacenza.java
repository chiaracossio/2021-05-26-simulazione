package it.polito.tdp.yelp.model;

public class Adiacenza {
	
	private Business partenza;
	private Business arrivo;
	private Double peso;
	
	public Adiacenza(Business partenza, Business arrivo, Double peso) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.peso = peso;
	}
	public Business getPartenza() {
		return partenza;
	}
	public void setPartenza(Business partenza) {
		this.partenza = partenza;
	}
	public Business getArrivo() {
		return arrivo;
	}
	public void setArrivo(Business arrivo) {
		this.arrivo = arrivo;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	

}
