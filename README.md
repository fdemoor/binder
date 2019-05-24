## How to run

1. Download our modified version of Apache Mahout [here](https://github.com/fdemoor/mahout/tree/branch-0.13.0) and compile with `mvn package` the mr and math packages.
2. Adapt the path in lines 4 and 12 of `installMahout.sh` and then run `./installMahout.sh`
3. Compile with `mvn package`
4. Run `./src/main/resources/genAll.sh`
4. Run with `./run.sh`

The run script can be modified to specify the output directory and where to find the configuration files for the experiments.
Configuration files can easily be built by copying the default file: `cp src/main/resources/default_config.yml my_config.yml` and then edititing the different parameters.

