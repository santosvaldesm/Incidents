--------------
-- RESERVAS --
--------------
WITH SINIESTROS
AS (
  SELECT ID
    ,PUBLICID PUBLICID_CLAIM
    ,CLAIMNUMBER
    ,POLICYID
    ,LOSSDATE FECHA_SINIESTRO
    ,REPORTEDDATE FECHA_AVISO
    ,ISMIGRATED_EXT ESMIGRADO
    ,ISREINSURANCERECALCULATED_EXT RECALCULADO
  FROM ADM_GWCC.CC_CLAIM
  WHERE RETIRED = 0
  AND CLAIMNUMBER NOT LIKE '8%'
),
  
TRANSACCIONES
AS (
  SELECT CLAIMNUMBER
    ,DECODE(MASTERPOLICYNUMBER, NULL, POLICYNUMBER, MASTERPOLICYNUMBER) POLICYNUMBER
    ,TO_CHAR(FECHA_SINIESTRO, 'YYYY/MM/DD') FECHA_SINIESTRO
    ,TO_CHAR(FECHA_AVISO, 'YYYY/MM/DD') FECHA_AVISO
    ,ESMIGRADO
    ,t.id ID_TRA
    ,CT.L_ES_CO TIPO_TRANSACCION
    ,T.SUBTYPE
    ,T.ISMIGRATED_EXT
    ,TST.L_ES_CO ESTADO
    ,TLCOS.L_ES_CO COST_CATEGORY
    ,T.CREATETIME
    ,SUM(TL.CLAIMAMOUNT) AS CLAIMAMOUNT
    ,T.PUBLICID PUBLICID_TRA
    ,T.CHECKID
    ,P.Coinsurance_Ext
    ,T.STATUS
    ,T.SAPDETAIL_EXT
    ,CONCAT (
      ctc.firstname
      ,ctc.lastname
      ) AS USUARIO
    ,RECALCULADO
    ,CASE WHEN P.POLICYTYPE = 10001 THEN '040' ELSE
      CASE WHEN TCM.EXTERNALCODE IS NULL THEN P.LINE_EXT ELSE TCM.EXTERNALCODE END     
     END RAMO_CONTABLE
    ,crr.typecode as MONEDA
  FROM SINIESTROS S
  INNER JOIN ADM_GWCC.CC_POLICY P ON S.POLICYID = P.ID
  INNER JOIN ADM_GWCC.CC_TRANSACTION T ON S.ID = T.CLAIMID
  INNER JOIN ADM_GWCC.CC_TRANSACTIONLINEITEM TL ON T.ID = TL.TRANSACTIONID
  INNER JOIN ADM_GWCC.CCTL_TRANSACTION CT ON T.SUBTYPE = CT.ID
  INNER JOIN ADM_GWCC.CCtl_TRANSACTIONSTATUS TST ON TST.ID = T.STATUS
  INNER JOIN ADM_GWCC.Cc_User us ON us.ID = T.CREATEUSERID
  INNER JOIN ADM_GWCC.cc_Contact ctc ON ctc.ID = us.contactid
  INNER JOIN ADM_GWCC.CCTL_COSTCATEGORY TLCOS ON T.COSTCATEGORY = TLCOS.ID
  LEFT JOIN ADM_GWCC.CCX_SAPDETAIL_EXT SDE ON T.SAPDETAIL_EXT = SDE.ID
  LEFT JOIN ADM_GWCC.CC_EXPOSURE EX ON EX.ID = T.Exposureid
  LEFT JOIN ADM_GWCC.CC_COVERAGE COV ON COV.ID = EX.COVERAGEID
  LEFT JOIN ADM_GWCC.Cctl_Coveragetype CTL ON ctl.ID = cov.type
  LEFT JOIN ADM_GWCC.Cctl_offeringtype_ext oft ON oft.ID=cov.offeringtype_ext
  LEFT JOIN ADM_GWCC.ccx_typecodemapper_ext tcm ON tcm.listname = 'Line_Ext'
       AND tcm.namespace = 'AccountingLineFromCoverageOffering'
       AND tcm.internalcode = CTL.TYPECODE||'-'||oft.TYPECODE
  LEFT JOIN ADM_GWCC.Cctl_currency crr ON crr.id=t.currency     
  WHERE 
   TST.NAME <> 'Rejected'
  AND T.RETIRED = 0
  AND TL.CLAIMAMOUNT != 0
  AND T.SUBTYPE IN (1,2)
    AND TST.L_ES_CO NOT IN ('Denegada', 'En espera de envío','Aprobación pendiente')
    AND (CASE WHEN T.sapdetail_ext IS NULL OR SDE.sapaccountingdate_ext IS NULL THEN T.createtime ELSE SDE.sapaccountingdate_ext END) -- Usando fecha de contabilizacion
    --AND T.CREATETIME -- Usando fecha de creacion
        BETWEEN TO_DATE(INITIAL_DATE || '000000', 'YYYYMMDDHH24MISS') AND TO_DATE(END_DATE|| '235959', 'YYYYMMDDHH24MISS')        
  GROUP BY CLAIMNUMBER
    ,DECODE(MASTERPOLICYNUMBER, NULL, POLICYNUMBER, MASTERPOLICYNUMBER)
    ,TO_CHAR(FECHA_SINIESTRO, 'YYYY/MM/DD')
    ,TO_CHAR(FECHA_AVISO, 'YYYY/MM/DD')
    ,ESMIGRADO
    ,t.id
    ,CT.L_ES_CO
    ,TLCOS.L_ES_CO
    ,T.SUBTYPE
    ,T.ISMIGRATED_EXT
    ,TST.L_ES_CO
    ,T.CREATETIME
    ,T.PUBLICID
    ,T.CHECKID
    ,P.Coinsurance_Ext
    ,T.STATUS
    ,T.SAPDETAIL_EXT
    ,CONCAT(ctc.firstname, ctc.lastname)
    ,RECALCULADO
    ,CASE WHEN P.POLICYTYPE = 10001 THEN '040' ELSE
      CASE WHEN TCM.EXTERNALCODE IS NULL THEN P.LINE_EXT ELSE TCM.EXTERNALCODE END     
     END
    ,crr.typecode
),
  
