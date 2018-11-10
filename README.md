# README

Check releases for builds.

## Requirements

* java

## How to run

    ./gradlew build
    java -jar build/libs/bioinform-0.5.0.jar --help
    java -jar build/libs/bioinform-0.5.0.jar --first configs/f1.fasta \
        --second configs/f2.fasta --gap-open -10 --gap-extend -1 --table \ 
        configs/blosum62.table --debug
    
Tests will be started automatically.

## Important

One file should contain one FASTA item.
    