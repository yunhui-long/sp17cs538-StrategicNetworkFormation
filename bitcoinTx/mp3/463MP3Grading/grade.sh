diffout="`pwd`/grade-out"
solution="`pwd`/463MP3Sol"
codir="`pwd`/mp3-co"
gradedir="`pwd`/mp3_grading"


COMP="javac"
RUN="java"
OUTDIR="./bin"
CLASSPATH=".:$OUTDIR"

FILES="./src/mp3_grading/*.java"

MAINCLASS="mp3_grading/Grader"

cd $gradedir
pwd

mkdir "$OUTDIR" 2> /dev/null

cmd=`echo "$COMP" -classpath "$CLASSPATH" -d "$OUTDIR" "$FILES"`

echo "Compilation command: \"$cmd\" ";

echo "------------------------------"

$cmd

if [ $? -eq 0 ]; then
	echo "Compilation succeded!";
else
	echo "Compilation failed!";
fi

cd $codir
for d in */; do
	cd $d;
	pwd
	
	echo -n "checking $d";

	netidfolder="`pwd`"
	fp="`pwd`/grading"
	mkdir $fp
	outf="$fp/status.txt"
	rm -rf "$outf";

	echo "Gradeing" > "$outf"

	chmod +x ./compile1.sh
	echo "compile1.sh" >> "$outf"
	./compile1.sh >> "$outf"
	echo "compile1.sh finished -----------"

	chmod +x ./run1.sh
	echo "run1.sh" >> "$outf"
	./run1.sh >> "$outf"
	echo "run1.sh finished ---------"

	chmod +x ./compile.sh
	echo "compile.sh" >> "$outf"
	./compile.sh >> "$outf"
	echo "compile.sh finished --------"

	chmod +x ./run_cluster.sh
	echo "run_cluster.sh" >> "$outf"
	./run_cluster.sh >> "$outf"
	echo "run_cluster.sh finished ------"

	cd ../..
	cd $gradedir
	pwd
	cmd=`echo "$RUN" -classpath "$CLASSPATH" "$MAINCLASS" "$netidfolder" "$solution" "$fp/grades.txt"`
	echo "Run command: \"$cmd\" ";
	echo "----------------------";
	$cmd

	if [ $? -eq 0 ]; then
		echo "Run excceded!";
	else
		echo "Run failed!";
	fi
	cd ..
	cd $codir
done

