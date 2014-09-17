package hello;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GreetingController {

	@Autowired
	private CuwyCpoeHolDb2 cuwyCpoeHolDb2;

	@RequestMapping(value = "/updateDrug", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> updateDrug(@RequestBody Map<String, Object> drugToUpdate) {
		System.out.println("/removeDrug");
		System.out.println(drugToUpdate);
		int updateDrug = cuwyCpoeHolDb2.updateDrug(drugToUpdate);
		List<Map<String, Object>> drug1sList = drug1sList();
		return drug1sList;
	}

	@RequestMapping(value = "/removeDrug", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> removeDrug(@RequestBody Map<String, Object> drugToRemove) {
		System.out.println("/removeDrug");
		System.out.println(drugToRemove);
		int removeDrugId = cuwyCpoeHolDb2.removeDrug(drugToRemove);
		System.out.println(removeDrugId);
		List<Map<String, Object>> drug1sList = drug1sList();
		return drug1sList;
	}

	@RequestMapping(value = "/saveNewDrug", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> saveNewDrug(@RequestBody Map<String, Object> newDrug) {
		System.out.println("/saveNewDrug");
		System.out.println(newDrug);
		newDrug = cuwyCpoeHolDb2.newDrug(newDrug);
		System.out.println(newDrug);
		List<Map<String, Object>> drug1sList = drug1sList();
		return drug1sList;
	}

	@RequestMapping(value = "/drug1sList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> drug1sList() {
		System.out.println("/drug1sList");
		List<Map<String, Object>> drug1sList = cuwyCpoeHolDb2.drug1sList();
		writeToJsDbFile("var drug1sList = ", drug1sList, drug1sListJsFileName);
		return drug1sList;
	}

	private String drug1sListJsFileName = "drug1sList.json.js";
	String applicationFolderPfad = "/home/roman/Documents/01_curepathway/work3/cuwy-cpoe-hol2/";
	String innerDbFolderPfad = "src/main/webapp/db/";
	private void writeToJsDbFile(String variable, Object objectForJson, String fileName) {
		File file = new File(applicationFolderPfad + innerDbFolderPfad + fileName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(variable.getBytes());
			mapper.writeValue(fileOutputStream, objectForJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final AtomicLong counter = new AtomicLong();
	private static final String template = "Hello, %s!";

	@RequestMapping("/greeting")
	public Greeting greeting(
			@RequestParam(value="name", required=false, defaultValue="World") 
			String name) {
		return new Greeting(counter.incrementAndGet(),
				String.format(template, name));
	}

}