TRANSACCION_DEF
AS (
  SELECT T1.CLAIMNUMBER
    ,T1.POLICYNUMBER
    ,T1.FECHA_SINIESTRO
    ,T1.FECHA_AVISO
    ,T1.ESMIGRADO
    ,T1.ID_TRA
    ,T1.TIPO_TRANSACCION
    ,T1.COST_CATEGORY
    ,T1.RAMO_CONTABLE
    ,T1.SUBTYPE
    ,T1.ISMIGRATED_EXT
    ,T1.RECALCULADO
    ,T1.ESTADO
    ,T1.CREATETIME
    ,T1.PUBLICID_TRA
    ,T1.CHECKID
    ,T1.Coinsurance_Ext
    ,T1.STATUS
    ,T1.SAPDETAIL_EXT
    ,T1.USUARIO
    ,T1.CLAIMAMOUNT
    ,'' ESTADO_CHEQUE
    ,T1.CREATETIME FECHA_SAP
    ,T1.MONEDA
  FROM TRANSACCIONES T1
  LEFT JOIN ADM_GWCC.CCX_SAPDETAIL_EXT SDE ON T1.SAPDETAIL_EXT = SDE.ID
  INNER JOIN ADM_GWCC.CCtl_TRANSACTIONSTATUS TST ON TST.ID = T1.STATUS
  WHERE T1.SUBTYPE = 2 
        AND T1.PUBLICID_TRA NOT LIKE 'ctm%'
  
  UNION ALL
  
  SELECT T2.CLAIMNUMBER
    ,T2.POLICYNUMBER
    ,T2.FECHA_SINIESTRO
    ,T2.FECHA_AVISO
    ,T2.ESMIGRADO
    ,T2.ID_TRA
    ,T2.TIPO_TRANSACCION
    ,T2.COST_CATEGORY
    ,T2.RAMO_CONTABLE
    ,T2.SUBTYPE
    ,T2.ISMIGRATED_EXT
    ,T2.RECALCULADO
    ,T2.ESTADO
    ,T2.CREATETIME
    ,T2.PUBLICID_TRA
    ,T2.CHECKID
    ,T2.Coinsurance_Ext
    ,T2.STATUS
    ,T2.SAPDETAIL_EXT
    ,T2.USUARIO
    ,t2.CLAIMAMOUNT * - 1 VALORPAGO
    ,TST.L_ES_CO ESTADO_CHEQUE
    ,SDE.SAPACCOUNTINGDATE_EXT FECHA_SAP
    ,T2.MONEDA
  FROM TRANSACCIONES T2
  LEFT JOIN ADM_GWCC.CC_CHECK CK ON CK.ID = T2.CHECKID
  LEFT JOIN ADM_GWCC.CCX_SAPDETAIL_EXT SDE ON CK.SAPDETAIL_EXT = SDE.ID
  LEFT JOIN ADM_GWCC.CCtl_TRANSACTIONSTATUS TST ON TST.ID = CK.STATUS
  WHERE ((TST.L_ES_CO = 'Detenido' AND ck.bulkinvoiceiteminfoid IS NULL)
        OR TST.L_ES_CO IN (
           'Solicitado'
           ,'Enviado'
           ,'Emitido'
           ,'Anulado'
           ,'Legalizado'
           ,'Solicitando'
        ))
    AND (T2.SUBTYPE = 1 
        AND T2.PUBLICID_TRA NOT LIKE 'ctm%')
),
  
