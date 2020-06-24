#!/bin/bash

export LANG=de_CH.UTF-8

rm simi/doc/puml/rendered/*

FILES=simi/doc/puml/src/*
for f in $FILES
do
  echo "****************** Processing $f ***********************************************************"
  
  name_suffix=$(echo $f | cut -d/ -f5) # split filepath with delimiter /, and pick 5th element of result array
  name=$(echo $name_suffix | cut -d. -f1) # split filename with delimiter ., and pick first element of result array
  out=simi/doc/puml/rendered/${name}.png

  cat $f | docker run --rm -i puml_burner > $out
done

git add simi/doc/puml/rendered/*
git commit -m 'Skript: PUML-Diagramme der Simi-Dokumentation regeneriert und beigefügt'
git push