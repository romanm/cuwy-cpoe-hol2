var historyFile = window.location.pathname.replace(/history_/,"history_id_");

cuwyApp.controller('HistoryCtrl', [ '$scope', '$http', '$filter', function ($scope, $http, $filter) {

	$scope.operationTree = operationTree;
	$scope.departmentsHol = configHol.departments;

	$scope.patientEditing = {}
	$scope.patient = {
		name: 'Patient name',
		history_no: parameters.hno,
		patientHistory: null
	};

	$http({
		method : 'GET',
		url : historyFile
	}).success(function(data, status, headers, config) {
		$scope.patientHistory = data;
	}).error(function(data, status, headers, config) {
	});

	$scope.calculateAge = function(birthDateStr) {
		if(!birthDateStr)
			return 0;
		var birthDate = new Date(birthDateStr)
			birthYear = birthDate.getFullYear(),
			birthMonth = birthDate.getMonth(),
			birthDay = birthDate.getDate();
		var todayDate = new Date(),
			todayYear = todayDate.getFullYear(),
			todayMonth = todayDate.getMonth(),
			todayDay = todayDate.getDate(),
			age = todayYear - birthYear; 

		if (todayMonth < birthMonth)
		{
			age--;
		}else if (birthMonth === todayMonth && todayDay < birthDay)
		{
			age--;
		}
		return age;
	};

	$scope.menuMovePatient = [
		['<span class="glyphicon glyphicon-transfer"></span> Переведеня', function ($itemScope) {
			console.debug('Move');
			console.debug($itemScope);
			$scope.patientHistory.collapseMovePatient = !$scope.patientHistory.collapseMovePatient
		}]
	];

} ] );
