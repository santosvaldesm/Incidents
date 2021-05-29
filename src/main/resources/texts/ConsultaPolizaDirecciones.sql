SELECT    
          T.dsdireccion             ADDRESS_LINE1,
          ''                        ADDRESS_LINE2,
          ''                        ADDRESS_LINE3,
          T.cdtipo_direccion        ADDRESS_TYPE,
          PO.cdpostal               CITY,
          PO.codpais                COUNTRY,
          ''                        COUNTY,
          T.dsobservacion           DESCRIPTION,
          PO.cdpostal               POSTAL_CODE,
          Substr(PO.cdpostal, 1, 2) STATE
FROM      tsic_direccions_pers T
LEFT JOIN postal PO
ON        (PO.cdmunicipio = T.cdmunicipio )
WHERE     T.dni = 'TIPO_NUM_IDENTIFICACION'
AND       T.cdtipo_direccion IN ('RS','TR','OP','SP','OT','RL')