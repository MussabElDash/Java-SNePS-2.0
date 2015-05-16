#!/bin/bash

function currentBranch()
{
	local branch_name="$(git symbolic-ref HEAD 2>/dev/null)" ||
	local branch_name="(unnamed branch)"
	local branch_name=${branch_name##refs/heads/}
	echo "$branch_name"
}

branch_name=`currentBranch`

git add .;
git commit -m "commiting";
git push origin ${branch_name};

for remote in `git branch -r`; do
	branch=${remote:7};
	git branch --track ${branch};
	git checkout ${branch};
	git branch --set-upstream-to=origin/${branch} ${branch};
	git pull origin ${branch}; 
done

git checkout ${branch_name}