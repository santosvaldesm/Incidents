package com.mycompany.incidents.otherResources;

public enum ClosureTypeEnum {
  origen,      
  tipo,
  moneda,
  referencia,
  //CIERRE FINANCIERO
  diferenciaCienMenorQue,
  diferenciaCienMayorQue,
  diferenciaReaseguroMenorQue,
  diferenciaReaseguroMayorQue,
  cienNoEncontradoEnSap,       
  reaseguroNoEncontradoenSAP,  
  cienNoEncontradoEnGW,       
  reaseguroNoEncontradoEnGW,     
  //CIERRE COASEGURO
  faltanteEnECO,
  faltanteEnSAP,
  AbsMayorQue,
  AbsMenorQue,
  TrmMenorQue,
  TrmMayorQue,
  //CIERRE REASEGURO
  NoEncontradaEnGw,
  NoEncontradaEnEco,
  conMasDeUnaCoinicdenciaEnGw,
  conMasDeUnaCoinicdenciaEnEco
}

