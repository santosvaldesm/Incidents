WITH normal AS
 (SELECT cp.cdramo || '-' || cb.cdsubramo || '-' || cb.cdgarantia || '-' ||
         cb.cdsubgarantia TYPE,
         cb.cdsubramo SUB_POLICY_LINE,
         cb.fefecto EFFECTIVE_DATE,
         cb.fvencimiento EXPIRATION_DATE,
         cp.cdmoneda CURRENCY,
         '' INCIDENT_LIMIT,
         (cb.NPOLIZA || cb.NCERTIFICADO || cb.CDRAMO || cb.CDSUBRAMO ||
         cb.CDGARANTIA || cb.CDSUBGARANTIA ||
         TO_CHAR(cb.FEFECTO, 'DDMMYYYY')) POLICY_SYSTEM_I_D,
         cb.CDRAMO OFFERING_TYPE,
         cb.ncertificado certificate_ext
    FROM coberturas cb, cuerpoliza cp
   WHERE cb.npoliza = 'NUM_POLIZA'
     AND cp.npoliza = cb.npoliza
     AND cb.ncertificado IN ('NUM_RIESGO')
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') >= trunc(cb.fefecto)
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') < trunc(cb.fvencimiento) + 1),
banca AS
 (SELECT cp.cdramo || '-' || cb.cdsubramo || '-' || cb.cdgarantia || '-' ||
         cb.cdsubgarantia TYPE,
         cb.cdsubramo SUB_POLICY_LINE,
         cb.fefecto EFFECTIVE_DATE,
         cb.fvencimiento EXPIRATION_DATE,
         cp.cdmoneda CURRENCY,
         '' INCIDENT_LIMIT,
         (cb.NPOLIZA || cb.NCERTIFICADO || cb.CDRAMO || cb.CDSUBRAMO ||
         cb.CDGARANTIA || cb.CDSUBGARANTIA ||
         TO_CHAR(cb.FEFECTO, 'DDMMYYYY')) POLICY_SYSTEM_I_D,
         cb.CDRAMO OFFERING_TYPE,
         cb.ncertificado
    FROM coberturas_ban cb, cuerpoliza_ban cp
   WHERE (SELECT COUNT(*) FROM normal) = 0
     AND cb.npoliza = 'NUM_POLIZA'
     AND cp.npoliza = cb.npoliza
     AND cb.ncertificado IN ('NUM_RIESGO')
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') >= trunc(cb.fefecto)
     AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') < trunc(cb.fvencimiento) + 1)
SELECT *
  FROM normal
UNION
SELECT * FROM banca