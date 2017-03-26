#!/bin/sh

out="./mp3-co"
rm -rf "$out";
mkdir "$out";

timestr="2016-04-08 00:05:00"
url="https://subversion.ews.illinois.edu/svn/sp16-cs463/"
rnf="./roster3.netids";

while read netid; do
	echo -n "Downloading $netid...";
	svn co --username=ylong4 "$url/$netid/mp3" "$out/$netid" -r "{$timestr}"
	./csvn.sh "$out/$netid"
done < "$rnf";

cd ..;
