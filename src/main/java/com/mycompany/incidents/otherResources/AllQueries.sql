-----------------------------------------------------
--             CONSULTAS COMUNES                   --
-----------------------------------------------------

ALTER TABLE NOTE ALTER COLUMN DESCRIPTION SET DATA TYPE varchar (20000)
ALTER TABLE NOTE ALTER COLUMN TYPENOTE SET DATA TYPE VARCHAR(200)
ALTER TABLE gw_lob_Model ALTER COLUMN loss_cause SET DATA TYPE VARCHAR(2000);

-----------------------------------------------------
--             CONSULTAS COMUNES                   --
-----------------------------------------------------

SELECT * FROM APP.gw_homolog_core_to_general FETCH FIRST 100 ROWS ONLY;
SELECT * FROM APP.gw_lob_model FETCH FIRST 100 ROWS ONLY;
SELECT * FROM APP.gw_type_code WHERE type_key_name like 'CovTermPattern' and other_category is not null;

SELECT COUNT(*) FROM APP.gw_ecosystem_coverages -- 11179
SELECT COUNT(*) FROM APP.gw_homolog_general_to_core; --13510
SELECT COUNT(*) FROM APP.gw_homolog_core_to_general;--8908

-----------------------------------------------------
--            TABLAS DE GUIDEWIRE                  --
-----------------------------------------------------
DROP TABLE gw_lob_Model;
DROP TABLE gw_type_code;
DROP TABLE gw_homolog_general_to_core;
DROP TABLE gw_homolog_core_to_general;

CREATE TABLE gw_lob_Model(
    ID INTEGER NOT NULL 
    GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) 
    CONSTRAINT gw_lob_model_PK PRIMARY KEY, 
    offering                VARCHAR(150), 
    lob_code                VARCHAR(50), 
    policy_type             VARCHAR(50), 
    loss_type               VARCHAR(50), 
    coverage_type           VARCHAR(100), 
    internal_policy_type    VARCHAR(50), 
    policy_tab              VARCHAR(100), 
    loss_party_Type         VARCHAR(50), 
    coverage_subtype        VARCHAR(100), 
    exposure_type           VARCHAR(50), 
    covTerm_pattern         VARCHAR(100), 
    coverage_subtype_class  VARCHAR(20), 
    limit_or_Deductible     VARCHAR(50), 
    loss_cause              VARCHAR(2000),             
    cost_category           VARCHAR(1400));    
        
CREATE TABLE gw_type_code (  
    ID INTEGER NOT NULL 
    GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) 
    CONSTRAINT gw_type_key_PK PRIMARY KEY, 
    type_key_name    VARCHAR(100),                     
    type_code       VARCHAR(100),                     
    name_es         VARCHAR(300), 
    name_us         VARCHAR(300), 
    retired         BOOLEAN, 
    other_category  VARCHAR(300), 
    is_new          BOOLEAN); 

CREATE TABLE gw_homolog_general_to_core ( 
    ID INTEGER NOT NULL 
    GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) 
    CONSTRAINT gw_homologations_general_core_PK PRIMARY KEY, 
    homologation_name    VARCHAR(100),                     
    source       VARCHAR(100),                     
    target       VARCHAR(300));     
  
       
CREATE TABLE gw_homolog_core_to_general ( 
    ID INTEGER NOT NULL 
    GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) 
    CONSTRAINT gw_homologations_core_general_PK PRIMARY KEY, 
    homologation_name    VARCHAR(100),                     
    source       VARCHAR(100),                     
    target         VARCHAR(300)); 

-----------------------------------------------------
--            TABLAS DE ECOSISTEMA                 --
-----------------------------------------------------
DROP TABLE gw_ecosystem_coverages
CREATE TABLE gw_ecosystem_coverages ( 
    ID INTEGER NOT NULL 
    GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) 
    CONSTRAINT gw_ecosystem_coverages_PK PRIMARY KEY, 
    ramo            VARCHAR(3), 
    subramo         VARCHAR(3), 
    garantia        VARCHAR(3), 
    subgarantia     VARCHAR(3), 
    ramo_contable   VARCHAR(3), 
    product         VARCHAR(100), 
    coverage_name   VARCHAR(100));

