#!/bin/bash

export LANG=de_CH.UTF-8

rm mp/puml/rendered/*

FILES=mp/puml/src/*
for f in $FILES
do
  echo "****************** Processing $f ***********************************************************"
  
  name_suffix=$(echo $f | cut -d/ -f4) # split filepath with delimiter /, and pick 5th element of result array
  name=$(echo $name_suffix | cut -d. -f1) # split filename with delimiter ., and pick first element of result array
  out=mp/puml/rendered/${name}.png

  cat $f | docker run --rm -i puml_burner > $out
done

git add mp/puml/rendered/*
git commit -m 'Skript: PUML-Diagramme der Projektdoku regeneriert und beigefügt'
git push