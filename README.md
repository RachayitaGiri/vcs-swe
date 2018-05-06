#vcs-swe
-------------
CLASS NUMBER
-------------

CECS 543 - Advanced Software Engineering

------------------------
PROJECT NUMBER AND NAME
------------------------

Project # 1
Version Control System - Create Repository

Project # 2
Version Control System - Checkout, Checkin, Labels

Project # 3
Version Control System - Merge

----------------------
TEAM NAME AND MEMBERS
----------------------

Name: BBG

Members:
1) Baker, Derek
2) Buddhadeo, Saloni
3) Giri, Rachayita

-------------
INTRODUCTION
-------------

The "Create Repository" project is the first part of a bigger project - Version Control System.

This project takes as input a source project folder and prepares a repository folder containing the entire source folder hierarchy. The files in the source folder have a corresponding folder with the same name in the target repository. Within this folder, all the artifacts (versions) of the file, shall be stored. 

The manifest file inside the repository folder is a text file containing the metadata for the repository, such as the command line, artifact ID of the artifacts, timestamp, etc. 

The "Checkout, Checkin, Labels" project is the second step in the Version Control System.

The Checkout feature helps the user create a local copy from the repository for a project version that the user wants. The version is referred to by the user by giving as input a manifest file name or label corresponding to it. 

The Checkin feature enables the users to update the repository with their version of the project. Also, the Label feature has been added to make the system more user-friendly. Users can assign labels to project versions so that they can keep track of manifest files more easily. It also helps a better referencing scheme in the command line. 

The "Merge" project is the final phase of the Version Control System.

In this phase, the user enters two versions of the repository that they want to merge. Merge can only be run between versions from two different branches. For the entered versions on the command line, the common ancestor is found by traversing over the command history or manifest hierarchy. Once the common ancestor is found, a 3-way merge is performed over the versions to be merged and the common ancestor version. In case of a name conflict, the conflict is resolved by adding suffixes _MR, _MG and _MT respectively 

----------------------------
CALCULATING THE ARTIFACT ID:
----------------------------
As described in the project problem, the artifact ID has been calculated using the following method.

The arifact ID is a 4-byte weighted checksum of all the characters (bytes) in the file (i.e., the bytes in the file's contents) followed by a hyphen and an “L” and the integer file size, followed by the file's extension. 

The 4 weights by which each 4 character group are multiplied are 1, 7, 11, and, 17. Thus, if the file contents is "HELLO WORLD", the checksum S is: 
S = 1*H + 7*E +11*L +17*L + 1*O + 7*' ' + 11*W + 17*O + 1*R + 7*L + 11*D;

and the file size is 11 (because HELLO WORLD = 11 characters). Note, the ASCII numeric value of each character is used and we indicated the space character by ' '. For example if this was the contents of a version of file fred.txt, then the artifact ID code name would be “6648-L11.txt”, in a leaf folder named “fred.txt”.

Modulus: Because the sum can get rather large for a big file, make sure the sum never gets too large by wrapping it using the following prime modulus operator: m == (2^31) - 1 == 2,147,483,647.

--------------------------------
CONTENTS IN THE .ZIP SUBMISSION
--------------------------------

The "543-p1_BBG.zip" folder contains the following files/directories:

	1) CreateMain.java
	2) CreateMain.class
	3) Main.java
	4) Main.class
	5) createManifest.java
	6) createManifest.class
	7) JSONMaker.java
	8) JSONMaker.class
	9) Label.java
	10) Label.class
	11) README.txt
	12) json-simple-1.1.jar
	13) src : a small directory with the following hierarchy structure for test run
	 	
	 	src
	 	|--- hello-world.txt
	 	|--- subdir
	 		|--- hi-there.txt
	 		|--- testfile.txt

	14) repo : the repository consisting of versions of the source folder 'src', with the hierarchy as
		
		repo
		|------ mani_create.json		
		|------	src
		 	|------	hello-world.txt
			|	|------	10426-L13.txt
			|		 	
			|------ subdir
		 		|------	hi-there.txt
				|	|------ 6945-L9.txt	
		 		|------	testfile.txt
					|------ 50518-L66.txt
----------------------
EXTERNAL REQUIREMENTS
----------------------

JDK 1.x or J2SE x.y

----------------------------------
SETUP, INSTALLATION and EXECUTION
----------------------------------

Follow the steps given below to run the project:
	
	1) Extract the 543-p1_BBG.zip folder. 
	2) Go to the Terminal (on Ubuntu) or the Command Prompt (on Windows).
	3) Navigate to the folder - 543-p1_BBG
	4) The folder already contains the compiled classes. You can skip this point and go to point if you want to run the program 	right away. Execute these commands if you want to compile the classes. 

		--$javac Main.java
		--$javac CreateMain.java
		--$javac createManifest.java

	5) To run the program, execute the program by typing in the following commands.

		--$java Main
		create
		<source_folder>
		<target_repository>
	
	6) CREATE - If you want to create another repository, continue using the same commands from "create", i.e.

		create
		<source_folder>
		<target_repository>

	7) If you are done with the execution, enter "exit".

		exit
		--$

		A SAMPLE INVOCATION:

		rachayitagiri@rachayitagiri-HP-Pavilion-15-Notebook-PC:~/javacodes$ java Main
		create
		src
		repo    
		Create a new repository from source: src
		Copy repository into: repo
		Creating repo...
		Repo created successfully.
		File: hello-world.txt
		src/hello-world.txt repo_path
		Dir: subdir
		repo/src/subdir
		File: testfile.txt
		src/subdir/testfile.txt repo_path
		File: hi-there.txt
		src/subdir/hi-there.txt repo_path
		exit
		rachayitagiri@rachayitagiri-HP-Pavilion-15-Notebook-PC:~/javacodes$

---------
FEATURES
---------
Included:
	1) Copies an existing repository into a destination folder of user's specified name.
	2) Exit application from the command prompt using "exit" command.
	3) Non-valid words (in this case, only "create" command is accepted) are rejected.

Not Included:
	1) Case-ignoring in keywords, only lower-case words are accepted as keywords.
	2) Testing of any kind.
	3) Error handling aside from a notification for the user.

-----
BUGS
-----
1) Only local files will be copied, full path names with drive types (i.e.: C:/...) will cause errors in the create command.
