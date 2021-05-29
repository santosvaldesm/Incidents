--HOMOLOGACIONES EXISTENTES DE GENERAL A GW (INSTANCIA PDNLNF)---------------------- 14020
--El archivo  creado debe tener el nombre: general_core.csv
SELECT 
       t.CDTIPO       AS homologation,
       f.CDAPLICACION AS source,
       t.CDAPLICACION AS target,
       f.CDVALOR AS source_value, 
       t.CDVALOR AS target_value
FROM   
       TTRD_MAPAS f, 
       TTRD_MAPAS t, 
       TTRD_HOMOLOGACION h
WHERE  f.CDAPLICACION = 'GENERAL_GW'       AND --fuente
       t.CDAPLICACION = 'CORE_GW'          AND --destino
       t.CDTIPO IN (
           'APIGATEWAY_PRODUCT_TYPE_CODE_FROM_OFFERING_TYPE_GW',
           'COBERTURA_GW',
           'COVERAGE_FROM_OFFERING_COVERAGE_ARTICLE_GW',
           'GARANTIA_SUBGARANTIA_EMPRESARIAL_GW',
           'LINE_FROM_OFFERING_TYPE_GW',
           'LOSS_CAUSE_FROM_SERVICES_GW',
           'LOSS_TYPE_FROM_LINE_SUBLINE_GW',
           'OFFERING_TYPE_FROM_LINE_SUBLINE_GW',
           'POLICY_TYPE_FROM_LINE_SUBLINE_GW',
           'REINSURANCECONTRACT_FROM_OFFERING_COVERAGE_GW',
           'TERMINO_GW')  AND --tipo de homologacion
       t.CDTIPO       = t.CDTIPO           AND
       f.CDMAPA       = h.CDMAPA           AND
       t.CDMAPA       = h.CDHOMOLOGADO;

--HOMOLOGACIONES EXISTENTES DE GW A GENERAL (INSTANCIA PDNLNF)---------------------- 10380
--El archivo  creado debe tener el nombre: core_general.csv

SELECT 
       t.CDTIPO       AS homologation,
       f.CDAPLICACION AS source,
       t.CDAPLICACION AS target,
       f.CDVALOR AS source_value, 
       t.CDVALOR AS target_value
FROM   TTRD_MAPAS f, 
       TTRD_MAPAS t, 
       TTRD_HOMOLOGACION h
WHERE  f.CDAPLICACION = 'CORE_GW'       AND --fuente
       t.CDAPLICACION = 'GENERAL_GW'    AND --destino
       t.CDTIPO IN (
           'APIGATEWAY_PRODUCT_TYPE_CODE_FROM_OFFERING_TYPE_GW',
           'COBERTURA_GW',
           'COVERAGE_FROM_OFFERING_COVERAGE_ARTICLE_GW',
           'GARANTIA_SUBGARANTIA_EMPRESARIAL_GW',
           'LINE_FROM_OFFERING_TYPE_GW',
           'LOSS_CAUSE_FROM_SERVICES_GW',
           'LOSS_TYPE_FROM_LINE_SUBLINE_GW',
           'OFFERING_TYPE_FROM_LINE_SUBLINE_GW',
           'POLICY_TYPE_FROM_LINE_SUBLINE_GW',
           'REINSURANCECONTRACT_FROM_OFFERING_COVERAGE_GW',
           'TERMINO_GW')  AND --tipo de homologacion
       t.CDTIPO       = t.CDTIPO           AND
       f.CDMAPA       = h.CDMAPA           AND
       t.CDMAPA       = h.CDHOMOLOGADO;

--LISTADO DE TODAS LAS COBERTURAS EXISTENTES EN ECOSISTEMA (INSTANCIA PDN)---------------------- 11326
--El archivo  creado debe tener el nombre: coberturas_ecosistema.csv
SELECT 
       C.CDRAMO        AS RAMO,
       C.CDSUBRAMO     AS SUBRAMO,
       C.CDGARANTIA    AS GARANTIA,
       C.CDSUBGARANTIA AS SUBGARANTIA,
       DECODE(SUBSTR(C.CDRAMO_CONTABLE, 0, 1), '1', C.CDRAMO_CONTABLE, C.CDRAMO_HOST) AS RAMO_CONTABLE,
       P.DSALIAS_1 AS PRODUCTO,
       C.DSALIAS_2 AS COBERTURA
FROM 
       DIC_ALIAS_COBERTURAS C, 
       OPS$CORP.DIC_ALIAS_RS P
WHERE   
       P.CDRAMO    = C.CDRAMO AND
       P.CDSUBRAMO = C.CDSUBRAMO