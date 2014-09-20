package dev;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

	@RequestMapping(value = "/saveNewProtocol", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> saveNewProtocol(@RequestBody Map<String, Object> newProtocol) {
		System.out.println("/saveNewProtocol");
		System.out.println(newProtocol);
		newProtocol = cuwyCpoeHolDb2.newProtocolOrder(newProtocol);
		System.out.println(newProtocol);
		List<Map<String, Object>> patient1sList = protocol1sList();
		return patient1sList;
	}
	@RequestMapping(value = "/removeProtocolOrder", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> removeProtocol(@RequestBody Map<String, Object> protocolOrderToRemove) {
		System.out.println("/removeProtocolOrder");
		System.out.println(protocolOrderToRemove);
		int removeProtocolId = cuwyCpoeHolDb2.removeProtocolOrder(protocolOrderToRemove);
		System.out.println(removeProtocolId);
		List<Map<String, Object>> patient1sList = protocol1sList();
		return patient1sList;
	}
	@RequestMapping(value = "/updateProtocol", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> updateProtocol(@RequestBody Map<String, Object> protocolToUpdate) {
		System.out.println("/removeProtocol");
		System.out.println(protocolToUpdate);
		int updateProtocol = cuwyCpoeHolDb2.updateProtocolOrder(protocolToUpdate);
		List<Map<String, Object>> patient1sList = protocol1sList();
		return patient1sList;
	}
	@RequestMapping(value = "/protocol1sList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> protocol1sList() {
		System.out.println("/protocol1sList");
		List<Map<String, Object>> patient1sList = cuwyCpoeHolDb2.protocol1sList();
		writeToJsDbFile("var protocolOrder1sList = ", patient1sList, protocolOrder1sListJsFileName);
		return patient1sList;
	}

	@RequestMapping(value = "/saveNewPatient", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> saveNewPatient(@RequestBody Map<String, Object> newPatient) {
		System.out.println("/saveNewPatient");
		System.out.println(newPatient);
		newPatient = cuwyCpoeHolDb2.newPatient(newPatient);
		System.out.println(newPatient);
		List<Map<String, Object>> patient1sList = patient1sList();
		return patient1sList;
	}
	@RequestMapping(value = "/removePatient", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> removePatient(@RequestBody Map<String, Object> patientToRemove) {
		System.out.println("/removePatient");
		System.out.println(patientToRemove);
		int removePatientId = cuwyCpoeHolDb2.removePatient(patientToRemove);
		System.out.println(removePatientId);
		List<Map<String, Object>> patient1sList = patient1sList();
		return patient1sList;
	}
	@RequestMapping(value = "/updatePatient", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> updatePatient(@RequestBody Map<String, Object> patientToUpdate) {
		System.out.println("/removePatient");
		System.out.println(patientToUpdate);
		int updatePatient = cuwyCpoeHolDb2.updatePatient(patientToUpdate);
		List<Map<String, Object>> patient1sList = patient1sList();
		return patient1sList;
	}
	@RequestMapping(value = "/patient1sList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> patient1sList() {
		System.out.println("/patient1sList");
		List<Map<String, Object>> patient1sList = cuwyCpoeHolDb2.patient1sList();
		writeToJsDbFile("var patient1sList = ", patient1sList, patient1sListJsFileName);
		return patient1sList;
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
	@RequestMapping(value = "/removeDrug", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> removeDrug(@RequestBody Map<String, Object> drugToRemove) {
		System.out.println("/removeDrug");
		System.out.println(drugToRemove);
		int removeDrugId = cuwyCpoeHolDb2.removeDrug(drugToRemove);
		System.out.println(removeDrugId);
		List<Map<String, Object>> drug1sList = drug1sList();
		return drug1sList;
	}
	@RequestMapping(value = "/updateDrug", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> updateDrug(@RequestBody Map<String, Object> drugToUpdate) {
		System.out.println("/removeDrug");
		System.out.println(drugToUpdate);
		int updateDrug = cuwyCpoeHolDb2.updateDrug(drugToUpdate);
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

	private String protocolOrder1sListJsFileName = "protocolOrder1sList.json.js";
	private String patient1sListJsFileName = "patient1sList.json.js";
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
	//--------------------
	@RequestMapping(value = "/dev/prescribes/save", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveDevPrescribes(
			@RequestBody HashMap<String, Object> prescribes) {
		System.out.println("/dev/prescribes/save");
		System.out.println(prescribes);
		return prescribes;
	}
	@RequestMapping(value = "/dev/prescribes", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> devPrescribes() {
		HashMap<String, Object> prescribes = new HashMap<String, Object>();
		prescribes.put("prescribes_name", "Some prescribes for some desease in some reanimation");
		ArrayList<Object> tasks = new ArrayList<Object>();
		HashMap<String, Object> drug = new HashMap<String, Object>();
		drug.put("drug_name", "Analgin");
		tasks.add(drug);
		drug = new HashMap<String, Object>();
		drug.put("drug_name", "Dimedrol");
		tasks.add(drug);
		prescribes.put("tasks", tasks);
		return prescribes;
	}
	//--------------------

}
