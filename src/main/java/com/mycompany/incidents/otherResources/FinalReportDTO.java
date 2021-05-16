/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.otherResources;

/**
 *
 * @author santvamu
 */
public class FinalReportDTO {

	String origen;   //No sale en informe(GW-SAP)
	String tipo;     //No sale en informe(PAGO,RESERVA,GATO,SALVAMENTO)
	Double valorCien;//No sale en informe (solo se usa temporalmente)
	Double valorReas;//No sale en informe (solo se usa temporalmente)
	String status; //se utilizara para determinar si se omite o no un registro
	String ramo;
	String Moneda;
	Double valorGW;
	Double gastosGW;
	Double reaseguroGW;
	Double valorSAP;
	Double gastosSAP;
	Double reaseguroSAP;
	Double diferenciaValor;
	Double diferenciaGastos;
	Double diferenciaReaseguro;
	

	public String getKey(){
		return origen + "-" + tipo + "-" + ramo + "-"  + Moneda;
	}
	
	public String getHeaders(){
		return "origen\ttipo\tramo\tMoneda\t"+
					 "valorCien\tvalorReas\t"+
					 "valorGW\tgastosGW\treaseguroGW\t"+
					 "valorSAP\tgastosSAP\treaseguroSAP\t"+
					 "diferenciaValor\tdiferenciaGastos\tdiferenciaReaseguro";
	}
	public String getSummary(){
		return origen    + "\t" + tipo      + "\t" + ramo + "\t"  + Moneda + "\t" + 
					 valorCien + "\t" + valorReas + "\t" + 
					 valorGW   + "\t" + gastosGW	+ "\t" + reaseguroGW  + "\t" + 
					 valorSAP  + "\t" + gastosSAP	+ "\t" + reaseguroSAP	+ "\t" + 
					 diferenciaValor	+ "\t" + diferenciaGastos	+ "\t" + diferenciaReaseguro;
	}
	
	public Double getValorCien() {
		return valorCien;
	}

	public void setValorCien(Double valorCien) {
		this.valorCien = valorCien;
	}

	public Double getValorReas() {
		return valorReas;
	}

	public void setValorReas(Double valorReas) {
		this.valorReas = valorReas;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getRamo() {
		return ramo;
	}

	public void setRamo(String ramo) {
		this.ramo = ramo;
	}

	public String getMoneda() {
		return Moneda;
	}

	public void setMoneda(String Moneda) {
		this.Moneda = Moneda;
	}

	public Double getValorGW() {
		return valorGW;
	}

	public void setValorGW(Double valorGW) {
		this.valorGW = valorGW;
	}

	public Double getGastosGW() {
		return gastosGW;
	}

	public void setGastosGW(Double gastosGW) {
		this.gastosGW = gastosGW;
	}

	public Double getReaseguroGW() {
		return reaseguroGW;
	}

	public void setReaseguroGW(Double reaseguroGW) {
		this.reaseguroGW = reaseguroGW;
	}

	public Double getValorSAP() {
		return valorSAP;
	}

	public void setValorSAP(Double valorSAP) {
		this.valorSAP = valorSAP;
	}

	public Double getGastosSAP() {
		return gastosSAP;
	}

	public void setGastosSAP(Double gastosSAP) {
		this.gastosSAP = gastosSAP;
	}

	public Double getReaseguroSAP() {
		return reaseguroSAP;
	}

	public void setReaseguroSAP(Double reaseguroSAP) {
		this.reaseguroSAP = reaseguroSAP;
	}

	public Double getDiferenciaValor() {
		return diferenciaValor;
	}

	public void setDiferenciaValor(Double diferenciaValor) {
		this.diferenciaValor = diferenciaValor;
	}

	public Double getDiferenciaGastos() {
		return diferenciaGastos;
	}

	public void setDiferenciaGastos(Double diferenciaGastos) {
		this.diferenciaGastos = diferenciaGastos;
	}

	public Double getDiferenciaReaseguro() {
		return diferenciaReaseguro;
	}

	public void setDiferenciaReaseguro(Double diferenciaReaseguro) {
		this.diferenciaReaseguro = diferenciaReaseguro;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
