----------------------------------------------------------------------------
-- Vinculaciones es la unica tabla que trae polizas de COREGW y CORESEG,  --
-- solo trae la ultima vigencia de una poliza                             --
----------------------------------------------------------------------------
SELECT 
	c.DNI, 
	c.NPOLIZA, 
	c.NCERTIFICADO, 
	c.FEFECTO, 
	c.FEVENCIMIENTO, 
	c.FECSUS, 
	c.CDFUENTE_INFORMACION 
FROM 
	tsic_vinculaciones_segcor c 
WHERE 
	c.npoliza = 'NUM_POLIZA' 
	and TO_DATE('INITIAL_DATE', 'YYYY-MM-DD') BETWEEN c.fefecto AND c.fevencimiento 
	and NCERTIFICADO = 'NUM_RIESGO'
	
---------------------------------------------------------
-- Si hay registros en cuerpoliza es de empresariales  --
---------------------------------------------------------
SELECT 
	* 
FROM 
	CUERPOLIZA 
WHERE 
	npoliza = 'NUM_POLIZA' 

----------------------------------------------------
-- Si hay registros en cuerpoliza_ban es de banca --
----------------------------------------------------
SELECT 
	* 
FROM 
	CUERPOLIZA_BAN 
WHERE 
	npoliza = 'NUM_POLIZA' 
	
----------------------------------------------------------
-- contiene las diferentes vigencias por cada cobertura --
----------------------------------------------------------
SELECT 
	* 
FROM 
	COBERTURAS C 
WHERE 
	C.npoliza like '013000134588'  
	and TO_DATE('INITIAL_DATE', 'YYYY-MM-DD') BETWEEN c.fefecto AND c.fvencimiento 
	and NCERTIFICADO = 'NUM_RIESGO' 
	
------------------------------------------------------
-- corresponde a los riesgos que contiene la poliza -- 
------------------------------------------------------
SELECT 
	* 
FROM 
	CUERPOLIZA_CERTIFICADO C 
WHERE 
	C.npoliza = 'NUM_POLIZA'; 

-----------------------
-- Tabla de personas --
-----------------------
SELECT 
	crd.DNI,  
	crd.NPOLIZA, 
	crd.NCERTIFICADO, 
	crd.FEFECTO, 
	crd.FEVENCIMIENTO 
FROM   
	personas_prod crd 
WHERE  
	crd.npoliza = 'NUM_POLIZA' 

--------------------------------------------------------
--  Solo para linea 012(arrendamiento - cumplimiento) --
--------------------------------------------------------
SELECT 
	crd.NPOLIZA, 
	crd.NCERTIFICADO, 
	crd.FEFECTO, 
	crd.FEVENCIMIENTO 
FROM   
	cuerpoliza_riesgos crd 
WHERE  
	crd.npoliza = 'NUM_POLIZA'