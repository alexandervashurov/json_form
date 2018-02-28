pipeline {
	agent any
	stages {
	    stage('Clone project') {
	    	steps {
	        	git 'https://github.com/temnovochka/jsonvalidator'
	        	echo 'Clone project executed'
	        }
	    }
	    stage('Building') {
	    	steps {
		        sh './gradlew docker'
		        echo 'Building executed'
		    }
	    }
	}
}