REASEGURO_CEDIDO
AS (
  SELECT agrg.transactionid
    ,sum(rri.participationamount) AS CEDIDO
  FROM ADM_GWCC.ccx_AgreementGroupRI_Ext agrg
  INNER JOIN ADM_GWCC.Ccx_Agreementri_Ext agri ON agrg.ID = agri.Agreementgroupid
  INNER JOIN ADM_GWCC.Ccx_Reinsurerri_Ext rri ON rri.agreementid = agri.ID
  INNER JOIN ADM_GWCC.cc_ClaimContact cco ON rri.reinsurerid = cco.ID
  INNER JOIN ADM_GWCC.cc_Contact ctc ON ctc.ID = cco.Contactid
  WHERE ctc.SURACODE_EXT <> '00000'
    AND agrg.retired = 0
    AND (
      agrg.distributiontype = 'RESERVE'
      OR agrg.distributiontype IS NULL
      )
    AND NVL(rri.participationamount, 0) <> 0  
    AND nvl(agrg.reflection, 0) = 0
    AND EXISTS (
        SELECT * FROM TRANSACCION_DEF T WHERE T.ID_TRA = agrg.Transactionid 
    )
  GROUP BY agrg.transactionid
),

REASEGURO_RETENIDO
AS (
  SELECT agrg.transactionid
    ,sum(rri.participationamount) AS RETENIDO
  FROM ADM_GWCC.ccx_AgreementGroupRI_Ext agrg
  INNER JOIN ADM_GWCC.Ccx_Agreementri_Ext agri ON agrg.ID = agri.Agreementgroupid
  INNER JOIN ADM_GWCC.Ccx_Reinsurerri_Ext rri ON rri.agreementid = agri.ID
  INNER JOIN ADM_GWCC.cc_ClaimContact cco ON rri.reinsurerid = cco.ID
  INNER JOIN ADM_GWCC.cc_Contact ctc ON ctc.ID = cco.Contactid
  WHERE ctc.SURACODE_EXT = '00000'
    AND agrg.retired = 0
    AND (
      agrg.distributiontype = 'RESERVE'
      OR agrg.distributiontype IS NULL
      )
    AND NVL(rri.participationamount, 0) <> 0  
    AND nvl(agrg.reflection, 0) = 0
    AND EXISTS (
        SELECT * FROM TRANSACCION_DEF T WHERE T.ID_TRA = agrg.Transactionid 
    )
  GROUP BY agrg.transactionid
),
  
REASEGURO_RETENCION_SURA
AS (
  SELECT agrg.transactionid
    ,sum(agrg.retentionamount) AS SURA_RETENIDO
  FROM ADM_GWCC.ccx_AgreementGroupRI_Ext agrg
  WHERE agrg.retired = 0
      AND (
          agrg.distributiontype = 'RESERVE'
          OR agrg.distributiontype IS NULL
      )
      AND NVL(agrg.retentionamount, 0) <> 0
      AND nvl(agrg.reflection, 0) = 0
      AND EXISTS (
        SELECT * FROM TRANSACCION_DEF T WHERE T.ID_TRA = agrg.Transactionid 
      )    
  GROUP BY agrg.transactionid
),
  
COASEGURO_CEDIDO
AS (
  SELECT ISC.CoinsuranceParticipation
    ,ISC.COINSURANCE
    ,I.L_ES_CO
  FROM ADM_GWCC.CCX_INSURANCECOINSURANCE_EXT ISC
  LEFT JOIN ADM_GWCC.CCX_COINSURANCE_EXT CO ON CO.ID = ISC.COINSURANCE
  LEFT JOIN ADM_GWCC.CCTL_INSURER_EXT I ON I.ID = ISC.NAME
  WHERE I.L_ES_CO = 'Seguros Generales Suramericana S.A.'
    AND ISC.retired = 0
  AND CO.GIVENORACEPTED = 0  
),
  
