#!/bin/bash

git init
read -p "git add * = ENTER : " add

if [ "$add" = "" ]; then
      git add *
else
      git add $add
fi

read -p "git commit -m : " commit
git commit -m "$commit"

read -p "git remote add origin http.../.git : " remote
git remote add origin "$remote"
git push origin master

