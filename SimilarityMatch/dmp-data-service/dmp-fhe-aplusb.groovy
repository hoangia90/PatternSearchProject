node {
	def mvnHome
	def bigpi_ips = ['192.168.1.200', '192.168.1.201' , '192.168.1.202' , '192.168.1.203' , '192.168.1.204' , '192.168.1.205']
	def bigpi_git = 'git@s19430717.onlinehome-server.info:bigpi-platform/dmp_platform/dmp_fhe/dmp-fhe-libraries/dmp-fhe-aplusb-service.git'
	//keep in the same order like bigpi_ips array
	def bigpi_services = ['dmp-fhe-aplusb.service']
	def bigpi_dir = '/opt/deployment/dmp/fhe'
	def bigpi_jar = 'dmp-fhe-aplusb-service.jar'
	def bigpi_cred = 'vinext_git_lab'
	
	stage('Preparation') { // for display purposes
	   // Get some code from a GitHub repository
	   checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: bigpi_cred, url: bigpi_git]]]
	   // Get the Maven tool.
	   // ** NOTE: This 'M3' Maven tool must be configured in
	   // **       in the global configuration.
	   mvnHome = tool 'M3'
	}
	
	stage ('Code analyse') {
		sh 'echo "Run some lints for analysis"'
	 }
	 stage ('Unit test') {
		sh 'echo "Tests will back"'
		sh "'/usr/bin/mvn' clean test -B -T 4"
	 }
 
	 stage('Build') {
	   // Run the maven build
	   if (isUnix()) {
		  sh "'/usr/bin/mvn' -Dmaven.test.skip=true clean package -B -T 4"
	   } else {
		  bat(/"${mvnHome}\bin\mvn" -Dmaven.test.skip=true clean package/)
	   }
	}
	
	stage('Enable Daemon Service') {
		for(int i = 0; i < bigpi_ips.size(); i++) {
			parallel {
				stage('Enable Daemon on Bigpi@'+bigpi_ips[i]) {
					if ( fileExists("./${bigpi_services[0]}") ) {
						 sh "ssh bigpi@${bigpi_ips[i]} 'sudo mkdir -p ${bigpi_dir}'"
						 sh "ssh bigpi@${bigpi_ips[i]} 'sudo chown -R bigpi:bigpi ${bigpi_dir}'"
						 sh "scp ./${bigpi_services[0]} bigpi@${bigpi_ips[i]}:${bigpi_dir}"
						 sh "ssh bigpi@${bigpi_ips[i]} 'sudo cp -f ${bigpi_dir}/${bigpi_services[0]} /etc/systemd/system'"
						 sh "ssh bigpi@${bigpi_ips[i]} 'sudo systemctl daemon-reload'"
						 sh "ssh bigpi@${bigpi_ips[i]} 'sudo systemctl enable ${bigpi_services[0]}'"
					 } else {
						 echo 'File daemon service is not existed'
					 }
				}
			}
		}
	}
	
	stage('Deploy') {
		 for(int i = 0; i < bigpi_ips.size(); i++) {
			 parallel {
				 stage('Deploy on Bigpi@'+bigpi_ips[i]) {
				   sh "ssh bigpi@${bigpi_ips[i]} 'sudo systemctl stop ${bigpi_services[0]}'"
				   sh "ssh bigpi@${bigpi_ips[i]} 'mkdir -p ${bigpi_dir}'"
				   sh "ssh bigpi@${bigpi_ips[i]} 'rm -f ${bigpi_dir}/${bigpi_jar}'"
				   sh "scp target/*.jar bigpi@${bigpi_ips[i]}:${bigpi_dir}"
				   sh "ssh bigpi@${bigpi_ips[i]} 'sudo systemctl start ${bigpi_services[0]}'"
				 }
			 }
		   }
	}
 }
 
 