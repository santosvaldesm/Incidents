/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.panels;

import com.mycompany.incidents.otherResources.PolicyQueryTypeEnum;
import java.text.SimpleDateFormat;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author santvamu
 */
public class DialogGeneratePolicyQuery extends javax.swing.JDialog {

	String aDay = "";
	String aYear = "";
	String aMonth = "";

	/**
	 * Creates new form DialogGeneratePolicyQuery
	 */
	public DialogGeneratePolicyQuery(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	public DefaultComboBoxModel<String> getPolicyQueryTypes() {
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
		PolicyQueryTypeEnum[] values = PolicyQueryTypeEnum.values();
		for (PolicyQueryTypeEnum value : values) {
			comboModel.addElement(value.toString());
		}
		return comboModel;
	}

	private void printInOutputText(String textToAdd) {
		outputTxt.setText(outputTxt.getText() + textToAdd);
		outputTxt.setCaretPosition(outputTxt.getDocument().getLength());
	}

	private void generateQueryBeneficiarios() {
		String out = "SELECT (\n"
						+ "	SELECT snnatural\n"
						+ "    FROM   tper_tipos_personas\n"
						+ "    WHERE  cdtipo_persona = a.cdtipo_identificacion) SN_NATURAL,\n"
						+ "       '_' || A.dni I_D,\n"
						+ "       PO.codpais                                        CELL_PHONE_COUNTRY,\n"
						+ "       A.dscelular                                       CELL_PHONE,\n"
						+ "       A.fnacim                                          DATE_OF_BIRTH,\n"
						+ "       A.dsnombre1                                       FIRST_NAME,\n"
						+ "       A.dsapellido1                                     LAST_NAME,\n"
						+ "       ''                                                LICENSE_NUMBER,\n"
						+ "       ''                                                LICENSE_STATE,\n"
						+ "       A.estado                                          MARITAL_STATUS,\n"
						+ "       A.dsnombre2                                       MIDDLE_NAME,\n"
						+ "       A.dsapellido2                                     SECOND_LAST_NAME_EXT,\n"
						+ "       Nvl(T.dse_mail, P.dse_mail)                       EMAIL_ADDRESS1,\n"
						+ "       ''                                                EMAIL_ADDRESS_2,\n"
						+ "       P.nmtelefono                                      HOME_PHONE,\n"
						+ "       A.dni                                             POLICY_SYSTEM_I_D,\n"
						+ "       B.cdtipo_direccion                                PRIMARY_PHONE,\n"
						+ "       A.dni                                             S_S_N_OFFICIAL_I_D,\n"
						+ "       A.dni                                             TAX_I_D,\n"
						+ "       ''                                                TAX_STATUS,\n"
						+ "       ''                                                VENDOR_TYPE,\n"
						+ "       0                                                 PREFERRED\n"
						+ "FROM   personas_prod B,\n"
						+ "       personas A\n"
						+ "       left join tsic_direccions_pers T\n"
						+ "              ON ( T.dni = A.dni\n"
						+ "                   AND T.sncorrespondencia = 'S'\n"
						+ "                   AND T.febaja IS NULL )\n"
						+ "       left join tsic_direccions_pers P\n"
						+ "              ON ( P.dni = A.dni\n"
						+ "                   AND P.cdtipo_direccion = 'OP'\n"
						+ "                   AND P.febaja IS NULL )\n"
						+ "       left join postal PO\n"
						+ "              ON ( PO.cdmunicipio IN P.cdmunicipio )\n"
						+ "WHERE  B.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "       AND B.ncertificado = '" + txtRiskNumber.getText() + "'\n"
						+ "       AND B.codigo IN ( '020', '120' )\n"
						+ "       AND A.dni = B.dni ";
		outputTxt.setText(out);
		outputTxt.setCaretPosition(0);
	}

	private void generateQueryCoaseguradores() {
		String out = "SELECT \n"
						+ " '' ID,\n"
						+ " '' ADMINISTRATIVE_EXPENSES,\n"
						+ " DECODE(c.cdcoa, 'C', 0, 'A', 1) GIVEN_OR_ACEPTED,\n"
						+ " SUBSTR(d.cdcia, 3) CODE,\n"
						+ " d.poreparto_coa PARTICIPATION,\n"
						+ " DECODE(d.cdabridora, 'A', 1, NULL, 0) LEADER\n"
						+ "  FROM coa_tratado c\n"
						+ " INNER JOIN coa_tratado_detalle d\n"
						+ "    ON d.npoliza = c.npoliza\n"
						+ "   AND d.fealta = c.fealta\n"
						+ " INNER JOIN companias cc\n"
						+ "    ON cc.cdcia = d.cdcia\n"
						+ "   AND cc.cdpais = '57'\n"
						+ " WHERE c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "   AND ((c.febaja IS NULL AND TO_DATE('" + aYear + "/" + aMonth + "/" + aDay + "','YYYY-MM-DD') >= trunc(c.fealta)) OR\n"
						+ "       (TO_DATE('" + aYear + "/" + aMonth + "/" + aDay + "','YYYY-MM-DD') BETWEEN trunc(c.fealta) AND trunc(c.febaja)))";
		outputTxt.setText(out);
		outputTxt.setCaretPosition(0);
	}

	private void generateCommonQueries() {
		String out = "----------------------------------------------------------------------------\n"
						+ "-- Vinculaciones es la unica tabla que trae polizas de COREGW y CORESEG,  --\n"
						+ "-- solo trae la ultima vigencia de una poliza                             --\n"
						+ "----------------------------------------------------------------------------\n"
						+ "SELECT \n"
						+ "	c.DNI, \n"
						+ "	c.NPOLIZA, \n"
						+ "	c.NCERTIFICADO, \n"
						+ "	c.FEFECTO, \n"
						+ "	c.FEVENCIMIENTO, \n"
						+ "	c.FECSUS, \n"
						+ "	c.CDFUENTE_INFORMACION \n"
						+ "FROM \n"
						+ "	tsic_vinculaciones_segcor c \n"
						+ "WHERE \n"
						+ "	c.npoliza = '" + txtPolicyNumber.getText() + "' \n"
						+ "	and TO_DATE('" + aYear + "/" + aMonth +"/" + aDay + "', 'YYYY-MM-DD') BETWEEN c.fefecto AND c.fevencimiento \n"
						+ "	and NCERTIFICADO = '" + txtRiskNumber.getText() + "'\n"
						+ "	\n"
						+ "---------------------------------------------------------\n"
						+ "-- Si hay registros en cuerpoliza es de empresariales  --\n"
						+ "---------------------------------------------------------\n"
						+ "SELECT \n"
						+ "	* \n"
						+ "FROM \n"
						+ "	CUERPOLIZA \n"
						+ "WHERE \n"
						+ "	npoliza = '" + txtPolicyNumber.getText() + "' \n"
						+ "\n"
						+ "----------------------------------------------------\n"
						+ "-- Si hay registros en cuerpoliza_ban es de banca --\n"
						+ "----------------------------------------------------\n"
						+ "SELECT \n"
						+ "	* \n"
						+ "FROM \n"
						+ "	CUERPOLIZA_BAN \n"
						+ "WHERE \n"
						+ "	npoliza = '" + txtPolicyNumber.getText() + "' \n"
						+ "	\n"
						+ "----------------------------------------------------------\n"
						+ "-- contiene las diferentes vigencias por cada cobertura --\n"
						+ "----------------------------------------------------------\n"
						+ "SELECT \n"
						+ "	* \n"
						+ "FROM \n"
						+ "	COBERTURAS C \n"
						+ "WHERE \n"
						+ "	C.npoliza like '013000134588'  \n"
						+ "	and TO_DATE('" + aYear + "/" + aMonth +"/" + aDay + "', 'YYYY-MM-DD') BETWEEN c.fefecto AND c.fvencimiento \n"
						+ "	and NCERTIFICADO = '" + txtRiskNumber.getText() + "' \n"
						+ "	\n"
						+ "------------------------------------------------------\n"
						+ "-- corresponde a los riesgos que contiene la poliza -- \n"
						+ "------------------------------------------------------\n"
						+ "SELECT \n"
						+ "	* \n"
						+ "FROM \n"
						+ "	CUERPOLIZA_CERTIFICADO C \n"
						+ "WHERE \n"
						+ "	C.npoliza = '" + txtPolicyNumber.getText() + "'; \n"
						+ "\n"
						+ "-----------------------\n"
						+ "-- Tabla de personas --\n"
						+ "-----------------------\n"
						+ "SELECT \n"
						+ "	crd.DNI,  \n"
						+ "	crd.NPOLIZA, \n"
						+ "	crd.NCERTIFICADO, \n"
						+ "	crd.FEFECTO, \n"
						+ "	crd.FEVENCIMIENTO \n"
						+ "FROM   \n"
						+ "	personas_prod crd \n"
						+ "WHERE  \n"
						+ "	crd.npoliza = '" + txtPolicyNumber.getText() + "' \n"
						+ "\n"
						+ "--------------------------------------------------------\n"
						+ "--  Solo para linea 012(arrendamiento - cumplimiento) --\n"
						+ "--------------------------------------------------------\n"
						+ "SELECT \n"
						+ "	crd.NPOLIZA, \n"
						+ "	crd.NCERTIFICADO, \n"
						+ "	crd.FEFECTO, \n"
						+ "	crd.FEVENCIMIENTO \n"
						+ "FROM   \n"
						+ "	cuerpoliza_riesgos crd \n"
						+ "WHERE  \n"
						+ "	crd.npoliza = '" + txtPolicyNumber.getText() + "'";
		outputTxt.setText(out);
		outputTxt.setCaretPosition(0);
	}

	private void generateQueryCoberturas() {
		String out = "WITH normal AS\n"
						+ " (SELECT cp.cdramo || '-' || cb.cdsubramo || '-' || cb.cdgarantia || '-' ||\n"
						+ "         cb.cdsubgarantia TYPE,\n"
						+ "         cb.cdsubramo SUB_POLICY_LINE,\n"
						+ "         cb.fefecto EFFECTIVE_DATE,\n"
						+ "         cb.fvencimiento EXPIRATION_DATE,\n"
						+ "         cp.cdmoneda CURRENCY,\n"
						+ "         '' INCIDENT_LIMIT,\n"
						+ "         (cb.NPOLIZA || cb.NCERTIFICADO || cb.CDRAMO || cb.CDSUBRAMO ||\n"
						+ "         cb.CDGARANTIA || cb.CDSUBGARANTIA ||\n"
						+ "         TO_CHAR(cb.FEFECTO, 'DDMMYYYY')) POLICY_SYSTEM_I_D,\n"
						+ "         cb.CDRAMO OFFERING_TYPE,\n"
						+ "         cb.ncertificado certificate_ext\n"
						+ "    FROM coberturas cb, cuerpoliza cp\n"
						+ "   WHERE cb.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND cp.npoliza = cb.npoliza\n"
						+ "     AND cb.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') >= trunc(cb.fefecto)\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') < trunc(cb.fvencimiento) + 1),\n"
						+ "banca AS\n"
						+ " (SELECT cp.cdramo || '-' || cb.cdsubramo || '-' || cb.cdgarantia || '-' ||\n"
						+ "         cb.cdsubgarantia TYPE,\n"
						+ "         cb.cdsubramo SUB_POLICY_LINE,\n"
						+ "         cb.fefecto EFFECTIVE_DATE,\n"
						+ "         cb.fvencimiento EXPIRATION_DATE,\n"
						+ "         cp.cdmoneda CURRENCY,\n"
						+ "         '' INCIDENT_LIMIT,\n"
						+ "         (cb.NPOLIZA || cb.NCERTIFICADO || cb.CDRAMO || cb.CDSUBRAMO ||\n"
						+ "         cb.CDGARANTIA || cb.CDSUBGARANTIA ||\n"
						+ "         TO_CHAR(cb.FEFECTO, 'DDMMYYYY')) POLICY_SYSTEM_I_D,\n"
						+ "         cb.CDRAMO OFFERING_TYPE,\n"
						+ "         cb.ncertificado\n"
						+ "    FROM coberturas_ban cb, cuerpoliza_ban cp\n"
						+ "   WHERE (SELECT COUNT(*) FROM normal) = 0\n"
						+ "     AND cb.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND cp.npoliza = cb.npoliza\n"
						+ "     AND cb.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') >= trunc(cb.fefecto)\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') < trunc(cb.fvencimiento) + 1)\n"
						+ "SELECT *\n"
						+ "  FROM normal\n"
						+ "UNION\n"
						+ "SELECT * FROM banca";
		outputTxt.setText(out);
		outputTxt.setCaretPosition(0);
	}

	private void generateQueryDirecciones() {
		String out = "SELECT    \n"
						+ "          T.dsdireccion             ADDRESS_LINE1,\n"
						+ "          ''                        ADDRESS_LINE2,\n"
						+ "          ''                        ADDRESS_LINE3,\n"
						+ "          T.cdtipo_direccion        ADDRESS_TYPE,\n"
						+ "          PO.cdpostal               CITY,\n"
						+ "          PO.codpais                COUNTRY,\n"
						+ "          ''                        COUNTY,\n"
						+ "          T.dsobservacion           DESCRIPTION,\n"
						+ "          PO.cdpostal               POSTAL_CODE,\n"
						+ "          Substr(PO.cdpostal, 1, 2) STATE\n"
						+ "FROM      tsic_direccions_pers T\n"
						+ "LEFT JOIN postal PO\n"
						+ "ON        (PO.cdmunicipio = T.cdmunicipio )\n"
						+ "WHERE     T.dni = '" + txtIdentification.getText() + "'\n"
						+ "AND       T.cdtipo_direccion IN ('RS','TR','OP','SP','OT','RL')";//aYear + aMonth + ADay +		
		outputTxt.setText(out);
		outputTxt.setCaretPosition(0);
	}

	private void generateQueryTerminos() {
		String out = "WITH normal AS\n"
						+ " (SELECT 'Deductible' AS MODEL_TYPE,\n"
						+ "         'ns2:CCNumericCovTerm' AS COV_TERM_SUB_TYPE,\n"
						+ "         '' AS COV_TERM_ORDER,\n"
						+ "         c.podeducible AS NUMERIC_VALUE,\n"
						+ "         'percent' AS UNITS,\n"
						+ "         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'DEDUC' AS COV_TERM_PATTERN,\n"
						+ "         '' AS MODEL_AGGREGATION,\n"
						+ "         '' AS MODEL_RESTRICTION,\n"
						+ "         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D\n"
						+ "    FROM coberturas c\n"
						+ "   WHERE c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)\n"
						+ "     AND c.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia) = '" + txtHomologation.getText() + "'\n"
						+ "  UNION\n"
						+ "  SELECT 'Deductible' MODEL_TYPE,\n"
						+ "         'ns2:CCClassificationCovTerm' AS COV_TERM_SUB_TYPE,\n"
						+ "         '' AS COV_TERM_ORDER,\n"
						+ "         NULL AS NUMERIC_VALUE,\n"
						+ "         'lossAmount-Loss Amount' UNITS,\n"
						+ "         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'TIPODEDUC' COV_TERM_PATTERN,\n"
						+ "         '' MODEL_AGGREGATION,\n"
						+ "         '' MODEL_RESTRICTION,\n"
						+ "         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D\n"
						+ "    FROM coberturas c\n"
						+ "   WHERE c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)\n"
						+ "     AND c.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||\n"
						+ "         c.cdsubgarantia) = '" + txtHomologation.getText() + "'\n"
						+ "  UNION\n"
						+ "  SELECT 'Deductible' AS MODEL_TYPE,\n"
						+ "         'ns2:CCFinancialCovTerm' AS COV_TERM_SUB_TYPE,\n"
						+ "         '' AS COV_TERM_ORDER,\n"
						+ "         CASE\n"
						+ "          WHEN c.cdunidad_deducible2 IN (61,12,60,14,16,25,26,27,28,29,30,31,\n"
						+ "		  32,33,34,24,23,22,15,35,1,19,64,63,37,38,4,41,43,13,11,62,36) THEN c.ptminimo_deducible\n"
						+ "          WHEN c.cdunidad_deducible2 IN (6, 20, 5)  AND cp.cdmoneda = 0 THEN c.ptminimo_deducible\n"
						+ "          WHEN c.cdunidad_deducible2 IN (7, 9, 21)  AND cp.cdmoneda = 1 THEN c.ptminimo_deducible\n"
						+ "          WHEN c.cdunidad_deducible2 IN (10, 8, 18) AND cp.cdmoneda = 9 THEN c.ptminimo_deducible\n"
						+ "          WHEN c.cdunidad_deducible2 IN (7, 9, 21)  AND cp.cdmoneda = 0 THEN round(c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (10, 8, 18) AND cp.cdmoneda = 0 THEN\n"
						+ "           round(c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '9'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 1 THEN\n"
						+ "           round(c.ptminimo_deducible /\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 9 THEN\n"
						+ "           round(c.ptminimo_deducible /\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '9'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (44, 42) AND cp.cdmoneda = 0 THEN\n"
						+ "           round(c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (44, 42) AND cp.cdmoneda = 1 THEN\n"
						+ "           round((c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3')) /\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (3, 2, 17) AND cp.cdmoneda = 0 THEN\n"
						+ "           round(c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio / 30 FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3'), 0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (3, 2, 17) AND cp.cdmoneda = 1 THEN\n"
						+ "           round((c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio / 30 FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3')) / \n"
						+ "				 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'), 0)\n"
						+ "          ELSE\n"
						+ "           c.ptminimo_deducible\n"
						+ "         END AS NUMERIC_VALUE,\n"
						+ "         '' AS UNITS,\n"
						+ "         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'MINDEDUC' AS COV_TERM_PATTERN,\n"
						+ "         '' AS MODEL_AGGREGATION,\n"
						+ "         '' AS MODEL_RESTRICTION,\n"
						+ "         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D\n"
						+ "    FROM coberturas c\n"
						+ "   INNER JOIN cuerpoliza cp\n"
						+ "      ON c.npoliza = cp.npoliza\n"
						+ "   WHERE c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)\n"
						+ "     AND c.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia) = '" + txtHomologation.getText() + "'\n"
						+ "  UNION\n"
						+ "  SELECT 'Limit' MODEL_TYPE,\n"
						+ "         'ns2:CCFinancialCovTerm' AS COV_TERM_SUB_TYPE,\n"
						+ "         '' AS COV_TERM_ORDER,\n"
						+ "         c.capital AS NUMERIC_VALUE,\n"
						+ "         '' UNITS,\n"
						+ "         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'LIMIT' COV_TERM_PATTERN,\n"
						+ "         '' MODEL_AGGREGATION,\n"
						+ "         '' MODEL_RESTRICTION,\n"
						+ "         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D\n"
						+ "    FROM coberturas c\n"
						+ "   WHERE c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)\n"
						+ "     AND c.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||\n"
						+ "         c.cdsubgarantia) = '" + txtHomologation.getText() + "'),\n"
						+ "banca AS\n"
						+ " (SELECT 'Deductible' AS MODEL_TYPE,\n"
						+ "         'ns2:CCNumericCovTerm' AS COV_TERM_SUB_TYPE,\n"
						+ "         '' AS COV_TERM_ORDER,\n"
						+ "         c.podeducible AS NUMERIC_VALUE,\n"
						+ "         'percent' AS UNITS,\n"
						+ "         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'DEDUC' AS COV_TERM_PATTERN,\n"
						+ "         '' AS MODEL_AGGREGATION,\n"
						+ "         '' AS MODEL_RESTRICTION,\n"
						+ "         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D\n"
						+ "    FROM coberturas_ban c\n"
						+ "    WHERE (SELECT COUNT(*) FROM normal) = 0\n"
						+ "     AND c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)\n"
						+ "     AND c.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia) = '" + txtHomologation.getText() + "'\n"
						+ "     AND c.podeducible IS NOT NULL\n"
						+ "  UNION\n"
						+ "  SELECT 'Deductible' MODEL_TYPE,\n"
						+ "         'ns2:CCClassificationCovTerm' AS COV_TERM_SUB_TYPE,\n"
						+ "         '' AS COV_TERM_ORDER,\n"
						+ "         NULL AS NUMERIC_VALUE,\n"
						+ "         'lossAmount-Loss Amount' UNITS,\n"
						+ "         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' || c.cdsubgarantia || '-' || 'TIPODEDUC' COV_TERM_PATTERN,\n"
						+ "         '' MODEL_AGGREGATION,\n"
						+ "         '' MODEL_RESTRICTION,\n"
						+ "         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo || c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D\n"
						+ "    FROM coberturas_ban c\n"
						+ "   WHERE (SELECT COUNT(*) FROM normal) = 0\n"
						+ "     AND c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)\n"
						+ "     AND c.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||\n"
						+ "         c.cdsubgarantia) = '" + txtHomologation.getText() + "'\n"
						+ "  UNION\n"
						+ "  SELECT 'Deductible' AS MODEL_TYPE,\n"
						+ "         'ns2:CCFinancialCovTerm' AS COV_TERM_SUB_TYPE,\n"
						+ "         '' AS COV_TERM_ORDER,\n"
						+ "         CASE\n"
						+ "          WHEN c.cdunidad_deducible2 IN (61,12,60,14,16,25,26,27,28,29,30,31,32,33,34,24,23,22,15,35,1,19,64,63,37,38,4,41,43,13,11,62,36) THEN\n"
						+ "           c.ptminimo_deducible\n"
						+ "          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 0 THEN\n"
						+ "           c.ptminimo_deducible\n"
						+ "          WHEN c.cdunidad_deducible2 IN (7, 9, 21) AND cp.cdmoneda = 1 THEN\n"
						+ "           c.ptminimo_deducible\n"
						+ "          WHEN c.cdunidad_deducible2 IN (10, 8, 18) AND cp.cdmoneda = 9 THEN\n"
						+ "           c.ptminimo_deducible\n"
						+ "          WHEN c.cdunidad_deducible2 IN (7, 9, 21) AND cp.cdmoneda = 0 THEN\n"
						+ "           round(c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (10, 8, 18) AND cp.cdmoneda = 0 THEN\n"
						+ "           round(c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '9'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 1 THEN\n"
						+ "           round(c.ptminimo_deducible /\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'), 0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (6, 20, 5) AND cp.cdmoneda = 9 THEN\n"
						+ "           round(c.ptminimo_deducible /\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '9'), 0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (44, 42) AND cp.cdmoneda = 0 THEN\n"
						+ "           round(c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3'), 0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (44, 42) AND cp.cdmoneda = 1 THEN\n"
						+ "           round((c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3')) / \n"
						+ "				 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'),0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (3, 2, 17) AND cp.cdmoneda = 0 THEN\n"
						+ "           round(c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio / 30 FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3'), 0)\n"
						+ "          WHEN c.cdunidad_deducible2 IN (3, 2, 17) AND cp.cdmoneda = 1 THEN\n"
						+ "           round((c.ptminimo_deducible *\n"
						+ "                 (SELECT m.tipo_cambio / 30 FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '3')) /\n"
						+ "                 (SELECT m.tipo_cambio FROM monedas_cambio m WHERE (SELECT COUNT(*) FROM normal) = 0 AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN m.fecambio_desde AND m.fecambio_hasta AND m.cdmoneda_base = '0' AND m.cdmoneda_cambio = '1'), 0)\n"
						+ "          ELSE\n"
						+ "           c.ptminimo_deducible\n"
						+ "         END AS NUMERIC_VALUE,\n"
						+ "         '' AS UNITS,\n"
						+ "         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||\n"
						+ "         c.cdsubgarantia || '-' || 'MINDEDUC' AS COV_TERM_PATTERN,\n"
						+ "         '' AS MODEL_AGGREGATION,\n"
						+ "         '' AS MODEL_RESTRICTION,\n"
						+ "         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo ||\n"
						+ "         c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D\n"
						+ "    FROM coberturas_ban c\n"
						+ "   INNER JOIN cuerpoliza_ban cp\n"
						+ "      ON c.npoliza = cp.npoliza\n"
						+ "   WHERE (SELECT COUNT(*) FROM normal) = 0\n"
						+ "     AND c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)\n"
						+ "     AND c.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||\n"
						+ "         c.cdsubgarantia) = '" + txtHomologation.getText() + "'\n"
						+ "  UNION\n"
						+ "  SELECT 'Limit' MODEL_TYPE,\n"
						+ "         'ns2:CCFinancialCovTerm' AS COV_TERM_SUB_TYPE,\n"
						+ "         '' AS COV_TERM_ORDER,\n"
						+ "         c.capital AS NUMERIC_VALUE,\n"
						+ "         '' UNITS,\n"
						+ "         c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||\n"
						+ "         c.cdsubgarantia || '-' || 'LIMIT' COV_TERM_PATTERN,\n"
						+ "         '' MODEL_AGGREGATION,\n"
						+ "         '' MODEL_RESTRICTION,\n"
						+ "         c.npoliza || c.ncertificado || c.cdramo || c.cdsubramo ||\n"
						+ "         c.cdgarantia || c.cdsubgarantia || TO_CHAR(c.fefecto, 'YYYYMMDD') AS POLICY_SYSTEM_I_D\n"
						+ "    FROM coberturas_ban c\n"
						+ "   WHERE (SELECT COUNT(*) FROM normal) = 0\n"
						+ "     AND c.npoliza = '" + txtPolicyNumber.getText() + "'\n"
						+ "     AND TO_DATE('" + aYear + "-" + aMonth + "-" + aDay + "','YYYY-MM-DD') BETWEEN TRUNC(c.fefecto) and TRUNC(c.fvencimiento)\n"
						+ "     AND c.ncertificado IN ('" + txtRiskNumber.getText() + "')\n"
						+ "     AND (c.cdramo || '-' || c.cdsubramo || '-' || c.cdgarantia || '-' ||\n"
						+ "         c.cdsubgarantia) = '" + txtHomologation.getText() + "')\n"
						+ "SELECT *\n"
						+ "  FROM normal\n"
						+ "UNION\n"
						+ "SELECT * FROM banca";
		outputTxt.setText(out);
		outputTxt.setCaretPosition(0);
	}

	private void configureDate() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
			String parseInitialDate = sdf.format(initialDateChooser.getDate());
			String[] splitInitialDate = parseInitialDate.split("-");
			aDay = splitInitialDate[0];
			aYear = splitInitialDate[2];
			aMonth = splitInitialDate[1];
		} catch (Exception e) {
			outputTxt.setText("El formato de las fechas no es correcto");
			outputTxt.setCaretPosition(0);
			//IncidentsUtil.printStackTrace(e,outputTxt);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    comboQueryType = new javax.swing.JComboBox<>();
    jLabel4 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    outputTxt = new javax.swing.JTextArea();
    btnGenerteQuery = new javax.swing.JButton();
    initialDateChooser = new com.toedter.calendar.JDateChooser();
    jLabel2 = new javax.swing.JLabel();
    txtIdentification = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    txtRiskNumber = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    txtPolicyNumber = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    txtHomologation = new javax.swing.JTextField();
    btnClear = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("GENERADOR DE CONSULTAS DE POLIZA");

    jLabel1.setText("Fecha (dd-MM-YYYY)");

    comboQueryType.setModel(getPolicyQueryTypes());
    comboQueryType.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboQueryTypeItemStateChanged(evt);
      }
    });
    comboQueryType.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        comboQueryTypePropertyChange(evt);
      }
    });

    jLabel4.setText("Tipo de query");

    outputTxt.setColumns(20);
    outputTxt.setRows(5);
    jScrollPane1.setViewportView(outputTxt);

    btnGenerteQuery.setText("Generar");
    btnGenerteQuery.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGenerteQueryActionPerformed(evt);
      }
    });

    initialDateChooser.setDateFormatString("dd-MM-yyyy");

    jLabel2.setText("Póliza");

    jLabel3.setText("Riesgo");

    jLabel5.setText("Tipo+ Num Identificación");

    txtPolicyNumber.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPolicyNumberActionPerformed(evt);
      }
    });

    jLabel6.setText("Homologación cobertura");

    btnClear.setText("Limpiar");
    btnClear.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(10, 10, 10)
            .addComponent(txtHomologation, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnGenerteQuery, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(10, 10, 10)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(txtRiskNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                    .addComponent(txtPolicyNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                    .addComponent(initialDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(txtIdentification)
                  .addComponent(comboQueryType, 0, 198, Short.MAX_VALUE))))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(initialDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(comboQueryType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(txtPolicyNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel2)
              .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtIdentification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(txtRiskNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtHomologation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6)
          .addComponent(btnClear)
          .addComponent(btnGenerteQuery, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        .addContainerGap())
    );

    setSize(new java.awt.Dimension(638, 400));
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void btnGenerteQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerteQueryActionPerformed
		if (comboQueryType.getSelectedIndex() == -1) {
			outputTxt.setText("No se ha seleccionado el tipo de query");
			outputTxt.setCaretPosition(0);
			return;
		}
		configureDate();
		PolicyQueryTypeEnum queryType = PolicyQueryTypeEnum.valueOf(
						comboQueryType.getSelectedItem().toString());
		switch (queryType) {
			case Beneficiarios:
				generateQueryBeneficiarios();
				break;
			case Coaseguradores:
				generateQueryCoaseguradores();
				break;
			case Coberturas:
				generateQueryCoberturas();
				break;
			case Direcciones:
				generateQueryDirecciones();
				break;
			case Terminos:
				generateQueryTerminos();
				break;
			case ConsultasComunes:
				generateCommonQueries();
				break;
		}
  }//GEN-LAST:event_btnGenerteQueryActionPerformed

  private void comboQueryTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboQueryTypeItemStateChanged

  }//GEN-LAST:event_comboQueryTypeItemStateChanged

  private void txtPolicyNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPolicyNumberActionPerformed
		// TODO add your handling code here:
  }//GEN-LAST:event_txtPolicyNumberActionPerformed

  private void comboQueryTypePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_comboQueryTypePropertyChange
		txtHomologation.setEnabled(false);
		txtIdentification.setEnabled(false);
		txtPolicyNumber.setEnabled(false);
		txtRiskNumber.setEnabled(false);
		btnGenerteQuery.setEnabled(false);
		initialDateChooser.setEnabled(false);
		if (comboQueryType.getSelectedIndex() != -1) {
			PolicyQueryTypeEnum queryType = PolicyQueryTypeEnum.valueOf(
							comboQueryType.getSelectedItem().toString());
			switch (queryType) {
				case Beneficiarios:
					txtPolicyNumber.setEnabled(true);
					txtRiskNumber.setEnabled(true);
					btnGenerteQuery.setEnabled(true);
					break;
				case Coaseguradores:
					initialDateChooser.setEnabled(true);
					txtPolicyNumber.setEnabled(true);
					btnGenerteQuery.setEnabled(true);
					break;
				case Coberturas:
					initialDateChooser.setEnabled(true);
					txtPolicyNumber.setEnabled(true);
					txtRiskNumber.setEnabled(true);
					btnGenerteQuery.setEnabled(true);
					break;
				case Direcciones:
					txtIdentification.setEnabled(true);
					btnGenerteQuery.setEnabled(true);
					break;
				case Terminos:
					txtHomologation.setEnabled(true);
					initialDateChooser.setEnabled(true);
					txtPolicyNumber.setEnabled(true);
					txtRiskNumber.setEnabled(true);
					btnGenerteQuery.setEnabled(true);
					break;
				case ConsultasComunes:
					initialDateChooser.setEnabled(true);
					txtPolicyNumber.setEnabled(true);
					txtRiskNumber.setEnabled(true);
					btnGenerteQuery.setEnabled(true);
					break;
			}
		}
  }//GEN-LAST:event_comboQueryTypePropertyChange

  private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed

		txtHomologation.setText("");
		txtIdentification.setText("");
		txtPolicyNumber.setText("");
		txtRiskNumber.setText("");
		outputTxt.setText("");

  }//GEN-LAST:event_btnClearActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(DialogGeneratePolicyQuery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(DialogGeneratePolicyQuery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(DialogGeneratePolicyQuery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(DialogGeneratePolicyQuery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				DialogGeneratePolicyQuery dialog = new DialogGeneratePolicyQuery(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnClear;
  private javax.swing.JButton btnGenerteQuery;
  private javax.swing.JComboBox<String> comboQueryType;
  private com.toedter.calendar.JDateChooser initialDateChooser;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTextArea outputTxt;
  private javax.swing.JTextField txtHomologation;
  private javax.swing.JTextField txtIdentification;
  private javax.swing.JTextField txtPolicyNumber;
  private javax.swing.JTextField txtRiskNumber;
  // End of variables declaration//GEN-END:variables
}
