cuwyApp.controller('lp24hCtrl', [ '$scope', '$http', function ($scope, $http) {
	var today = new Date()
	$scope.prescribeStartHour = today.getHours();
	$scope.numberOfChange = 0;
	$scope.drug1sList = drug1sList;
	$scope.parameters = parameters;
	if(!$scope.parameters.id)
		$scope.parameters.id = 0;

	getDayHoursEmpty = function(){
		var dayHours = [];
		for(var i=0;i<24;i++) dayHours.push(null);
		return dayHours;
	}

	getDayHours = function(){
		var dayHours = [];
		for(var i=0;i<24;i++) dayHours.push(i);
		return dayHours;
	}
	$scope.dayHours = getDayHours();
	$scope.config = config;
	$scope.siteMap = config.siteMap.siteMaps[2];
	$scope.tasksInDay = [];
	for(var ii=0;ii<19;ii++){
		$scope.tasksInDay.push({i:ii,isCollapsed:false});
	}

	$http({
		method : 'GET',
		url : '/read/prescribe_'+$scope.parameters.id
	}).success(function(data, status, headers, config) {
		$scope.prescribes = data;
		if(null == $scope.prescribes.tasks)
			$scope.prescribes.tasks = [];
		console.log($scope.prescribes);
	}).error(function(data, status, headers, config) {
	});

	$scope.getLp24hourStr = function(dayHour){
		var lp24hour = getLp24hour(dayHour);
		return (lp24hour>9?'':'0')+lp24hour;
	}
	getLp24hour= function(dayHour){
		var lp24hour = dayHour + $scope.config.startHour24lp;
		lp24hour = lp24hour>23?lp24hour-24:lp24hour;
		return lp24hour;
	}

	$scope.isMinus = function(taskInDayIndex, $index){
		if(!$scope.prescribes || !$scope.prescribes.tasks[taskInDayIndex]
		|| !$scope.prescribes.tasks[taskInDayIndex].times
		)
			return false;
		var lp24hour = getLp24hour($index);
		var isMinus = ('-' == $scope.prescribes.tasks[taskInDayIndex].times.hours[lp24hour]);
		return isMinus;
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
	$scope.useHour = function(taskInDay, taskInDayIndex, $index){
		if(taskInDay.isCollapsed){
			var hour =  getLp24hour($index);
			var editedDrug = $scope.prescribes.tasks[taskInDay.i];
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
		}
	}

	$scope.selectMultiple = function($index){
		console.log("selectMultiple");
		console.log($scope.prescribes.tasks);
		if(null == $scope.prescribes.tasks[$index]){
			$scope.prescribes.tasks[$index] = {};
		}
		$scope.prescribes.tasks[$index].selectMultiple = !$scope.prescribes.tasks[$index].selectMultiple;
	}
	$scope.openPrescribeDrugDialog = function(taskInDay, $index){
		var oldCollapsed = taskInDay.isCollapsed;
		$($scope.tasksInDay).each(function () {
			this.isCollapsed = false;
		} );
		taskInDay.isCollapsed = !oldCollapsed;
		$scope.editedPrescribeDrug =  $scope.prescribes.tasks[$index];
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

//----------------drug document----------------------------
	$scope.newDrugDocumentDose = {};
	$scope.addNewDrugDocumentDose = function(){
		$scope.newDrugDocumentDose.DOSE_ID = $scope.drugDocument.localIdSequence++;
		if(null == $scope.drugDocument.doses)
			$scope.drugDocument.doses = [];
		console.log($scope.drugDocument);
		$scope.drugDocument.doses.push($scope.newDrugDocumentDose);
		$scope.addDoseToPrescribeDrug($scope.newDrugDocumentDose);
		saveDrugDocument();
		$scope.newDrugDocumentDose = {};
		$scope.numberOfChange++;
	};

	saveDrugDocument = function(){
		console.log("--------devPost-----------");
		console.log($scope.drugDocument);
		console.log($scope.drugDocument.localIdSequence);
		$http({
			method : 'POST',
			data : $scope.drugDocument,
			url : "/save/drug"
		}).success(function(data, status, headers, config){
			$scope.drugDocument = data;
			console.log($scope.drugDocument);
			console.log($scope.drugDocument.localIdSequence);
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}

//---------------------drug document---------------------END-------

	$scope.addDoseToPrescribeDrug = function(doseToPrescribe){
		$scope.editedPrescribeDrug.dose = doseToPrescribe;
		$scope.numberOfChange++;
	}
	insertDrugToTask = function(drug, position){
		var addNull = position - $scope.prescribes.tasks.length;
		if(addNull > 0){
			for (i = 0; i < addNull; i++) {
				$scope.prescribes.tasks.push(null);
			}
			$scope.prescribes.tasks.push(drug);
		}else if(null == $scope.prescribes.tasks[position]){
			$scope.prescribes.tasks[position] = drug;
		}else{
			$scope.prescribes.tasks[position] = drug;
		}
		$scope.numberOfChange++;
	}
	$scope.drugToTask = function(drug, taskInDay){
		var position = taskInDay.i;
		insertDrugToTask(drug, position);
		taskInDay.dialogTab = "dose";
		readDrugDocument(drug);
		$scope.editedPrescribeDrug =  $scope.prescribes.tasks[position];
	}

	$scope.devPost = function(){
		$scope.prescribes.editPrescribesName = false;
		$($scope.tasksInDay).each(function () {
			this.isCollapsed = false;
		} );
		$http({
			method : 'POST',
			data : $scope.prescribes,
			url : "/save/prescribes"
		}).success(function(data, status, headers, config){
			$scope.prescribes = data;
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}

	$scope.saveNewDrug = function(seekDrug, taskInDay){
		$http({
			method : 'POST',
			data : {"DRUG_NAME":seekDrug},
			url : '/saveNewDrug'
		}).success(function(data, status, headers, config){
			$scope.drug1sList = data;
			var newDrug = $scope.drug1sList[$scope.drug1sList.length-1];
			$scope.drugToTask(newDrug, taskInDay);
		}).error(function(data, status, headers, config) {
			$scope.error = data;
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

	//--------------context menu------------------------
	contextMenuPaste = function($itemScope){
		$http({
			method : 'GET',
			url : '/session/paste'
		}).success(function(data, status, headers, config) {
			console.log(data);
			console.log(data.selectMultiple);
			if(data.selectMultiple && data.tasks){
				console.log($itemScope.taskInDay);
				var position = $itemScope.taskInDay.i;
				$(data.tasks).each(function () {
					if(this.selectMultiple){
						console.log(this);
						insertDrugToTask(this, position++);
					}
				});
			}else{
				var drug = data;
				$scope.drugToTask(drug, $itemScope.taskInDay);
			}
		}).error(function(data, status, headers, config) {
		});
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
	$scope.menuPrescribe = [
		['<span class="glyphicon glyphicon-edit"></span> Корекція', function ($itemScope) {
			$itemScope.prescribes.editPrescribesName = !$itemScope.prescribes.editPrescribesName;
		}]
	];
	$scope.menuTasksAll = [
		['<i class="fa fa-copy"></i> Копіювати', function ($itemScope) { contextMenuCopy($itemScope.prescribes); }],
		['<i class="fa fa-paste"></i> Вставити', function ($itemScope) { 
			$http({
				method : 'GET',
				url : '/session/paste'
			}).success(function(data, status, headers, config) {
				if($scope.prescribes.tasks.length == 0){
					$scope.prescribes.tasks = data.tasks;
					$scope.numberOfChange += $scope.prescribes.tasks.length;
				}
			}).error(function(data, status, headers, config) {
			});
		}]
	];
	$scope.menuTask = [
		['<i class="fa fa-copy"></i> Копіювати', function ($itemScope) { 
			var drug = $itemScope.prescribes.tasks[$itemScope.$index];
			console.log(drug);
			console.log(drug.selectMultiple);
			if(drug.selectMultiple){
				$itemScope.prescribes.selectMultiple = true;
				contextMenuCopy($itemScope.prescribes); 
			}else{
				contextMenuCopy(drug); 
			}
		}],
		['<i class="fa fa-paste"></i> Вставити', function ($itemScope) { contextMenuPaste($itemScope); }],
		null,
		['<span class="glyphicon glyphicon-plus"></span> Додати строчку', function ($itemScope) {
			$itemScope.prescribes.tasks.splice($itemScope.$index, 0, null);
			$scope.numberOfChange++;
		}],
		['<span class="glyphicon glyphicon-remove"></span> Видалити', function ($itemScope) {
			var isMultipleSelect = false;
			for(var i=$itemScope.prescribes.tasks.length-1;i>=0;i--){
				if($itemScope.prescribes.tasks[i] && $itemScope.prescribes.tasks[i].selectMultiple){
					$itemScope.prescribes.tasks.splice(i, 1);
					isMultipleSelect = true;
					$scope.numberOfChange++;
				}
			}
			if(!isMultipleSelect){
				$itemScope.prescribes.tasks.splice($itemScope.$index, 1);
				$scope.numberOfChange++;
			}
		}],
		null,
		['<span class="glyphicon glyphicon-arrow-up"></span> Догори', function ($itemScope) {
			moveMinus($itemScope.prescribes.tasks, $itemScope.$index);
		}],
		['<span class="glyphicon glyphicon-arrow-down"></span> Донизу', function ($itemScope) {
			movePlus($itemScope.prescribes.tasks, $itemScope.$index + 1);
		}],
		null,
		['<i class="fa fa-reply-all"></i> скасувати вибір', function ($itemScope) {
			$($itemScope.prescribes.tasks).each(function () {
				this.selectMultiple = false;
			});
		}]
	];

} ] );
