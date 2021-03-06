package dev;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class GreetingController {
	private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

	@Autowired
	private CuwyCpoeHolDb2 cuwyCpoeHolDb2;

	@RequestMapping(value = "/saveNewProtocol", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> saveNewProtocol(
			@RequestBody Map<String, Object> newProtocol) {
		System.out.println("/saveNewProtocol");
		System.out.println(newProtocol);
		newProtocol = cuwyCpoeHolDb2.newProtocolOrder(newProtocol);
		System.out.println(newProtocol);
		List<Map<String, Object>> protocol1sList = protocol1sList();
		return protocol1sList;
	}
	@RequestMapping(value = "/removeProtocolOrder", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> removeProtocol(@RequestBody Map<String, Object> protocolOrderToRemove) {
		System.out.println("/removeProtocolOrder");
		System.out.println(protocolOrderToRemove);
		int removeProtocolId = cuwyCpoeHolDb2.removeProtocolOrder(protocolOrderToRemove);
		System.out.println(removeProtocolId);
		List<Map<String, Object>> protocol1sList = protocol1sList();
		return protocol1sList;
	}
	@RequestMapping(value = "/updateProtocol", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> updateProtocol(@RequestBody Map<String, Object> protocolToUpdate) {
		System.out.println("/removeProtocol");
		System.out.println(protocolToUpdate);
		int updateProtocol = cuwyCpoeHolDb2.updateProtocolOrder(protocolToUpdate);
		List<Map<String, Object>> protocol1sList = protocol1sList();
		return protocol1sList;
	}
	@RequestMapping(value = "/protocol1sList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> protocol1sList() {
		System.out.println("/protocol1sList");
		List<Map<String, Object>> protocol1sList = cuwyCpoeHolDb2.protocol1sList();
		writeToJsDbFile("var protocolOrder1sList = ", protocol1sList, protocolOrder1sListJsFileName);
		return protocol1sList;
	}
	//------------------protocol-------------------END---

	//------------------prescribe----------------------
	@RequestMapping(value = "/saveNewPrescribe", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> saveNewPrescribe(
			@RequestBody Map<String, Object> newPrescribe) {
		System.out.println("/saveNewPrescribe");
		System.out.println(newPrescribe);
		newPrescribe = cuwyCpoeHolDb2.newPrescribe(newPrescribe);
		System.out.println(newPrescribe);
		List<Map<String, Object>> prescribe1sList = prescribe1sList();
		return prescribe1sList;
	}
	@RequestMapping(value = "/removePrescribeOrder", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> removePrescribe(
			@RequestBody Map<String, Object> prescribeToRemove) {
		System.out.println("/removePrescribeOrder");
		System.out.println(prescribeToRemove);
		int removePrescribeId = cuwyCpoeHolDb2.removePrescribeOrder(prescribeToRemove);
		System.out.println(removePrescribeId);
		List<Map<String, Object>> prescribe1sList = prescribe1sList();
		return prescribe1sList;
	}
	@RequestMapping(value = "/updatePrescribe", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> updatePrescribe(
			@RequestBody Map<String, Object> prescribeToUpdate) {
		System.out.println("/updatePrescribe");
		System.out.println(prescribeToUpdate);
		int updateProtocol = cuwyCpoeHolDb2.updatePrescribeOrder(prescribeToUpdate);
		Integer prescribeId = (Integer) prescribeToUpdate.get("PRESCRIBE_ID");
		Map<String, Object> readPrescribes = readPrescribes(prescribeId);
		String prescribeName = (String) prescribeToUpdate.get("PRESCRIBE_NAME");
		readPrescribes.put("PRESCRIBE_NAME", prescribeName);
		writeToJsonDbFile(readPrescribes, getPrescribeDbJsonName(prescribeId));
		List<Map<String, Object>> prescribe1sList = prescribe1sList();
		return prescribe1sList;
	}
	@RequestMapping(value = "/prescribe1sList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> prescribe1sList() {
		System.out.println("/prescribe1sList");
		List<Map<String, Object>> prescribe1sList = cuwyCpoeHolDb2.prescribe1sList();
		writeToJsDbFile("var prescribeOrder1sList = ", prescribe1sList, prescribeOrder1sListJsFileName);
		return prescribe1sList;
	}
	@RequestMapping(value = "/dev/prescribes/save", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> savePrescribes(
			@RequestBody HashMap<String, Object> prescribes) {
		System.out.println("/dev/prescribes/save");
		System.out.println(prescribes);
		writeToJsonDbFile(prescribes, fileNamePrescribes);
		return prescribes;
	}
	@RequestMapping(value = "/save/prescribes", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> savePrescribes(
			@RequestBody Map<String, Object> prescribes){
		Integer prescribeId = (Integer) prescribes.get("PRESCRIBE_ID");
		writeToJsonDbFile(prescribes, getPrescribeDbJsonName(prescribeId));
		cuwyCpoeHolDb2.updatePrescribeOrder(prescribes);
		prescribe1sList();
		return prescribes;
	}
	@RequestMapping(value="/read/prescribe_{prescribeId}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> readPrescribes(@PathVariable Integer prescribeId) {
		String fileNameWithPathAdd = getPrescribeDbJsonName(prescribeId);
		Map<String, Object> readJsonDbFile2map = readJsonDbFile2map(fileNameWithPathAdd);
		if(null == readJsonDbFile2map){
			readJsonDbFile2map = cuwyCpoeHolDb2.readPrescribe(prescribeId);
			writeToJsonDbFile(readJsonDbFile2map, fileNameWithPathAdd);
		}
		return readJsonDbFile2map;
	}
	@RequestMapping(value = "/dev/prescribes/read", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> readPrescribes(
			@RequestBody Map<String, Object> prescribeRead) {
		System.out.println(prescribeRead);
		Map<String, Object> readJsonDbFile2map = readJsonDbFile2map(fileNamePrescribes);
		Object prescribeName = readJsonDbFile2map.get("prescribes_name");
		System.out.println(prescribeName);
		System.out.println(readJsonDbFile2map.get("tasks"));
		return readJsonDbFile2map;
	}
	//------------------prescribe-------------------END---

	//------------------patient----------------------
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
	@RequestMapping(value="/read/patient_{patientId}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> readPatient(@PathVariable Integer patientId) {
		System.out.println("/read/patient_"+patientId);
		String fileNameWithPathAdd = getPatientDbJsonName(patientId);
		Map<String, Object> readJsonDbFile2map = readJsonDbFile2map(fileNameWithPathAdd);
		if(null == readJsonDbFile2map){
			readJsonDbFile2map = cuwyCpoeHolDb2.readPatient(patientId);
			writeToJsonDbFile(readJsonDbFile2map, fileNameWithPathAdd);
		}
		return readJsonDbFile2map;
	}
	@RequestMapping(value = "/save/patient", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> savePatient(
			@RequestBody Map<String, Object> patient){
		Integer patientId = (Integer) patient.get("PATIENT_ID");
		String fileNameWithPathAdd = getPatientDbJsonName(patientId);
		writeToJsonDbFile(patient, fileNameWithPathAdd);
		return patient;
	}
	//------------------patient---------------END-------

	//------------------drug----------------------
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
	@RequestMapping(value = "/save/drug", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveDrug(
			@RequestBody Map<String, Object> drug){
		Integer prescribeId = (Integer) drug.get("DRUG_ID");
		writeToJsonDbFile(drug, getDrugDbJsonName(prescribeId));
		return drug;
	}
	@RequestMapping(value="/read/drug_{drugId}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> readDrug(@PathVariable Integer drugId) {
		String fileNameWithPathAdd = getDrugDbJsonName(drugId);
		Map<String, Object> readJsonDbFile2map = readJsonDbFile2map(fileNameWithPathAdd);
		if(null == readJsonDbFile2map){
			readJsonDbFile2map = cuwyCpoeHolDb2.readDrug(drugId);
			writeToJsonDbFile(readJsonDbFile2map, fileNameWithPathAdd);
		}
		return readJsonDbFile2map;
	}
	//------------------drug----------------END------

	private String getPatientDbJsonName(Integer patientId) {
		return "patient/patient_"+ patientId+ ".json";
	}
	private String getPrescribeDbJsonName(Integer prescribeId) {
		return "prescribe/prescribe_"+ prescribeId+ ".json";
	}
	private String getDrugDbJsonName(Integer drugId) {
		return "drug/drug_"+ drugId+ ".json";
	}
	String fileNamePrescribes = "prescribes.json";
	private String prescribeOrder1sListJsFileName = "prescribeOrder1sList.json.js";
	private String protocolOrder1sListJsFileName = "protocolOrder1sList.json.js";
	private String patient1sListJsFileName = "patient1sList.json.js";
	private String drug1sListJsFileName = "drug1sList.json.js";
//	String applicationFolderPfad = "C:\\opt\\hol-vait\\";
//	String innerDbFolderPfad = "src\\main\\webapp\\db\\";
	String applicationFolderPfad = "/home/roman/Documents/01_curepathway/work3/cuwy-cpoe-hol2/";
	String innerDbFolderPfad = "src/main/webapp/db/";

	private void writeToJsDbFileLinie(String variable, Object objectForJson, String fileName) {
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
	private void writeToJsDbFile(String variable, Object objectForJson, String fileName) {
		File file = new File(applicationFolderPfad + innerDbFolderPfad + fileName);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writerWithDefaultPrettyPrinter = mapper.writerWithDefaultPrettyPrinter();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(variable.getBytes());
			writerWithDefaultPrettyPrinter.writeValue(fileOutputStream, objectForJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void writeToJsonDbFile(Object java2jsonObject, String fileName) {
		File file = new File(applicationFolderPfad + innerDbFolderPfad + fileName);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writerWithDefaultPrettyPrinter = mapper.writerWithDefaultPrettyPrinter();
		try {
			logger.warn(writerWithDefaultPrettyPrinter.writeValueAsString(java2jsonObject));
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			writerWithDefaultPrettyPrinter.writeValue(fileOutputStream, java2jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> readJsonDbFile2map(String fileName) {
		String pathToFile = applicationFolderPfad + innerDbFolderPfad + fileName;
		System.out.println(pathToFile);
		File file = new File(pathToFile);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> readJsonDbFile2map = null;// = new HashMap<String, Object>();
		try {
			readJsonDbFile2map = mapper.readValue(file, Map.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return readJsonDbFile2map;
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
	//---------prescribes-----------
	@RequestMapping(value = "/session/paste", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> sessionPaste(HttpSession session){
		System.out.println(session);
		Map<String, Object> copyObj = (Map<String, Object>) session.getAttribute("copyObj");
		System.out.println(copyObj);
		return copyObj;
	}
	@RequestMapping(value = "/session/copy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sessionCopy(
			@RequestBody Map<String, Object> copyObj, HttpSession session){
		System.out.println(copyObj);
		System.out.println(session);
		session.setAttribute("copyObj", copyObj);
		return copyObj;
	}


	//------------------development----------------------
	@RequestMapping(value = "/dev/prescribes/dummy", method = RequestMethod.GET)
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
		writeToJsonDbFile(prescribes, fileNamePrescribes);
		return prescribes;
	}
	//------------------development----------------END----

}