RESERVA_TOTAL
AS (
  SELECT T.*
    ,NVL(RC.CEDIDO, 0) AS CEDIDO
    ,NVL(RET.RETENIDO, 0) AS RETENIDO
    ,NVL(SRET.SURA_RETENIDO, 0) AS SURA_RETENIDO  
    ,CASE 
      WHEN CC.L_ES_CO = 'Seguros Generales Suramericana S.A.' and T.COST_CATEGORY != 'Solo SURA' and T.COST_CATEGORY != 'Gastos proceso jurídico Solo SURA' and T.COST_CATEGORY != 'Gastos Solo SURA'
        THEN round((CC.CoinsuranceParticipation / 100) * T.CLAIMAMOUNT)
      ELSE T.CLAIMAMOUNT
      END AS VALOR_BRUTO
    ,CASE 
      WHEN T.SUBTYPE = 1 THEN
        CASE WHEN T.CLAIMAMOUNT > 0 THEN '03' ELSE '05' END
      WHEN T.CLAIMAMOUNT > 0
        THEN '01'
      ELSE '02'
      END AS MOVIMIENTO
  FROM TRANSACCION_DEF T
  LEFT JOIN REASEGURO_CEDIDO RC ON RC.transactionid = T.ID_TRA
  LEFT JOIN COASEGURO_CEDIDO CC ON CC.COINSURANCE = T.COINSURANCE_EXT
  LEFT JOIN REASEGURO_RETENIDO RET ON RET.transactionid = T.ID_TRA
  LEFT JOIN REASEGURO_RETENCION_SURA SRET ON SRET.transactionid = T.ID_TRA
),
  
RESERVA_TOTAL_F
AS (
  SELECT CLAIMNUMBER
    ,ESMIGRADO
    ,POLICYNUMBER
    ,ID_TRA
    ,TIPO_TRANSACCION
    ,COST_CATEGORY
    ,RAMO_CONTABLE
    ,ESTADO
    ,CREATETIME
    ,PUBLICID_TRA
    ,NULL AS TRANSACCION_ORIGEN
    ,CLAIMAMOUNT
    ,CEDIDO
    ,RETENIDO
    ,SURA_RETENIDO
    ,VALOR_BRUTO
    ,MONEDA
    ,MOVIMIENTO
    ,ESTADO_CHEQUE
    ,RECALCULADO
    ,(VALOR_BRUTO) - (CEDIDO + RETENIDO + SURA_RETENIDO) DIFERENCIA
    ,0 AS REFLECTION
  FROM RESERVA_TOTAL RTOT
),

--SELECT * FROM RESERVA_TOTAL_F

/* ******************************************************************************************* */
/* ********************************** REFLEJOS DE RESERVAS *********************************** */
/* ******************************************************************************************* */

