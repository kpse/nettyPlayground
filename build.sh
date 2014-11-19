#!/bin/sh

function build_local {
  gradle test
}

function build_and_push {
  git pull && build_local && git push
}

function deploy_prod {
    echo ".... start to deploy on env $1 ..."
    now=$(date +"%s")
    srcFilename="$(pwd)/build/distributions/location2-1.0.zip"
    destFilename="location2-1.0.$now.zip"
    destServer="kulebao@$1"
    destPath="$destServer:~/$destFilename"
    gradle distZip && \
    scp $srcFilename $destPath && \
    ssh $destServer "unzip -x $destFilename -d /var/location/$now/" && \
    ssh $destServer "rm /var/location/current" && \
    ssh $destServer "ln -s /var/location/$now/location2-1.0/ /var/location/current" && \
    ssh $destServer "echo coco999 | sudo -S service location restart"

    retvalue=$?
    echo "Return value: $retvalue"
    echo "Done deployment $1"
}

function main {
  	case $1 in
		d) deploy_prod stage.cocobabys.com ;;
		p) build_and_push ;;
		*) build_local ;;
	esac
}

main $@