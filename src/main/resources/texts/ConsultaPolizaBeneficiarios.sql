SELECT (
	SELECT snnatural
    FROM   tper_tipos_personas
    WHERE  cdtipo_persona = a.cdtipo_identificacion) SN_NATURAL,
       '_' || A.dni I_D,
       PO.codpais                                        CELL_PHONE_COUNTRY,
       A.dscelular                                       CELL_PHONE,
       A.fnacim                                          DATE_OF_BIRTH,
       A.dsnombre1                                       FIRST_NAME,
       A.dsapellido1                                     LAST_NAME,
       ''                                                LICENSE_NUMBER,
       ''                                                LICENSE_STATE,
       A.estado                                          MARITAL_STATUS,
       A.dsnombre2                                       MIDDLE_NAME,
       A.dsapellido2                                     SECOND_LAST_NAME_EXT,
       Nvl(T.dse_mail, P.dse_mail)                       EMAIL_ADDRESS1,
       ''                                                EMAIL_ADDRESS_2,
       P.nmtelefono                                      HOME_PHONE,
       A.dni                                             POLICY_SYSTEM_I_D,
       B.cdtipo_direccion                                PRIMARY_PHONE,
       A.dni                                             S_S_N_OFFICIAL_I_D,
       A.dni                                             TAX_I_D,
       ''                                                TAX_STATUS,
       ''                                                VENDOR_TYPE,
       0                                                 PREFERRED
FROM   personas_prod B,
       personas A
       left join tsic_direccions_pers T
              ON ( T.dni = A.dni
                   AND T.sncorrespondencia = 'S'
                   AND T.febaja IS NULL )
       left join tsic_direccions_pers P
              ON ( P.dni = A.dni
                   AND P.cdtipo_direccion = 'OP'
                   AND P.febaja IS NULL )
       left join postal PO
              ON ( PO.cdmunicipio IN P.cdmunicipio )
WHERE  B.npoliza = 'NUM_POLIZA'
       AND B.ncertificado = 'NUM_RIESGO'
       AND B.codigo IN ( '020', '120' )
       AND A.dni = B.dni 