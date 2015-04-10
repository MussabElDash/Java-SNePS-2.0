#!/bin/bash

for remote in `git branch -r`; do
	branch=${remote:7};
	git branch --track ${branch};
	git checkout ${branch};
	git branch --set-upstream-to=origin/${branch} ${branch};
	git pull origin ${branch}; 
done