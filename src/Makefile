# Makefile for .java files using parallel java 2 library
#
# to run PROGRAM_NAME on RIT CS systems using builtin pj2.jar:
#
# java -cp $CLASSPATH pj2 debug=makespan PROGRAM_NAME
#
# example: java -cp '.:/home/fac/ark/public_html/pj2.jar' pj2 debug=makespan PiSmp 142857 4000000000
#
# Modular class path settings for multi-OS running
#
# Set CLASSPATH symbol based on the uname value.
# values for uname: Linux == linux, Darwin == macos, and ???
#
# CLASSPATHS for pj2:
# bks macos: .:/Library/Java/Extensions/ParallelJava/pj2.jar
# CSD linux: .:/home/fac/ark/public_html/pj2.jar
#
UNAME_VALUE =$(shell uname)

CLASSPATH=.:/home/timebomb2788/rit_course_work/3rd_year/spring/copads-csci251/pj2.jar

all::
	@echo on $(UNAME_VALUE) using classpath: $(CLASSPATH)
	-javac -cp $(CLASSPATH) -Xlint *.java

demo::
	@echo on $(UNAME_VALUE) using classpath: $(CLASSPATH)
	-java -cp $(CLASSPATH) pj2 debug=makespan FriendlySeq 4 10000

clean::
	-rm -f *.class

