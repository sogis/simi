#!/bin/bash

export LANG=de_CH.UTF-8

rm doc/puml/rendered/*

FILES=doc/puml/src/*
for f in $FILES
do
  echo "****************** Processing $f ***********************************************************"
  
  name_suffix=$(echo $f | cut -d/ -f4) # split filepath with delimiter /, and pick 4th element of result array
  name=$(echo $name_suffix | cut -d. -f1) # split filename with delimiter ., and pick first element of result array
  out=doc/puml/rendered/${name}.png

  cat $f | docker run --rm -i puml_burner > $out
done

git add doc/puml/rendered/*
git commit -m 'Skript: PUML-Diagramme der Simi-Dokumentation regeneriert und beigefügt'
git push