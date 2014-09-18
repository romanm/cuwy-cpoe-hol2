package hello;

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

	public int updatePatient(Map<String, Object> patientToUpdate) {
		String patientName = (String) patientToUpdate.get("PATIENT_NAME");
		Integer patientId = (Integer) patientToUpdate.get("PATIENT_ID");
		int update = this.jdbcTemplate.update("update patient1 set patient_name = ? where patient_id = ?",
				patientName, patientId);
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