-----------------------------------------------------
--            TABLAS DE CIERRES                    --
-----------------------------------------------------
DROP TABLE CLOSURE

CREATE TABLE CLOSURE (
    ID INTEGER NOT NULL
    GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)
    CONSTRAINT CLOSURE_PK PRIMARY KEY,
    ORIGEN          VARCHAR(10),                    
    TIPO            VARCHAR(20),                    
    CLAIMNUMBER     VARCHAR(20),                    
    POLICYNUMBER    VARCHAR(20),                    
    RAMO            VARCHAR(5),                    
    REFERENCIA      VARCHAR(300),
    ROW_TXT         VARCHAR(1000),                    
    VALOR_CIEN_GW   DOUBLE,                    
    VALOR_REAS_GW   DOUBLE,                    
    VALOR_CIEN_SAP  DOUBLE,                    
    VALOR_REAS_SAP  DOUBLE,                    
    MONEDA          VARCHAR(10),                    
    ESTADO          VARCHAR(30),
    DIFER_CIEN      DOUBLE,
    DIFER_REAS      DOUBLE)
    
CREATE INDEX ClosuresIndex ON CLOSURE(REFERENCIA)


-----------------------------------------------------
--            TABLAS DE LISTAS                     --
-----------------------------------------------------


ALTER TABLE LISTA ADD DESCRIPCION VARCHAR(2000);
ALTER TABLE LISTA DROP COLUMN DESCRIPCION;  

DROP TABLE LISTA

CREATE TABLE LISTA (
    ID INTEGER NOT NULL
    GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)
    CONSTRAINT LISTA_PK PRIMARY KEY,
    NOMBRE         VARCHAR(20),
    VALOR          VARCHAR(2000));

INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Causa','1. Capacitación - Tiene la opción pero no sabe cómo hacerlo');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Causa','2. Capacitación - Desconoce el proceso de Negocio');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Causa','3. Capacitación - Desconoce el uso del aplicativo');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Causa','4. Mejoras - Redefinición de Políticas y/o Procesos de negocio');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Causa','5. Mejoras - Se requiere una nueva funcionalidad (El negocio no tiene como ejecutar la operación)');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Causa','6. Errores de la aplicación ');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Causa','7. Perfilacion / Accesos - El usuario no cuenta con los permisos');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','4. Tranferir a la mesa');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','7. Capacitación');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','12. Transferir para requerimiento');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','64. Actualización de datos de sistema');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','65. Actualización de datos de usuario');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','71. Actualizacion de datos de contactos(Sura cliente - Contact)');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','72. Integracion SAP - Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','73. Errores de migración a Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','74. Solicitud de servicios en Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','75. Integracion P8 - Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','76. Integracion ATR - Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','77. Parametrización Contactos (Contact Manager - Sura Cliente)');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','78. Nueva funcionalidad con errores');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','79. Escenarios no contemplados en Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','80. Prametrización de productos');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','81. Consulta Poliza (Policy- Produccion)');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','82. Actualizar reaseguro');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','83. Actualizar poliza');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','84. Integracion Case Tracking - Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','85. Exposiciones en Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','86. Plan de actividades en Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','87. Inconsistencias datos de la póliza');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','88. Pago en Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','89. indisponibilidad base de datos');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','90. Reserva siniestros');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','91. Consulta de reaseguro');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','92. Recupero en Claims');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','93. Creación de siniestro');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','94. Siniestro sin piso (poliza sin vigencia)');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Agrupador','95. Cierre siniestro');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Proceso','3. Reclamacion');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Proceso','12. Configuracion de Producto');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Proceso','5. Modificacion');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Estado','1. Identificada');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Estado','2. En curso');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Estado','3. Por Identificar');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Estado','4. Aislado');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Responsable','1. TI');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Responsable','2. Negocio');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Responsable','3. TI/Negocio');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Operatividad','Si');
INSERT INTO APP.LISTA (NOMBRE,VALOR) VALUES ('Operatividad','No');