REFLECTION_RES 
AS (
 SELECT C.claimnumber
  ,DECODE(P.masterpolicynumber, NULL, P.policynumber, P.masterpolicynumber) policynumber 
  ,CASE WHEN P.policytype = 10001 THEN '040' ELSE
      CASE WHEN TCM.externalcode IS NULL THEN P.line_Ext ELSE TCM.externalcode END     
     END ramo_contable
  ,CRR.typecode as moneda
  ,TLCOS.l_es_co cost_category
  ,R.publicid as reflection_id
  ,CASE WHEN NVL(R.ID, 0) = 0 THEN AGRG.CREATETIME ELSE CASE WHEN R.SAPACCOUNTINGDATE IS NULL THEN R.CREATETIME ELSE R.SAPACCOUNTINGDATE END END createtime
  ,AGRG.id agreementgroupri
  ,R.statusworkqueue
  ,R.sapreplydate
  ,CASE WHEN NVL(R.id, 0) = 0 THEN 'SIN ENVIO A SAP' ELSE CASE WHEN R.sapstatus = 0 THEN 'SIN RESPUESTA DE SAP' ELSE 'RECIBIDO SAP' END END sapstatus
  ,T.publicid transaction_id
  ,TRST.l_es_co transactiontype 
  ,C.LOSSDATE FECHA_SINIESTRO
  ,C.ISMIGRATED_EXT ESMIGRADO
  ,T.STATUS
  ,T.SUBTYPE
  ,T.CHECKID
 FROM adm_gwcc.ccx_agreementgroupri_ext AGRG
  LEFT JOIN adm_gwcc.ccx_messagingreflection_ext R ON R.agreementgroupri = AGRG.id 
  INNER JOIN adm_gwcc.cc_claim C ON AGRG.claimid = C.id
  INNER JOIN adm_gwcc.cc_policy P ON C.POLICYID = P.ID 
  LEFT JOIN adm_gwcc.cc_transaction T ON T.id = AGRG.transactionid
  LEFT JOIN adm_gwcc.cc_exposure EX ON EX.id = T.exposureid
  LEFT JOIN adm_gwcc.cc_coverage COV ON COV.id = EX.coverageid
  LEFT JOIN adm_gwcc.cctl_coveragetype CTL ON CTL.id = COV.type
  LEFT JOIN adm_gwcc.cctl_offeringtype_ext OFT ON OFT.id = COV.offeringtype_ext
  LEFT JOIN adm_gwcc.ccx_typecodemapper_ext TCM ON TCM.listname = 'Line_Ext'
       AND TCM.namespace = 'AccountingLineFromCoverageOffering'
       AND TCM.internalcode = CTL.typecode||'-'||OFT.typecode
  LEFT JOIN adm_gwcc.cctl_currency CRR ON CRR.id = T.currency 
  LEFT JOIN adm_gwcc.cctl_costcategory TLCOS ON T.costcategory = TLCOS.id
  LEFT JOIN adm_gwcc.cctl_transaction TRST ON T.subtype = TRST.id
 WHERE 
  AGRG.Retired = 0 AND NVL(R.Retired, 0) = 0 -- reflejos y distribuciones no retiradas
  AND NVL(AGRG.reflection, 0) = 1 -- distribuciones tipo reflejos
  AND T.subtype IN (1,2) -- transacciones tipo reserva y pago 
  AND AGRG.distributiontype = 'RESERVE' -- distribucion de reaseguro tipo reserva
  AND (CASE WHEN P.policytype = 10001 AND T.PUBLICID LIKE 'ctm%' THEN 0 ELSE 1 END) = 1 -- excluir transacciones migradas de Autos
  AND NVL(T.ISRSA_EXT, 0) = 0 -- excluir transacciones de RSA
  -- Filtro de fecha, si tiene tabla de reflejos por fecha de contabilizacion si la tiene, sino fecha de creacion, si no tiene tabla de reflejos fecha de creacion de la distribucion
  AND (CASE WHEN NVL(R.ID, 0) = 0 THEN AGRG.CREATETIME ELSE CASE WHEN R.SAPACCOUNTINGDATE IS NULL THEN R.CREATETIME ELSE R.SAPACCOUNTINGDATE END END)
      BETWEEN TO_DATE(INITIAL_DATE || '000000', 'YYYYMMDDHH24MISS') AND TO_DATE(END_DATE|| '235959', 'YYYYMMDDHH24MISS')
),

REFLECTION_RES_FILT AS (
 SELECT DISTINCT R.*
 FROM REFLECTION_RES R
  INNER JOIN ADM_GWCC.CC_CHECK CH ON R.CHECKID = CH.ID 
 WHERE R.SUBTYPE = 1
  AND CH.STATUS NOT IN (5,7) -- transacciones de cheques no denegados o detenidos que no se van a sap
  
 UNION ALL
 
 SELECT DISTINCT R.*
 FROM REFLECTION_RES R
 WHERE R.SUBTYPE = 2
  AND R.STATUS NOT IN (5,7) -- transacciones de reserva no denegados o detenidos que no se van a sap
),

REASEGURO_CEDIDO_REF
AS (
  SELECT agrg.id
    ,sum(rri.participationamount) AS CEDIDO
    ,count(rri.id) AS NUMREA_CEDIDO
  FROM ADM_GWCC.ccx_AgreementGroupRI_Ext agrg
  INNER JOIN ADM_GWCC.Ccx_Agreementri_Ext agri ON agrg.ID = agri.Agreementgroupid
  INNER JOIN ADM_GWCC.Ccx_Reinsurerri_Ext rri ON rri.agreementid = agri.ID
  INNER JOIN ADM_GWCC.cc_ClaimContact cco ON rri.reinsurerid = cco.ID
  INNER JOIN ADM_GWCC.cc_Contact ctc ON ctc.ID = cco.Contactid
  WHERE ctc.SURACODE_EXT <> '00000'
    AND agrg.retired = 0
    AND NVL(rri.participationamount, 0) <> 0  
    AND agrg.distributiontype = 'RESERVE'
    AND nvl(agrg.reflection, 0) = 1
    AND EXISTS (
        SELECT * FROM REFLECTION_RES_FILT R WHERE R.agreementgroupri = agrg.id 
    )
  GROUP BY agrg.id
),

