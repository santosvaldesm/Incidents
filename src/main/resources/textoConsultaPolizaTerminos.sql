WITH normal AS
 (SELECT 'Deductible' AS MODEL_TYPE,
         'ns2:CCNumericCovTerm' AS COV_TERM_SUB_TYPE,
         '' AS COV_TERM_ORDER,
         c.podeducible AS NUMERIC_VALUE,
         'percent' AS UNITS,
         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'DEDUC' AS COV_TERM_PATTERN,
         '' AS MODEL_AGGREGATION,
         '' AS MODEL_RESTRICTION,
         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D
    FROM coberturas c
   WHERE c.npoliza = 'NUM_POLIZA'
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)
     AND c.ncertificado IN ('NUM_RIESGO')
     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia) = 'HOMOLOGACION_COBERTURA'
  UNION
  SELECT 'Deductible' MODEL_TYPE,
         'ns2:CCClassificationCovTerm' AS COV_TERM_SUB_TYPE,
         '' AS COV_TERM_ORDER,
         NULL AS NUMERIC_VALUE,
         'lossAmount-Loss Amount' UNITS,
         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'TIPODEDUC' COV_TERM_PATTERN,
         '' MODEL_AGGREGATION,
         '' MODEL_RESTRICTION,
         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D
    FROM coberturas c
   WHERE c.npoliza = 'NUM_POLIZA'
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)
     AND c.ncertificado IN ('NUM_RIESGO')
     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||
         c.cdsubgarantia) = 'HOMOLOGACION_COBERTURA'
  UNION
  SELECT 'Deductible' AS MODEL_TYPE,
         'ns2:CCFinancialCovTerm' AS COV_TERM_SUB_TYPE,
         '' AS COV_TERM_ORDER,
         CASE
          WHEN c.cdunidad_deducible2 IN (61,12,60,14,16,25,26,27,28,29,30,31,
		  32,33,34,24,23,22,15,35,1,19,64,63,37,38,4,41,43,13,11,62,36) THEN c.ptminimo_deducible
          WHEN c.cdunidad_deducible2 IN (6, 20, 5)  AND cp.cdmoneda = 0 THEN c.ptminimo_deducible
          WHEN c.cdunidad_deducible2 IN (7, 9, 21)  AND cp.cdmoneda = 1 THEN c.ptminimo_deducible
          WHEN c.cdunidad_deducible2 IN (10, 8, 18) AND cp.cdmoneda = 9 THEN c.ptminimo_deducible
          WHEN c.cdunidad_deducible2 IN (7, 9, 21)  AND cp.cdmoneda = 0 THEN round(c.ptminimo_deducible *
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)
          WHEN c.cdunidad_deducible2 IN (10, 8, 18) AND cp.cdmoneda = 0 THEN
           round(c.ptminimo_deducible *
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '9'),0)
          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 1 THEN
           round(c.ptminimo_deducible /
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)
          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 9 THEN
           round(c.ptminimo_deducible /
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '9'),0)
          WHEN c.cdunidad_deducible2 IN (44, 42) AND cp.cdmoneda = 0 THEN
           round(c.ptminimo_deducible *
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3'),0)
          WHEN c.cdunidad_deducible2 IN (44, 42) AND cp.cdmoneda = 1 THEN
           round((c.ptminimo_deducible *
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3')) /
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)
          WHEN c.cdunidad_deducible2 IN (3, 2, 17) AND cp.cdmoneda = 0 THEN
           round(c.ptminimo_deducible *
                 (SELECT m.tipo_cambio / 30 FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3'), 0)
          WHEN c.cdunidad_deducible2 IN (3, 2, 17) AND cp.cdmoneda = 1 THEN
           round((c.ptminimo_deducible *
                 (SELECT m.tipo_cambio / 30 FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3')) / 
				 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'), 0)
          ELSE
           c.ptminimo_deducible
         END AS NUMERIC_VALUE,
         '' AS UNITS,
         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'MINDEDUC' AS COV_TERM_PATTERN,
         '' AS MODEL_AGGREGATION,
         '' AS MODEL_RESTRICTION,
         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D
    FROM coberturas c
   INNER JOIN cuerpoliza cp
      ON c.npoliza = cp.npoliza
   WHERE c.npoliza = 'NUM_POLIZA'
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)
     AND c.ncertificado IN ('NUM_RIESGO')
     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia) = 'HOMOLOGACION_COBERTURA'
  UNION
  SELECT 'Limit' MODEL_TYPE,
         'ns2:CCFinancialCovTerm' AS COV_TERM_SUB_TYPE,
         '' AS COV_TERM_ORDER,
         c.capital AS NUMERIC_VALUE,
         '' UNITS,
         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'LIMIT' COV_TERM_PATTERN,
         '' MODEL_AGGREGATION,
         '' MODEL_RESTRICTION,
         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D
    FROM coberturas c
   WHERE c.npoliza = 'NUM_POLIZA'
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)
     AND c.ncertificado IN ('NUM_RIESGO')
     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||
         c.cdsubgarantia) = 'HOMOLOGACION_COBERTURA'),
banca AS
 (SELECT 'Deductible' AS MODEL_TYPE,
         'ns2:CCNumericCovTerm' AS COV_TERM_SUB_TYPE,
         '' AS COV_TERM_ORDER,
         c.podeducible AS NUMERIC_VALUE,
         'percent' AS UNITS,
         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'DEDUC' AS COV_TERM_PATTERN,
         '' AS MODEL_AGGREGATION,
         '' AS MODEL_RESTRICTION,
         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D
    FROM coberturas_ban c
    WHERE (SELECT COUNT(*) FROM normal) = 0
     AND c.npoliza = 'NUM_POLIZA'
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)
     AND c.ncertificado IN ('NUM_RIESGO')
     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia) = 'HOMOLOGACION_COBERTURA'
     AND c.podeducible IS NOT NULL
  UNION
  SELECT 'Deductible' MODEL_TYPE,
         'ns2:CCClassificationCovTerm' AS COV_TERM_SUB_TYPE,
         '' AS COV_TERM_ORDER,
         NULL AS NUMERIC_VALUE,
         'lossAmount-Loss Amount' UNITS,
         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'TIPODEDUC' COV_TERM_PATTERN,
         '' MODEL_AGGREGATION,
         '' MODEL_RESTRICTION,
         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D
    FROM coberturas_ban c
   WHERE (SELECT COUNT(*) FROM normal) = 0
     AND c.npoliza = 'NUM_POLIZA'
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)
     AND c.ncertificado IN ('NUM_RIESGO')
     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||
         c.cdsubgarantia) = 'HOMOLOGACION_COBERTURA'
  UNION
  SELECT 'Deductible' AS MODEL_TYPE,
         'ns2:CCFinancialCovTerm' AS COV_TERM_SUB_TYPE,
         '' AS COV_TERM_ORDER,
         CASE
          WHEN c.cdunidad_deducible2 IN (61,12,60,14,16,25,26,27,28,29,30,31,32,33,34,24,23,22,15,35,1,19,64,63,37,38,4,41,43,13,11,62,36) THEN
           c.ptminimo_deducible
          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 0 THEN
           c.ptminimo_deducible
          WHEN c.cdunidad_deducible2 IN (7, 9, 21) AND cp.cdmoneda = 1 THEN
           c.ptminimo_deducible
          WHEN c.cdunidad_deducible2 IN (10, 8, 18) AND cp.cdmoneda = 9 THEN
           c.ptminimo_deducible
          WHEN c.cdunidad_deducible2 IN (7, 9, 21) AND cp.cdmoneda = 0 THEN
           round(c.ptminimo_deducible *
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)
          WHEN c.cdunidad_deducible2 IN (10, 8, 18) AND cp.cdmoneda = 0 THEN
           round(c.ptminimo_deducible *
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '9'),0)
          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 1 THEN
           round(c.ptminimo_deducible /
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'), 0)
          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 9 THEN
           round(c.ptminimo_deducible /
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '9'), 0)
          WHEN c.cdunidad_deducible2 IN (44, 42) AND cp.cdmoneda = 0 THEN
           round(c.ptminimo_deducible *
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3'), 0)
          WHEN c.cdunidad_deducible2 IN (44, 42) AND cp.cdmoneda = 1 THEN
           round((c.ptminimo_deducible *
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3')) / 
				 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)
          WHEN c.cdunidad_deducible2 IN (3, 2, 17) AND cp.cdmoneda = 0 THEN
           round(c.ptminimo_deducible *
                 (SELECT m.tipo_cambio / 30 FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3'), 0)
          WHEN c.cdunidad_deducible2 IN (3, 2, 17) AND cp.cdmoneda = 1 THEN
           round((c.ptminimo_deducible *
                 (SELECT m.tipo_cambio / 30 FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3')) /
                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'), 0)
          ELSE
           c.ptminimo_deducible
         END AS NUMERIC_VALUE,
         '' AS UNITS,
         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||
         c.cdsubgarantia || '-' || 'MINDEDUC' AS COV_TERM_PATTERN,
         '' AS MODEL_AGGREGATION,
         '' AS MODEL_RESTRICTION,
         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo ||
         c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D
    FROM coberturas_ban c
   INNER JOIN cuerpoliza_ban cp
      ON c.npoliza = cp.npoliza
   WHERE (SELECT COUNT(*) FROM normal) = 0
     AND c.npoliza = 'NUM_POLIZA'
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)
     AND c.ncertificado IN ('NUM_RIESGO')
     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||
         c.cdsubgarantia) = 'HOMOLOGACION_COBERTURA'
  UNION
  SELECT 'Limit' MODEL_TYPE,
         'ns2:CCFinancialCovTerm' AS COV_TERM_SUB_TYPE,
         '' AS COV_TERM_ORDER,
         c.capital AS NUMERIC_VALUE,
         '' UNITS,
         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||
         c.cdsubgarantia || '-' || 'LIMIT' COV_TERM_PATTERN,
         '' MODEL_AGGREGATION,
         '' MODEL_RESTRICTION,
         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo ||
         c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D
    FROM coberturas_ban c
   WHERE (SELECT COUNT(*) FROM normal) = 0
     AND c.npoliza = 'NUM_POLIZA'
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)
     AND c.ncertificado IN ('NUM_RIESGO')
     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||
         c.cdsubgarantia) = 'HOMOLOGACION_COBERTURA')
SELECT *
  FROM normal
UNION
SELECT * FROM banca