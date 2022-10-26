def String fhe_aplusb(def a, def b) {	
	def command = "echo 'Tests ${a} + ${b}' "
	def proc = command.execute()
	proc.waitFor()		
	if(proc.exitValue() != 0) {
		println "Process exit code: ${proc.exitValue()}"
		println "Std Err: ${proc.err.text}"
	}	
	return proc.in.text		
}