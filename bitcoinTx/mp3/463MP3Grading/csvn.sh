#!/bin/sh

dirp="$1";
if [ -z "$dirp" ]; then
	dirp=".";
fi

find "$dirp" -name .svn -print0 | xargs -0 rm -r
