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
    
    String origen;//no sale en el informe(GW-SAP)
    String tipo;//No sale en el informe(PAGO,RESERVA,GATO,SALVAMENTO)
    String ramo;
    String Moneda;
    double valorGW;
    double gastosGW;
    double reaseguroGW;
    double valorSAP;
    double gastosSAP;
    double reaseguroSAP;
    double diferenciaValor;
    double diferenciaGastos;
    double diferenciaReaseguro;
    
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

    public double getValorGW() {
        return valorGW;
    }

    public void setValorGW(double valorGW) {
        this.valorGW = valorGW;
    }

    public double getGastosGW() {
        return gastosGW;
    }

    public void setGastosGW(double gastosGW) {
        this.gastosGW = gastosGW;
    }

    public double getReaseguroGW() {
        return reaseguroGW;
    }

    public void setReaseguroGW(double reaseguroGW) {
        this.reaseguroGW = reaseguroGW;
    }

    public double getValorSAP() {
        return valorSAP;
    }

    public void setValorSAP(double valorSAP) {
        this.valorSAP = valorSAP;
    }

    public double getGastosSAP() {
        return gastosSAP;
    }

    public void setGastosSAP(double gastosSAP) {
        this.gastosSAP = gastosSAP;
    }

    public double getReaseguroSAP() {
        return reaseguroSAP;
    }

    public void setReaseguroSAP(double reaseguroSAP) {
        this.reaseguroSAP = reaseguroSAP;
    }

    public double getDiferenciaValor() {
        return diferenciaValor;
    }

    public void setDiferenciaValor(double diferenciaValor) {
        this.diferenciaValor = diferenciaValor;
    }

    public double getDiferenciaGastos() {
        return diferenciaGastos;
    }

    public void setDiferenciaGastos(double diferenciaGastos) {
        this.diferenciaGastos = diferenciaGastos;
    }

    public double getDiferenciaReaseguro() {
        return diferenciaReaseguro;
    }

    public void setDiferenciaReaseguro(double diferenciaReaseguro) {
        this.diferenciaReaseguro = diferenciaReaseguro;
    }
    
}
