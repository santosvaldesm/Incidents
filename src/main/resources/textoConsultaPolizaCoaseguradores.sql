SELECT 
 '' ID,
 '' ADMINISTRATIVE_EXPENSES,
 DECODE(c.cdcoa, 'C', 0, 'A', 1) GIVEN_OR_ACEPTED,
 SUBSTR(d.cdcia, 3) CODE,
 d.poreparto_coa PARTICIPATION,
 DECODE(d.cdabridora, 'A', 1, NULL, 0) LEADER
  FROM coa_tratado c
 INNER JOIN coa_tratado_detalle d
    ON d.npoliza = c.npoliza
   AND d.fealta = c.fealta
 INNER JOIN companias cc
    ON cc.cdcia = d.cdcia
   AND cc.cdpais = '57'
 WHERE c.npoliza = 'NUM_POLIZA'
   AND ((c.febaja IS NULL AND TO_DATE('INITIAL_DATE','YYYY-MM-DD') >= trunc(c.fealta)) OR
       (TO_DATE('INITIAL_DATE','YYYY-MM-DD') BETWEEN trunc(c.fealta) AND trunc(c.febaja)))