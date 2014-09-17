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
		System.out.println("------CuwyCpoeHolDb2-------");
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:h2:file:~/01_curepathway/h2-db-server/cuwy-cpoe-hol1");
		dataSource.setUsername("sa");
//		dataSource.setPassword("");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		System.out.println("------CuwyCpoeHolDb2-------"+jdbcTemplate);
		testDb1();
	}
	
	public List<Map<String, Object>> drug1sList() {
		String sql = "SELECT * FROM drug1";
		System.out.println("\n"+sql);
		List<Map<String, Object>> drug1sList = jdbcTemplate.queryForList(sql);
		return drug1sList;
	}
	
	public void testDb1() {
		//create table drug1 (drug_id int PRIMARY KEY auto_increment, drug_name varchar(50) NOT NULL UNIQUE);
		String sql = "SELECT * FROM drug1";
		System.out.println("\n"+sql);
		List<Map<String, Object>> testDbSelect1
			= jdbcTemplate.queryForList(sql);
		System.out.println(testDbSelect1);
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
	
}
