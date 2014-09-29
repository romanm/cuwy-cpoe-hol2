cuwyApp.controller('patientLp24hCtrl', [ '$scope', '$http', function ($scope, $http) {
	console.log("patientLp24hCtrl");
	$scope.numberOfChange = 0;
	$scope.drug1sList = drug1sList;
	$scope.config = config;
	$scope.siteMap = config.siteMap.siteMaps[4];
	$scope.parameters = parameters;
	getDayHours = function(){
		var dayHours = [];
		for(var i=0;i<24;i++) dayHours.push(i);
		return dayHours;
	}
	$scope.dayHours = getDayHours();
	getTasksInDay = function(){
		var tasksInDay = [];
		for(var ii=0;ii<19;ii++){
			tasksInDay.push({i:ii,isCollapsed:false});
		}
		return tasksInDay;
	}
	$scope.tasksInDay = getTasksInDay();
	for(var ii=0;ii<19;ii++){
		$scope.tasksInDay.push({i:ii,isCollapsed:false});
	}

	$http({
		method : 'GET',
		url : '/read/patient_'+$scope.parameters.id
	}).success(function(data, status, headers, config) {
		$scope.patient = data;
		if(null == $scope.patient.prescribesHistory){
			$scope.patient.prescribesHistory = [];
			$scope.newPrescribes();
		}
		console.log($scope.patient);
	}).error(function(data, status, headers, config) {
	});

	$scope.newPrescribes = function(){
		console.log("---newPrescribes------------------");
		var today = new Date();
		var prescrebeHistory = {date:today, prescribes:{tasks:[]}}
		prescrebeHistory.tasksInDay = getTasksInDay();
		$scope.patient.prescribesHistory.splice(0, 0, prescrebeHistory);
	}
	$scope.savePatient = function(){
		console.log("--------devPost-----------");
		$http({
			method : 'POST',
			data : $scope.patient,
			url : "/save/patient"
		}).success(function(data, status, headers, config){
			console.log(data);
			$scope.prescribes = data;
			$scope.numberOfChange = 0;
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}


	$scope.open = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened = true;
	};

	$scope.dateOptions = {
		formatYear: 'yyyy',
		startingDay: 1
	};

$scope.openPrescribeDrugDialog = function(taskInDay, taskInDayIndex, prescribeHistory){
	var oldCollapsed = taskInDay.isCollapsed;
	$($scope.tasksInDay).each(function () {
		this.isCollapsed = false;
	} );
	taskInDay.isCollapsed = !oldCollapsed;
	$scope.editedPrescribeDrug =  prescribeHistory.prescribes.tasks[taskInDayIndex];
	if(null == $scope.editedPrescribeDrug || null == $scope.editedPrescribeDrug.DRUG_NAME){
		taskInDay.dialogTab = "drug";
	}else{
		taskInDay.dialogTab = "dose";
	}

	if($scope.editedPrescribeDrug && $scope.editedPrescribeDrug.DRUG_ID){
		readDrugDocument($scope.editedPrescribeDrug);
	}
}

readDrugDocument = function(drug){
	$http({
		method : 'GET',
		url : '/read/drug_'+drug.DRUG_ID
	}).success(function(data, status, headers, config) {
		$scope.drugDocument = data;
	}).error(function(data, status, headers, config) {
		$scope.drugDocument = null;
	});
}

$scope.saveNewDrug = function(seekDrug){
	$http({
		method : 'POST',
		data : {"DRUG_NAME":seekDrug},
		url : '/saveNewDrug'
	}).success(function(data, status, headers, config){
		$scope.drug1sList = data;
	}).error(function(data, status, headers, config) {
		$scope.error = data;
	});
}

$scope.drugToTask = function(drug, $itemScope){
	var position = $itemScope.taskInDay.i;
	insertDrugToTask(drug, position, $itemScope.$parent.prescribeHistory);
	taskInDay.dialogTab = "dose";
	readDrugDocument(drug);
	$scope.editedPrescribeDrug =  $itemScope.$parent.prescribeHistory.prescribes.tasks[position];
}

insertDrugToTask = function(drug, position, prescribeHistory){
	var addNull = position - prescribeHistory.prescribes.tasks.length;
	if(addNull > 0){
		for (i = 0; i < addNull; i++) {
			prescribeHistory.prescribes.tasks.push(null);
		}
		prescribeHistory.prescribes.tasks.push(drug);
	}else if(null == prescribeHistory.prescribes.tasks[position]){
		prescribeHistory.prescribes.tasks[position] = drug;
	}else{
		prescribeHistory.prescribes.tasks[position] = drug;
	}
	console.log(prescribeHistory.prescribes);
	$scope.numberOfChange++;
}

$scope.addDoseToPrescribeDrug = function(doseToPrescribe){
	console.log(doseToPrescribe);
	$scope.editedPrescribeDrug.dose = doseToPrescribe;
	console.log($scope.editedPrescribeDrug);
	$scope.numberOfChange++;
}

$scope.useHour = function(taskInDay, taskInDayIndex, dayHourIndex, prescribeHistory){
	if(taskInDay.isCollapsed){
		var hour =  getLp24hour(dayHourIndex);
		var editedDrug = prescribeHistory.prescribes.tasks[taskInDay.i];
		if(!editedDrug.times){
			editedDrug.times = {};
			editedDrug.times.hours = getDayHoursEmpty();
		}
		if(!editedDrug.times.hours[hour]){
			editedDrug.times.hours[hour] = "-";
		}else{
			editedDrug.times.hours[hour] = null;
		}
		$scope.numberOfChange++;
		taskInDay.dialogTab = "times";
		console.log(editedDrug);
	}
}

getLp24hour= function(dayHour){
	var lp24hour = dayHour + $scope.config.startHour24lp;
	lp24hour = lp24hour>23?lp24hour-24:lp24hour;
	return lp24hour;
}

getDayHoursEmpty = function(){
	var dayHours = [];
	for(var i=0;i<24;i++) dayHours.push(null);
	return dayHours;
}

$scope.isMinus = function(taskInDayIndex, dayHourIndex, prescribeHistory){
	if(!prescribeHistory.prescribes || !prescribeHistory.prescribes.tasks[taskInDayIndex]
	|| !prescribeHistory.prescribes.tasks[taskInDayIndex].times
	)
		return false;
	var lp24hour = getLp24hour(dayHourIndex);
	var isMinus = ('-' == prescribeHistory.prescribes.tasks[taskInDayIndex].times.hours[lp24hour]);
	return isMinus;
}

$scope.getLp24hourStr = function(dayHour){
	var lp24hour = getLp24hour(dayHour);
	return (lp24hour>9?'':'0')+lp24hour;
}
getLp24hour= function(dayHour){
	var lp24hour = dayHour + $scope.config.startHour24lp;
	lp24hour = lp24hour>23?lp24hour-24:lp24hour;
	return lp24hour;
}

$scope.getHoures2prescribe = function(drugForEdit){
	var houres2prescribe = [];
	if(drugForEdit && drugForEdit.times)
	angular.forEach(drugForEdit.times.hours, function(value, key){
		if(value){
			houres2prescribe.push(key);
		}
	} );
	return houres2prescribe;
}
$scope.prescribeHoursLeft = function(drugForEdit){
	var houres2prescribe = $scope.getHoures2prescribe(drugForEdit);
	angular.forEach(houres2prescribe, function(hourValue, key){
		$scope.prescribeHourLeft(drugForEdit, hourValue);
	});
}
$scope.prescribeHourLeft = function(drugForEdit, hour){
	moveMinus(drugForEdit.times.hours, hour);
}
$scope.prescribeHourRight = function(drugForEdit, hour){
	movePlus(drugForEdit.times.hours, hour + 1);
}
$scope.prescribeHoursRight = function(drugForEdit){
	var houres2prescribe = $scope.getHoures2prescribe(drugForEdit);
	angular.forEach(houres2prescribe, function(hourValue, key){
		$scope.prescribeHourRight(drugForEdit, hourValue);
	});
}

moveTo = function(arrayToSort, indexFrom, indexTo){
	var el = arrayToSort.splice(indexFrom, 1);
	arrayToSort.splice(indexTo, 0, el[0]);
	$scope.numberOfChange++;
}

moveUp = function(arrayToSort, index){
	moveTo(arrayToSort, index,index-1);
}

movePlus = function(arrayToSort, index){
	if(index < arrayToSort.length){
		moveUp(arrayToSort, index);
	}else{
		moveTo(arrayToSort, index-1,0);
	}
}
moveMinus = function(arrayToSort, index){
	if(index > 0){
		moveUp(arrayToSort, index);
	}else{
		moveTo(arrayToSort, 0, arrayToSort.length-1);
	}
}
contextMenuCopy = function(copyObject){
	$http({
		method : 'POST',
		data : copyObject,
		url : "/session/copy"
	}).success(function(data, status, headers, config){
	}).error(function(data, status, headers, config) {
		$scope.error = data;
	});
}

$scope.menuTask = [
	['<i class="fa fa-copy"></i> Copy', function ($itemScope) { 
		var drug = $itemScope.$parent.prescribeHistory.prescribes.tasks[$itemScope.$index];
		console.log(drug);
		console.log(drug.selectMultiple);
		if(drug.selectMultiple){
			$itemScope.$parent.prescribeHistory.prescribes.selectMultiple = true;
			contextMenuCopy($itemScope.$parent.prescribeHistory.prescribes); 
		}else{
			contextMenuCopy(drug); 
		}
	}],
	['<i class="fa fa-paste"></i> Paste', function ($itemScope) { contextMenuPaste($itemScope); }],
	null,
	['<span class="glyphicon glyphicon-plus"></span> Додати строчку', function ($itemScope) {
		$itemScope.$parent.prescribeHistory.prescribes.tasks.splice($itemScope.$index, 0, null);
		$scope.numberOfChange++;
	}],
	['<span class="glyphicon glyphicon-remove"></span> Видалити', function ($itemScope) {
		$itemScope.$parent.prescribeHistory.prescribes.tasks.splice($itemScope.$index, 1);
		$scope.numberOfChange++;
	}],
	null,
	['<span class="glyphicon glyphicon-arrow-up"></span> Догори', function ($itemScope) {
		moveMinus($itemScope.$parent.prescribeHistory.prescribes.tasks, $itemScope.$index);
	}],
	['<span class="glyphicon glyphicon-arrow-down"></span> Донизу', function ($itemScope) {
		movePlus($itemScope.$parent.prescribeHistory.prescribes.tasks, $itemScope.$index + 1);
	}],
	null,
	['<i class="fa fa-reply-all"></i> скасувати вибір', function ($itemScope) {
		$($itemScope.$parent.prescribeHistory.prescribes.tasks).each(function () {
			this.selectMultiple = false;
		});
	}]
];
$scope.menuTasksAll = [
	['<i class="fa fa-copy"></i> Copy', function ($itemScope) { 
		contextMenuCopy($itemScope.prescribeHistory.prescribes); 
	}],
	['<i class="fa fa-paste"></i> Paste', function ($itemScope) { 
		$http({
			method : 'GET',
			url : '/session/paste'
		}).success(function(data, status, headers, config) {
			console.log($itemScope.prescribeHistory.prescribes);
			console.log($itemScope.prescribeHistory.prescribes.tasks.length);
			if($itemScope.prescribeHistory.prescribes.tasks.length == 0){
				$itemScope.prescribeHistory.prescribes.tasks = data.tasks;
				$scope.numberOfChange += $itemScope.prescribeHistory.prescribes.tasks.length;
			}else{
				console.log(data.tasks);
				console.log(data.tasks.length);
				if(data.tasks.length + $itemScope.prescribeHistory.prescribes.tasks.length < 19){
					console.log("add");
					$itemScope.prescribeHistory.prescribes.tasks.push
					.apply($itemScope.prescribeHistory.prescribes.tasks, data.tasks);
				}
			}
		}).error(function(data, status, headers, config) {
		});
	}]
];

contextMenuPaste = function($itemScope){
	$http({
		method : 'GET',
		url : '/session/paste'
	}).success(function(data, status, headers, config) {
		console.log(data);
		if(data.selectMultiple && data.tasks){
			var position = $itemScope.taskInDay.i;
			$(data.tasks).each(function () {
				if(this.selectMultiple){
					insertDrugToTask(this, position++, $itemScope.$parent.prescribeHistory);
				}
			});
		}else{
			var drug = data;
			$scope.drugToTask(drug, $itemScope);
		}
	}).error(function(data, status, headers, config) {
	});
}

$scope.selectMultiple = function(taskInDayIndex, prescribeHistory){
	console.log("selectMultiple");
	console.log(prescribeHistory.prescribes.tasks);
	if(null == prescribeHistory.prescribes.tasks[taskInDayIndex]){
		prescribeHistory.prescribes.tasks[taskInDayIndex] = {};
	}
	prescribeHistory.prescribes.tasks[taskInDayIndex].selectMultiple = !prescribeHistory.prescribes.tasks[taskInDayIndex].selectMultiple;
}

}]);

