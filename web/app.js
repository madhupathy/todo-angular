/**
 * Created by mohit on 11/8/13. 
 */
var app = angular.module("ToDoApp", ["ngResource"]).
    config(function($routeProvider) {
        $routeProvider
            .when("/", {
                controller: ListCtrl, templateUrl:'list.html'
            })
            .when("/new", {
                controller: CreateCtrl, templateUrl:'details.html'
            })
            .when("/edit/:editId", {
                controller: EditCtrl, templateUrl:'details.html'
            })
            .otherwise({ redirectTo: "/" }); 
    }); 

app.factory('Todo', function($resource) { 
    return $resource('api.jsp', {id: '@id'}, {update: {method : 'GET'}});
});

var EditCtrl = function ($scope, $location, $routeParams, $filter, Todo) {
	$scope.action = "Update";
	var id = $routeParams.editId;
	var todo = Todo.get({action:"getTodo", id: id}, function(u, getResponseHeaders) {
		todo.DueDate = $filter('date')(todo.dueDateLong, 'MM/dd/yyyy')
		$scope.todo = todo;
	}); 
	
	$scope.save = function() {
		
	};
};
var CreateCtrl = function($scope, $location, $routeParams, Todo) {
	$scope.action = "Add";
	$scope.save = function() {
		Todo.query({ action: "save", todo: JSON.stringify(this.todo)}, function() {
			$location.path("/");
		}); 
	};
};

var ListCtrl = function($scope, $location, Todo) {

    $scope.search = function() {
        Todo.query({
                sort:$scope.sort_order,
                desc:$scope.is_desc,
                limit: $scope.limit,
                offset: $scope.offset,
                q:$scope.query
        },
        function(data) {
        	$scope.more = data.length === 10;
            $scope.todos = data;
        });
    };

    $scope.sort = function(col) {
        if ($scope.sort_order === col) {
            $scope.is_desc = !$scope.is_desc;
        } else {
        	$scope.sort_order = col;
        	$scope.is_desc = false;
        }
        $scope.sort_order = col;
        $scope.search();
    }
    
    $scope.show_more =function() {
        $scope.offset += $scope.limit;
        $scope.search();
    }
    
    $scope.has_more = function() {
    	return $scope.more;
    }
    
    $scope.reset = function() {
        $scope.limit = 10;
        $scope.offset = 0;
        $scope.more = false;
        $scope.search();    	
    }
    $scope.deleteToDo = function() {
    	var id = this.todo.Id; 
    	Todo.query({ id : id , action : "delete"}, function() {
    		$('#todo_' + id).fadeOut();
    	});
    }
    $scope.sort_order = "Priority";
    $scope.is_desc = false;
    $scope.reset();

}