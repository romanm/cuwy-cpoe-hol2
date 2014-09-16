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
	
	public void testDb1() {
		//create table testtable4 (id int PRIMARY KEY auto_increment, name varchar(50) NOT NULL UNIQUE);
		String sql = "select * from testtable4";
		System.out.println("\n"+sql);
		List<Map<String, Object>> testDbSelect1
			= jdbcTemplate.queryForList(sql);
		System.out.println(testDbSelect1);
	}
	
}