-----------------------------------------------------
--            ANALISIS DE CIERRE COASEGURO         --
-----------------------------------------------------

SELECT * FROM APP.CLOSURE FETCH FIRST 2000 ROWS ONLY;
SELECT COUNT(*) FROM APP.CLOSURE WHERE ORIGEN = 'SALVAM_SAP';  --23
SELECT COUNT(*) FROM APP.CLOSURE WHERE ORIGEN = 'SINIES_SAP';  --343
SELECT COUNT(*) FROM APP.CLOSURE WHERE ORIGEN = 'SINIES_ECO';  --169
SELECT COUNT(*) FROM APP.CLOSURE WHERE ORIGEN = 'SALVAM_ECO';  --13
SELECT * FROM APP.CLOSURE WHERE REFERENCIA = '1324070';

/**
 El caso de prueba sera la referencia 1324070:
|---------------------------------------------------------------------------------------------------------|
| CAMPO          | SALIO | CORRECTO    | REPRESENT                                                        |
|---------------------------------------------------------------------------------------------------------|
| VALOR_CIEN_GW  | BIEN  | 10761.4     | Suma de PARTICIPATION(CuentasCoaseguro)                          |
| VALOR_REAS_GW  | BIEN  | 3515.65     | Promedio de PTTASA_CAMBIO(CuentasCoaseguro) primero es sumatoria |
| ROW_TXT        | BIEN  | 3           | almacenara el divisor para sacar promedio a PTTASA_CAMBIO        |
| VALOR_CIEN_SAP | BIEN  | 10761.67    | suma de ABS(de SAP)                                              |
| VALOR_REAS_SAP | BIEN  | 3515.650046 | promedio de TRM(de SAP) primero es sumatoria                     |
| ESTADO         | BIEN  | 3           | almacenara el divisor para sacar promedio a TRM                  |
| DIFER_CIEN     | BIEN  | -0.2700     | VALOR_CIEN_GW - VALOR_CIEN_SAP                                   |
| DIFER_REAS     | BIEN  | -0.00005    | VALOR_REAS_GW - VALOR_REAS_SAP                                   |
|---------------------------------------------------------------------------------------------------------|    
*/

SELECT * FROM APP.CLOSURE WHERE REFERENCIA = '1329265';

/**
 El caso de prueba sera la referencia 1324070:
|---------------------------------------------------------------------------------------------------------|
| CAMPO          | SALIO | CORRECTO    | REPRESENT                                                        |
|---------------------------------------------------------------------------------------------------------|
| VALOR_CIEN_GW  | -     | 2369.2      | Suma de PARTICIPATION(CuentasCoaseguro)                          |
| VALOR_REAS_GW  | -     | 3537.86     | Promedio de PTTASA_CAMBIO(CuentasCoaseguro) primero es sumatoria |
| ROW_TXT        | -     | -           | almacenara el divisor para sacar promedio a PTTASA_CAMBIO        |
| VALOR_CIEN_SAP | -     | 2369.1      | suma de ABS(de SAP)                                              |
| VALOR_REAS_SAP | -     | 3515.650046 | promedio de TRM(de SAP) primero es sumatoria                     |
| ESTADO         | -     | -           | almacenara el divisor para sacar promedio a TRM                  |
| DIFER_CIEN     | -     | 0.1000      | VALOR_CIEN_GW - VALOR_CIEN_SAP                                   |
| DIFER_REAS     | -     | 0.00005     | VALOR_REAS_GW - VALOR_REAS_SAP                                   |
|---------------------------------------------------------------------------------------------------------|    
*/
