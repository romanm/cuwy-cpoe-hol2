package dev;

import java.util.List;
import java.util.Map;

import org.h2.Driver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

@Component("cuwyCpoeHolDb2")
public class CuwyCpoeHolDb2 {

	private JdbcTemplate jdbcTemplate;

	public CuwyCpoeHolDb2() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:h2:file:~/01_curepathway/h2-db-server/cuwy-cpoe-hol1");
		dataSource.setUsername("sa");
//		dataSource.setPassword("");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		System.out.println("------CuwyCpoeHolDb2-------"+jdbcTemplate);
	}
	
	public int updatePrescribeOrder(Map<String, Object> prescribeToUpdate) {
		String prescribeName = (String) prescribeToUpdate.get("PRESCRIBE_NAME");
		Integer prescribeId = (Integer) prescribeToUpdate.get("PRESCRIBE_ID");
		int update = this.jdbcTemplate.update("update prescribe1 set prescribe_name = ? where prescribe_id = ? ",
			prescribeName, prescribeId);
		return update;
	}
	public int removePrescribeOrder(Map<String, Object> removePatient) {
		Integer prescribeId = (Integer) removePatient.get("PRESCRIBE_ID");
		int update = jdbcTemplate.update("delete from prescribe1 where prescribe_id = ?",prescribeId);
		return update;
	}
	public Map<String, Object> newPrescribe(Map<String, Object> newPrescribeOrder) {
		Object prescribeName = newPrescribeOrder.get("PRESCRIBE_NAME");
		jdbcTemplate.update("INSERT INTO prescribe1 (prescribe_name) VALUES (?)",prescribeName);
		String sqlSelectPatient1 = "SELECT prescribe_id FROM prescribe1 WHERE prescribe_name = ? ORDER BY prescribe_id DESC LIMIT 1";
		List<Map<String, Object>> prescribe1sList = jdbcTemplate.queryForList(sqlSelectPatient1, prescribeName);
		Integer newPrescribeId = (Integer) prescribe1sList.get(0).get("PRESCRIBE_ID");
		newPrescribeOrder.put("PRESCRIBE_ID", newPrescribeId);
		return newPrescribeOrder;
	}
	public Map<String, Object> readPatient(Integer id) {
		String sql = "SELECT * FROM patient1 WHERE patient_id = ?";
		System.out.println("\n"+sql.replaceFirst("\\?", ""+id));
		List<Map<String, Object>> patient1sList = jdbcTemplate.queryForList(sql, id);
		Map<String, Object> map = patient1sList.get(0);
		return map;
	}
	public Map<String, Object> readDrug(Integer id) {
		String sql = "SELECT * FROM drug1 WHERE drug_id = ?";
		System.out.println("\n"+sql.replaceFirst("\\?", ""+id));
		List<Map<String, Object>> prescribe1sList = jdbcTemplate.queryForList(sql, id);
		Map<String, Object> map = prescribe1sList.get(0);
		return map;
	}
	public Map<String, Object> readPrescribe(Integer id) {
		String sql = "SELECT * FROM prescribe1 WHERE prescribe_id = ?";
		System.out.println("\n"+sql.replaceFirst("\\?", ""+id));
		List<Map<String, Object>> prescribe1sList = jdbcTemplate.queryForList(sql, id);
		Map<String, Object> map = prescribe1sList.get(0);
		return map;
	}
	public List<Map<String, Object>> prescribe1sList() {
		String sql = "SELECT * FROM prescribe1 ";
		System.out.println("\n"+sql);
		List<Map<String, Object>> prescribe1sList = jdbcTemplate.queryForList(sql);
		return prescribe1sList;
	}

	public int updateProtocolOrder(Map<String, Object> patientToUpdate) {
		String patientName = (String) patientToUpdate.get("ORDER_NAME");
		Integer patientId = (Integer) patientToUpdate.get("ORDER_ID");
		int update = this.jdbcTemplate.update("update order1 set order_name = ?, order_type='protocol' where order_id = ? ",
				patientName, patientId);
		return update;
	}
	public int removeProtocolOrder(Map<String, Object> removePatient) {
		Integer patientId = (Integer) removePatient.get("ORDER_ID");
		int update = jdbcTemplate.update("delete from order1 where order_id = ?",patientId);
		return update;
	}
	public Map<String, Object> newProtocolOrder(Map<String, Object> newProtocolOrder) {
		Object patientName = newProtocolOrder.get("ORDER_NAME");
		jdbcTemplate.update("INSERT INTO order1 (order_name, order_type) VALUES (?, 'protocol')",patientName);
		String sqlSelectPatient1 = "SELECT order_id FROM order1 WHERE order_name = ? AND order_type = 'protocol' ORDER BY order_id DESC LIMIT 1";
		List<Map<String, Object>> patient1sList = jdbcTemplate.queryForList(sqlSelectPatient1, patientName);
		Integer newPatientId = (Integer) patient1sList.get(0).get("ORDER_ID");
		newProtocolOrder.put("ORDER_ID", newPatientId);
		return newProtocolOrder;
	}
	public List<Map<String, Object>> protocol1sList() {
		String sql = "SELECT order_id,order_name FROM order1 WHERE order_type ='protocol'";
		System.out.println("\n"+sql);
		List<Map<String, Object>> patient1sList = jdbcTemplate.queryForList(sql);
		return patient1sList;
	}

	public int updatePatient(Map<String, Object> patientToUpdate) {
		String patientName = (String) patientToUpdate.get("PATIENT_NAME");
		Boolean patientArchive = (Boolean) patientToUpdate.get("PATIENT_ARCHIVE");
		Integer patientId = (Integer) patientToUpdate.get("PATIENT_ID");
		int update = this.jdbcTemplate.update(
				"update patient1 set patient_name = ?, patient_archive = ? where patient_id = ?",
				patientName, patientArchive, patientId);
		return update;
	}
	public int removePatient(Map<String, Object> removePatient) {
		Integer patientId = (Integer) removePatient.get("PATIENT_ID");
		int update = jdbcTemplate.update("delete from patient1 where patient_id = ?",patientId);
		return update;
	}
	public Map<String, Object> newPatient(Map<String, Object> newPatient) {
		Object patientName = newPatient.get("PATIENT_NAME");
		jdbcTemplate.update("INSERT INTO patient1 (patient_name) VALUES (?)",patientName);
		String sqlSelectPatient1 = "SELECT patient_id FROM patient1 WHERE patient_name = ? limit 1";
		List<Map<String, Object>> patient1sList = jdbcTemplate.queryForList(sqlSelectPatient1, patientName);
		Integer newPatientId = (Integer) patient1sList.get(0).get("PATIENT_ID");
		newPatient.put("PATIENT_ID", newPatientId);
		return newPatient;
	}
	public List<Map<String, Object>> patient1sList() {
		String sql = "SELECT * FROM patient1";
		System.out.println("\n"+sql);
		List<Map<String, Object>> patient1sList = jdbcTemplate.queryForList(sql);
		return patient1sList;
	}

	public int updateDrug(Map<String, Object> drugToUpdate) {
		String drugName = (String) drugToUpdate.get("DRUG_NAME");
		Integer drugId = (Integer) drugToUpdate.get("DRUG_ID");
		int update = this.jdbcTemplate.update("update drug1 set drug_name = ? where drug_id = ?",
			drugName, drugId);
		return update;
	}
	public int removeDrug(Map<String, Object> removeDrug) {
		Integer drugId = (Integer) removeDrug.get("DRUG_ID");
		int update = jdbcTemplate.update("delete from drug1 where drug_id = ?",drugId);
		return update;
	}
	public Map<String, Object> newDrug(Map<String, Object> newDrug) {
		Object drugName = newDrug.get("DRUG_NAME");
		jdbcTemplate.update("INSERT INTO drug1 (drug_name) VALUES (?)",drugName);
		String sqlSelectDrug1 = "SELECT drug_id FROM drug1 WHERE drug_name = ? limit 1";
		List<Map<String, Object>> drug1sList = jdbcTemplate.queryForList(sqlSelectDrug1, drugName);
		Integer newDrugId = (Integer) drug1sList.get(0).get("DRUG_ID");
		newDrug.put("DRUG_ID", newDrugId);
		return newDrug;
	}
	public List<Map<String, Object>> drug1sList() {
		String sql = "SELECT * FROM drug1";
		System.out.println("\n"+sql);
		List<Map<String, Object>> drug1sList = jdbcTemplate.queryForList(sql);
		return drug1sList;
	}
}