REASEGURO_RETENIDO_REF
AS (
  SELECT agrg.id
    ,sum(rri.participationamount) AS RETENIDO
    ,count(rri.id) AS NUMREA_RETENIDO
  FROM ADM_GWCC.ccx_AgreementGroupRI_Ext agrg
  INNER JOIN ADM_GWCC.Ccx_Agreementri_Ext agri ON agrg.ID = agri.Agreementgroupid
  INNER JOIN ADM_GWCC.Ccx_Reinsurerri_Ext rri ON rri.agreementid = agri.ID
  INNER JOIN ADM_GWCC.cc_ClaimContact cco ON rri.reinsurerid = cco.ID
  INNER JOIN ADM_GWCC.cc_Contact ctc ON ctc.ID = cco.Contactid
  WHERE ctc.SURACODE_EXT = '00000'
    AND agrg.retired = 0
    AND NVL(rri.participationamount, 0) <> 0  
    AND agrg.distributiontype = 'RESERVE'
    AND nvl(agrg.reflection, 0) = 1
    AND EXISTS (
        SELECT * FROM REFLECTION_RES_FILT R WHERE R.agreementgroupri = agrg.id 
    )
  GROUP BY agrg.id
),

RETENCION_SURA_REF
AS (
  SELECT agrg.id
    ,sum(agrg.retentionamount) AS SURA_RETENIDO
    ,count(agrg.id) AS NUMREA_SURA_RETENIDO
  FROM ADM_GWCC.ccx_AgreementGroupRI_Ext agrg
  WHERE agrg.retired = 0
      AND NVL(agrg.retentionamount, 0) <> 0
      AND agrg.distributiontype = 'RESERVE'
      AND nvl(agrg.reflection, 0) = 1
      AND EXISTS (
        SELECT * FROM REFLECTION_RES_FILT R WHERE R.agreementgroupri = agrg.id  
      )    
  GROUP BY agrg.id
),

REFLECTION_RES_F
AS (
  SELECT R.CLAIMNUMBER
   ,R.ESMIGRADO
   ,R.POLICYNUMBER
   ,NULL AS ID_TRA
   ,R.TRANSACTIONTYPE AS TIPO_TRANSACCION
   ,R.COST_CATEGORY
   ,R.RAMO_CONTABLE
   ,R.SAPSTATUS AS ESTADO
   ,R.CREATETIME
   ,R.REFLECTION_ID AS PUBLICID_TRA
   ,R.TRANSACTION_ID AS TRANSACCION_ORIGEN
   ,0 AS CLAIMAMOUNT
   ,NVL(CED.CEDIDO, 0) CEDIDO 
   ,NVL(RET.RETENIDO, 0) RETENIDO
   ,NVL(RSURA.SURA_RETENIDO, 0) SURA_RETENIDO
   ,0 AS VALOR_BRUTO
   ,R.MONEDA
   ,NULL AS MOVIMIENTO
   ,NULL AS ESTADO_CHEQUE
   ,NULL AS RECALCULADO
   ,0 AS DIFERENCIA
   ,1 AS REFLECTION
   --,NVL(CED.NUMREA_CEDIDO, 0) NUMREA_CEDIDO
   --,NVL(RET.NUMREA_RETENIDO, 0) NUMREA_RETENIDO
   --,NVL(RSURA.NUMREA_SURA_RETENIDO, 0) NUMREA_SURA_RETENIDO
  FROM REFLECTION_RES_FILT R
   LEFT JOIN REASEGURO_CEDIDO_REF CED ON R.agreementgroupri = CED.id
   LEFT JOIN REASEGURO_RETENIDO_REF RET ON R.agreementgroupri = RET.id
   LEFT JOIN RETENCION_SURA_REF RSURA ON R.agreementgroupri = RSURA.id
  WHERE NVL(CED.NUMREA_CEDIDO, 0) > 0 -- Solo tener en cuenta las distribucione/reflejos que tienen movimientos cedidos, ya que movimientos solo retenidos no se van a sap 
  ORDER BY R.CREATETIME
)


SELECT * FROM (
 SELECT * FROM RESERVA_TOTAL_F
 UNION ALL
 SELECT * FROM REFLECTION_RES_F
) F




